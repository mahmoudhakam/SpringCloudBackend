package com.se.details.services.actors.parametric;

import akka.event.Logging;
import akka.event.LoggingAdapter;
import com.se.details.dto.DetailsActorsResponse;
import com.se.details.dto.PartdetailsFeatures;
import com.se.details.services.actors.DetailsPublisherActor;
import com.se.details.util.PDMicroserviceConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * @author MAHMOUD_ABDELHAKAM
 */

@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Component(PDMicroserviceConstants.ActorNames.PARAMETRICFEATURES)
public class ParametricCategoryActor extends DetailsPublisherActor
{

	private final ParametricCategoryActorHelper parametricActorHelper;
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	private final LoggingAdapter log = Logging.getLogger(getContext().system(), this);

	@Autowired
	public ParametricCategoryActor(ParametricCategoryActorHelper parametricActorHelper)
	{
		this.parametricActorHelper = parametricActorHelper;
	}

	@Override
	public Receive createReceive()
	{
		return waitingForReplies();
	}

	protected Receive waitingForReplies()
	{
		return receiveBuilder().match(DetailsPublisherActor.DetailsMessage.class, message -> {
			logger.info("ParametricActor received a message with id:{}", message.getActorId());
			Map<String, List<PartdetailsFeatures>> features = getDetailsFeatures(message);
			getSender().tell(new DetailsActorsResponse.ActorMessageReponse(features, PDMicroserviceConstants.ActorNames.PARAMETRICFEATURES, message.getActorId()), self());
			logger.info("{} ParametricActor replied with a message with id:{} to:{}", self().path(), message.getActorId(), getSender().path());
		}).build();
	}

	@Override
	public Map<String, List<PartdetailsFeatures>> getDetailsFeatures(DetailsMessage message)
	{
		return parametricActorHelper.getParametricFeaturesFunction.getFeatures(message);
	}

}
