package com.se.details.enumeration;

import com.se.details.util.PDMicroserviceConstants;
import lombok.Getter;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;

/**
 * @author MAHMOUD_ABDELHAKAM
 */

/**
 * Strategy to get the feature mapping
 */
@Getter
public enum FeatureMapperFunctions implements Supplier<Map<String, String>>
{
	// RiskFeatures Function //
	RiskFeatures(() -> {
		Map<String, String> map = new HashMap<>();
		map.put(PDMicroserviceConstants.LCForecastSolrFields.AVAILABILITY_RISK, PDMicroserviceConstants.RiksFeaturesNames.AVAILABILITYRISK);
		map.put(PDMicroserviceConstants.LCForecastSolrFields.LIFECYCLE_RISK, PDMicroserviceConstants.RiksFeaturesNames.LIFECYCLERISK);
		map.put(PDMicroserviceConstants.LCForecastSolrFields.LIFECYCLE_STATUS, PDMicroserviceConstants.RiksFeaturesNames.LIFECYCLESTATUS);
		map.put(PDMicroserviceConstants.LCForecastSolrFields.NUMBER_OF_OTHER_SOURCES, PDMicroserviceConstants.RiksFeaturesNames.OTHERSOURCES);
		map.put(PDMicroserviceConstants.LCForecastSolrFields.YEARS_EOL, PDMicroserviceConstants.RiksFeaturesNames.YEARSTOENDOFLIFE);
		return map;
	}),
	// SummaryFeatures Function //
	SummaryFeatures(() -> {
		Map<String, String> map = new HashMap<>();
		map.put(PDMicroserviceConstants.SummaryCoreFields.COO, PDMicroserviceConstants.SummaryFeatureName.COO);
		map.put(PDMicroserviceConstants.SummaryCoreFields.HTSUSA, PDMicroserviceConstants.SummaryFeatureName.HTSUSA);
		return map;
	}),
	// UNKNOWN Function //
	UNKNOWN(() -> new HashMap<>());

	private Supplier<Map<String, String>> solrFeaturesToFeatureName;

	FeatureMapperFunctions(Supplier<Map<String, String>> solrFeaturesToFeatureName)
	{
		this.solrFeaturesToFeatureName = solrFeaturesToFeatureName;
	}

	public Map<String, String> getFeatureNameToSolrFeaturesMap()
	{
		return this.getSolrFeaturesToFeatureName().get();
	}

	@Override
	public Map<String, String> get()
	{
		return this.getSolrFeaturesToFeatureName().get();
	}

	public static FeatureMapperFunctions getFeaturesMapping(String sectionName)
	{
//		try
//		{
//			return FeatureMapperFunctions.valueOf(sectionName);
//		}
//		catch(Exception e)
//		{
//			return FeatureMapperFunctions.UNKNOWN;
//		}

		return Optional.of(FeatureMapperFunctions.valueOf(sectionName)).orElse(UNKNOWN);

	}

}
