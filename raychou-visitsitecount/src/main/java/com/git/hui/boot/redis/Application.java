package com.git.hui.boot.redis;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * Created by @author yihui in 16:16 19/5/12.
 */
@SpringBootApplication
@EnableScheduling
@EnableAsync
@MapperScan(basePackages = {"com.git.hui.boot.redis.mapper"})
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class);
    }

}
