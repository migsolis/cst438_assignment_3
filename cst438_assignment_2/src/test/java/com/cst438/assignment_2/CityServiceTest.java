package com.cst438.assignment_2;

import com.cst438.assignment_2.domain.City;
import com.cst438.assignment_2.domain.CityInfo;
import com.cst438.assignment_2.domain.CityRepository;
import com.cst438.assignment_2.domain.Country;
import com.cst438.assignment_2.domain.CountryRepository;
import com.cst438.assignment_2.domain.TimeAndTemp;
import com.cst438.assignment_2.service.CityService;
import com.cst438.assignment_2.service.WeatherService;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CityServiceTest {
	// classes being stubbed
	@MockBean
	private CityRepository mockCityRepository;
	
	@MockBean
	private CountryRepository mockCountryRepository;
	
	@MockBean
	private WeatherService mockWeatherService;
	
	// Service being tested
	private CityService cs;
	
	@BeforeEach
	public void setupEach() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	void testValidCityName() {
		// Setup required test variables 
		List<City> cities = new ArrayList<City>();
		Country testCountry = new Country("ABC", "ABC123");
		City testCity = new City(1234L,"Testton","Test District", 123456789, testCountry);
		cities.add(testCity);
		
		// give response to mocked classes
		given(mockCityRepository.findByName("Testton")).willReturn(cities);
		given(mockWeatherService.getTempAndTime("Testton")).willReturn(
				new TimeAndTemp(310.928, 1620759600L, -25200));
		
		// Create class to be tested.
		cs = new CityService(mockCityRepository, mockCountryRepository, mockWeatherService);
		
		CityInfo cityInfo = cs.getCityInfo("Testton");
		
		assertEquals(1234L, cityInfo.getId());
		assertEquals("ABC", cityInfo.getCountryCode());
		assertEquals("100.0\u00B0F", cityInfo.getTemp());
		assertEquals("12:00 PM", cityInfo.getTime());
		
		verify(mockCityRepository, times(1)).findByName("Testton");
		verify(mockWeatherService, times(1)).getTempAndTime("Testton");
	}
	
	@Test
	void testInvalidCityName() {
		// Setup required test variables 
		List<City> cities = new ArrayList<City>();
		
		// give response to mocked classes
		given(mockCityRepository.findByName("1234")).willReturn(cities);
		
		cs = new CityService(mockCityRepository, mockCountryRepository, mockWeatherService);
		
		CityInfo cityInfo = cs.getCityInfo("1234");
		
		
		assertEquals(new CityInfo(), cityInfo);
	}
	
	@Test
	void testMultipleCitiesWithName() {
		// Setup required test variables 
		List<City> cities = new ArrayList<City>();
		Country testCountry = new Country("ABC", "ABC123");
		Country testCountry2 = new Country("DEF", "DEF456");

		City testCity = new City(1234L,"Testton","Test District", 123456789, testCountry);
		cities.add(testCity);
		City testCity2 = new City(1235L,"Testton","Test District2", 123456790, testCountry2);
		cities.add(testCity2);
		
		// give response to mocked classes
		given(mockCityRepository.findByName("Testton")).willReturn(cities);
		given(mockWeatherService.getTempAndTime("Testton")).willReturn(
				new TimeAndTemp(310.928, 1620759600L, -25200));
		
		// Create class to be tested.
		cs = new CityService(mockCityRepository, mockCountryRepository, mockWeatherService);
		
		CityInfo cityInfo = cs.getCityInfo("Testton");
		
		assertEquals(1234L, cityInfo.getId());
		assertEquals("ABC", cityInfo.getCountryCode());
		
		verify(mockCityRepository, times(1)).findByName("Testton");
	}
}
