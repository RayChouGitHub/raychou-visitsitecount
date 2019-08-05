package com.git.hui.boot.redis.service;


import com.git.hui.boot.redis.entity.SiteVisitRank;
import com.git.hui.boot.redis.entity.SiteVisitRecord;
import com.git.hui.boot.redis.entity.SiteVisitTotal;
import com.git.hui.boot.redis.mapper.SiteVisitTotalMapper;
import com.git.hui.boot.redis.utils.RedisOperator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.RedisZSetCommands;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
@Slf4j
public class SiteVisitTotalTService {

    @Autowired
    private SiteVisitRankService siteVisitRankService;

    @Autowired
    private SiteVisitRecordTService siteVisitRecordService;

    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    @Autowired
    private RedisOperator redisOperator;

    private static String appName = "gddz-customer";

    @Autowired
    private SiteVisitTotalMapper busSiteVisitTotalTMapper;

    @Transactional(propagation = Propagation.REQUIRED,rollbackFor = Exception.class)
    public void taskSyncVisitData(){
        log.info("[站点访问统计] 开始同步站点访问量数据从缓存同步数据库，执行时间:{}",LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        long start = System.currentTimeMillis();
        Set<String> hotList = redisTemplate.keys("hot_cnt_*");
        Set<String> pvList = redisTemplate.keys("pv_cnt_*");
        Set<String> uvRankList = redisTemplate.keys("uv_uri_rank_*");
        Set<String> uvYesterdayList = redisTemplate.keys("pv_uri_tag_".concat(LocalDateTime.now().minusDays(1).format(DateTimeFormatter.ofPattern("yyyyMMdd"))).concat("_*"));

        List<SiteVisitTotal> visitTotals = new ArrayList<>();
        List<SiteVisitRank> visitRanks = new ArrayList<>();
        List<SiteVisitRecord> visitRecords = new ArrayList<>();
        Date nowDate = Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant());

        if(hotList != null && pvList != null && uvRankList != null){
            pvList.forEach(item ->{  // pv统计录入
                Map<Object, Object> map = redisOperator.hgetall(item);
                if(map != null){
                    map.keySet().forEach(s->{
                        Long value = Long.valueOf(map.get(s).toString());
                        SiteVisitTotal siteVisitTotal = new SiteVisitTotal();
                        siteVisitTotal.setAppName(appName);
                        siteVisitTotal.setUri(s.toString());
                        siteVisitTotal.setType((byte)2);
                        SiteVisitTotal result = selectRecentEndVisitValue(s.toString(),(byte) 2);
                        if(result != null && result.getEndVisit() != null){
                            siteVisitTotal.setInitialVisit(result.getEndVisit());
                            siteVisitTotal.setVisitValue(value - result.getEndVisit());
                            siteVisitTotal.setEndVisit(value);
                        }else{
                            siteVisitTotal.setInitialVisit(0l);
                            siteVisitTotal.setVisitValue(value);
                            siteVisitTotal.setEndVisit(value);
                        }
                        siteVisitTotal.setCalcDate(nowDate);
                        siteVisitTotal.setCreateDate(new Date());
                        siteVisitTotal.setLastupdateDate(new Date());
                        siteVisitTotal.setStatus((byte)1);

                        visitTotals.add(siteVisitTotal);
                    });
                }
            });

            hotList.forEach(item ->{ // hot统计录入
                Map<Object, Object> map = redisOperator.hgetall(item);
                if(map != null){
                    map.keySet().forEach(s->{
                        Long value = Long.valueOf(map.get(s).toString());
                        SiteVisitTotal siteVisitTotal = new SiteVisitTotal();
                        siteVisitTotal.setUri(s.toString());
                        siteVisitTotal.setAppName(appName);
                        siteVisitTotal.setType((byte)3);
                        SiteVisitTotal result = selectRecentEndVisitValue(s.toString(),(byte) 3);
                        if(result != null && result.getEndVisit() != null){
                            siteVisitTotal.setVisitValue(value - result.getEndVisit());
                            siteVisitTotal.setInitialVisit(result.getEndVisit());
                            siteVisitTotal.setEndVisit(value);
                        }else{
                            siteVisitTotal.setInitialVisit(0l);
                            siteVisitTotal.setVisitValue(value);
                            siteVisitTotal.setEndVisit(value);
                        }
                        siteVisitTotal.setCalcDate(nowDate);
                        siteVisitTotal.setLastupdateDate(new Date());
                        siteVisitTotal.setCreateDate(new Date());
                        siteVisitTotal.setStatus((byte)1);

                        visitTotals.add(siteVisitTotal);
                    });
                }
            });

            uvRankList.forEach(item ->{  //uv统计录入
                Set<RedisZSetCommands.Tuple> tuples = redisOperator.rangeWithScores(item, -1l, -1l);
                SiteVisitTotal siteVisitTotal = new SiteVisitTotal();
                Iterator<RedisZSetCommands.Tuple> iterator = tuples.iterator();
                while (iterator.hasNext())
                {
                    RedisZSetCommands.Tuple typedTuple = iterator.next();
                    siteVisitTotal.setUri(item);
                    siteVisitTotal.setAppName(appName);
                    siteVisitTotal.setType((byte)1);
                    SiteVisitTotal result = selectRecentEndVisitValue(item,(byte) 1);
                    if(result != null && result.getEndVisit() != null){
                        siteVisitTotal.setVisitValue(typedTuple.getScore().longValue() - result.getEndVisit());
                        siteVisitTotal.setInitialVisit(result.getEndVisit());
                        siteVisitTotal.setEndVisit(typedTuple.getScore().longValue());
                    }else{
                        siteVisitTotal.setInitialVisit(0l);
                        siteVisitTotal.setVisitValue(typedTuple.getScore().longValue());
                        siteVisitTotal.setEndVisit(typedTuple.getScore().longValue());
                    }
                    siteVisitTotal.setCalcDate(nowDate);
                    siteVisitTotal.setLastupdateDate(new Date());
                    siteVisitTotal.setStatus((byte)1);
                    siteVisitTotal.setCreateDate(new Date());
                }
                visitTotals.add(siteVisitTotal);
            });

            uvRankList.forEach(item ->{ // uv_rank统计录入
                Long maxRank = siteVisitRankService.selectRankScoreByAppNameAndUri(appName, item);
                Set<RedisZSetCommands.Tuple> tuples = redisOperator.zRangeByScoreWithScores(item, (maxRank == null ? 0l : maxRank) + 1L,Long.MAX_VALUE);
                Iterator<RedisZSetCommands.Tuple> iterator = tuples.iterator();
                while (iterator.hasNext())
                {
                    RedisZSetCommands.Tuple typedTuple = iterator.next();
                    SiteVisitRank siteVisitRank = new SiteVisitRank();
                    siteVisitRank.setAppName(appName);
                    siteVisitRank.setUri(item);
                    siteVisitRank.setIp(new String(typedTuple.getValue()));
                    siteVisitRank.setRank(typedTuple.getScore().longValue());
                    siteVisitRank.setLastupdateDate(new Date());
                    siteVisitRank.setCreateDate(new Date());
                    siteVisitRank.setStatus((byte)1);

                    visitRanks.add(siteVisitRank);
                }
            });
        }
        if(uvYesterdayList != null){ // uv每日访问记录录入
            uvYesterdayList.forEach(item ->{
                Map<Object, Object> map = redisOperator.hgetall(item);
                if(map != null){
                    map.keySet().forEach(m ->{
                        String value = map.get(m).toString();
                        SiteVisitRecord siteVisitRecord = new SiteVisitRecord();
                        siteVisitRecord.setAppName(appName);
                        siteVisitRecord.setUri(item);
                        siteVisitRecord.setIp(m.toString());
                        try {
                            siteVisitRecord.setVisitTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(value));
                            siteVisitRecord.setVisitDate(new SimpleDateFormat("yyyy-MM-dd").parse(value));
                        } catch (ParseException e) {
                            log.error("[访问数据统计] 数据访问时间转换异常");
                            throw new RuntimeException(e);
                        }
                        siteVisitRecord.setLastupdateDate(new Date());
                        siteVisitRecord.setCreateDate(new Date());
                        siteVisitRecord.setStatus((byte)1);
                        visitRecords.add(siteVisitRecord);
                    });
                }
            });
        }

        siteVisitRankService.batchInsert(visitRanks);
        siteVisitRecordService.batchInsert(visitRecords);
        batchInsert(visitTotals);
        log.info("[站点访问统计] 同步站点访问量数据从缓存同步数据库完毕，耗时{}秒，执行时间:{}",(System.currentTimeMillis()-start)/1000, LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
    }

    public SiteVisitTotal selectRecentEndVisitValue(String uri, Byte type){
        return busSiteVisitTotalTMapper.selectRecentEndVisitValue(uri,type);
    }

    public void batchInsert(List<SiteVisitTotal> busSiteVisitTotalTS){
        if(busSiteVisitTotalTS == null || busSiteVisitTotalTS.size() == 0)return;
        busSiteVisitTotalTMapper.insertList(busSiteVisitTotalTS);
    }
}