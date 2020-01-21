package com.se.details.services.cache;
/**
 * @author MAHMOUD_ABDELHAKAM
 */

import com.google.common.cache.CacheLoader;
import com.se.details.dto.PartdetailsFeatures;
import com.se.details.dto.TaxonomyCachedResultDTO;
import com.se.details.services.PDHelperService;
import com.se.details.services.SESolrConnectorService;
import com.se.details.services.factory.PDSolrServerDelegate;
import com.se.details.util.PDMicroserviceConstants;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class TaxonomyResultCacheService extends CacheLoader<String, Optional<TaxonomyCachedResultDTO>>
{

	private PDSolrServerDelegate solrService;
	private PDHelperService helperService;
	private SESolrConnectorService solrConnector;
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	public TaxonomyResultCacheService(PDSolrServerDelegate solrService, PDHelperService helperService, SESolrConnectorService solrConnector)
	{
		this.solrService = solrService;
		this.helperService = helperService;
		this.solrConnector = solrConnector;
	}

	Function<SolrDocument, PartdetailsFeatures> convertDocumentToFeature = d -> {
		PartdetailsFeatures feature = new PartdetailsFeatures();
		feature.setFeaturesName(helperService.emptyStringIfNull(d.getFieldValue(PDMicroserviceConstants.TaxonomySolrFields.FEATURENAME)));
		feature.setHColName(helperService.emptyStringIfNull(d.getFieldValue(PDMicroserviceConstants.TaxonomySolrFields.HCOLNAME)));
		return feature;
	};

	@Override
	public Optional<TaxonomyCachedResultDTO> load(String query) throws Exception
	{
		return getParametricTaxonomyResult(query);
	}

	protected Optional<TaxonomyCachedResultDTO> getParametricTaxonomyResult(String query)
	{
		TaxonomyCachedResultDTO nullableCache = null;
		SolrQuery solrQuery = formateNumberOfDocumentsQuery(query);
		try
		{
			QueryResponse response = solrConnector.executeSorlQueryPOST(solrQuery, solrService.getTaxonomySolrServer());
			if(response != null)
			{
				solrQuery = formateTaxonomyQuery(query, response.getResults().getNumFound());
				response = solrConnector.executeSorlQueryPOST(solrQuery, solrService.getTaxonomySolrServer());
				SolrDocumentList documents = response.getResults();
				Map<String, PartdetailsFeatures> features = mapHColNameToFeature(documents);
				nullableCache = new TaxonomyCachedResultDTO();
				nullableCache.setFeatures(features);
				return Optional.ofNullable(nullableCache);
			}
		}
		catch(SolrServerException e)
		{
			logger.error("Solr Server Error calling taxonomy core", e);
		}
		catch(IOException e)
		{
			logger.error("IO Error calling taxonomy core", e);
		}
		return Optional.ofNullable(nullableCache);
	}

	protected SolrQuery formateTaxonomyQuery(final String queryStr, long rows)
	{
		SolrQuery query = new SolrQuery();
		query.set("q", queryStr);
		query.set("fl", PDMicroserviceConstants.TaxonomySolrFields.FEATURENAME, PDMicroserviceConstants.TaxonomySolrFields.HCOLNAME);
		query.setRows((int) rows);
		return query;
	}

	protected SolrQuery formateNumberOfDocumentsQuery(final String queryStr)
	{
		SolrQuery query = new SolrQuery();
		query.set("q", queryStr);
		query.setRows(0);
		return query;
	}

	protected Map<String, PartdetailsFeatures> mapHColNameToFeature(SolrDocumentList documents)
	{
		return documents.stream().map(convertDocumentToFeature).collect(Collectors.toMap(PartdetailsFeatures::getHColName, Function.identity(), (d1, d2) -> d1));
	}

}
