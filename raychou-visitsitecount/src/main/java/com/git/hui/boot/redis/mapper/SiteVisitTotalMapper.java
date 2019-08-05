package com.git.hui.boot.redis.mapper;


import com.git.hui.boot.redis.entity.SiteVisitTotal;
import com.git.hui.boot.redis.mappers.MyMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface SiteVisitTotalMapper extends MyMapper<SiteVisitTotal> {

    SiteVisitTotal selectRecentEndVisitValue(@Param("uri") String uri, @Param("type") Byte type);
}