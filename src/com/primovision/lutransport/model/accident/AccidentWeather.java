package com.primovision.lutransport.model.accident;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.primovision.lutransport.model.AbstractBaseModel;

@Entity
@Table(name = "accident_weather")
public class AccidentWeather extends AbstractBaseModel {
	@Column(name="weather")
	private String weather;

	@Column(name="description")
	private String description;

	public String getWeather() {
		return weather;
	}

	public void setWeather(String weather) {
		this.weather = weather;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}
