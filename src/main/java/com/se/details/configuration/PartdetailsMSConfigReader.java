package com.se.details.configuration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "partdetails-service")
@Component
@RefreshScope
@Getter
@Setter
public class PartdetailsMSConfigReader
{
	private int maxNumberOfThreads;
	private int numberOfThreads;
	private String taxonomyCoreUrl;
	private String parametricCoreUrl;
	private String lcForecastCoreUrl;
	private int partsPerActor;
	private int actorsPerCategory;
	private int detailsBatchSize;
	private int actorMangerTimeout;
	private int maxCacheDuration;
	private String summarySolrServer;
	private String cooSolrServer;
	private Swagger swagger = new Swagger();

	@Getter
	@Setter
	class Swagger
	{

		private String title;
		private String description;
		private String version;
		private String termsOfServiceUrl;
		private String contactName;
		private String contactUrl;
		private String contactEmail;
		private String license;
		private String licenseUrl;
	}

}
