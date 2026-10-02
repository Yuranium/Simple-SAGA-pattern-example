package com.yuranium.goodservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.scheduling.annotation.EnableScheduling;

import static org.springframework.data.web.config.EnableSpringDataWebSupport.PageSerializationMode.VIA_DTO;

@EnableScheduling
@SpringBootApplication
@EnableSpringDataWebSupport(pageSerializationMode = VIA_DTO)
public class GoodServiceApplication
{
    static void main(String[] args)
    {
        SpringApplication.run(GoodServiceApplication.class, args);
    }

    @Bean
    public ObjectMapper objectMapper()
    {
        return new ObjectMapper();
    }
}