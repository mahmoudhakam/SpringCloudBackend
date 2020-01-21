package com.se.details.services.actors;

import com.se.details.dto.PartdetailsFeatures;

import java.util.List;
import java.util.Map;

@FunctionalInterface
public interface FeaturesPublisherFunction
{
	Map<String, List<PartdetailsFeatures>> getFeatures(DetailsPublisherActor.DetailsMessage message);
}
