package com.garbagecollectors.app;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import com.garbagecollectors.app.repository.UserRepository;
import com.garbagecollectors.app.security.JwtFilter;

@SpringBootApplication
@ComponentScan(basePackages = { "com.garbagecollectors.app" })
@EnableJpaRepositories(basePackages = { "com.garbagecollectors.app" })
public class GarbageCollectorsApplication implements CommandLineRunner{

	@Autowired
	UserRepository userRepostory;
	
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

	@Override
	public void run(String... args) throws Exception {
		
		/*
		 * Only for testing
		 */
		int adminId = 5000;
		
		User admin = userRepostory.findById(adminId).get();
		
		if (admin == null) {
			
			//create initial Administrator
			
			User newAdmin = new User();
			
		    newAdmin.setUser_id(adminId);
		    newAdmin.setPassword("x");
		    newAdmin.setUsername("x");
		    newAdmin.setUser_role(ERole.ADMIN);
		    
		    Profile adminProfile = new Profile();
		    adminProfile.setFirst_name("x");
		    adminProfile.setLast_name("x");
		    
		    
		    newAdmin.setUserProfile(adminProfile);
		    
		    userRepostory.save(newAdmin);
		    
		    
		}
		
		
	}

}
