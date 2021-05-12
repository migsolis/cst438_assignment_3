package com.cst438.assignment_2.domain;

public class TimeAndTemp {

	public double temp;
	public long time;
	public int timezone;
	
	public TimeAndTemp() { }
	
	public TimeAndTemp(double temp, Long time, int timezone) {
		super();
		this.temp = temp;
		this.time = time;
		this.timezone = timezone;
	}
}
