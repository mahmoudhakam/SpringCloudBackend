package com.se.details.util;

import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class PDMicroserviceConstantsTest
{
	@Test(expected = java.lang.IllegalAccessException.class)
	public void validateObjectCreationFailedActorNames() throws ClassNotFoundException, IllegalAccessException, InstantiationException
	{
		Class cls = Class.forName("com.se.details.util.PDMicroserviceConstants$ActorNames");
		PDMicroserviceConstants.ActorNames actorNames = (PDMicroserviceConstants.ActorNames) cls.newInstance(); // exception here
	}

	@Test(expected = java.lang.IllegalAccessException.class)
	public void validateObjectCreationFailedParametricSolrFields() throws ClassNotFoundException, IllegalAccessException, InstantiationException
	{
		Class cls = Class.forName("com.se.details.util.PDMicroserviceConstants$ParametricSolrFields");
		PDMicroserviceConstants.ParametricSolrFields actorNames = (PDMicroserviceConstants.ParametricSolrFields) cls.newInstance(); // exception here
	}

	@Test(expected = java.lang.IllegalAccessException.class)
	public void validateObjectCreationFailedTaxonomySolrFields() throws ClassNotFoundException, IllegalAccessException, InstantiationException
	{
		Class cls = Class.forName("com.se.details.util.PDMicroserviceConstants$TaxonomySolrFields");
		PDMicroserviceConstants.TaxonomySolrFields actorNames = (PDMicroserviceConstants.TaxonomySolrFields) cls.newInstance(); // exception here
	}

	@Test(expected = java.lang.IllegalAccessException.class)
	public void validateObjectCreationFailedPartDetailsEndPointResponseColumns() throws ClassNotFoundException, IllegalAccessException, InstantiationException
	{
		Class cls = Class.forName("com.se.details.util.PDMicroserviceConstants$PartDetailsEndPointResponseColumns");
		PDMicroserviceConstants.PartDetailsEndPointResponseColumns actorNames = (PDMicroserviceConstants.PartDetailsEndPointResponseColumns) cls.newInstance(); // exception here
	}

	@Test(expected = java.lang.IllegalAccessException.class)
	public void validateObjectCreationFailedFeaturesNames() throws ClassNotFoundException, IllegalAccessException, InstantiationException
	{
		Class cls = Class.forName("com.se.details.util.PDMicroserviceConstants$RiksFeaturesNames");
		PDMicroserviceConstants.RiksFeaturesNames actorNames = (PDMicroserviceConstants.RiksFeaturesNames) cls.newInstance(); // exception here
	}

	@Test(expected = java.lang.IllegalAccessException.class)
	public void validateObjectCreationFailedLCForecastSolrFields() throws ClassNotFoundException, IllegalAccessException, InstantiationException
	{
		Class cls = Class.forName("com.se.details.util.PDMicroserviceConstants$LCForecastSolrFields");
		PDMicroserviceConstants.LCForecastSolrFields actorNames = (PDMicroserviceConstants.LCForecastSolrFields) cls.newInstance(); // exception here
	}

	@Test(expected = java.lang.IllegalAccessException.class)
	public void validateObjectCreationFailedJackson() throws ClassNotFoundException, IllegalAccessException, InstantiationException
	{
		Class cls = Class.forName("com.se.details.util.PDMicroserviceConstants$Jackson");
		PDMicroserviceConstants.Jackson actorNames = (PDMicroserviceConstants.Jackson) cls.newInstance(); // exception here
	}

	@Test(expected = java.lang.IllegalAccessException.class)
	public void validateObjectCreationFailedRequestResponseFormat() throws ClassNotFoundException, IllegalAccessException, InstantiationException
	{
		Class cls = Class.forName("com.se.details.util.PDMicroserviceConstants$RequestResponseFormat");
		PDMicroserviceConstants.RequestResponseFormat actorNames = (PDMicroserviceConstants.RequestResponseFormat) cls.newInstance(); // exception here
	}

	@Test(expected = java.lang.IllegalAccessException.class)
	public void validateObjectCreationFailedAsyncConfig() throws ClassNotFoundException, IllegalAccessException, InstantiationException
	{
		Class cls = Class.forName("com.se.details.util.PDMicroserviceConstants$AsyncConfig");
		PDMicroserviceConstants.AsyncConfig actorNames = (PDMicroserviceConstants.AsyncConfig) cls.newInstance(); // exception here
	}

	@Test(expected = java.lang.IllegalAccessException.class)
	public void validateObjectCreationFailedGSheetStatisticsFeaturesNames() throws ClassNotFoundException, IllegalAccessException, InstantiationException
	{
		Class cls = Class.forName("com.se.details.util.PDMicroserviceConstants$GSheetStatisticsFeaturesNames");
		PDMicroserviceConstants.GSheetStatisticsFeaturesNames actorNames = (PDMicroserviceConstants.GSheetStatisticsFeaturesNames) cls.newInstance(); // exception here
	}

	@Test(expected = java.lang.IllegalAccessException.class)
	public void validateObjectCreationFailedPararmeterNames() throws ClassNotFoundException, IllegalAccessException, InstantiationException
	{
		Class cls = Class.forName("com.se.details.util.PDMicroserviceConstants$PararmeterNames");
		PDMicroserviceConstants.PararmeterNames v = (PDMicroserviceConstants.PararmeterNames) cls.newInstance(); // exception here
	}
}
