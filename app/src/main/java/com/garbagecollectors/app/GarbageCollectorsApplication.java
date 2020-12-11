package com.garbagecollectors.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import com.garbagecollectors.app.security.JwtFilter;

@SpringBootApplication
@ComponentScan(basePackages = { "com.garbagecollectors.app" })
@EnableJpaRepositories(basePackages = { "com.garbagecollectors.app" })
public class GarbageCollectorsApplication {

	public static void main(String[] args) {
		SpringApplication.run(GarbageCollectorsApplication.class, args);
	}
	
	@Bean
	public FilterRegistrationBean<JwtFilter> jwtFilter() {
		final FilterRegistrationBean<JwtFilter> registrationBean = new FilterRegistrationBean<>();

		registrationBean.setFilter(new JwtFilter());
		registrationBean.addUrlPatterns("/garbagecollectors/admin/*");
		registrationBean.addUrlPatterns("/garbagecollectors/user/*");

		return registrationBean;
	}

}
