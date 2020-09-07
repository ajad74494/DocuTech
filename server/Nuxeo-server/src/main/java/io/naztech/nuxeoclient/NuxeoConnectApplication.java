package io.naztech.nuxeoclient;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
@EnableScheduling
public class NuxeoConnectApplication extends SpringBootServletInitializer {

	@Value("${allowed.origins}")
	private String allowedOrigins;
	
	public static void main(String[] args) {
		SpringApplication.run(NuxeoConnectApplication.class, args);
	}

	/*
	 * @Bean public WebMvcConfigurer corsConfigurer() { return new
	 * WebMvcConfigurerAdapter() {
	 * 
	 * @Override public void addCorsMappings(CorsRegistry registry) {
	 * registry.addMapping("/").allowedOrigins("localhost:8080"); } }; }
	 */

	/**
	 * @author mahbub.hasan
	 * @since 2019-12-08
	 *  Removed depricated version of WebMvcConfigurer
	 * */
	@Bean
	public WebMvcConfigurer corsConfigurer() {
		return new WebMvcConfigurer() {
			@Override
			public void addCorsMappings(CorsRegistry registry) {
				registry.addMapping("/").allowedOrigins(allowedOrigins);
			}
		};
	}
}
