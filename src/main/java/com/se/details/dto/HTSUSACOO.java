package com.se.details.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.se.details.util.PDMicroserviceConstants;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiConsumer;

@Getter
@Setter
@NoArgsConstructor
public class HTSUSACOO implements BiConsumer<Integer, List<PartdetailsFeatures>>
{
	@JsonProperty("HTSUSA") private AtomicInteger partsHaveHTSUSA = new AtomicInteger(0);
	@JsonProperty("NoHTSUSA") private AtomicInteger partsWithoutHTSUSA = new AtomicInteger(0);
	@JsonProperty("COO") private AtomicInteger partsHaveCOO = new AtomicInteger(0);
	@JsonProperty("NoCOO") private AtomicInteger partsWithoutCOO = new AtomicInteger(0);
	@JsonProperty("HTSUSAandCOO") private AtomicInteger partsHaveHTSUSAAndCOO = new AtomicInteger(0);
	@JsonProperty("NoHTSUSAandCOO") private AtomicInteger partsWithoutHTSUSAAndCOO = new AtomicInteger(0);

	@Override
	public void accept(Integer size, List<PartdetailsFeatures> partdetailsFeatures)
	{
		if(partsWithoutCOO.get() == 0 && partsWithoutHTSUSA.get() == 0 && partsWithoutHTSUSAAndCOO.get() == 0)
		{
			partsWithoutCOO.set(size);
			partsWithoutHTSUSA.set(size);
			partsWithoutHTSUSAAndCOO.set(size);
		}
		AtomicInteger hasHTSUSAAndCOO = new AtomicInteger(0);
		partdetailsFeatures.forEach(f -> {
			if(f.getFeaturesName().equals(PDMicroserviceConstants.SummaryFeatureName.COO) && f.getFeatureValue() != null && !f.getFeatureValue().isEmpty())
			{
				hasHTSUSAAndCOO.incrementAndGet();
				partsHaveCOO.incrementAndGet();
				partsWithoutCOO.decrementAndGet();
			}
			else if(f.getFeaturesName().equals(PDMicroserviceConstants.SummaryFeatureName.HTSUSA) && f.getFeatureValue() != null && !f.getFeatureValue().isEmpty())
			{
				hasHTSUSAAndCOO.incrementAndGet();
				partsHaveHTSUSA.incrementAndGet();
				partsWithoutHTSUSA.decrementAndGet();
			}
			if(hasHTSUSAAndCOO.get() == 2)
			{
				partsHaveHTSUSAAndCOO.incrementAndGet();
				partsWithoutHTSUSAAndCOO.decrementAndGet();
			}
		});
	}
}
