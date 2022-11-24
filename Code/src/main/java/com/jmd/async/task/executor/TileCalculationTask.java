package com.jmd.async.task.executor;

import java.util.List;
import java.util.concurrent.Future;

import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Component;

import com.jmd.entity.geo.Polygon;
import com.jmd.entity.geo.Tile;
import com.jmd.entity.task.TaskBlockEntity;
import com.jmd.util.GeoUtils;

@Component
public class TileCalculationTask {

	@Async("TileCalculationExecutorPool")
	public Future<TaskBlockEntity> exec(int z, int x0, int x1, int y0, int y1, List<Polygon> polygons) {
		String name = z + "-" + x0 + "-" + x1 + "-" + y0 + "-" + y1;
		int count = 0;
		for (int x = x0; x <= x1; x++) {
			if (Thread.currentThread().isInterrupted()) {
				break;
			}
			for (int y = y0; y <= y1; y++) {
				if (Thread.currentThread().isInterrupted()) {
					break;
				}
				Tile tile = GeoUtils.getTile(z, x, y);
				for (int i = 0; i < polygons.size(); i++) {
					if (Thread.currentThread().isInterrupted()) {
						break;
					}
					if (GeoUtils.isTileInPolygon(tile, polygons.get(i))
							|| GeoUtils.isPolygonInTile(tile, polygons.get(i))) {
						count = count + 1;
						break;
					}
				}
			}
		}
		TaskBlockEntity block = new TaskBlockEntity(name, z, x0, x1, y0, y1, count, x0, y0, 0);
		return new AsyncResult<TaskBlockEntity>(block);
	}

}
