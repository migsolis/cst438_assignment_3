package com.cst438.assignment_2.service;

import com.cst438.assignment_2.domain.City;
import com.cst438.assignment_2.domain.CityInfo;
import com.cst438.assignment_2.domain.CityRepository;
import com.cst438.assignment_2.domain.CountryRepository;
import com.cst438.assignment_2.domain.TimeAndTemp;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class CityService {
	
	@Autowired
	private CityRepository cityRepository;
	
	@Autowired
	private CountryRepository countryRepository;
	
	@Autowired
	private WeatherService weatherService;
	
	// This constructor is for stubbing the repositories and weather service
	public CityService(CityRepository cityRepo, CountryRepository countryRepo, 
			WeatherService weatherService) {
		this.cityRepository = cityRepo;
		this.countryRepository = countryRepo;
		this.weatherService = weatherService;
	}
	
	public CityInfo getCityInfo(String cityName) {
		List<City> cities = cityRepository.findByName(cityName);
		if (cities.size() == 0) {
			return new CityInfo();
		}
		City city = cities.get(0);
		CityInfo cityInfo = new CityInfo();
		TimeAndTemp timeAndTemp = weatherService.getTempAndTime(cityName);
		
		cityInfo.setId(city.getId());
		cityInfo.setName(city.getName());
		cityInfo.setCountryCode(city.getCountry().getCode());
		cityInfo.setCountryName(city.getCountry().getName());
		cityInfo.setDistrict(city.getDistrict());
		cityInfo.setPopulation(city.getPopulation());
		cityInfo.setTemp(convertTempToF(timeAndTemp.temp));
		cityInfo.setTime(timeToString(timeAndTemp.time, timeAndTemp.timezone));
		
		return cityInfo;
	}
	
	// Converts temperature from K double to F String.
	private String convertTempToF(double temp) {
		double tempF = Math.round((temp - 273.15) * 9.0/5.0 + 32.0);
		String tempString = tempF + "\u00B0F";
		
		return tempString;
	}
	
	// Formats time to 12HR am/pm
	private String timeToString(long time, int timezone) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("h:mm a");
		LocalDateTime dt = 
				LocalDateTime.ofEpochSecond(time, 0, ZoneOffset.ofTotalSeconds(timezone));
		String timeString = dt.format(formatter);
		return timeString;
	}

}
