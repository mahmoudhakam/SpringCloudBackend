package com.se.details.services.cache;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.se.details.configuration.PartdetailsMSConfigReader;
import com.se.details.dto.TaxonomyCachedResultDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * @author MAHMOUD_ABDELHAKAM
 */
@Component
public class PartsFeaturesCacheService
{
	private final CacheLoader<String, Optional<TaxonomyCachedResultDTO>> taxonomyCacheLoader;
	private final LoadingCache<String, Optional<TaxonomyCachedResultDTO>> taxonomyCache;
	private final PartdetailsMSConfigReader configReader;
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	private static final String CACHEALLTAXONOMYQUERYKEY = "*:* AND SHEETVIEWFLAG:(1 3) AND PACKAGEFLAG:0";

	@Autowired
	PartsFeaturesCacheService(CacheLoader<String, Optional<TaxonomyCachedResultDTO>> taxonomyCacheLoader, PartdetailsMSConfigReader configReader)
	{
		this.taxonomyCacheLoader = taxonomyCacheLoader;
		this.configReader = configReader;
		taxonomyCache = CacheBuilder.newBuilder().refreshAfterWrite(this.configReader.getMaxCacheDuration(), TimeUnit.HOURS).build(this.taxonomyCacheLoader);
	}

	public TaxonomyCachedResultDTO getTaxonomyResult(final String query)
	{
		try
		{
			if(taxonomyCache.get(query).isPresent())
			{
				return taxonomyCache.get(query).get();
			}
			return null;
		}
		catch(ExecutionException e)
		{
			logger.error("Error during trying to get cache from parametric taxonomy ", e);
			return null;
		}
	}

	public void refreshTaxonomyResult(final String query)
	{
		long start = System.currentTimeMillis();
		logger.info("Start refreshing caching taxonomy tree ...");
		taxonomyCache.refresh(query);
		logger.info("Refreshing  taxonomy tree takes about :{} sec", (System.currentTimeMillis() - start) / 1000);
	}

	@PostConstruct
	public void init()
	{
		long start = System.currentTimeMillis();
		logger.info("Start caching taxonomy tree ...");
		getTaxonomyResult(CACHEALLTAXONOMYQUERYKEY);
		logger.info("Caching taxonomy tree takes about :{} sec", (System.currentTimeMillis() - start) / 1000);
	}
}
