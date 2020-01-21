package com.se.details.services.actors;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Cancellable;
import akka.actor.Props;
import akka.routing.Router;
import com.se.details.dto.DetailsActorsResponse;
import com.se.details.dto.PartdetailsFeatures;
import com.se.details.enumeration.DefaultOrFilteredFeaturesFunctions;
import com.se.details.enumeration.FeatureMapperFunctions;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import scala.concurrent.duration.FiniteDuration;

import java.util.*;
import java.util.function.BiFunction;

/**
 * @author MAHMOUD_ABDELHAKAM
 * */
public class PartdetailsActorsManager extends AbstractActor
{

	final long requestId;
	private ActorRef requester;
	final int partsPerActor;
	private Cancellable queryTimeoutTimer;
	private final Map<String, Router> routers;
	private FiniteDuration timeoutDuration;
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	private Map<Long, String> runningActorIds;

	public static final class CollectionTimeout
	{
	}

	public PartdetailsActorsManager(long requestId, FiniteDuration timeoutDuration, int partsPerActor, Map<String, Router> routers)
	{
		this.requestId = requestId;
		this.partsPerActor = partsPerActor;
		this.routers = routers;
		this.timeoutDuration = timeoutDuration;
	}

	@Override
	public void postStop()
	{
		cleanUpRunningActorsAndTimer();
	}

	@Override
	public Receive createReceive()
	{
		return waitingForReplies(new LinkedHashMap<String, Map<String, List<PartdetailsFeatures>>>());
	}

	private Receive waitingForReplies(Map<String, Map<String, List<PartdetailsFeatures>>> response)
	{
		return receiveBuilder().match(AllComIdsRequest.class, requestMessage -> {
			this.requester = requestMessage.getRequester();
			queryTimeoutTimer = getContext().getSystem().scheduler().scheduleOnce(timeoutDuration, getSelf(), new CollectionTimeout(), getContext().getDispatcher(), getSelf());
			getContext().become(waitingForReplies(response));
			logger.info("PartdetailsManagerActor received a message and tring to sent it to {}", requestMessage.getCategories());
			sendComIdsToRequestedCategories(requestMessage.getComIds(), requestMessage.getCategories());
			cancelTheSubmittedTask(response);
		}).match(CollectionTimeout.class, timeoutMessage -> {
			logger.info("Received timeout message start cleaning up");
			cleanUpRunningActorsAndTimer();
			getContext().become(waitingForReplies(response));
			this.requester.tell(new PartdetailsActorsManager.AllComIdsResponse(response), self());
		}).match(DetailsActorsResponse.ActorMessageReponse.class, resultMessage -> {
			logger.info("Received message from category: {} ", resultMessage.getCategory());
			subscribeToPartdetailsFeatures(resultMessage.getFeatures(), resultMessage.getActorId(), resultMessage.getCategory(), response);
		}).build();
	}

	private void cancelTheSubmittedTask(Map<String, Map<String, List<PartdetailsFeatures>>> response)
	{
		if(runningActorIds == null || runningActorIds.isEmpty())
		{
			logger.info("There are NO eligible actors to the submitted task, Unknown category");
			queryTimeoutTimer.cancel();
			this.requester.tell(new PartdetailsActorsManager.AllComIdsResponse(response), self());
		}
	}

	private void subscribeToPartdetailsFeatures(Map<String, List<PartdetailsFeatures>> features, Long actorId, String categoryName, Map<String, Map<String, List<PartdetailsFeatures>>> response)
	{
		if(!runningActorIds.isEmpty())
		{
			runningActorIds.remove(actorId);
			mergeCategoryFeatures(features, response, categoryName);
			if(runningActorIds.isEmpty())
			{
				logger.info("No more running actors ... Sending the whole result to the controller");
				queryTimeoutTimer.cancel();
				this.requester.tell(new PartdetailsActorsManager.AllComIdsResponse(response), self());
			}
			else
			{
				getContext().become(waitingForReplies(response));
			}
		}
	}

	private void mergeCategoryFeatures(Map<String, List<PartdetailsFeatures>> features, Map<String, Map<String, List<PartdetailsFeatures>>> response, String categoryName)
	{
		if(features == null || features.isEmpty())
		{
			return;
		}
		features.entrySet().forEach(e -> {
			final String comId = e.getKey();
			final List<PartdetailsFeatures> categoryFeatures = e.getValue();
			if(response.get(comId) == null)
			{
				response.put(comId, new HashMap<>());
			}
			BiFunction<? super String, ? super Map<String, List<PartdetailsFeatures>>, ? extends Map<String, List<PartdetailsFeatures>>> addingCategoryFeaturesFunction = (k, v) -> {
				v.put(categoryName, categoryFeatures);
				return v;
			};
			response.computeIfPresent(comId, addingCategoryFeaturesFunction);
		});
	}

	private void sendComIdsToRequestedCategories(List<String> comIds, Map<String, Set<String>> categoriesMap)
	{
		runningActorIds = new HashMap<>();
		categoriesMap.entrySet().forEach(e -> {
			String catName = e.getKey();
			Set<String> feautres = e.getValue();
			Router catRouters = routers.get(catName);
			if(catRouters != null)
			{
				for(int i = 0; i < comIds.size(); i += partsPerActor)
				{
					final Long actorId = System.nanoTime();
					List<String> subList = comIds.subList(i, Math.min(i + partsPerActor, comIds.size()));
					catRouters.route(new DetailsPublisherActor.DetailsMessage(subList, feautres, actorId, DefaultOrFilteredFeaturesFunctions.getFilteredFeatures(catName), FeatureMapperFunctions.getFeaturesMapping(catName)), self());
					runningActorIds.put(actorId, catName);
				}
			}
		});
		logger.info("Info about the running actors and categories:{}", runningActorIds);
	}

	public static Props props(long requestId, FiniteDuration timeoutDuration, int partsPerActor, Map<String, Router> routers)
	{
		return Props.create(PartdetailsActorsManager.class, () -> new PartdetailsActorsManager(requestId, timeoutDuration, partsPerActor, routers));
	}

	private void cleanUpRunningActorsAndTimer()
	{
		runningActorIds.clear();
		queryTimeoutTimer.cancel();
	}

	@Getter
	public static final class AllComIdsRequest
	{
		private final List<String> comIds;
		private final ActorRef requester;
		private final Map<String, Set<String>> categories;

		public AllComIdsRequest(List<String> comIds, Map<String, Set<String>> categories, ActorRef requester)
		{
			super();
			this.comIds = comIds;
			this.requester = requester;
			this.categories = categories;
		}

	}

	@Getter
	public static final class AllComIdsResponse
	{
		private final Map<String, Map<String, List<PartdetailsFeatures>>> response;

		public AllComIdsResponse(Map<String, Map<String, List<PartdetailsFeatures>>> response)
		{
			super();
			this.response = response;
		}
	}

}
