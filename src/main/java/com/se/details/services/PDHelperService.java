package com.se.details.services;

import com.se.details.dto.PartdetailsFeatures;
import com.se.details.util.Multipliers;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.client.solrj.util.ClientUtils;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class PDHelperService
{
	public boolean isQueryResultNullOrEmpty(QueryResponse response)
	{
		return (response == null || response.getResults() == null || (response.getResults().isEmpty()));
	}

	public String escapeSolrQueryChars(String searchWord)
	{
		return ClientUtils.escapeQueryChars(searchWord);
	}

	public List<String> splitByDelimeter(String value, String delimeter)
	{
		return Arrays.asList(value.split(delimeter));
	}

	public String getFullUrl(HttpServletRequest request)
	{
		final int REQ_LEN = 4000;
		return stringTruncate(request.getRequestURL().toString() + "?" + request.getQueryString(), REQ_LEN);
	}

	private String stringTruncate(String str, int len)
	{
		int limit = str.length();
		if(len < limit)
		{
			limit = len;
			return str.substring(0, limit) + "...";
		}
		return str;
	}

	public Integer calculateNumberOfThreads(int size, int chunk)
	{
		int threads = size / chunk;
		if(size % chunk == 0)
		{
			return threads;
		}
		return threads + 1;
	}

	public String extractHColName(String field)
	{
		return field.substring(0, field.lastIndexOf('_'));
	}

	public String emptyStringIfNull(Object fieldValue)
	{
		return fieldValue == null ? "" : String.valueOf(fieldValue);
	}

	public boolean validateAgainistRegex(String input, String regex)
	{
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(input);

		return matcher.find();
	}

	public String convertListToSeparatedString(List<String> names, String delimeter)
	{
		return names.stream().collect(Collectors.joining(delimeter));
	}

	public Double getMultiplierValue(String multiplier, String unit)
	{
		int baseNumber = 10;
		if(multiplier != null && !multiplier.trim().isEmpty())
		{
			multiplier = multiplier.trim();
			if(isSpecailUnit(unit, multiplier))
			{
				multiplier = multiplier + "b";
				baseNumber = 2;
			}
		}

		Integer multiplierPower = Multipliers.getMultiplierValue(multiplier);

		if(multiplierPower == null)
		{
			return 1.0;
		}

		return Math.pow(baseNumber, multiplierPower);
	}

	private boolean isSpecailUnit(String fetUnit, String multiplier)
	{
		// check if this feature is binary feature with k=1024 not k=1000
		// check if unit and multiplier together appended are special unit
		// Ex: unit in database is Mbps alougth it had to be bps
		if(fetUnit != null && fetUnit.indexOf(multiplier) == 0)
		{
			fetUnit = new StringBuilder(fetUnit).deleteCharAt(0).toString();
		}
		return Multipliers.getSpecialunits().contains(fetUnit);
	}

	public void fillEmptyMap(List<String> parts, Map<String, List<PartdetailsFeatures>> featuresMap)
	{
		parts.forEach(p -> {
			featuresMap.computeIfAbsent(p, v -> new ArrayList<>());
		});
	}
}
