package com.jmd.util;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import com.jmd.entity.task.TaskAllInfoEntity;
import com.jmd.entity.task.TaskBlockDivide;

public class TaskUtils {

	public static TaskAllInfoEntity getExistTaskByPath(String path) {
		TaskAllInfoEntity taskAllInfo = null;
		try {
			taskAllInfo = (TaskAllInfoEntity) CommonUtils.readFile2Obj(path + "/task_info.jmd");
		} catch (ClassNotFoundException | IOException e) {
			e.printStackTrace();
			return null;
		}
		return taskAllInfo;
	}

	public static TaskAllInfoEntity getExistTaskByFile(File file) {
		TaskAllInfoEntity taskAllInfo = null;
		try {
			taskAllInfo = (TaskAllInfoEntity) CommonUtils.readFile2Obj(file);
		} catch (ClassNotFoundException | IOException e) {
			e.printStackTrace();
			return null;
		}
		return taskAllInfo;
	}

	public static String getSuffix(int imgType) {
		String suffix = "";
		switch (imgType) {
		case 0:
			suffix = "png";
			break;
		case 1:
		case 2:
		case 3:
			suffix = "jpg";
			break;
		default:
			break;
		}
		return suffix;
	}

	public static String getFilePathName(String pathStyle, int imgType, int z, int x, int y) {
		pathStyle = pathStyle.replaceAll("\\{x\\}", String.valueOf(x));
		pathStyle = pathStyle.replaceAll("\\{y\\}", String.valueOf(y));
		pathStyle = pathStyle.replaceAll("\\{z\\}", String.valueOf(z));
		pathStyle = pathStyle.replaceAll("\\[png or jpg\\]", getSuffix(imgType));
		return pathStyle;
	}

	public static TaskBlockDivide blockDivide(int xStart, int xEnd, int yStart, int yEnd, double d) {
		TaskBlockDivide divide = new TaskBlockDivide();
		int countX = xEnd - xStart + 1;
		int countY = yEnd - yStart + 1;
		int eachX = (int) Math.ceil(countX / d);
		int eachY = (int) Math.ceil(countY / d);
		if (countX <= d) {
			eachX = (int) Math.ceil(countX / 2);
			eachX = eachX == 0 ? 1 : eachX;
		}
		if (countY <= d) {
			eachY = (int) Math.ceil(countY / 2);
			eachY = eachY == 0 ? 1 : eachY;
		}
		ArrayList<Integer[]> divideX = new ArrayList<>();
		ArrayList<Integer[]> divideY = new ArrayList<>();
		if (countX / eachX <= 1) {
			Integer arr[] = { 0, countX };
			divideX.add(arr);
		} else {
			int cnt = (int) Math.floor(countX / eachX);
			int e = countX % eachX;
			for (int i = 0; i < cnt; i++) {
				Integer arr[] = { i * eachX, (i + 1) * eachX - 1 };
				divideX.add(arr);
			}
			if (cnt * eachX < cnt * eachX + e) {
				Integer arrEnd[] = { cnt * eachX, cnt * eachX + e - 1 };
				divideX.add(arrEnd);
			}
		}
		if (countY / eachY <= 1) {
			Integer arr[] = { 0, countY };
			divideY.add(arr);
		} else {
			int cnt = (int) Math.floor(countY / eachY);
			int e = countY % eachY;
			for (int i = 0; i < cnt; i++) {
				Integer arr[] = { i * eachY, (i + 1) * eachY - 1 };
				divideY.add(arr);
			}
			if (cnt * eachY < cnt * eachY + e) {
				Integer arrEnd[] = { cnt * eachY, cnt * eachY + e - 1 };
				divideY.add(arrEnd);
			}
		}
		divide.setCountX(countX);
		divide.setCountY(countY);
		divide.setDividX(divideX);
		divide.setDividY(divideY);
		return divide;
	}

}
