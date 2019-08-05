package com.git.hui.boot.redis.controller;

import com.git.hui.boot.redis.service.SiteVisitTotalTService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author RayChou
 * @version 1.0
 * @date 2019/7/29 10:40
 */
@RequestMapping("/visits")
@RestController
@Slf4j
public class CalcController {

    @Autowired
    private SiteVisitTotalTService siteVisitTotalService;

    @GetMapping("/data/sync")
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


    private static Map<String, String> map = new ConcurrentHashMap();
    private boolean isInCache(String key) {
        if (map.containsKey(key)) return true;
        else map.put(key, key);
        return false;
    }
    private void removeFromCache(String key) { map.remove(key); }
    
}
