package com.se.details.services.cache;

import com.se.details.dto.PartdetailsFeatures;
import com.se.details.dto.TaxonomyCachedResultDTO;
import com.se.details.services.PDHelperService;
import com.se.details.services.SESolrConnectorService;
import com.se.details.services.factory.PDSolrServerDelegate;
import com.se.details.util.PDMicroserviceConstants;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * @author MAHMOUD_ABDELHAKAM
 */
@SpringBootTest
public class TaxonomyResultCacheServiceTest
{
	private final PDSolrServerDelegate solrServerDelegate = mock(PDSolrServerDelegate.class);
	private final PDHelperService helperService = new PDHelperService();
	private final SESolrConnectorService solrConnector = mock(SESolrConnectorService.class);
	private final TaxonomyResultCacheService taxonomyResultCacheService = new TaxonomyResultCacheService(solrServerDelegate, helperService, solrConnector);
	private final String CACHEALLTAXONOMYQUERYKEY = "*:* AND SHEETVIEWFLAG:(1 3) AND PACKAGEFLAG:0";

	@Before
	public void init()
	{
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void shouldReturnCachedHColNameToFeatures_WhenHColNameExists() throws IOException, SolrServerException
	{
		QueryResponse response = mock(QueryResponse.class);
		SolrDocumentList results = new SolrDocumentList();
		SolrDocument document = new SolrDocument();
		String hColName = "C_2345";
		document.addField(PDMicroserviceConstants.TaxonomySolrFields.FEATURENAME, "Maximun Operating Temp");
		document.addField(PDMicroserviceConstants.TaxonomySolrFields.HCOLNAME, hColName);
		results.add(document);
		results.setNumFound(3);
		SolrClient taxonomySolrServer = mock(SolrClient.class);
		when(solrServerDelegate.getTaxonomySolrServer()).thenReturn(taxonomySolrServer);
		when(solrConnector.executeSorlQueryPOST(any(SolrQuery.class), eq(taxonomySolrServer))).thenReturn(response);
		when(response.getResults()).thenReturn(results);
		TaxonomyCachedResultDTO actual = taxonomyResultCacheService.getParametricTaxonomyResult(CACHEALLTAXONOMYQUERYKEY).get();
		boolean nonEmptyFeatures = actual.getFeatures().containsKey(hColName);
		assertTrue(nonEmptyFeatures);
	}

	@Test
	public void shouldReturnOptionalOfNullableCache_WhenResponseIsNull() throws IOException, SolrServerException
	{
		when(solrConnector.executeSorlQueryPOST(any(SolrQuery.class), any(SolrClient.class))).thenReturn(null);
		Optional<TaxonomyCachedResultDTO> actual = taxonomyResultCacheService.getParametricTaxonomyResult(CACHEALLTAXONOMYQUERYKEY);
		boolean isEmpty = actual.isPresent();
		assertFalse(isEmpty);
	}

	@Test
	public void mapHColNameToFeature_shouldReturnHColNameAsKey()
	{
		String hColName = "C_2345";
		SolrDocumentList documents = new SolrDocumentList();
		SolrDocument document = new SolrDocument();
		document.addField(PDMicroserviceConstants.TaxonomySolrFields.FEATURENAME, "Maximun Operating Temp");
		document.addField(PDMicroserviceConstants.TaxonomySolrFields.HCOLNAME, hColName);
		documents.add(document);
		String hColName2 = "C_7865";
		document = new SolrDocument();
		document.addField(PDMicroserviceConstants.TaxonomySolrFields.FEATURENAME, "Type");
		document.addField(PDMicroserviceConstants.TaxonomySolrFields.HCOLNAME, hColName);
		documents.add(document);
		Map<String, PartdetailsFeatures> map = taxonomyResultCacheService.mapHColNameToFeature(documents);
		PartdetailsFeatures fet = map.get(hColName);
		String expected = fet.getFeaturesName();
		String actual = "Maximun Operating Temp";
		assertEquals(expected, actual);
	}

	@Test
	public void formateNumberOfDocumentsQuery_ShouldContainsZeroRows()
	{
		SolrQuery expected = taxonomyResultCacheService.formateNumberOfDocumentsQuery(CACHEALLTAXONOMYQUERYKEY);
		SolrQuery query = new SolrQuery();
		query.set("q", CACHEALLTAXONOMYQUERYKEY);
		query.setRows(0);
		assertEquals(expected.toString(), query.toString());
	}

	@Test
	public void formateTaxonomyQuery_ShouldWellBeFormatted()
	{
		long rows = 3;
		SolrQuery expected = taxonomyResultCacheService.formateTaxonomyQuery(CACHEALLTAXONOMYQUERYKEY, rows);
		SolrQuery query = new SolrQuery();
		query.set("q", CACHEALLTAXONOMYQUERYKEY);
		query.set("fl", PDMicroserviceConstants.TaxonomySolrFields.FEATURENAME, PDMicroserviceConstants.TaxonomySolrFields.HCOLNAME);
		query.setRows((int) rows);
		assertEquals(expected.toString(), query.toString());
	}

	@Test
	public void convertDocumentToFeature_shouldReturnValidFeature()
	{
		String hColName = "C_2345";
		String fetName = "Type";
		SolrDocument d = new SolrDocument();
		d.addField(PDMicroserviceConstants.TaxonomySolrFields.FEATURENAME, fetName);
		d.addField(PDMicroserviceConstants.TaxonomySolrFields.HCOLNAME, hColName);
		PartdetailsFeatures feature = taxonomyResultCacheService.convertDocumentToFeature.apply(d);
		boolean isValidFeature = feature.getFeaturesName().equals(fetName) && feature.getHColName().equals(hColName);
		assertTrue(isValidFeature);
	}
}
