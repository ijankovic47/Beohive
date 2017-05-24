package net.beotel.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;

@Configuration
@EnableWebSecurity
@EnableAspectJAutoProxy
@EnableGlobalMethodSecurity(prePostEnabled=true)
@ComponentScan(basePackages="net.beotel")
public class SecurityAppConfiguration extends WebSecurityConfigurerAdapter{

	@Autowired
	private UserDetailsService operatorUserDetailsService;
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(operatorUserDetailsService);
		auth.authenticationProvider(authenticationProvider());
	}
	
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
			.authorizeRequests()
			.antMatchers("/registration", "/register","/editPartner","/odustani", "/edit", "/partners2", "/updatePartner","/partnerInfo","/back","/getPartnerOp","/owc/**").permitAll()
			.antMatchers("/modeli/**").hasRole("BEOTEL")
			.antMatchers("/uredjaj/**","/cenovnik/**").access("hasRole('BEOTEL') or hasRole('OPERATER')")
			.antMatchers("/paketi/**", "/subscribers/**").access("hasRole('BEOTEL') or hasRole('OPERATER')")
			.antMatchers(HttpMethod.GET, "/partners", "/getPartner").hasRole("BEOTEL")
			.antMatchers(HttpMethod.GET, "/operater/").hasRole("BEOTEL")
			.antMatchers(HttpMethod.GET, "/operater/*/operateri").hasRole("BEOTEL")
			.antMatchers(HttpMethod.GET, "/operater/*").access("hasRole('BEOTEL') or hasRole('OPERATER')")
			.antMatchers(HttpMethod.POST, "/operater/chpasswd/*").hasRole("BEOTEL")
			.anyRequest().authenticated()
			.and()
			.formLogin()
			.loginPage("/login").successForwardUrl("/welcome")
			.failureUrl("/login?error").permitAll()
			.and()
			.logout().logoutSuccessUrl("/login")
			.permitAll();   
		   // http.csrf().disable();
	}
	
	@Override
	public void configure(WebSecurity web) throws Exception {
	    web.ignoring().antMatchers("/resources/**");
	}
	
	@Bean
	public DaoAuthenticationProvider authenticationProvider(){
		DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
		authenticationProvider.setUserDetailsService(operatorUserDetailsService);
		authenticationProvider.setPasswordEncoder(passwordEncoder());
		return authenticationProvider;
	}
	
	@Bean
	public Md5PasswordEncoder passwordEncoder(){
		Md5PasswordEncoder passwordEncoder = new Md5PasswordEncoder();
		return passwordEncoder;
	}
}
