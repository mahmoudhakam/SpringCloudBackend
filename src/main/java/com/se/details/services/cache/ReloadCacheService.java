package com.se.details.services.cache;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReloadCacheService
{
	@Autowired private PartsFeaturesCacheService cacheService;
	private static final String CACHEALLTAXONOMYQUERYKEY = "*:* AND SHEETVIEWFLAG:(1 3) AND PACKAGEFLAG:0";

	public void reloadCache()
	{
		cacheService.refreshTaxonomyResult(CACHEALLTAXONOMYQUERYKEY);
	}
}
