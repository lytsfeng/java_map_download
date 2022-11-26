package com.jmd.async.pool.scheduler;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import com.jmd.rx.SharedService;
import com.jmd.rx.SharedType;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.scheduling.support.PeriodicTrigger;
import org.springframework.stereotype.Component;


@Component
public class IntervalTaskSchedulerPool {

    @Autowired
    private SharedService sharedService;
    @Autowired
    private ThreadPoolTaskScheduler taskScheduler;

    private Map<Integer, ScheduledFuture<?>> intervalMap = new HashMap<>();

    @PostConstruct
    private void init() {
        this.subShared();
    }

    @Async("IntervalTaskSchedulerPool")
    public ScheduledFuture<?> setInterval(Runnable task, int secInterval) {
        return taskScheduler.schedule(task, (arg0) -> {
            String corn = "*/" + secInterval + " * * * * ?";
            return new CronTrigger(corn).nextExecution(arg0);
        });
    }

    @Async("IntervalTaskSchedulerPool")
    public ScheduledFuture<?> setInterval(Runnable task, long millInterval) {
        PeriodicTrigger periodicTrigger = new PeriodicTrigger(Duration.ofMillis(millInterval));
        periodicTrigger.setFixedRate(true);
        periodicTrigger.setInitialDelay(Duration.ofMillis(millInterval));
        return taskScheduler.schedule(task, periodicTrigger);
    }

    public boolean clearInterval(ScheduledFuture<?> future) {
        return future.cancel(true);
    }

    private void subShared() {
        sharedService.sub(SharedType.SET_INTERVAL).subscribe((res) -> {
            IntervalConfig config = (IntervalConfig) res;
            if (intervalMap.get(config.getId()) == null) {
                ScheduledFuture<?> future = setInterval(config.getTask(), config.getMill());
                intervalMap.put(config.getId(), future);
            }
        });
        sharedService.sub(SharedType.CLEAR_INTERVAL).subscribe((res) -> {
            int id = (int) res;
            ScheduledFuture<?> future = intervalMap.get(id);
            if (future != null) {
                clearInterval(future);
                intervalMap.remove(id);
            }
        });
    }

}
