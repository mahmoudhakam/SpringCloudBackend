package com.se.details;

import com.se.details.dto.*;
import com.se.details.services.StatisticsFunctions;
import com.se.details.services.strategy.PartdetailsStrategy;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @author MAHMOUD_ABDELHAKAM
 */
@Service
public class PartdetailsMicroserviceController
{
	public PartDetailsResponse partdetailsSearch(PartDetailsRequestDTO partdetailsRequestDTO, PartdetailsStrategy partdetailsSearchStrategyImpl)
	{
		long start = System.currentTimeMillis();
		PartDetailsResponse partdetailsResponse = new PartDetailsResponse(new Status(OperationMessages.SUCCESSFULL_OPERATION, true));
		// request validation will add validation strategy

		Set<CategoryDTO> sentCats = partdetailsRequestDTO.getCategories();
		Map<String, Set<String>> categories = new HashMap<>();
		sentCats.forEach(c -> categories.put(c.getCategoryName(), c.getFeatures()));

		Map<String, Map<String, List<PartdetailsFeatures>>> response = partdetailsSearchStrategyImpl.publishPartdetailsData(new ArrayList<>(partdetailsRequestDTO.getComIds()), categories, true);
		if(response.isEmpty())
		{
			partdetailsResponse = new PartDetailsResponse(new Status(OperationMessages.NO_RESULT_FOUND, true));
			partdetailsResponse.setServiceTime((System.currentTimeMillis() - start) + " ms");
			return partdetailsResponse;
		}

		partdetailsResponse.setgSheetStatistic(getFeaturesStatistics(response));
		partdetailsResponse.setResponse(response);
		partdetailsResponse.setServiceTime((System.currentTimeMillis() - start) + " ms");

		return partdetailsResponse;
	}

	private FeaturesStatistic getFeaturesStatistics(Map<String, Map<String, List<PartdetailsFeatures>>> response)
	{
		StatisticsFunctions statisticsFunction = new StatisticsFunctions();
		return statisticsFunction.getStatistics(response);
	}

}
