package com.se.details.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PartdetailsFeatures
{
	@JsonIgnore private String hColName;
	@JsonIgnore private String comId;
	private String featureUnit;
	private String featuresName;
	private String featureValue;
	private String multiplier;
	private Double multiplierValue;

	public PartdetailsFeatures(String featuresName, String featureValue)
	{
		super();
		this.featuresName = featuresName;
		this.featureValue = featureValue;
	}

	public PartdetailsFeatures(String hColName, String featuresName, String featureValue, String featureUnit)
	{
		super();
		this.hColName = hColName;
		this.featuresName = featuresName;
		this.featureValue = featureValue;
		this.featureUnit = featureUnit;
	}
}
