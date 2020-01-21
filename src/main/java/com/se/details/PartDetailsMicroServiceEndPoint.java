package com.se.details;

import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.se.details.dto.PartDetailsRequestDTO;
import com.se.details.dto.PartDetailsResponse;
import com.se.details.enumeration.PartDetailsResultType;
import com.se.details.exception.InvalidRequestParameterException;
import com.se.details.services.PartdetailsSearchStrategyImpl;
import com.se.details.services.cache.ReloadCacheService;
import com.se.details.util.PDMicroserviceConstants;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;
import java.util.Set;

/**
 * @author MAHMOUD_ABDELHAKAM
 */
@RestController
@RequestMapping("/api")
@Api(tags = { "Part Details EndPoint" })
public class PartDetailsMicroServiceEndPoint
{

	private final Logger logger = LoggerFactory.getLogger(PartDetailsMicroServiceEndPoint.class);

	private final PartdetailsMicroserviceController partdetailsMicroserviceController;
	private final PartdetailsSearchStrategyImpl partdetailsSearchStrategyImpl;
	private final ReloadCacheService reloadCacheService;

	public PartDetailsMicroServiceEndPoint(PartdetailsMicroserviceController partdetailsMicroserviceController, PartdetailsSearchStrategyImpl partdetailsSearchStrategyImpl, ReloadCacheService reloadCacheService)
	{
		this.partdetailsMicroserviceController = partdetailsMicroserviceController;
		this.partdetailsSearchStrategyImpl = partdetailsSearchStrategyImpl;
		this.reloadCacheService = reloadCacheService;
	}

	@ApiOperation(value = "get parts details with statistics")
	@PostMapping("/v1/parts")
	public ResponseEntity<MappingJacksonValue> getPartDetails(@Valid @RequestParam(name = "resultType", required = false) PartDetailsResultType partDetailsResultType, @Valid @RequestBody PartDetailsRequestDTO partDetailsRequestDTO,
			BindingResult bindingResult)
	{
		logger.info("REST request to get part details with result type {}, and query {}", partDetailsResultType, partDetailsRequestDTO);
		if(bindingResult.hasErrors())
		{
			throw new InvalidRequestParameterException(bindingResult);
		}
		PartDetailsResponse partDetailsResponse = partdetailsMicroserviceController.partdetailsSearch(partDetailsRequestDTO, partdetailsSearchStrategyImpl);
		Set<String> fields = Optional.ofNullable(partDetailsResultType).orElse(PartDetailsResultType.FULL).getFields();
		return ResponseEntity.ok(getPartDetailsResponseWithSpecificFields(partDetailsResponse, fields));
	}

	private MappingJacksonValue getPartDetailsResponseWithSpecificFields(PartDetailsResponse partdetailsResponse, Set<String> fields)
	{
		MappingJacksonValue wrapper = new MappingJacksonValue(partdetailsResponse);
		wrapper.setFilters(new SimpleFilterProvider().addFilter(PDMicroserviceConstants.Jackson.PART_DETAILS_FILTER_NAME, SimpleBeanPropertyFilter.filterOutAllExcept(fields)));
		return wrapper;
	}

	@GetMapping("/refreshCaching")
	@ApiOperation(value = "Refresh Cache", hidden = true)
	public ResponseEntity<String> refreshCaching()
	{
		reloadCacheService.reloadCache();
		return new ResponseEntity<>("Caching refreshed successfully", HttpStatus.OK);
	}

}
