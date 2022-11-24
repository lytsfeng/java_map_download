package com.jmd.async.task.executor;

import java.util.List;
import java.util.concurrent.Future;

import com.jmd.taskfunc.TaskState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Component;

import com.jmd.entity.result.BlockAsyncTaskResult;
import com.jmd.entity.result.DownloadResult;
import com.jmd.entity.task.ErrorTileEntity;
import com.jmd.entity.task.TaskAllInfoEntity;
import com.jmd.entity.task.TaskBlockEntity;
import com.jmd.http.HttpDownload;
import com.jmd.util.CommonUtils;
import com.jmd.util.TaskUtils;

@Component
public class TileErrorDownloadTask {

	@Value("${download.retry}")
	private int retry;

	@Autowired
	private HttpDownload download;

	@Async("TileDownloadExecutorPool")
	public Future<BlockAsyncTaskResult> exec(TaskAllInfoEntity taskAllInfo, List<ErrorTileEntity> errorTileList) {
		BlockAsyncTaskResult result = new BlockAsyncTaskResult();
		/** 获取URL */
		List<String> urls = CommonUtils.expandUrl(taskAllInfo.getTileUrl());
		int urlIndex = 0;
		for (ErrorTileEntity errorTile : errorTileList) {
			/** 任务取消 */
			if (Thread.currentThread().isInterrupted()) {
				break;
			}
			/** 任务暂停 */
			if (TaskState.IS_TASK_PAUSING) {
				while (true) {
					try {
						Thread.sleep(500);
					} catch (InterruptedException e) {
						System.out.println("暂停中取消任务");
					}
					if (!TaskState.IS_TASK_PAUSING) {
						break;
					}
				}
			}
			/** 声明变量 */
			int z = errorTile.getTile().getZ();
			int x = errorTile.getTile().getX();
			int y = errorTile.getTile().getY();
			String filePathAndName = taskAllInfo.getSavePath()
					+ TaskUtils.getFilePathName(taskAllInfo.getPathStyle(), taskAllInfo.getImgType(), z, x, y);
			String url = urls.get(urlIndex).replace("{z}", String.valueOf(z)).replace("{x}", String.valueOf(x))
					.replace("{y}", String.valueOf(y));
			/** 下载瓦片 */
			DownloadResult downloadResult = download.downloadTile(url, taskAllInfo.getHttpConfig().getHeaders(),
					taskAllInfo.getImgType(), filePathAndName, retry);
			boolean success = downloadResult.isSuccess();
			/** 下载瓦片 - 结果 */
			if (success) {
				// 下载成功，删除ErrorTile
				taskAllInfo.getErrorTiles().remove(errorTile.getKeyName());
				TaskBlockEntity block = taskAllInfo.getEachLayerTask().get(z).getBlocks().get(errorTile.getBlockName());
				block.setRunCount(block.getRunCount() + 1);
				taskAllInfo.getEachLayerTask().get(z).getBlocks().put(errorTile.getBlockName(), block);
			} else {
				// 下载失败，打印日志
				if (taskAllInfo.getErrorTiles().size() <= 50) {
					System.out.println("Tile Download Error: " + url);
				}
			}
			/** URL Index */
			urlIndex = urlIndex + 1;
			if (urlIndex == urls.size()) {
				urlIndex = 0;
			}
			/** 稳定下载，防止过快 */
			try {
				if (errorTileList.size() >= 50) {
					Thread.sleep(100);
				} else if (errorTileList.size() >= 20) {
					Thread.sleep(200);
				} else if (errorTileList.size() >= 10) {
					Thread.sleep(300);
				} else {
					Thread.sleep(500);
				}
			} catch (InterruptedException e) {
			}
		}
		return new AsyncResult<BlockAsyncTaskResult>(result);
	}

}
