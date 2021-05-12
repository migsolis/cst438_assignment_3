package com.cst438.assignment_2;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;

import com.cst438.assignment_2.controller.CityRestController;
import com.cst438.assignment_2.domain.CityInfo;
import com.cst438.assignment_2.service.CityService;
import com.fasterxml.jackson.databind.ObjectMapper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;


@WebMvcTest(CityRestController.class)
class CityRestControllerTest {
	
	@MockBean
	private CityService cityService;
	
	@Autowired
	private MockMvc mvc;
	
	private JacksonTester<CityInfo> jsonCityInfoAttempt;
	
	@BeforeEach
	public void setupEach() {
		MockitoAnnotations.initMocks(this);
		JacksonTester.initFields(this, new ObjectMapper());
	}
	
	@Test
	public void test1() throws Exception {
		CityInfo cityInfoTest = new CityInfo(1234L, "Testton", "ABC", "ABC123", "Test District",
				123456789, "100.0\u00B0F", "12:00 PM");
		
		given(cityService.getCityInfo("Testton")).willReturn(cityInfoTest);
		
		MockHttpServletResponse response = mvc.perform(get("/api/cities/Testton"))
				.andReturn().getResponse();
		
		assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
		
		CityInfo cityInfoResult = jsonCityInfoAttempt.parseObject(response.getContentAsString());
		
		assertEquals(1234L, cityInfoResult.getId());
	}
	
	@Test
	public void test2() throws Exception {
		
		given(cityService.getCityInfo("Testton")).willReturn(new CityInfo());
		
		MockHttpServletResponse response = mvc.perform(get("/api/cities/Testton"))
				.andReturn().getResponse();
		
		assertThat(response.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
	}
}
