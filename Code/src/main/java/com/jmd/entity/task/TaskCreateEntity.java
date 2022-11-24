package com.jmd.entity.task;

import java.util.List;

import com.jmd.entity.geo.Polygon;

import lombok.Data;

@Data
public class TaskCreateEntity {

	private List<Integer> zoomList;
	private List<Polygon> polygons;
	private String tileUrl;
	private String savePath;
	private String tileName;
	private String mapType;
	private Integer imgType;
	private String pathStyle;
	private Boolean isCoverExists;
	private Boolean isMergeTile;

}
