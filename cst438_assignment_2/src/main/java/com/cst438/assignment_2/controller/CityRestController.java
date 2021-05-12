package com.cst438.assignment_2.controller;

import com.cst438.assignment_2.domain.CityInfo;
import com.cst438.assignment_2.service.CityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
public class CityRestController {
	
	@Autowired
	private CityService cityService;
	
	// This constructor is for stubbing city CityService class
	public CityRestController(CityService cityService) {
		this.cityService = cityService;
	}
	
	@GetMapping("/api/cities/{city}")
	public CityInfo getWeather(@PathVariable("city") String cityName) {
		CityInfo cityInfo = new CityInfo();
		
		cityInfo = cityService.getCityInfo(cityName);
		if(cityInfo.getName() == null) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND,
					"The city " + cityName + " not found.");
		}
		
		return cityInfo;
	}
}
