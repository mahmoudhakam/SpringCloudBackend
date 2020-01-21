package com.se.details.services.actors.risk;
/**
 * @author MAHMOUD_ABDELHAKAM
 */

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

@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Component(PDMicroserviceConstants.ActorNames.RISKFEATURES)
public class RiskCategoryActor extends DetailsPublisherActor
{
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	private final LoggingAdapter log = Logging.getLogger(getContext().system(), this);
	private final RiskCategoryActorHelper riskCategoryActorHelper;

	@Autowired
	public RiskCategoryActor(RiskCategoryActorHelper riskCategoryActorHelper)
	{
		super();
		this.riskCategoryActorHelper = riskCategoryActorHelper;
	}

	@Override
	public Map<String, List<PartdetailsFeatures>> getDetailsFeatures(DetailsMessage message)
	{
		return riskCategoryActorHelper.getRiskFeatures.getFeatures(message);
	}

	@Override
	public Receive createReceive()
	{
		return waitingForReplies();
	}

	private Receive waitingForReplies()
	{
		return receiveBuilder().match(DetailsPublisherActor.DetailsMessage.class, message -> {
			logger.info("RiskActor received a message with id:{}", message.getActorId());
			Map<String, List<PartdetailsFeatures>> features = getDetailsFeatures(message);
			getSender().tell(new DetailsActorsResponse.ActorMessageReponse(features, PDMicroserviceConstants.ActorNames.RISKFEATURES, message.getActorId()), self());
			logger.info("{} RiskActor replied with a message with id:{} to:{}", self().path(), message.getActorId(), getSender().path());
		}).build();
	}

}
