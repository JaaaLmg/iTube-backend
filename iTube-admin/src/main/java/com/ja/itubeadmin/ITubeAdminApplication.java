package com.ja.itubeadmin;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication(scanBasePackages = {"com.ja"})
@MapperScan("com.ja.itubecommon.mappers")
@EnableTransactionManagement
@EnableScheduling
public class ITubeAdminApplication {
    public static void main(String[] args) {
        SpringApplication.run(ITubeAdminApplication.class, args);
    }

}
