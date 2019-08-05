package com.git.hui.boot.redis.controller;

import com.git.hui.boot.redis.service.SiteVisitTotalTService;
import com.git.hui.boot.redis.site.SiteVisitFacade;
import com.git.hui.boot.redis.site.model.VisitReqDTO;
import com.git.hui.boot.redis.site.vo.SiteVisitDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * @author RayChou
 * @version 1.0
 * @date 2019/7/29 10:40
 */
@RequestMapping("/visit")
@RestController
@Slf4j
public class CalcController {

    @Autowired
    private SiteVisitTotalTService siteVisitTotalService;

    @Autowired
    private SiteVisitFacade siteVisitFacade;

    /**
     * 同步redis visit数据到mysql数据库
     * @return
     */
    @Scheduled(cron = "0 10 0 * * ?") // 每天00:10执行定时任务 同步前一天数据到数据库
    public ResponseEntity<Object> taskSyncVisitData(){
        String syncVisitDataLockKey = "syncVisitDataLockKey";
        try{
            if(!isInCache(syncVisitDataLockKey)){
                siteVisitTotalService.taskSyncVisitData();
                return new ResponseEntity<>("同步成功",HttpStatus.OK);
            }else{
                return new ResponseEntity<>("正在同步...",HttpStatus.OK);
            }
        }finally {
            removeFromCache(syncVisitDataLockKey);
        }
    }

    /**
     * 站点访问统计
     * @param reqDTO
     * @return
     * @throws ExecutionException
     * @throws InterruptedException
     */
    @GetMapping("/count")
    @ResponseBody
    public SiteVisitDTO visit(VisitReqDTO reqDTO) throws ExecutionException, InterruptedException {
        Future<SiteVisitDTO> visit = siteVisitFacade.visit(reqDTO);
        while (true){
            if(visit.isDone()){
                return visit.get();
            }
        }
    }


    /**
     * 单机版：线程锁
     */
    private static Map<String, String> map = new ConcurrentHashMap();
    private boolean isInCache(String key) {
        if (map.containsKey(key)) return true;
        else map.put(key, key);
        return false;
    }
    private void removeFromCache(String key) { map.remove(key); }
    
}
