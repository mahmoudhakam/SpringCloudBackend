package com.se.details.services.factory;

import lombok.Getter;
import org.apache.solr.client.solrj.SolrClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Getter
public class PDSolrServerDelegate
{
	private final SolrClient parametricSolrServer;
	private final SolrClient taxonomySolrServer;
	private final SolrClient lcForcastSolrServer;
	private final SolrClient summarySolrServer;
	private final SolrClient cooSolrServer;

	@Autowired
	public PDSolrServerDelegate(SolrClient parametricSolrServer, SolrClient taxonomySolrServer, SolrClient lcForcastSolrServer, SolrClient summarySolrServer,SolrClient cooSolrServer)
	{
		super();
		this.parametricSolrServer = parametricSolrServer;
		this.taxonomySolrServer = taxonomySolrServer;
		this.lcForcastSolrServer = lcForcastSolrServer;
		this.summarySolrServer = summarySolrServer;
		this.cooSolrServer = cooSolrServer;
	}
}
