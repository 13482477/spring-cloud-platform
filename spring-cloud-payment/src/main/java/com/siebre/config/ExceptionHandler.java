package com.siebre.config;

import org.apache.log4j.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

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
		
		String code = "500";
		String message = "internal error";
		
		MappingJackson2JsonView view = new MappingJackson2JsonView();
		Map<String, Object> attributes = new HashMap<String, Object>();
		attributes.put("returnCode", code);
		attributes.put("returnMessage", message);
		view.setAttributesMap(attributes);
		mv.setView(view);
		return mv;
	}

	@Bean
	public ExceptionHandler createExceptionHandler() {
		return new ExceptionHandler();
	}
}