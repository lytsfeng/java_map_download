package com.jmd.taskfunc;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import com.jmd.async.task.executor.TileCalculationTask;
import com.jmd.async.task.executor.TileDownloadTask;
import com.jmd.async.task.executor.TileErrorDownloadTask;
import com.jmd.async.task.executor.TileMergeTask;
import com.jmd.callback.LayerDownloadCallback;
import com.jmd.callback.LogCallback;
import com.jmd.callback.TileDownloadedCallback;
import com.jmd.common.StaticVar;
import com.jmd.entity.config.HttpClientConfigEntity;
import com.jmd.entity.geo.Bound;
import com.jmd.entity.geo.Polygon;
import com.jmd.entity.geo.Tile;
import com.jmd.entity.result.BlockAsyncTaskResult;
import com.jmd.entity.result.ImageMergeAsyncTaskResult;
import com.jmd.entity.task.ErrorTileEntity;
import com.jmd.entity.task.TaskAllInfoEntity;
import com.jmd.entity.task.TaskBlockDivide;
import com.jmd.entity.task.TaskBlockEntity;
import com.jmd.entity.task.TaskCreateEntity;
import com.jmd.entity.task.TaskExecEntity;
import com.jmd.entity.task.TaskInstEntity;
import com.jmd.http.HttpClient;
import com.jmd.http.HttpConfig;
import com.jmd.util.GeoUtils;
import com.jmd.util.TaskUtils;

import lombok.extern.slf4j.Slf4j;

import javax.swing.*;

@Slf4j
@Component
public class TaskStepFunc {

    @Value("${tile.block-divide}")
    private int blockDivide;

    @Lazy
    @Autowired
    private TaskExecFunc taskExecFunc;

    @Autowired
    private HttpClient httpClient;
    @Autowired
    private HttpConfig httpConfig;
    @Autowired
    private TileCalculationTask tileCalculationTask;
    @Autowired
    private TileDownloadTask tileDownloadTask;
    @Autowired
    private TileErrorDownloadTask tileErrorDownloadTask;
    @Autowired
    private TileMergeTask tileMergeTask;

    /**
     * 创建下载任务
     */
    public TaskAllInfoEntity tileDownloadTaskCreate(TaskCreateEntity taskCreate, LogCallback logback) {
        TaskAllInfoEntity taskAllInfo = new TaskAllInfoEntity();
        ConcurrentHashMap<Integer, TaskInstEntity> eachLayerTask = new ConcurrentHashMap<>();
        logback.execute("开始计算...");
        int count = 0;
        long start = System.currentTimeMillis();
        for (int z : taskCreate.getZoomList()) {
            try {
                TaskInstEntity inst = tileTaskInstCalculation(z, taskCreate.getPolygons(), logback);
                inst.setNeedMerge(taskCreate.getIsMergeTile());
                inst.setIsMerged(false);
                count = count + inst.getRealCount();
                eachLayerTask.put(z, inst);
            } catch (InterruptedException | ExecutionException e) {
                log.error("Tile Calculation Error", e);
            }
        }
        long end = System.currentTimeMillis();
        taskAllInfo.setTileUrl(taskCreate.getTileUrl());
        taskAllInfo.setImgType(taskCreate.getImgType());
        taskAllInfo.setTileName(taskCreate.getTileName());
        taskAllInfo.setMapType(taskCreate.getMapType());
        taskAllInfo.setSavePath(taskCreate.getSavePath());
        taskAllInfo.setPathStyle(taskCreate.getPathStyle());
        taskAllInfo.setIsCoverExists(taskCreate.getIsCoverExists());
        taskAllInfo.setIsMergeTile(taskCreate.getIsMergeTile());
        taskAllInfo.setAllRealCount(count);
        taskAllInfo.setAllRunCount(0);
        taskAllInfo.setEachLayerTask(eachLayerTask);
        taskAllInfo.setErrorTiles(new ConcurrentHashMap<>());
        logback.execute("计算完成");
        logback.execute("需要下载的总数：" + count + "，瓦片图计算所用时间：" + (end - start) / 1000 + "秒");
        logback.execute("开始下载...");
        return taskAllInfo;
    }

    /**
     * 计算任务实例下载量
     */
    public TaskInstEntity tileTaskInstCalculation(int zoom, List<Polygon> polygons, LogCallback logback)
            throws InterruptedException, ExecutionException {
        Bound bound = GeoUtils.getPolygonsBound(polygons);
        Tile topLeftTile = GeoUtils.getTile(zoom, bound.getTopLeft());
        Tile bottomRightTile = GeoUtils.getTile(zoom, bound.getBottomRight());
        TaskBlockDivide divide = TaskUtils.blockDivide(topLeftTile.getX(), bottomRightTile.getX(), topLeftTile.getY(),
                bottomRightTile.getY(), Math.sqrt(blockDivide));
        ArrayList<Integer[]> divideX = divide.getDividX();
        ArrayList<Integer[]> divideY = divide.getDividY();
        int countX = divide.getCountX();
        int countY = divide.getCountY();
        List<Future<TaskBlockEntity>> futures = new ArrayList<>();
        for (Integer[] x : divideX) {
            for (Integer[] y : divideY) {
                Future<TaskBlockEntity> future = tileCalculationTask.exec(zoom, topLeftTile.getX() + x[0],
                        topLeftTile.getX() + x[1], topLeftTile.getY() + y[0],
                        topLeftTile.getY() + y[1], polygons);
                futures.add(future);
            }
        }
        TaskInstEntity inst = new TaskInstEntity();
        inst.setZ(zoom);
        inst.setPolygons(polygons);
        inst.setDivideX(divideX);
        inst.setDivideY(divideY);
        inst.setXStart(topLeftTile.getX());
        inst.setYStart(topLeftTile.getY());
        inst.setXEnd(bottomRightTile.getX());
        inst.setYEnd(bottomRightTile.getY());
        ConcurrentHashMap<String, TaskBlockEntity> blocks = new ConcurrentHashMap<>();
        int count = 0;
        boolean isCanceled = false;
        for (Future<TaskBlockEntity> future : futures) {
            if (isCanceled) {
                break;
            }
            try {
                TaskBlockEntity block = future.get();
                if (block.getRealCount() > 0) {
                    blocks.put(block.getName(), block);
                    count = count + block.getRealCount();
                }
            } catch (InterruptedException e1) {
                isCanceled = true;
                for (Future<TaskBlockEntity> f : futures) {
                    f.cancel(true);
                }
            } catch (ExecutionException e2) {
                e2.printStackTrace();
            }
        }
        inst.setRealCount(count);
        inst.setAllCount(countX * countY);
        inst.setBlocks(blocks);
        logback.execute(zoom + "级该多边形bound内瓦片图总数" + inst.getAllCount() + "，需要下载的总数：" + count);
        return inst;
    }

    /**
     * 配置HTTP
     */
    public TaskAllInfoEntity setHttpConfig(TaskAllInfoEntity taskAllInfo, LogCallback logBack) {
        HttpClientConfigEntity config;
        if (taskAllInfo.getMapType().indexOf("OpenStreet") == 0) {
            config = httpConfig.getOsmConfig();
            logBack.execute("OpenStreet下载，配置okhttp3");
        } else if (taskAllInfo.getTileName().indexOf("Tianditu") == 0) {
            logBack.execute("天地图下载，配置okhttp3");
            config = httpConfig.getTianConfig();
        } else if (taskAllInfo.getTileName().indexOf("Google") == 0) {
            logBack.execute("谷歌地图下载，配置okhttp3");
            config = httpConfig.getGoogleConfig();
        } else if (taskAllInfo.getTileName().indexOf("AMap") == 0) {
            logBack.execute("高德地图下载，配置okhttp3");
            config = httpConfig.getAmapConfig();
        } else if (taskAllInfo.getTileName().indexOf("Tencent") == 0) {
            logBack.execute("腾讯地图下载，配置okhttp3");
            config = httpConfig.getTencentConfig();
        } else if (taskAllInfo.getTileName().indexOf("Bing") == 0) {
            logBack.execute("必应地图下载，配置okhttp3");
            config = httpConfig.getBingConfig();
        } else {
            config = httpConfig.getDefaultConfig();
        }
        logBack.execute("ConnectTimeout: " + config.getConnectTimeout() + "ms");
        logBack.execute("ReadTimeout: " + config.getReadTimeout() + "ms");
        logBack.execute("WriteTimeout: " + config.getWriteTimeout() + "ms");
        logBack.execute("KeepAliveDuration: " + config.getKeepAliveDuration() + "ms");
        logBack.execute("MaxIdleConnections: " + config.getMaxIdleConnections());
        String result = httpClient.rebuild(config);
        if (result.equals("success")) {
            taskAllInfo.setHttpConfig(config);
        } else {
            JOptionPane.showMessageDialog(null, result);
        }
        return taskAllInfo;
    }

    /**
     * 任务下载方法
     */
    public void tileDownload(TaskAllInfoEntity taskAllInfo, LayerDownloadCallback layerStartBack,
                             LayerDownloadCallback layerEndBack, TileDownloadedCallback tileCB, LogCallback logBack) {
        boolean isCanceled = false;
        for (TaskInstEntity inst : taskAllInfo.getEachLayerTask().values()) {
            if (isCanceled || taskExecFunc.isCancel()) {
                break;
            }
            logBack.execute("正在下载第" + inst.getZ() + "级地图...");
            layerStartBack.execute(inst.getZ());
            // 分配多线程下载任务
            List<Future<BlockAsyncTaskResult>> futures = new ArrayList<>();
            for (TaskBlockEntity block : inst.getBlocks().values()) {
                int xStart = block.getXStart();
                int yStart = block.getYStart();
                int xRun = block.getXRun();
                int yRun = block.getYRun();
                int xEnd = block.getXEnd();
                int yEnd = block.getYEnd();
                int runCount = block.getRunCount();
                boolean addTaskFlag = false;
                if (xStart == xEnd || yStart == yEnd) {
                    if (xStart == xEnd && yStart == yEnd) {
                        addTaskFlag = true;
                    }
                    if (xStart == xEnd && yStart != yEnd) {
                        if (yRun != yEnd) {
                            addTaskFlag = true;
                        }
                    }
                    if (xStart != xEnd && yStart == yEnd) {
                        if (xRun != xEnd) {
                            addTaskFlag = true;
                        }
                    }
                } else {
                    if (xRun != xEnd || yRun != yEnd) {
                        addTaskFlag = true;
                    }
                }
                if (addTaskFlag) {
                    TaskExecEntity exec = new TaskExecEntity();
                    exec.setZ(inst.getZ());
                    exec.setXStart(xStart);
                    exec.setXEnd(xEnd);
                    exec.setYStart(yStart);
                    exec.setYEnd(yEnd);
                    exec.setXRun(xRun);
                    exec.setYRun(yRun);
                    exec.setStartCount(runCount);
                    exec.setPolygons(inst.getPolygons());
                    exec.setTileName(taskAllInfo.getTileName());
                    exec.setDownloadUrl(taskAllInfo.getTileUrl());
                    exec.setImgType(taskAllInfo.getImgType());
                    exec.setSavePath(taskAllInfo.getSavePath());
                    exec.setPathStyle(taskAllInfo.getPathStyle());
                    exec.setIsCoverExists(taskAllInfo.getIsCoverExists());
                    exec.setTileCB(tileCB);
                    exec.setHttpConfig(taskAllInfo.getHttpConfig());
                    Future<BlockAsyncTaskResult> future = tileDownloadTask.exec(exec);
                    futures.add(future);
                }
            }
            for (Future<BlockAsyncTaskResult> future : futures) {
                try {
                    future.get();
                } catch (InterruptedException e1) {
                    isCanceled = true;
                    for (Future<BlockAsyncTaskResult> f : futures) {
                        f.cancel(true);
                    }
                    break;
                } catch (ExecutionException e2) {
                    e2.printStackTrace();
                }
            }
            if (!isCanceled) {
                layerEndBack.execute(inst.getZ());
                logBack.execute("第" + inst.getZ() + "级地图下载完成");
            }
        }
    }

    /**
     * 错误瓦片下载方法
     */
    public void tileErrorDownload(TaskAllInfoEntity taskAllInfo, LogCallback logBack) {
        if (taskAllInfo.getErrorTiles().size() == 0) {
            return;
        }
        boolean isCanceled = false;
        logBack.execute("正在重新下载该层级错误瓦片，剩余数量：" + taskAllInfo.getErrorTiles().size());
        System.out.println("[正在重新下载该层级错误瓦片]");
        int maxThread = blockDivide;
        int eachDivide;
        if ((double) taskAllInfo.getErrorTiles().size() / (double) maxThread <= 10.0) {
            eachDivide = 10;
        } else if ((double) taskAllInfo.getErrorTiles().size() / (double) maxThread <= 15.0) {
            eachDivide = 15;
        } else if ((double) taskAllInfo.getErrorTiles().size() / (double) maxThread <= 20.0) {
            eachDivide = 20;
        } else {
            eachDivide = taskAllInfo.getErrorTiles().size() / maxThread;
        }
        int mapIndex = 0;
        int listIndex = 0;
        // 分配多线程下载任务
        List<Future<BlockAsyncTaskResult>> futures = new ArrayList<>();
        List<List<ErrorTileEntity>> errorTileList = new ArrayList<>();
        for (Map.Entry<String, ErrorTileEntity> entry : taskAllInfo.getErrorTiles().entrySet()) {
            mapIndex = mapIndex + 1;
            if (errorTileList.size() <= listIndex) {
                errorTileList.add(new ArrayList<>());
            }
            errorTileList.get(listIndex).add(entry.getValue());
            if (mapIndex % eachDivide == 0 || mapIndex == taskAllInfo.getErrorTiles().size()) {
                Future<BlockAsyncTaskResult> future = tileErrorDownloadTask.exec(taskAllInfo,
                        errorTileList.get(listIndex));
                futures.add(future);
                listIndex = listIndex + 1;
            }
        }
        for (Future<BlockAsyncTaskResult> future : futures) {
            try {
                future.get();
            } catch (InterruptedException e1) {
                isCanceled = true;
                for (Future<BlockAsyncTaskResult> f : futures) {
                    f.cancel(true);
                }
                break;
            } catch (ExecutionException e2) {
                e2.printStackTrace();
            }
        }
        // 下载完成，递归
        if (!isCanceled && taskAllInfo.getErrorTiles().size() > 0) {
            try {
                if (taskAllInfo.getErrorTiles().size() >= 6) {
                    Thread.sleep(1500);
                } else if (taskAllInfo.getErrorTiles().size() >= 3) {
                    Thread.sleep(3000);
                } else if (taskAllInfo.getErrorTiles().size() == 1) {
                    Thread.sleep(5000);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            tileErrorDownload(taskAllInfo, logBack);
        }
    }

    /**
     * 合并图片
     */
    public void mergeTileImage(TileMergeMatWrap mat, TaskAllInfoEntity taskAllInfo, int zoom, LogCallback logBack,
                               LogCallback firshFinishBack) {
        if (zoom == 0) {
            return;
        }
        try {
            logBack.execute("正在合并第" + zoom + "级地图，请勿关闭程序 implemented by OpenCV");
            // 声明变量
            TaskInstEntity taskInst = taskAllInfo.getEachLayerTask().get(zoom);
            int z = taskInst.getZ();
            int xStart = taskInst.getXStart();
            int xEnd = taskInst.getXEnd();
            int yStart = taskInst.getYStart();
            int yEnd = taskInst.getYEnd();
            int mergeImageWidth = StaticVar.TILE_WIDTH * (xEnd - xStart + 1);
            int mergeImageHeight = StaticVar.TILE_HEIGHT * (yEnd - yStart + 1);
            long xiangsudaxiao = (long) mergeImageWidth * (long) mergeImageHeight;
            logBack.execute("合并后的图片width：" + mergeImageWidth + "，height：" + mergeImageHeight + "，像素大小："
                    + xiangsudaxiao);
            if (xiangsudaxiao > (long) Integer.MAX_VALUE) {
                logBack.execute("该" + zoom + "级地图合并后像素大小大于int最大值" + Integer.MAX_VALUE + "，合并时间可能会稍长，建议低配置电脑不要执行超大尺寸合并");
            }
            String outPath = taskAllInfo.getSavePath() + "/tile-merge";
            File outDir = new File(outPath);
            if (!outDir.exists() || !outDir.isDirectory()) {
                outDir.mkdirs();
            }
            // 文件类型
            String suffix = TaskUtils.getSuffix(taskAllInfo.getImgType());
            String outputFile = outPath + "/z=" + z + "." + suffix;
            // 开启线程
            mat.init(mergeImageWidth, mergeImageHeight);
            int cpuCoreCount = Runtime.getRuntime().availableProcessors();
            double d = Math.floor(Math.sqrt(cpuCoreCount));
            TaskBlockDivide divide = TaskUtils.blockDivide(xStart, xEnd, yStart, yEnd, d);
            ArrayList<Integer[]> divideX = divide.getDividX();
            ArrayList<Integer[]> divideY = divide.getDividY();
            List<Future<ImageMergeAsyncTaskResult>> futures = new ArrayList<>();
            for (int i = 0; i < divideX.size(); i++) {
                for (int j = 0; j < divideY.size(); j++) {
                    Future<ImageMergeAsyncTaskResult> future = tileMergeTask.exec(mat, z, xStart, yStart,
                            xStart + divideX.get(i)[0], xStart + divideX.get(i)[1], yStart + divideY.get(j)[0],
                            yStart + divideY.get(j)[1], taskInst.getPolygons(), taskAllInfo.getImgType(),
                            taskAllInfo.getSavePath(), taskAllInfo.getPathStyle(), i, j);
                    futures.add(future);
                }
            }
            for (Future<ImageMergeAsyncTaskResult> future : futures) {
                try {
                    future.get();
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }
            }
            firshFinishBack.execute("true");
            logBack.execute("正在写入至硬盘...");
            mat.output(outputFile);
            mat.destroy();
            logBack.execute("第" + zoom + "级地图合并完成");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
