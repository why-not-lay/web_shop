package com.web.web_shop;

import com.web.web_shop.Configuration.StorageProperties;
import com.web.web_shop.Service.StorageService;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableConfigurationProperties(StorageProperties.class)
public class WebShopApplication {

    public static void main(String[] args) {
        SpringApplication.run(WebShopApplication.class, args);
    }

    @Bean
    CommandLineRunner init(StorageService storageService) {
        return (args)-> {
            storageService.init();
        };
    }

}
