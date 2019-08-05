package com.git.hui.boot.redis.service;

import com.git.hui.boot.redis.entity.SiteVisitRecord;
import com.git.hui.boot.redis.mapper.SiteVisitRecordMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author RayChou
 * @version 1.0
 * @date 2019/7/29 15:04
 */
@Service
public class SiteVisitRecordTService {

    @Autowired
    private SiteVisitRecordMapper busSiteVisitRecordTMapper;

    public void batchInsert(List<SiteVisitRecord> busSiteVisitRecordTS){
        if(busSiteVisitRecordTS == null || busSiteVisitRecordTS.size() == 0)return;
        busSiteVisitRecordTMapper.insertList(busSiteVisitRecordTS);
    }
}
