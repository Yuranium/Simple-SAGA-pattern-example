package com.yuranium.goodservice.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaConfig
{
    @Bean
    public NewTopic goodCommandTopic()
    {
        return TopicBuilder.name("good-command-topic")
                .partitions(1)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic orderEventsTopic()
    {
        return TopicBuilder.name("order-events-topic")
                .partitions(1)
                .replicas(1)
                .build();
    }
}
