/*
 * WebConfig.java
 * This class is used to configure the web
 */
package com.gcu;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * WebConfig class
 * Configures the resource handlers for serving static resources
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

	/**
	 * Adds resource handlers for serving static resources
	 * @param registry The ResourceHandlerRegistry to add resource handlers to
	 */
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/uploads/**")
				.addResourceLocations("file:uploads/catalogs/");
	}
}
