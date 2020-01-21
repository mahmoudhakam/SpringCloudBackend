package com.se.details.services.actors.risk;

import com.se.details.dto.PartdetailsFeatures;
import com.se.details.services.PDHelperService;
import com.se.details.services.SESolrConnectorService;
import com.se.details.services.actors.FeaturesPublisherFunction;
import com.se.details.services.factory.PDSolrServerDelegate;
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
import java.util.stream.Collectors;

@Service
public class RiskCategoryActorHelper
{
	private PDSolrServerDelegate solrService;
	private PDHelperService helperService;
	private SESolrConnectorService solrConnector;
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	protected Map<String, String> solrFeaturesMap = null;

	@Autowired
	public RiskCategoryActorHelper(PDSolrServerDelegate solrService, PDHelperService helperService, SESolrConnectorService solrConnector)
	{
		super();
		this.solrService = solrService;
		this.helperService = helperService;
		this.solrConnector = solrConnector;
	}

	protected SolrQuery formateRiskSolrQuery(String query, int rows, String filterQuery)
	{
		SolrQuery solrQuery = new SolrQuery();
		solrQuery.set("q", query);
		solrQuery.set("fl", "COM_ID," + filterQuery);
		solrQuery.setRows(rows);
		logger.info("RiskQuery: {}", solrQuery);
		return solrQuery;
	}

	protected String formateRiskQueryStr(List<String> comIds)
	{
		return comIds.stream().collect(Collectors.joining(" ", "COM_ID:(", ")"));
	}

	protected Function<Set<String>, String> getFilteredQuery = features -> features.stream().collect(Collectors.joining(","));
	protected BiFunction<SolrDocument, Set<String>, List<PartdetailsFeatures>> mapFilteredFeatures = (d, filteredFeature) -> filteredFeature.stream().map(f -> mapSingleFeature(d, f)).collect(Collectors.toList());

	protected FeaturesPublisherFunction getRiskFeatures = message -> {
		List<String> comIds = message.getComIds();
		Map<String, List<PartdetailsFeatures>> featuresMap = new HashMap<>();
		try
		{
			solrFeaturesMap = message.getFeatureMappingSupplier().get();
			String query = formateRiskQueryStr(comIds);
			Set<String> filteredFeatures = message.getDefaultOrFilteredFeatures().apply(message.getFeautres());
			String filterQuery = getFilteredQuery.apply(filteredFeatures);
			SolrQuery solrQuery = formateRiskSolrQuery(query, comIds.size(), filterQuery);
			QueryResponse response = solrConnector.executeSorlQueryPOST(solrQuery, solrService.getLcForcastSolrServer());
			if(!helperService.isQueryResultNullOrEmpty(response))
			{
				SolrDocumentList documents = response.getResults();
				featuresMap = mapSolrDocumentToFeatures(documents, filteredFeatures);
			}
		}
		catch(Exception e)
		{
			logger.error("Error during getting Risk data", e);
		}
		helperService.fillEmptyMap(comIds, featuresMap);
		return featuresMap;
	};

	protected Map<String, List<PartdetailsFeatures>> mapSolrDocumentToFeatures(SolrDocumentList documents, Set<String> filteredFeatures)
	{
		return documents.stream().map(d -> mapSingleDocumentToFeatureList(d, filteredFeatures, mapFilteredFeatures)).filter(Objects::nonNull).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (o, n) -> o));
	}

	protected Map.Entry<String, List<PartdetailsFeatures>> mapSingleDocumentToFeatureList(SolrDocument d, Set<String> filteredFeatures, BiFunction<SolrDocument, Set<String>, List<PartdetailsFeatures>> mapFilteredFeatures)
	{
		String comId = helperService.emptyStringIfNull(d.getFieldValue("COM_ID"));
		if(comId.isEmpty())
		{
			return null;
		}
		return new AbstractMap.SimpleEntry<>(comId, mapFilteredFeatures.apply(d, filteredFeatures));
	}

	protected PartdetailsFeatures mapSingleFeature(SolrDocument d, String f)
	{
		return new PartdetailsFeatures(solrFeaturesMap.get(f) != null ? solrFeaturesMap.get(f) : f, helperService.emptyStringIfNull(d.getFieldValue(f)));
	}

}
