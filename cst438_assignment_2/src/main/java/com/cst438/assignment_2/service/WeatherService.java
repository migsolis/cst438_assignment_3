package com.cst438.assignment_2.service;

import com.cst438.assignment_2.domain.TimeAndTemp;
import com.fasterxml.jackson.databind.JsonNode;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class WeatherService {
	
	private static final Logger log = LoggerFactory.getLogger(WeatherService.class);
	private RestTemplate restTemplate;
	private String weatherUrl;
	private String apiKey;
	
	public WeatherService(
			@Value("${weather.url}") final String weatherUrl,
			@Value("${weather.apiKey}") final String apiKey){
				this.restTemplate = new RestTemplate();
				this.weatherUrl = weatherUrl;
				this.apiKey = apiKey;
	}
	
	public TimeAndTemp getTempAndTime(String cityName) {
		ResponseEntity<JsonNode> response =
				restTemplate.getForEntity(
						weatherUrl + "?q=" + cityName +"&appid=" + apiKey,
						JsonNode.class);
		JsonNode json = response.getBody();
		log.info("Status code from weather server:" + apiKey, JsonNode.class);
		double temp = json.get("main").get("temp").asDouble();
		long time = json.get("dt").asLong();
		int timezone = json.get("timezone").asInt();
		return new TimeAndTemp(temp, time, timezone);
	}
}
