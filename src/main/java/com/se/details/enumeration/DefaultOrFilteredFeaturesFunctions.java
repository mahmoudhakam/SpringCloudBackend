package com.se.details.enumeration;

import com.se.details.util.PDMicroserviceConstants;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

import java.util.HashSet;
import java.util.Set;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;

/**
 * @author MAHMOUD_ABDELHAKAM
 */

/**
 * Strategy pattern to get filtered features
 */

@Getter
public enum DefaultOrFilteredFeaturesFunctions implements UnaryOperator<Set<String>>
{
	// RiskFeatures Function //
	RiskFeatures(filteredFeatures -> {
		if(filteredFeatures == null || filteredFeatures.isEmpty())
		{
			Set<String> defaultFeatures = new HashSet<>();
			defaultFeatures.add(PDMicroserviceConstants.LCForecastSolrFields.AVAILABILITY_RISK);
			defaultFeatures.add(PDMicroserviceConstants.LCForecastSolrFields.LIFECYCLE_RISK);
			defaultFeatures.add(PDMicroserviceConstants.LCForecastSolrFields.LIFECYCLE_STATUS);
			defaultFeatures.add(PDMicroserviceConstants.LCForecastSolrFields.NUMBER_OF_OTHER_SOURCES);
			defaultFeatures.add(PDMicroserviceConstants.LCForecastSolrFields.YEARS_EOL);
			return defaultFeatures;
		}
		return filteredFeatures.stream().filter(StringUtils::isNotBlank).collect(Collectors.toSet());
	}),
	// SummaryFeatures Function //
	SummaryFeatures(filteredFeatures -> {
		if(filteredFeatures == null || filteredFeatures.isEmpty())
		{
			Set<String> defaultFeatures = new HashSet<>();
			defaultFeatures.add(PDMicroserviceConstants.SummaryCoreFields.COO);
			defaultFeatures.add(PDMicroserviceConstants.SummaryCoreFields.HTSUSA);
			return defaultFeatures;
		}
		return filteredFeatures.stream().filter(StringUtils::isNotBlank).collect(Collectors.toSet());
	}),
	// UNKNOWN Function //
	UNKNOWN(filteredFeatures -> new HashSet<>());

	private UnaryOperator<Set<String>> function;

	DefaultOrFilteredFeaturesFunctions(UnaryOperator<Set<String>> function)
	{
		this.function = function;
	}

	@Override
	public Set<String> apply(Set<String> filteredFeatures)
	{
		return this.getFunction().apply(filteredFeatures);
	}

	public static DefaultOrFilteredFeaturesFunctions getFilteredFeatures(String sectionName)
	{
		try
		{
			return DefaultOrFilteredFeaturesFunctions.valueOf(sectionName);
		}
		catch(Exception e)
		{
			return DefaultOrFilteredFeaturesFunctions.UNKNOWN;
		}
	}
}
