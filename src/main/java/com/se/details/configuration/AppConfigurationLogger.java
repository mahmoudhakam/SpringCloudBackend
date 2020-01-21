package com.se.details.configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Component
public class AppConfigurationLogger
{
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@EventListener
	public void handleContextRefreshed(ContextRefreshedEvent event)
	{
		printActiveProperties((ConfigurableEnvironment) event.getApplicationContext().getEnvironment());
	}

	/**
	 * This method is used only for debugging
	 *
	 * @param env Environment variables
	 */
	private void printActiveProperties(ConfigurableEnvironment env)
	{

		logger.info("************************* ACTIVE APP PROPERTIES ******************************");

		List<MapPropertySource> propertySources = new ArrayList<>();

		env.getPropertySources().forEach(it -> {
			if(it instanceof MapPropertySource && it.getName().contains("applicationConfig"))
			{
				propertySources.add((MapPropertySource) it);
			}
		});

		propertySources.stream().map(propertySource -> propertySource.getSource().keySet()).flatMap(Collection::stream).distinct().sorted().forEach(key -> {
			try
			{
				logger.info("{} =", env.getProperty(key));
			}
			catch(Exception e)
			{
				logger.info(String.format("{%s} -> {%s}", key, e.getMessage()));
			}
		});
		logger.info("******************************************************************************");
	}

}
