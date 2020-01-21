package com.se.details.services.actors;

import akka.actor.AbstractActor;
import com.se.details.dto.PartdetailsFeatures;
import lombok.Getter;

import java.util.*;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

public abstract class DetailsPublisherActor extends AbstractActor
{
	public abstract Map<String, List<PartdetailsFeatures>> getDetailsFeatures(DetailsMessage message);

	protected Map<String, List<PartdetailsFeatures>> seedFeaturesResultMap(List<String> comIds)
	{
		Map<String, List<PartdetailsFeatures>> resultMap = new HashMap<>();
		comIds.forEach(part -> resultMap.put(part, new ArrayList<PartdetailsFeatures>()));
		return resultMap;
	}

	@Getter
	public static final class DetailsMessage
	{
		private final List<String> comIds;
		private final Set<String> feautres;
		private final Long actorId;
		private final UnaryOperator<Set<String>> defaultOrFilteredFeatures;
		private final Supplier<Map<String, String>> featureMappingSupplier;

		public DetailsMessage(List<String> comIds, Set<String> feautres, Long actorId, UnaryOperator<Set<String>> defaultOrFilteredFeatures, Supplier<Map<String, String>> featureMappingSupplier)
		{
			super();
			this.comIds = comIds;
			this.feautres = feautres;
			this.actorId = actorId;
			this.defaultOrFilteredFeatures = defaultOrFilteredFeatures;
			this.featureMappingSupplier = featureMappingSupplier;
		}
	}
}
