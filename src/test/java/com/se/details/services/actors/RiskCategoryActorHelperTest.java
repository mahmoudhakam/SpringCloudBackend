package com.se.details.services.actors;

import com.se.details.services.PDHelperService;
import com.se.details.services.SESolrConnectorService;
import com.se.details.services.actors.risk.RiskCategoryActorHelper;
import com.se.details.services.factory.PDSolrServerDelegate;
import org.springframework.boot.test.context.SpringBootTest;

import static org.mockito.Mockito.mock;

@SpringBootTest
public class RiskCategoryActorHelperTest
{
	private final PDSolrServerDelegate solrService = mock(PDSolrServerDelegate.class);
	private final PDHelperService helperService = new PDHelperService();
	private final SESolrConnectorService solrConnector = mock(SESolrConnectorService.class);
	private final RiskCategoryActorHelper riskCategoryActorHelper = new RiskCategoryActorHelper(solrService, helperService, solrConnector);
}
