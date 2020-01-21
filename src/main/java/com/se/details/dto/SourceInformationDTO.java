package com.se.details.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.se.details.util.PDMicroserviceConstants;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;

@Getter
@Setter
@NoArgsConstructor
public class SourceInformationDTO implements Function<PartdetailsFeatures, AtomicInteger>
{
	@JsonProperty("SoleSource") private AtomicInteger soleSource = new AtomicInteger(0);
	@JsonProperty("SingleSource") private AtomicInteger singleSource = new AtomicInteger(0);
	@JsonProperty("MultiSource") private AtomicInteger multiSource = new AtomicInteger(0);

	@Override
	public AtomicInteger apply(PartdetailsFeatures f)
	{
		String fetName = f.getFeaturesName();
		if(fetName.equalsIgnoreCase(PDMicroserviceConstants.RiksFeaturesNames.OTHERSOURCES))
		{
			Double otherSources = Double.parseDouble(f.getFeatureValue());
			if(otherSources == 0)
			{
				this.soleSource.incrementAndGet();
			}
			else if(otherSources == 1)
			{
				this.singleSource.incrementAndGet();
			}
			else
			{
				this.multiSource.incrementAndGet();
			}
		}
		return null;
	}

}
