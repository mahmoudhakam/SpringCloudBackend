package com.se.details.services.actors.summary;

import akka.actor.AbstractActor;
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
@Component(PDMicroserviceConstants.ActorNames.SUMMARYFEATURES)
public class SummaryCategoryActor extends DetailsPublisherActor
{
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	private final LoggingAdapter log = Logging.getLogger(getContext().system(), this);
	private final SummaryCategoryActorHelper summaryCategoryActorHelper;

	@Autowired
	public SummaryCategoryActor(SummaryCategoryActorHelper summaryCategoryActorHelper)
	{
		super();
		this.summaryCategoryActorHelper = summaryCategoryActorHelper;
	}

	@Override
	public Map<String, List<PartdetailsFeatures>> getDetailsFeatures(DetailsPublisherActor.DetailsMessage message)
	{
		return summaryCategoryActorHelper.getRiskFeatures.getFeatures(message);
	}

	@Override
	public AbstractActor.Receive createReceive()
	{
		return waitingForReplies();
	}

	private AbstractActor.Receive waitingForReplies()
	{
		return receiveBuilder().match(DetailsPublisherActor.DetailsMessage.class, message -> {
			logger.info("SummaryCategoryActor received a message with id:{}", message.getActorId());
			Map<String, List<PartdetailsFeatures>> features = getDetailsFeatures(message);
			getSender().tell(new DetailsActorsResponse.ActorMessageReponse(features, PDMicroserviceConstants.ActorNames.SUMMARYFEATURES, message.getActorId()), self());
			logger.info("{} SummaryCategoryActor replied with a message with id:{} to:{}", self().path(), message.getActorId(), getSender().path());
		}).build();
	}
}
