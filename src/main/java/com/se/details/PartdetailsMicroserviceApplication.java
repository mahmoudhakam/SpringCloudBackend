package com.se.details;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScans;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

/**
 * @author MAHMOUD_ABDELHAKAM
 */
@SpringBootApplication
@EnableAsync
@EnableWebMvc
@ComponentScans(value = { @ComponentScan("com.se") })
public class PartdetailsMicroserviceApplication
{

	public static void main(String[] args)
	{
		SpringApplication.run(PartdetailsMicroserviceApplication.class, args);
	}

}
