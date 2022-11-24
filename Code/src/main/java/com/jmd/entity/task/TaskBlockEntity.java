package com.jmd.entity.task;

import java.io.Serializable;

import lombok.Data;

@Data
public class TaskBlockEntity implements Serializable {

	private static final long serialVersionUID = -2871903479117630871L;

	private String name;
	private Integer z;

	private Integer xStart;
	private Integer xEnd;
	private Integer yStart;
	private Integer yEnd;

	private Integer realCount;

	private Integer xRun;
	private Integer yRun;
	private Integer runCount;

	public TaskBlockEntity() {

	}

	public TaskBlockEntity(String name, Integer z, Integer xStart, Integer xEnd, Integer yStart, Integer yEnd,
			Integer realCount, Integer xRun, Integer yRun, Integer runCount) {
		this.name = name;
		this.z = z;
		this.xStart = xStart;
		this.xEnd = xEnd;
		this.yStart = yStart;
		this.yEnd = yEnd;
		this.realCount = realCount;
		this.xRun = xRun;
		this.yRun = yRun;
		this.runCount = runCount;
	}

}
