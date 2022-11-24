package com.jmd.entity.task;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import com.jmd.entity.geo.Polygon;

import lombok.Data;

@Data
public class TaskInstEntity implements Serializable {

	private static final long serialVersionUID = -7964576148008799649L;

	private Integer z;
	private List<Polygon> polygons;
	private ArrayList<Integer[]> dividX;
	private ArrayList<Integer[]> dividY;
	private ConcurrentHashMap<String, TaskBlockEntity> blocks;
	private Integer allCount;
	private Integer realCount;

	private Integer xStart;
	private Integer xEnd;
	private Integer yStart;
	private Integer yEnd;

	private Boolean needMerge;
	private Boolean isMerged;

}
