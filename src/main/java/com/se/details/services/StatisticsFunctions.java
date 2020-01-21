package com.se.details.services;

import com.se.details.dto.*;
import com.se.details.util.PDMicroserviceConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

public class StatisticsFunctions
{
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	public FeaturesStatistic getStatistics(Map<String, Map<String, List<PartdetailsFeatures>>> response)
	{
		long start = System.currentTimeMillis();
		SourceInformationDTO sourceInformationStatistic = new SourceInformationDTO();
		LifecycleRiskDTO lifecycleDTO = new LifecycleRiskDTO();
		InventoryRiskDTO inventoryRiskDTO = new InventoryRiskDTO();
		YEOLValuesDTO yeolValuesDTO = new YEOLValuesDTO();
		HTSUSACOO htsusacoo = new HTSUSACOO();
		int partsSize = response.keySet().size();
		response.entrySet().forEach(e -> {
			Map<String, List<PartdetailsFeatures>> sectionFeatures = e.getValue();
			List<PartdetailsFeatures> riskFeatures = sectionFeatures.get(PartdetailsCategories.RISK.getCategoryName());
			if(riskFeatures != null)
			{
				riskFeatures.forEach(f -> {
					String fetName = f.getFeaturesName();
					sourceInformationStatistic.apply(f);
					lifecycleDTO.apply(f);
					inventoryRiskDTO.apply(f);
					if(fetName.equalsIgnoreCase(PDMicroserviceConstants.RiksFeaturesNames.YEARSTOENDOFLIFE))
					{
						Double yeol = Double.parseDouble(f.getFeatureValue());
						if(yeol != -1)
						{
							yeolValuesDTO.getYeols().add(yeol);
						}
					}

				});
			}
			List<PartdetailsFeatures> summaryFeatures = sectionFeatures.get(PartdetailsCategories.SUMMARY.getCategoryName());
			if(summaryFeatures != null)
			{
				htsusacoo.accept(partsSize, summaryFeatures);
			}

		});
		FeaturesStatistic gSheetStatistic = new FeaturesStatistic();
		gSheetStatistic.setSourceInformationStatistic(sourceInformationStatistic);
		gSheetStatistic.setLifecycleDTO(lifecycleDTO);
		gSheetStatistic.setInventoryRiskDTO(inventoryRiskDTO);
		gSheetStatistic.setYeolValuesDTO(yeolValuesDTO);
		gSheetStatistic.setHtsusacoo(htsusacoo);
		logger.info("Getting statistic finished in:{} ms", System.currentTimeMillis() - start);
		return gSheetStatistic;
	}
}
