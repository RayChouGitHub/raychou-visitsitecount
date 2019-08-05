package com.git.hui.boot.redis.service;

import com.git.hui.boot.redis.entity.SiteVisitRank;
import com.git.hui.boot.redis.mapper.SiteVisitRankMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author RayChou
 * @version 1.0
 * @date 2019/7/29 15:03
 */
@Service
public class SiteVisitRankService {

    @Autowired
    private SiteVisitRankMapper busSiteVisitRankTMapper;

    public void batchInsert(List<SiteVisitRank> busSiteVisitRankTS){
        if(busSiteVisitRankTS == null || busSiteVisitRankTS.size() == 0)return;
        busSiteVisitRankTMapper.insertList(busSiteVisitRankTS);
    }

    public Long selectRankScoreByAppNameAndUri(String appName,String uri){
        return busSiteVisitRankTMapper.selectRankScoreByAppNameAndUri(appName,uri);
    }
}
