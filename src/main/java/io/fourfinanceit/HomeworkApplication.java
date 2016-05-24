package io.fourfinanceit;

import java.util.Map;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.DefaultErrorAttributes;
import org.springframework.boot.autoconfigure.web.ErrorAttributes;
import org.springframework.context.annotation.Bean;
import org.springframework.web.context.request.RequestAttributes;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

@SpringBootApplication
public class HomeworkApplication {
	
	@Bean
	public ObjectMapper configureJSONDatesAsISOFormat() {
		ObjectMapper mapper = new ObjectMapper();
		mapper.registerModule(new JavaTimeModule());
		mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
		return mapper;
	}
	
	@Bean
	public ErrorAttributes errorAttributes() {
	    return new DefaultErrorAttributes() {
	        @Override
	        public Map<String, Object> getErrorAttributes(RequestAttributes requestAttributes, boolean includeStackTrace) {
	            Map<String, Object> errorAttributes = super.getErrorAttributes(requestAttributes, includeStackTrace);
	            return errorAttributes;
	        }

	   };
	}
	
	public static void main(String[] args) {
		SpringApplication.run(HomeworkApplication.class, args);
	}
}
