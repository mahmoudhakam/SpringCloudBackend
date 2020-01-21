package com.se.details.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class YEOLValuesDTO
{
	@JsonProperty("EstimatedYearsToEOL") private List<Double> yeols = new ArrayList<>();
}
