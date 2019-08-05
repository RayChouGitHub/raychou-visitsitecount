package com.git.hui.boot.redis.mapper;

import com.git.hui.boot.redis.entity.SiteVisitRank;
import com.git.hui.boot.redis.mappers.MyMapper;
import org.apache.ibatis.annotations.Param;

public interface SiteVisitRankMapper extends MyMapper<SiteVisitRank> {

    Long selectRankScoreByAppNameAndUri(@Param("appName") String appName, @Param("uri") String uri);
}