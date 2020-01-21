package com.se.details.services;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Inbox;
import akka.actor.PoisonPill;
import akka.routing.ActorRefRoutee;
import akka.routing.RoundRobinRoutingLogic;
import akka.routing.Routee;
import akka.routing.Router;
import com.se.details.configuration.PartdetailsMSConfigReader;
import com.se.details.dto.PartdetailsCategories;
import com.se.details.dto.PartdetailsFeatures;
import com.se.details.services.actors.PartdetailsActorsManager;
import com.se.details.services.actors.PartdetailsActorsManager.AllComIdsResponse;
import com.se.details.services.strategy.PartdetailsStrategy;
import com.se.details.util.PDMicroserviceConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import scala.concurrent.duration.FiniteDuration;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.se.details.configuration.SpringExtension.SPRING_EXTENSION_PROVIDER;

@Service
public class PartdetailsSearchStrategyImpl implements PartdetailsStrategy
{

	private PartdetailsMSConfigReader configManager;
	private ActorSystem actorSystem;
	private Map<String, Router> routers;

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	public PartdetailsSearchStrategyImpl(PartdetailsMSConfigReader configManager, ActorSystem actorSystem)
	{
		super();
		this.configManager = configManager;
		this.actorSystem = actorSystem;
	}

	@PostConstruct
	void inti()
	{
		routers = createCategoriesRoutersPool();
	}

	@Override
	public Map<String, Map<String, List<PartdetailsFeatures>>> publishPartdetailsData(List<String> comIds, Map<String, Set<String>> categories, boolean debug)
	{
		Map<String, Map<String, List<PartdetailsFeatures>>> response = new HashMap<>();

		int partsPerActor = configManager.getPartsPerActor();
		int actorsPerCategory = configManager.getActorsPerCategory();
		int actorMangerTimeout = configManager.getActorMangerTimeout();
		int inProgress = 0;
		int finishedParts = 0;
		int batchNumber = 1;
		List<String> originalPartList = new LinkedList<>(comIds);
		ActorRef partdetailsActorRef = actorSystem.actorOf(PartdetailsActorsManager.props(System.nanoTime(), FiniteDuration.create(actorMangerTimeout, TimeUnit.SECONDS), partsPerActor, routers), PDMicroserviceConstants.ActorNames.PART_DETAILS_ACTOR);
		final Inbox inbox = Inbox.create(actorSystem);
		int detailsBatchSize = configManager.getDetailsBatchSize();
		logger.info("Getting features with criteria batchSize:{} parts/Actor:{} Actors/Section:{}", detailsBatchSize, partsPerActor, actorsPerCategory);
		for(int i = 0; i < comIds.size(); i += detailsBatchSize)
		{
			List<String> actorComIDs = comIds.subList(i, Math.min(i + detailsBatchSize, comIds.size()));
			inProgress = actorComIDs.size();
			finishedParts += inProgress;
			partdetailsActorRef.tell(new PartdetailsActorsManager.AllComIdsRequest(actorComIDs, categories, inbox.getRef()), inbox.getRef());
			FiniteDuration duration = FiniteDuration.create(actorMangerTimeout + 1, TimeUnit.SECONDS);
			try
			{
				AllComIdsResponse allComIdsResponse = (AllComIdsResponse) inbox.receive(duration);
				response = Stream.concat(allComIdsResponse.getResponse().entrySet().stream(), response.entrySet().stream()).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (v1, v2) -> v2));
				logger.info("Finished getting features for batch:{} for {} parts -- Expected to be finished {} -- Actually Finished {} parts", batchNumber, inProgress, finishedParts, response.size());
				batchNumber++;
			}
			catch(Exception e)
			{
				logger.error("Getting partdetails failed: ", e);
			}
		}
		partdetailsActorRef.tell(PoisonPill.getInstance(), ActorRef.noSender());

		if(debug)
		{
			originalPartList.removeAll(response.keySet());
			if(!originalPartList.isEmpty())
			{
				logger.info("Missing parts: {}", originalPartList);
			}
		}
		return response;
	}

	private Map<String, Router> createCategoriesRoutersPool()
	{
		int actorsPerCategory = configManager.getActorsPerCategory();
		long start = System.currentTimeMillis();
		Map<String, Router> routersPool = new HashMap<>();
		for(PartdetailsCategories cat : PartdetailsCategories.values())
		{
			List<Routee> routees = new ArrayList<>();
			for(int i = 0; i < actorsPerCategory; i++)
			{
				ActorRef validator = actorSystem.actorOf(SPRING_EXTENSION_PROVIDER.get(actorSystem).props(cat.getCategoryName()));
				routees.add(new ActorRefRoutee(validator));
			}
			Router router = new Router(new RoundRobinRoutingLogic(), routees);
			routersPool.put(cat.getCategoryName(), router);
		}
		logger.info("Creating details category routers finished in {}", (System.currentTimeMillis() - start));
		return routersPool;
	}

}
