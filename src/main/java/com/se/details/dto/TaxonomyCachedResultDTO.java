package com.se.details.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
public class TaxonomyCachedResultDTO
{
	private Map<String, PartdetailsFeatures> features;

	public TaxonomyCachedResultDTO(Map<String, PartdetailsFeatures> features)
	{
		this.features = features;
	}
}
