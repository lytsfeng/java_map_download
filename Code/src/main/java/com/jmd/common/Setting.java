package com.jmd.common;

import java.io.Serializable;

import lombok.Data;

@Data
public class Setting implements Serializable {

	private static final long serialVersionUID = 2372192962922130729L;

	private String themeName;
	private String themeClazz;

}
