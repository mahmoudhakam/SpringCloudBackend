package com.se.details.dto;

import lombok.Getter;

import java.util.List;
import java.util.Map;

public class DetailsActorsResponse
{
	private DetailsActorsResponse()
	{
	}

	@Getter
	public static class ActorMessageReponse
	{
		private final Map<String, List<PartdetailsFeatures>> features;
		private final String category;
		private final Long actorId;

		public ActorMessageReponse(Map<String, List<PartdetailsFeatures>> features, String category, Long actorId)
		{
			this.features = features;
			this.category = category;
			this.actorId = actorId;
		}
	}

}
