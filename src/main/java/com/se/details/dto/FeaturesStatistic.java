package com.se.details.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class FeaturesStatistic
{
	@JsonProperty("SourceInformation") private SourceInformationDTO sourceInformationStatistic;
	@JsonProperty("LifecycleRisk") private LifecycleRiskDTO lifecycleDTO;
	@JsonProperty("InventoryRisk") private InventoryRiskDTO inventoryRiskDTO;
	@JsonProperty("YEOL") private YEOLValuesDTO yeolValuesDTO;
	@JsonProperty("HTSUSACOO") private HTSUSACOO htsusacoo;

}
