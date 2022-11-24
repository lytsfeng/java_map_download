package com.jmd.async.task.executor;

import java.util.List;
import java.util.concurrent.Future;

import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Component;

import com.jmd.common.StaticVar;
import com.jmd.entity.geo.Polygon;
import com.jmd.entity.geo.Tile;
import com.jmd.entity.result.ImageMergeAsyncTaskResult;
import com.jmd.taskfunc.TileMergeMatWrap;
import com.jmd.util.GeoUtils;
import com.jmd.util.TaskUtils;

@Component
public class TileMergeTask {

	@Async("TileMergeExecutorPool")
	public Future<ImageMergeAsyncTaskResult> exec(TileMergeMatWrap mat, int z, int topLeftX, int topLeftY, int xStart,
			int xEnd, int yStart, int yEnd, List<Polygon> polygons, int imgType, String savePath, String pathStyle,
			int divideXIndex, int divideYIndex) {
		ImageMergeAsyncTaskResult result = new ImageMergeAsyncTaskResult();
		result.setXStart(xStart);
		result.setXEnd(xEnd);
		result.setYStart(yStart);
		result.setYEnd(yEnd);
		result.setDivideXIndex(divideXIndex);
		result.setDivideYIndex(divideYIndex);
		for (int x = xStart; x <= xEnd; x++) {
			if (Thread.currentThread().isInterrupted()) {
				break;
			}
			for (int y = yStart; y <= yEnd; y++) {
				if (Thread.currentThread().isInterrupted()) {
					break;
				}
				Tile tile = GeoUtils.getTile(z, x, y);
				int positionX = StaticVar.TILE_WIDTH * (x - topLeftX);
				int positionY = StaticVar.TILE_HEIGHT * (y - topLeftY);
				String filePathAndName = savePath + TaskUtils.getFilePathName(pathStyle, imgType, z, x, y);
				boolean isInFlag = false;
				for (int i = 0; i < polygons.size(); i++) {
					isInFlag = GeoUtils.isTileInPolygon(tile, polygons.get(i))
							|| GeoUtils.isPolygonInTile(tile, polygons.get(i));
					if (isInFlag) {
						break;
					}
				}
				mat.mergeToMat(filePathAndName, positionX, positionY, isInFlag);
			}
		}
		return new AsyncResult<ImageMergeAsyncTaskResult>(result);
	}

}
