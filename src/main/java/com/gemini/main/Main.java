package com.gemini.main;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

import com.gemini.http.Server;

@Configuration
@PropertySource("classpath:chitchat.properties")
public class Main {
	private static Logger l = LoggerFactory.getLogger(Main.class);
	
	public static void main(String[] args) {
		try {
			new AnnotationConfigApplicationContext(Main.class);
		} catch (Exception e) {
			l.error("Error occured: {}", e.getMessage());
		}
	}
	
	@Bean(initMethod="initialize")
	public Server server(){
		l.debug("Creating Server bean");
		return new Server();
	}
	
	@Bean
    public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
       return new PropertySourcesPlaceholderConfigurer();
    }
}
