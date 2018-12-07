package springData;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@EnableWebSecurity
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    protected void configure(HttpSecurity http) throws Exception {
    	http
        // SECURE COMMUNICATION
    		// TODO: exercise 6 Task 1c
	    .requiresChannel()
	    		.anyRequest()
	    		.requiresSecure()
	    	// END exercise 6 Task 1c
    		// AUTHENTICATION
	    	.and()
		    .formLogin()
		    		// to show the page where we enter login credentials 
				.loginPage("/user-login") 
				// to process authentication: /login handler method implemented by Spring Security
				.loginProcessingUrl("/login")
				// where to go after successful login
				.defaultSuccessUrl("/success-login",true) // the second parameter is for enforcing this url always
				// to show an error page if the authentication failed
				.failureUrl("/error-login")
				// everyone can access these requests
				.permitAll()
		.and()
			.logout()
				// to logout
				.invalidateHttpSession(true)
				// with CSRF we need to map the POST request /logout
				// if CSRF is disabled the GET request /logout is mapped by default
				// to an internal Spring Security handler method
				.logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
				.logoutSuccessUrl("/user-login")
				.permitAll()
		// AUTHORIZATION
		.and()
			.authorizeRequests()
				.antMatchers("/admin/**").hasRole("ADMIN")  
				.antMatchers("/create/**").hasRole("MANAGER")  
				.antMatchers("/delete/**").hasAnyRole("MANAGER") 
				.antMatchers("/list/**").hasAnyRole("ASISTANT","MANAGER") 
				.anyRequest().authenticated() // all requests ABOVE this statement require authentication
		.and()
			// to redirect the user when trying to access a resource to which access is not granted
	        .exceptionHandling().accessDeniedPage("/access-denied");
    }

	@Autowired 
	private UserDetailsService userDetailsService; 	

    @Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		BCryptPasswordEncoder pe = new  BCryptPasswordEncoder();
		auth.userDetailsService(userDetailsService).passwordEncoder(pe);
		
	}
    
}
	