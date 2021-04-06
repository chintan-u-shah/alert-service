package com.wheelsup.alertservice;

import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

/**
 * @author Akash Patel (apatel)
 */
@EnableEurekaClient
@EnableEncryptableProperties
@EnableMongoRepositories(basePackages = {"com.wheelsup.common.mongo"})
@SpringBootApplication
@RefreshScope
@ComponentScan(basePackages = {"com.wheelsup.alertservice", "com.wheelsup.common"})
@EnableFeignClients
public class AlertServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(AlertServiceApplication.class, args);
    }
}
