package com.jmd.async.task.scheduler;

import com.jmd.callback.DownloadMonitoringCallback;
import com.jmd.entity.task.TaskAllInfoEntity;
import com.jmd.entity.task.TaskBlockEntity;
import com.jmd.entity.task.TaskInstEntity;
import com.jmd.entity.task.TaskProgressEntity;

public class DownloadMonitoringInterval implements Runnable {

	private TaskAllInfoEntity taskAllInfo;
	private DownloadMonitoringCallback callBack;
	private int lastCount = 0;

	public DownloadMonitoringInterval(TaskAllInfoEntity taskAllInfo, DownloadMonitoringCallback callBack) {
		this.taskAllInfo = taskAllInfo;
		this.callBack = callBack;
	}

	@Override
	public void run() {
		int runCount = getRunCount();
		double allCount = taskAllInfo.getAllRealCount();
		double perc = (double) runCount / allCount;
		this.callBack.execute(new TaskProgressEntity(lastCount, runCount, perc));
		this.lastCount = runCount;
	}

	private int getRunCount() {
		int count = 0;
		for (TaskInstEntity inst : taskAllInfo.getEachLayerTask().values()) {
			for (TaskBlockEntity block : inst.getBlocks().values()) {
				count = count + block.getRunCount();
			}
		}
		return count;
	}

}
