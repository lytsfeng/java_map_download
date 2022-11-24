package com.jmd.entity.task;

import java.util.List;

import com.jmd.callback.TileDownloadedCallback;
import com.jmd.entity.config.HttpClientConfigEntity;
import com.jmd.entity.geo.Polygon;

import lombok.Data;

@Data
public class TaskExecEntity {

	private String tileName;
	private Integer z;
	private Integer xStart;
	private Integer xEnd;
	private Integer yStart;
	private Integer yEnd;
	private Integer xRun;
	private Integer yRun;
	private Integer startCount;
	private List<Polygon> polygons;
	private String downloadUrl;
	private Integer imgType;
	private String pathStyle;
	private String savePath;
	private Boolean isCoverExists;
	private TileDownloadedCallback tileCB;
	private HttpClientConfigEntity httpConfig;

}
