package com.se.details.services;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrRequest;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class SESolrConnectorService
{
	public QueryResponse executeSorlQueryPOST(SolrQuery query, SolrClient solrCore) throws SolrServerException, IOException
	{
		return solrCore.query(query, SolrRequest.METHOD.POST);
	}
}
