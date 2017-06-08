package com.siebre.product.config;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

/**
 * 
 * @author lizhiqiang
 *
 */
@Configuration
public class ExceptionHandler implements HandlerExceptionResolver {

	private static Logger LOGGER = Logger.getLogger(ExceptionHandler.class);
	
	public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
		ModelAndView mv = new ModelAndView();

		LOGGER.error(ResultCode.C500.message, ex);
		
		String message = "";
		String code = "";
		
		MappingJackson2JsonView view = new MappingJackson2JsonView();
		Map<String, Object> attributes = new HashMap<String, Object>();
		attributes.put("code", code);
		attributes.put("message", message);
		view.setAttributesMap(attributes);
		mv.setView(view);
		return mv;
	}

	@Bean
	public ExceptionHandler createExceptionHandler() {
		return new ExceptionHandler();
	}
}