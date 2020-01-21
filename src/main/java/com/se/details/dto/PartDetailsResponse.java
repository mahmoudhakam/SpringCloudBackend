package com.se.details.dto;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.se.details.util.PDMicroserviceConstants;

import java.util.List;
import java.util.Map;

@JsonPropertyOrder({ "status", "serviceTime", "gSheetStatistic", "response" })
@JsonFilter(PDMicroserviceConstants.Jackson.PART_DETAILS_FILTER_NAME)
public class PartDetailsResponse extends PartDetailsBaseResponse
{

	@JsonProperty(PDMicroserviceConstants.PartDetailsEndPointResponseColumns.PART_FEATURES) private Map<String, Map<String, List<PartdetailsFeatures>>> response;

	@JsonProperty(PDMicroserviceConstants.PartDetailsEndPointResponseColumns.STATISTICS) private FeaturesStatistic gSheetStatistic;

	public Map<String, Map<String, List<PartdetailsFeatures>>> getResponse()
	{
		return response;
	}

	public void setResponse(Map<String, Map<String, List<PartdetailsFeatures>>> response)
	{
		this.response = response;
	}

	public PartDetailsResponse(Status status)
	{
		super(status);
	}

	public PartDetailsResponse()
	{
	}

	public FeaturesStatistic getgSheetStatistic()
	{
		return gSheetStatistic;
	}

	public void setgSheetStatistic(FeaturesStatistic gSheetStatistic)
	{
		this.gSheetStatistic = gSheetStatistic;
	}

	@Override
	public String toString()
	{
		return "PartDetailsResponse{" + "response=" + response + ", gSheetStatistic=" + gSheetStatistic + "} " + super.toString();
	}

}
