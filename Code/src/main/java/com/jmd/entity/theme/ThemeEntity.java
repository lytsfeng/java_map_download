package com.jmd.entity.theme;

import java.util.List;

import lombok.Data;

@Data
public class ThemeEntity {

	private String name;
	private String clazz;
	private List<ThemeEntity> sub;

	public ThemeEntity(String name, List<ThemeEntity> sub) {
		this.name = name;
		this.sub = sub;
	}

	public ThemeEntity(String name, String clazz) {
		this.name = name;
		this.clazz = clazz;
	}

}
