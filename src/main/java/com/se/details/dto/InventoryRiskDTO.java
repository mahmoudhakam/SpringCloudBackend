package com.se.details.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;

@Getter
@Setter
@NoArgsConstructor
public class InventoryRiskDTO implements Function<PartdetailsFeatures, AtomicInteger>
{
	@JsonProperty("LowRisk") private AtomicInteger lowRisk = new AtomicInteger(0);
	@JsonProperty("MediumRisk") private AtomicInteger mediumRisk = new AtomicInteger(0);
	@JsonProperty("HighRisk") private AtomicInteger highRisk = new AtomicInteger(0);
	@JsonProperty("Unknown") private AtomicInteger unknown = new AtomicInteger(0);

	@Override
	public AtomicInteger apply(PartdetailsFeatures f)
	{
		String fetValue = f.getFeatureValue();
		if(fetValue.equalsIgnoreCase("low risk"))
		{
			this.getLowRisk().incrementAndGet();
		}
		else if(fetValue.equalsIgnoreCase("high risk"))
		{
			this.getHighRisk().incrementAndGet();
		}
		else if(fetValue.equalsIgnoreCase("unknown"))
		{
			this.getUnknown().incrementAndGet();
		}
		else
		{
			this.getMediumRisk().incrementAndGet();
		}
		return null;
	}

}
