package com.se.details.converter;

import com.se.details.enumeration.PartDetailsResultType;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class StringToPartDetailsSelectionTypeConverterTest
{

	private final StringToPartDetailsSelectionTypeConverter stringToPartDetailsSelectionTypeConverter = new StringToPartDetailsSelectionTypeConverter();

	@Test
	public void shouldReturnFullType_whenCallConvertWithFullSmallLetters()
	{
		PartDetailsResultType partDetailsResultType = stringToPartDetailsSelectionTypeConverter.convert("full");
		assertThat(partDetailsResultType).isEqualTo(PartDetailsResultType.FULL);
	}

	@Test
	public void shouldReturnFullType_whenCallConvertWithFullCapitalLetters()
	{
		PartDetailsResultType partDetailsResultType = stringToPartDetailsSelectionTypeConverter.convert("FULL");
		assertThat(partDetailsResultType).isEqualTo(PartDetailsResultType.FULL);
	}

	@Test
	public void shouldReturnStatisticsType_whenCallConvertWithStatisticsSmallLetters()
	{
		PartDetailsResultType partDetailsResultType = stringToPartDetailsSelectionTypeConverter.convert("statistics");
		assertThat(partDetailsResultType).isEqualTo(PartDetailsResultType.STATISTICS);
	}

	@Test
	public void shouldReturnStatisticsType_whenCallConvertWithStatisticsCapitalLetters()
	{
		PartDetailsResultType partDetailsResultType = stringToPartDetailsSelectionTypeConverter.convert("STATISTICS");
		assertThat(partDetailsResultType).isEqualTo(PartDetailsResultType.STATISTICS);
	}

	@Test
	public void shouldReturnResponseType_whenCallConvertWithResponseSmallLetters()
	{
		PartDetailsResultType partDetailsResultType = stringToPartDetailsSelectionTypeConverter.convert("response");
		assertThat(partDetailsResultType).isEqualTo(PartDetailsResultType.RESPONSE);
	}

	@Test
	public void shouldReturnResponseType_whenCallConvertWithResponseCapitalLetters()
	{
		PartDetailsResultType partDetailsResultType = stringToPartDetailsSelectionTypeConverter.convert("RESPONSE");
		assertThat(partDetailsResultType).isEqualTo(PartDetailsResultType.RESPONSE);
	}

	@Test(expected = IllegalArgumentException.class)
	public void shouldThrowException_whenCallConvertWithNotFoundInstance()
	{
		stringToPartDetailsSelectionTypeConverter.convert("NOT_FOUND");
	}

}