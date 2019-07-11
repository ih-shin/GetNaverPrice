package com.orz.config;


import java.io.IOException;

import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

@Configuration
public class PropertiesConfig {

	@Bean(name="app")
	public static PropertiesFactoryBean app() throws IOException {
		PropertiesFactoryBean pspc = new PropertiesFactoryBean();
		Resource[] resources = new PathMatchingResourcePatternResolver().getResources("classpath:application.properties");
		pspc.setLocations(resources);
		return pspc;
	}
}