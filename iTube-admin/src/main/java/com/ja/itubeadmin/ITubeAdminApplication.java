package com.ja.itubeadmin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(scanBasePackages = {"com.ja"}, exclude = {DataSourceAutoConfiguration.class})
public class ITubeAdminApplication {

    public static void main(String[] args) {
        SpringApplication.run(ITubeAdminApplication.class, args);
    }

}
