package com.se.details.dto;

import com.se.details.util.PDMicroserviceConstants;
import lombok.Getter;

@Getter
public enum PartdetailsCategories
{
	PARAMETRIC(PDMicroserviceConstants.ActorNames.PARAMETRICFEATURES),
	SUMMARY(PDMicroserviceConstants.ActorNames.SUMMARYFEATURES),
	RISK(PDMicroserviceConstants.ActorNames.RISKFEATURES);
	private final String categoryName;

	PartdetailsCategories(String categoryName)
	{
		this.categoryName = categoryName;
	}

	public String getCategoryName()
	{
		return categoryName;
	}
}
