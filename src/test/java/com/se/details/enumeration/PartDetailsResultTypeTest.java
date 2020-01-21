package com.se.details.enumeration;

import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class PartDetailsResultTypeTest
{

	@Test
	public void shouldGetStatisticsFields_whenStatisticsEnumerationCalled()
	{
		PartDetailsResultType partDetailsStatisticsResultType = PartDetailsResultType.STATISTICS;
		Set<String> statisticsFields = new HashSet<>();
		statisticsFields.add("Status");
		statisticsFields.add("ServiceTime");
		statisticsFields.add("Statistics");

		assertThat(partDetailsStatisticsResultType.getName()).isEqualTo("statistics");
		assertThat(partDetailsStatisticsResultType.getFields()).isEqualTo(statisticsFields);
	}

	@Test
	public void shouldGetResponseFields_whenResponseEnumerationCalled()
	{
		PartDetailsResultType partDetailsResponseResultType = PartDetailsResultType.RESPONSE;
		Set<String> responseFields = new HashSet<>();
		responseFields.add("Status");
		responseFields.add("ServiceTime");
		responseFields.add("PartsFeatures");

		assertThat(partDetailsResponseResultType.getName()).isEqualTo("response");
		assertThat(partDetailsResponseResultType.getFields()).isEqualTo(responseFields);
	}

	@Test
	public void shouldGetAllFields_whenFullEnumerationCalled()
	{
		PartDetailsResultType partDetailsFullResultType = PartDetailsResultType.FULL;
		Set<String> allFields = new HashSet<>();
		allFields.add("Status");
		allFields.add("ServiceTime");
		allFields.add("Statistics");
		allFields.add("PartsFeatures");

		assertThat(partDetailsFullResultType.getName()).isEqualTo("full");
		assertThat(partDetailsFullResultType.getFields()).isEqualTo(allFields);
	}

}