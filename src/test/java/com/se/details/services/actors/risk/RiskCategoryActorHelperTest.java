package com.se.details.services.actors.risk;

import com.se.details.services.PDHelperService;
import com.se.details.services.SESolrConnectorService;
import com.se.details.services.factory.PDSolrServerDelegate;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;
import java.util.StringJoiner;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

@SpringBootTest
public class RiskCategoryActorHelperTest
{
	private final PDSolrServerDelegate solrService = mock(PDSolrServerDelegate.class);
	private final PDHelperService helperService = new PDHelperService();
	private final SESolrConnectorService solrConnector = mock(SESolrConnectorService.class);
	private final RiskCategoryActorHelper riskCategoryActorHelper = new RiskCategoryActorHelper(solrService, helperService, solrConnector);

	@Before
	public void init()
	{
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void formateRiskQueryStr_ShouldbeWellFormatted()
	{
		List<String> parts = Arrays.asList("1234", "45646", "78955");
		StringJoiner actual = new StringJoiner(" ", "COM_ID:(", ")");
		parts.forEach(actual::add);
		String expected = riskCategoryActorHelper.formateRiskQueryStr(parts);
		assertEquals(expected, actual.toString());
	}
}
