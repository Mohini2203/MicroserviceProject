//package com.josh.Service;
//
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Component;
//
//import java.text.SimpleDateFormat;
//import java.util.Date;
//
//@Component
//@Slf4j
//public class Scheduler {
//    @Scheduled(fixedRate = 120000)
//    public void ScheduleTask(){
//
//        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss.SSS");
//        String strDate = simpleDateFormat.format(new Date());
//
//        log.info("Scheduler working ");
//        System.out.println(
//                "Fixed rate Scheduler: Task running at - "
//                        + strDate);
//    }
//
//    }
//
