package com.siebre.product.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.orm.hibernate4.support.OpenSessionInViewFilter;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.JstlView;

import java.nio.charset.Charset;

@Configuration
public class WebConfig extends WebMvcConfigurerAdapter {

    private static final Charset UTF8 = Charset.forName("UTF-8");


    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**");
    }

    @Bean
    public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }

    @Bean
    public FilterRegistrationBean characterEncodingFilter() {
        CharacterEncodingFilter characterEncodingFilter = new CharacterEncodingFilter();
        characterEncodingFilter.setEncoding("UTF-8");
        characterEncodingFilter.setForceEncoding(true);

        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(characterEncodingFilter);
        registration.addUrlPatterns("/*");
        registration.setName("openSessionInViewFilter");
        registration.setOrder(1);
        return registration;
    }

    @Bean
    public FilterRegistrationBean openSessionInViewFilter() {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(new OpenSessionInViewFilter());
//        registration.setFilterter(characterEncodingFilter());
        registration.addUrlPatterns("/*");
        registration.setName("openSessionInViewFilter");
        registration.setOrder(1);
        return registration;
    }

//    public Filter authorizationFilter() {
//        AuthorizationFilter authorizationFilter = new AuthorizationFilter();
//        return authorizationFilter;
//    }

    @Bean
    public MappingJackson2HttpMessageConverter converter() {
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        return converter;
    }

    @Bean
    public ViewResolver getViewResolver() {
        InternalResourceViewResolver resolver = new InternalResourceViewResolver();
        resolver.setViewClass(JstlView.class);
        resolver.setPrefix("/jsp");
        resolver.setSuffix(".jsp");
        return resolver;
    }

    @Bean
    public StandardServletMultipartResolver getStandardServletMultipartResolver() {
        return new StandardServletMultipartResolver();
    }
}