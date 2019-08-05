package com.git.hui.boot.redis.mappers;

import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;

/**
 * @author RayChou
 * @version 1.0
 * @date 2019/7/29 10:30
 */
public interface MyMapper<T> extends Mapper<T>, MySqlMapper<T> {
}
