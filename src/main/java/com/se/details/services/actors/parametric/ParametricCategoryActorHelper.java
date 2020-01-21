package com.se.details.services.actors.parametric;

import com.se.details.dto.PartdetailsFeatures;
import com.se.details.dto.TaxonomyCachedResultDTO;
import com.se.details.services.PDHelperService;
import com.se.details.services.SESolrConnectorService;
import com.se.details.services.actors.FeaturesPublisherFunction;
import com.se.details.services.cache.PartsFeaturesCacheService;
import com.se.details.services.factory.PDSolrServerDelegate;
import com.se.details.util.PDMicroserviceConstants;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;

@Service
public class ParametricCategoryActorHelper
{
	private static final String CACHEALLTAXONOMYQUERYKEY = "*:* AND SHEETVIEWFLAG:(1 3) AND PACKAGEFLAG:0";
	private PDSolrServerDelegate solrService;
	private PDHelperService helperService;
	private PartsFeaturesCacheService cacheService;
	private SESolrConnectorService solrConnector;
	protected UnaryOperator<String> removeValueFromHColName = f -> f.substring(0, f.lastIndexOf('_'));
	protected Predicate<String> filterNonParametricFields = f -> f.startsWith("C_");
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	protected FeaturesPublisherFunction getParametricFeaturesFunction = message -> {
		List<String> comIds = message.getComIds();
		Map<String, List<PartdetailsFeatures>> featuresMap = new HashMap<>();
		try
		{
			TaxonomyCachedResultDTO taxonomyCachedResultDTO = cacheService.getTaxonomyResult(CACHEALLTAXONOMYQUERYKEY);
			if(taxonomyCachedResultDTO == null)
			{
				helperService.fillEmptyMap(comIds, featuresMap);
				return featuresMap;
			}
			SolrQuery partsQueryStr = formateFeatureValuesQuery(comIds);
			QueryResponse response = solrConnector.executeSorlQueryPOST(partsQueryStr, solrService.getParametricSolrServer());
			if(!helperService.isQueryResultNullOrEmpty(response))
			{
				SolrDocumentList documents = response.getResults();
				featuresMap = convertSolrDocumentsToFeaturesV2(documents, taxonomyCachedResultDTO.getFeatures(), message.getFeautres());
			}
		}
		catch(Exception e)
		{
			logger.error("Error during running paramtric search -- ", e);
		}
		helperService.fillEmptyMap(comIds, featuresMap);
		return featuresMap;
	};



	@Autowired
	public ParametricCategoryActorHelper(PDSolrServerDelegate solrService, PDHelperService helperService, PartsFeaturesCacheService cacheService, SESolrConnectorService solrConnector)
	{
		this.solrService = solrService;
		this.helperService = helperService;
		this.cacheService = cacheService;
		this.solrConnector = solrConnector;
	}

	protected Map<String, List<PartdetailsFeatures>> convertSolrDocumentsToFeaturesV2(SolrDocumentList documents, Map<String, PartdetailsFeatures> hColNameToFeatures, Set<String> filteredFeatures)
	{
		Predicate<Map.Entry<String, List<PartdetailsFeatures>>> filterFeatures = e -> {
			Predicate<PartdetailsFeatures> filterFeaturesFunction = c -> c.getFeaturesName().isEmpty() || !filteredFeatures.contains(c.getFeaturesName());
			Predicate<PartdetailsFeatures> filterEmptyNamesFeatures = c -> c.getFeaturesName().isEmpty();
			List<PartdetailsFeatures> v = e.getValue();
			boolean filterd = false;
			if(filteredFeatures == null || filteredFeatures.isEmpty())
			{
				filterd = v.removeIf(filterEmptyNamesFeatures);
			}
			else
			{
				filterd = v.removeIf(filterFeaturesFunction);
			}
			return filterd;
		};
		return documents.stream().map(d -> convertDocumentToPartAndFeatures(d, hColNameToFeatures)).filter(filterFeatures).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (oldValue, newValue) -> oldValue));
	}

	protected Map.Entry<String, List<PartdetailsFeatures>> convertDocumentToPartAndFeatures(SolrDocument d, Map<String, PartdetailsFeatures> hColNameToFeatures)
	{
		String comId = helperService.emptyStringIfNull(d.getFieldValue(PDMicroserviceConstants.ParametricSolrFields.PART_ID));
		List<String> hColNames = d.getFieldNames().stream().filter(filterNonParametricFields).map(removeValueFromHColName).collect(Collectors.toList());
		if(!hColNames.isEmpty())
		{
			return new AbstractMap.SimpleEntry<>(comId, generateFeatureListFromHColName(hColNames, hColNameToFeatures, d));
		}
		return new AbstractMap.SimpleEntry<>(comId, new ArrayList<>());
	}

	protected List<PartdetailsFeatures> generateFeatureListFromHColName(List<String> hColNames, Map<String, PartdetailsFeatures> hColNameToFeatures, SolrDocument d)
	{
		return hColNames.stream().map(h -> convertHColNameToFeature(h, hColNameToFeatures, d)).filter(Objects::nonNull).collect(Collectors.toList());
	}

	protected PartdetailsFeatures convertHColNameToFeature(String hColName, Map<String, PartdetailsFeatures> hColNameToFeatures, SolrDocument d)
	{
		PartdetailsFeatures feature = hColNameToFeatures.get(hColName);
		if(feature == null)
		{
			return null;
		}
		String fullValue = helperService.emptyStringIfNull(d.getFieldValue(hColName + PDMicroserviceConstants.ParametricSolrFields.FULL_VALUE));
		if(fullValue.isEmpty())
		{
			return null;
		}
		String[] splittedValues = fullValue.split("!~!", -1);
		PartdetailsFeatures tempFeature = new PartdetailsFeatures();
		tempFeature.setHColName(feature.getHColName());
		tempFeature.setFeaturesName(feature.getFeaturesName());
		tempFeature.setFeatureValue(helperService.emptyStringIfNull(splittedValues[2]));
		tempFeature.setMultiplier(helperService.emptyStringIfNull(splittedValues[3]));
		tempFeature.setFeatureUnit(helperService.emptyStringIfNull(splittedValues[4]));
		tempFeature.setMultiplierValue(helperService.getMultiplierValue(tempFeature.getMultiplier(), tempFeature.getFeatureUnit()));
		return tempFeature;
	}

	protected SolrQuery formateFeatureValuesQuery(List<String> parts)
	{
		SolrQuery featureValues = new SolrQuery();
		String q = parts.stream().collect(Collectors.joining(" ", "PART_ID:(", ")"));
		featureValues.setQuery(q);
		featureValues.set("fl", PDMicroserviceConstants.ParametricSolrFields.FILTERD_FIELDS);
		featureValues.setRows(parts.size());
		logger.info("ParametricCore Query :{}", featureValues);
		return featureValues;
	}
}
