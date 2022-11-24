package com.jmd.entity.task;

import lombok.Data;

@Data
public class TaskProgressEntity {

	private Integer lastCount;
	private Integer currentCount;
	private Double perc;

	public TaskProgressEntity() {

	}

	public TaskProgressEntity(int lastCount, int currentCount, double perc) {
		this.lastCount = lastCount;
		this.currentCount = currentCount;
		this.perc = perc;
	}

}
