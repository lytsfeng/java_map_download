package com.jmd.entity.task;

import java.util.ArrayList;

import lombok.Data;

@Data
public class TaskBlockDivide {

	private Integer countX;
	private Integer countY;
	private ArrayList<Integer[]> dividX;
	private ArrayList<Integer[]> dividY;

}
