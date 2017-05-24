package net.beotel.config;

import org.springframework.security.web.context.AbstractSecurityWebApplicationInitializer;

public class SecurityWebApplicationInitializer extends AbstractSecurityWebApplicationInitializer{

	public SecurityWebApplicationInitializer(){
		super(SecurityAppConfiguration.class);
	}
	
}
