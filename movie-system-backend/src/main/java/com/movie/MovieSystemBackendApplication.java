package com.movie;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class MovieSystemBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(MovieSystemBackendApplication.class, args);
    }

}
