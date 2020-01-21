package com.se.details.services.actors.summary;

import com.se.details.dto.PartdetailsFeatures;
import com.se.details.services.PDHelperService;
import com.se.details.services.SESolrConnectorService;
import com.se.details.services.actors.FeaturesPublisherFunction;
import com.se.details.services.factory.PDSolrServerDelegate;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class SummaryCategoryActorHelper
{
	private PDSolrServerDelegate solrService;
	private PDHelperService helperService;
	private SESolrConnectorService solrConnector;
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	protected Map<String, String> solrFeaturesMap = null;

	@Autowired
	public SummaryCategoryActorHelper(PDSolrServerDelegate solrService, PDHelperService helperService, SESolrConnectorService solrConnector)
	{
		super();
		this.solrService = solrService;
		this.helperService = helperService;
		this.solrConnector = solrConnector;
	}

	protected Function<Set<String>, String> getFilteredQuery = features -> features.stream().collect(Collectors.joining(","));
	protected Function<List<String>, String> formatePartsSummaryStr = comIds -> comIds.stream().collect(Collectors.joining(" ", "COM_ID:(", ")"));
	protected BiFunction<SolrDocument, Set<String>, List<PartdetailsFeatures>> mapFilteredFeatures = (d, filteredFeature) -> filteredFeature.stream().map(f -> mapSingleFeature(d, f)).collect(Collectors.toList());

	protected FeaturesPublisherFunction getRiskFeatures = message -> {
		List<String> comIds = message.getComIds();
		Map<String, List<PartdetailsFeatures>> featuresMap = new HashMap<>();
		Map<String, SolrDocument> partToSolrDocument = new HashMap<>();
		try
		{
			solrFeaturesMap = message.getFeatureMappingSupplier().get();
			Set<String> filteredFeatures = message.getDefaultOrFilteredFeatures().apply(message.getFeautres());
			//Parts Summary core
			SolrDocumentList summaryDocumentList = getSummaryCoreDocuments(message.getComIds(), filteredFeatures, formatePartsSummaryStr, getFilteredQuery);
			fillUnifiedSolrDocument(partToSolrDocument, summaryDocumentList, "COM_ID", filteredFeatures);
			//COO core
			summaryDocumentList = getCOOCoreDocuments(message.getComIds(), filteredFeatures, formatePartsSummaryStr, getFilteredQuery);
			fillUnifiedSolrDocument(partToSolrDocument, summaryDocumentList, "COM_ID", filteredFeatures);
			featuresMap = mapSolrDocumentToFeatures(getSolrDocumentList(partToSolrDocument), filteredFeatures);
		}
		catch(Exception e)
		{
			logger.error("Error during getting Summary data", e);
		}
		helperService.fillEmptyMap(comIds, featuresMap);
		return featuresMap;
	};

	protected SolrDocumentList getSolrDocumentList(Map<String, SolrDocument> partToSolrDocument)
	{
		SolrDocumentList list = new SolrDocumentList();
		list.addAll(partToSolrDocument.values());
		return list;
	}

	protected SolrDocumentList getSummaryCoreDocuments(List<String> comIds, Set<String> filteredFeatures, Function<List<String>, String> formatePartsSummaryStr, Function<Set<String>, String> getFilteredQuery) throws IOException, SolrServerException
	{
		SolrQuery solrQuery = formatePartsSummarySolrQuery(formatePartsSummaryStr, getFilteredQuery, filteredFeatures, comIds);
		QueryResponse response = solrConnector.executeSorlQueryPOST(solrQuery, solrService.getSummarySolrServer());
		if(!helperService.isQueryResultNullOrEmpty(response))
		{
			return response.getResults();
		}
		return null;
	}

	protected SolrDocumentList getCOOCoreDocuments(List<String> comIds, Set<String> filteredFeatures, Function<List<String>, String> formatePartsSummaryStr, Function<Set<String>, String> getFilteredQuery) throws IOException, SolrServerException
	{
		SolrQuery solrQuery = formatePartsSummarySolrQuery(formatePartsSummaryStr, getFilteredQuery, filteredFeatures, comIds);
		QueryResponse response = solrConnector.executeSorlQueryPOST(solrQuery, solrService.getCooSolrServer());
		if(!helperService.isQueryResultNullOrEmpty(response))
		{
			return response.getResults();
		}
		return null;
	}

	protected void fillUnifiedSolrDocument(Map<String, SolrDocument> partToSolrDocumentMap, SolrDocumentList documents, String partFieldName, Set<String> solrFields)
	{
		documents.stream().forEach(d -> {
			String comId = helperService.emptyStringIfNull(d.getFieldValue(partFieldName));
			SolrDocument solrDocument = partToSolrDocumentMap.get(comId);
			if(solrDocument == null)
			{
				solrDocument = new SolrDocument();
				solrDocument.addField("COM_ID", comId);
			}
			SolrDocument finalSolrDocument = solrDocument;
			solrFields.forEach(f -> {
				String value = helperService.emptyStringIfNull(d.getFieldValue(f));
				if(value != null && !value.isEmpty())
				{
					finalSolrDocument.addField(f, value);
				}
			});
			partToSolrDocumentMap.put(comId, finalSolrDocument);
		});
	}

	protected SolrQuery formatePartsSummarySolrQuery(Function<List<String>, String> formatePartsSummaryStr, Function<Set<String>, String> getFilteredQuery, Set<String> filteredFeatures, List<String> comIds)
	{
		SolrQuery solrQuery = new SolrQuery();
		solrQuery.set("q", formatePartsSummaryStr.apply(comIds));
		solrQuery.set("fl", "COM_ID," + getFilteredQuery.apply(filteredFeatures));
		solrQuery.setRows(comIds.size());
		logger.info("SummarQuery: {}", solrQuery);
		return solrQuery;
	}

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
