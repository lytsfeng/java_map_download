package com.jmd.async.task.executor;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Future;

import com.jmd.taskfunc.TaskState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Component;

import com.jmd.callback.TileDownloadedCallback;
import com.jmd.entity.geo.Polygon;
import com.jmd.entity.geo.Tile;
import com.jmd.entity.result.BlockAsyncTaskResult;
import com.jmd.entity.result.DownloadResult;
import com.jmd.entity.task.TaskExecEntity;
import com.jmd.http.HttpDownload;
import com.jmd.util.CommonUtils;
import com.jmd.util.GeoUtils;
import com.jmd.util.TaskUtils;

@Component
public class TileDownloadTask {

    @Value("${download.retry}")
    private int retry;

    @Autowired
    private HttpDownload download;

    @Async("TileDownloadExecutorPool")
    public Future<BlockAsyncTaskResult> exec(TaskExecEntity taskExec) {
        BlockAsyncTaskResult result = new BlockAsyncTaskResult();
        /** 声明变量 */
        int z = taskExec.getZ();
        int xStart = taskExec.getXStart();
        int xEnd = taskExec.getXEnd();
        int yStart = taskExec.getYStart();
        int yEnd = taskExec.getYEnd();
        int xRun = taskExec.getXRun();
        int yRun = taskExec.getYRun();
        int startCount = taskExec.getStartCount();
        List<Polygon> polygons = taskExec.getPolygons();
        String downloadUrl = taskExec.getDownloadUrl();
        int imgType = taskExec.getImgType();
        String tileName = taskExec.getTileName();
        String savePath = taskExec.getSavePath();
        String pathStyle = taskExec.getPathStyle();
        boolean isCoverExist = taskExec.getIsCoverExists();
        TileDownloadedCallback callback = taskExec.getTileCB();
        HashMap<String, String> headers = taskExec.getHttpConfig().getHeaders();
        // 存储返回值信息
        result.setXStart(xStart);
        result.setXEnd(xEnd);
        result.setYStart(yStart);
        result.setYEnd(yEnd);
        // 执行方法
        List<String> urls = CommonUtils.expandUrl(downloadUrl);
        int urlIndex = 0;
        String name = z + "-" + xStart + "-" + xEnd + "-" + yStart + "-" + yEnd;
        int count = startCount;
        // 让每个线程不会在同一时间访问服务器
        try {
            int randomSleep = (int) (Math.random() * 500) + 10; // 10 - 500
            Thread.sleep(randomSleep);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // 循环开始
        for (int x = xStart; x <= xEnd; x++) {
            if (Thread.currentThread().isInterrupted()) {
                break;
            }
            if (x < xRun) {
                continue;
            }
            for (int y = yStart; y <= yEnd; y++) {
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
                if (xRun != xStart || yRun != yStart) {
                    if (x == xRun && y <= yRun) {
                        continue;
                    }
                }
                Tile tile = GeoUtils.getTile(z, x, y);
                boolean success = true;
                boolean isInFlag = false;
                for (Polygon polygon : polygons) {
                    isInFlag = GeoUtils.isTileInPolygon(tile, polygon)
                            || GeoUtils.isPolygonInTile(tile, polygon);
                    if (isInFlag) {
                        break;
                    }
                }
                String url = urls.get(urlIndex);
                if (isInFlag) {
                    String filePathAndName = savePath + TaskUtils.getFilePathName(pathStyle, imgType, z, x, y);
                    if (isCoverExist) {
                        // 覆盖：是
                        DownloadResult downloadResult = download(tileName, url, headers, pathStyle, imgType,
                                filePathAndName, z, x, y, retry);
                        success = downloadResult.isSuccess();
                    } else {
                        // 覆盖：否
                        File file = new File(filePathAndName);
                        if (file.exists() && file.isFile()) {
                            success = true;
                        } else {
                            DownloadResult downloadResult = download(tileName, url, headers, pathStyle, imgType,
                                    filePathAndName, z, x, y, retry);
                            success = downloadResult.isSuccess();
                        }
                    }
                    if (success) {
                        count = count + 1;
                    } else if (!Thread.currentThread().isInterrupted()) {
                        // System.out.println("Tile Download Error: " + "x-" + x + ",y-" + y + ",z-" +
                        // z);
                    }
                    urlIndex = urlIndex + 1;
                    if (urlIndex == urls.size()) {
                        urlIndex = 0;
                    }
                }
                callback.execute(z, name, count, x, y, success);
            }
        }
        result.setFlag(1);
        return new AsyncResult<>(result);
    }

    private DownloadResult download(String tileName, String downloadUrl, HashMap<String, String> headers,
                                    String pathStyle, int imgType, String filePathAndName, int z, int x, int y, int retry) {
        String url = CommonUtils.getDialectUrl(tileName, downloadUrl, z, x, y);
        return download.downloadTile(url, headers, imgType, filePathAndName, retry);
    }

}
