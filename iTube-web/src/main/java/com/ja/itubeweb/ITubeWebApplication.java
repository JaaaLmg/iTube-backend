package com.ja.itubeweb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(scanBasePackages = {"com.ja"}, exclude = {DataSourceAutoConfiguration.class})
public class ITubeWebApplication {
    public static void main(String[] args) {
        SpringApplication.run(ITubeWebApplication.class, args);
    }
}
