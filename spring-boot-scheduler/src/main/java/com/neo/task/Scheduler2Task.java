package com.neo.task;

import lombok.extern.java.Log;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by summer on 2016/12/1.
 */
@Log
@Component
public class Scheduler2Task {
    private Long s = 100l;

    @Scheduled(cron = "0 0/2 * * * ?")
    public void cre() {
        int end = s.intValue();
        s = s + 100;
        log.warning("第一次开始==="+end);
        log.warning("");
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        log.warning("第一次结束==="+s.toString());


    }

}
