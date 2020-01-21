package com.se.details.configuration;

import akka.actor.ActorSystem;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

import static com.se.details.configuration.SpringExtension.SPRING_EXTENSION_PROVIDER;

/**
 * @author MAHMOUD_ABDELHAKAM
 */
@Configuration
public class PartdetailsMicroserviceConfiguration implements AsyncConfigurer
{

	private Logger logger = LoggerFactory.getLogger(this.getClass());
	@Autowired private Environment env;
	@Autowired private PartdetailsMSConfigReader propertyPublisher;
	@Autowired private ApplicationContext applicationContext;

	@Bean
	public static PropertySourcesPlaceholderConfigurer placeHolderConfigurer()
	{
		return new PropertySourcesPlaceholderConfigurer();
	}

	@Bean
	public ActorSystem actorSystem(ApplicationContext applicationContext)
	{
		ActorSystem system = ActorSystem.create("akka-spring");
		SPRING_EXTENSION_PROVIDER.get(system).initialize(applicationContext);
		return system;
	}

	@Bean(name = "asyncExecutor")
	@Override
	public Executor getAsyncExecutor()
	{
		ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
		taskExecutor.setMaxPoolSize(propertyPublisher.getMaxNumberOfThreads());
		taskExecutor.setCorePoolSize(propertyPublisher.getNumberOfThreads());
		taskExecutor.setThreadNamePrefix("ParaAsync-");
		taskExecutor.initialize();

		return taskExecutor;
	}

	@Bean(name = "taxonomySolrServer")
	public SolrClient getTaxonomySolrServer()
	{
		return new HttpSolrClient.Builder(propertyPublisher.getTaxonomyCoreUrl()).allowCompression(true).build();
	}

	@Bean(name = "parametricSolrServer")
	public SolrClient getParametricSolrServer()
	{
		return new HttpSolrClient.Builder(propertyPublisher.getParametricCoreUrl()).allowCompression(true).build();
	}

	@Bean(name = "lcForcastSolrServer")
	public SolrClient getLcForcastSolrServer()
	{
		return new HttpSolrClient.Builder(propertyPublisher.getLcForecastCoreUrl()).allowCompression(true).build();
	}

	@Bean(name = "summarySolrServer")
	public SolrClient getSummarySolrServer()
	{
		return new HttpSolrClient.Builder(propertyPublisher.getSummarySolrServer()).allowCompression(true).build();
	}

	@Bean(name = "cooSolrServer")
	public SolrClient getCOOSolrServer()
	{
		return new HttpSolrClient.Builder(propertyPublisher.getCooSolrServer()).allowCompression(true).build();
	}

	@Override
	public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler()
	{
		return (ex, method, params) -> logger.error("Method:{} , Parameters:{} e:{}", method.getName(), params, ex);
	}
}
