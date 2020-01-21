package com.se.details.services.actors.parametric;

import com.se.details.dto.PartdetailsFeatures;
import com.se.details.services.PDHelperService;
import com.se.details.services.SESolrConnectorService;
import com.se.details.services.cache.PartsFeaturesCacheService;
import com.se.details.services.factory.PDSolrServerDelegate;
import com.se.details.util.PDMicroserviceConstants;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.common.SolrDocument;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

@SpringBootTest
public class ParametricCategoryActorHelperTest
{
	private final PDSolrServerDelegate solrService = mock(PDSolrServerDelegate.class);
	private final PDHelperService helperService = new PDHelperService();
	private final PartsFeaturesCacheService cacheService = mock(PartsFeaturesCacheService.class);
	private final SESolrConnectorService solrConnector = mock(SESolrConnectorService.class);
	private final ParametricCategoryActorHelper parametricDetailsActor = new ParametricCategoryActorHelper(solrService, helperService, cacheService, solrConnector);
	private final Map<String, PartdetailsFeatures> hColNameToFeatures = new HashMap<>();

	@Before
	public void init()
	{
		MockitoAnnotations.initMocks(this);
		hColNameToFeatures.put("C_4567", new PartdetailsFeatures("C_4567", "Max opertaing temp", "120", "C"));
		hColNameToFeatures.put("C_3578", new PartdetailsFeatures("C_3578", "Type", "semi", ""));
		hColNameToFeatures.put("C_1300", new PartdetailsFeatures("C_1300", "Maximum DC Resistance", "0.469", "Ohm"));
	}

	@Test
	public void filterNonParametricFields_ShouldReturnTrue_WhenFeatureStartsWithC()
	{
		boolean expected = parametricDetailsActor.filterNonParametricFields.test("C_456");
		assertTrue(expected);
	}

	@Test
	public void filterNonParametricFields_ShouldReturnFalse_WhenFeatureStartsWithC()
	{
		boolean expected = parametricDetailsActor.filterNonParametricFields.test("ROHS");
		assertFalse(expected);
	}

	@Test
	public void removeValueFromHColName_ShouldReturnValidHColName()
	{
		String hColName = "C_4587";
		String value = hColName + "_VALUE";
		String expected = parametricDetailsActor.removeValueFromHColName.apply(value);
		assertEquals(expected, hColName);
	}

	@Test
	public void formateFeatureValuesQuery_ShouldReturnQuery()
	{
		List<String> parts = Arrays.asList("456456", "45646", "7897");
		SolrQuery featureValues = new SolrQuery();
		String q = parts.stream().collect(Collectors.joining(" ", "PART_ID:(", ")"));
		featureValues.setQuery(q);
		featureValues.set("fl", PDMicroserviceConstants.ParametricSolrFields.FILTERD_FIELDS);
		featureValues.setRows(parts.size());
		SolrQuery query = parametricDetailsActor.formateFeatureValuesQuery(parts);
		assertEquals(query.toString(), featureValues.toString());
	}

	@Test
	public void convertHColNameToFeature_ShouldReturnFeature_WhenHColNameExistsInCache()
	{
		String fullValue = ".469!~!0.469(Typ)!~!0.469!~!!~!Ohm!~!";
		String hColName = "C_1300";
		SolrDocument d = new SolrDocument();
		d.addField(hColName + "_FULL", fullValue);
		String[] splittedValues = fullValue.split("!~!", -1);
		PartdetailsFeatures fet = parametricDetailsActor.convertHColNameToFeature(hColName, hColNameToFeatures, d);
		boolean isValidFeature =
				fet.getHColName().equals(hColName) && fet.getFeaturesName().equals(hColNameToFeatures.get(hColName).getFeaturesName()) && fet.getFeatureValue().equals(splittedValues[2]) && fet.getMultiplier().equals(splittedValues[3]) && fet
						.getFeatureUnit().equals(splittedValues[4]);
		assertTrue(isValidFeature);
	}

	@Test
	public void convertHColNameToFeature_ShouldReturnNull_WhenFullValueIsEmpty()
	{
		String fullValue = "";
		String hColName = "C_1300";
		SolrDocument d = new SolrDocument();
		d.addField(hColName + "_FULL", fullValue);
		PartdetailsFeatures fet = parametricDetailsActor.convertHColNameToFeature(hColName, hColNameToFeatures, d);
		assertNull(fet);
	}

	@Test
	public void convertHColNameToFeature_ShouldReturnNull_WhenHColNameAbsentCache()
	{
		String fullValue = ".469!~!0.469(Typ)!~!0.469!~!!~!Ohm!~!";
		String hColName = "C_1300";
		SolrDocument d = new SolrDocument();
		d.addField(hColName + "_FULL", fullValue);
		PartdetailsFeatures fet = parametricDetailsActor.convertHColNameToFeature("C_45454554", hColNameToFeatures, d);
		assertNull(fet);
	}
}