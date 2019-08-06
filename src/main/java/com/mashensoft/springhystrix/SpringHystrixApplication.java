package com.mashensoft.springhystrix;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
@RestController
@EnableEurekaClient
@EnableHystrix
@SpringBootApplication
public class SpringHystrixApplication {
	@Autowired
	RestTemplate restTemplate;

	@Bean
	public RestTemplate getRestTemplate() {
		return new RestTemplate();
	}

	public Map<String, Object> defaultFallback() {
		Map<String, Object> map = new HashMap<>();
		map.put("code", "001");
		map.put("codeDesc", "失败,断路器已打开");
		return map;
	}

	@HystrixCommand(fallbackMethod = "defaultFallback")
	@RequestMapping(value = "/", produces = "application/json;charset=UTF-8")
	public Map<String, Object> getUser() {
		Map<String, Object> map = restTemplate.getForObject("http://localhost:8080/", Map.class);
		return map;
	}

	public static void main(String[] args) {
		SpringApplication.run(SpringHystrixApplication.class, args);
	}

}
