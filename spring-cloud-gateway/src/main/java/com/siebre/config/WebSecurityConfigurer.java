package com.siebre.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.cloud.netflix.ribbon.RibbonAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.vote.AffirmativeBased;
import org.springframework.security.access.vote.AuthenticatedVoter;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.authentication.dao.ReflectionSaltSource;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.security.authentication.encoding.PasswordEncoder;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;

import com.siebre.gateway.security.metadata.CustomerSecurityMetadata;
import com.siebre.gateway.security.userdetails.CustomerUserDetailsService;
import com.siebre.gateway.security.voter.FullyMatchRoleVoter;

@SuppressWarnings("deprecation")
@Configuration
@AutoConfigureAfter(value = RibbonAutoConfiguration.class)
public class WebSecurityConfigurer extends WebSecurityConfigurerAdapter {
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
		.authorizeRequests()
			.antMatchers("/login.html", "/login").permitAll()
			.anyRequest().authenticated()
			.and()
		.formLogin()
			.loginPage("/login.html")
			.loginProcessingUrl("/login")
			.defaultSuccessUrl("/index.html")
			.and()
		.logout()
			.logoutUrl("/logout")
			.logoutSuccessUrl("/login.html")
			.invalidateHttpSession(true)
			.and()
		.csrf()
			.disable();
	}
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth
			.authenticationProvider(authenticationProvider())
			;
	}
	
	@Bean
	public AuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
		daoAuthenticationProvider.setUserDetailsService(this.userDetailsService());
		daoAuthenticationProvider.setPasswordEncoder(this.passwordEncoder());
		daoAuthenticationProvider.setSaltSource(this.saltSource());
		
		return daoAuthenticationProvider;
	}
	
	@Bean
	public UserDetailsService userDetailsService() {
		return new CustomerUserDetailsService();
	}
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new Md5PasswordEncoder();
	}
	
	@Bean
	public ReflectionSaltSource saltSource() {
		ReflectionSaltSource saltSource = new ReflectionSaltSource();
		saltSource.setUserPropertyToUse("username");
		return saltSource;
	}
	
	
	@Override
	public void configure(WebSecurity web) throws Exception {
		web
			.debug(true)
			.securityInterceptor(this.filterSecurityInterceptor());
	}
	
	@Bean
	public FilterSecurityInterceptor filterSecurityInterceptor() {
		FilterSecurityInterceptor filterSecurityInterceptor = new FilterSecurityInterceptor();
		filterSecurityInterceptor.setSecurityMetadataSource(this.securityMetadataSource());
		filterSecurityInterceptor.setAccessDecisionManager(this.accessDecisionManager());
		return filterSecurityInterceptor;
	}
	
	@Bean
	public FilterInvocationSecurityMetadataSource securityMetadataSource() {
		CustomerSecurityMetadata securityMetadataSource = new CustomerSecurityMetadata();
		return securityMetadataSource;
	}
	
	@Bean
	public AccessDecisionManager accessDecisionManager() {
		List<AccessDecisionVoter<? extends Object>> voters = new ArrayList<AccessDecisionVoter<? extends Object>>();
		voters.add(this.authenticatedVoter());
		voters.add(this.fullyMatchRoleVoter());
		
		AffirmativeBased accessDecisionManager = new AffirmativeBased(voters);
		return accessDecisionManager;
	}
	
	@Bean
	public AuthenticatedVoter authenticatedVoter() {
		return new AuthenticatedVoter();
	}
	
	@Bean
	public FullyMatchRoleVoter fullyMatchRoleVoter() {
		return new FullyMatchRoleVoter();
	}
	
	
}
