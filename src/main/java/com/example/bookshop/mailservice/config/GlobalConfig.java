package com.example.bookshop.mailservice.config;

import com.example.bookshop.mailservice.CommonConstants;
import com.example.bookshop.mailservice.model.AccountConfirmation;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.DelegatingByTopicDeserializer;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.regex.Pattern;

@Configuration
@EnableKafka
public class GlobalConfig {
    @Bean
    public CopyOnWriteArrayList<AccountConfirmation> accountConfirmation() {
        return new CopyOnWriteArrayList<>();
    }

    @Bean
    public ConsumerFactory<String, Object> generateFactory(Map<String, Object> configs) {
        return new DefaultKafkaConsumerFactory<>(
                configs,
                new StringDeserializer(),
                new DelegatingByTopicDeserializer(Map.of(
                        Pattern.compile(CommonConstants.KAFKA_TOPIC_ACCOUNT_REGISTRATION), new StringDeserializer()),
                        new StringDeserializer())
        );
    }

    @Bean
    public NewTopic accountConfirmSuccessTopic() {
        return TopicBuilder.name(CommonConstants.KAFKA_TOPIC_ACCOUNT_CONFIRMED).build();
    }

    @Bean
    public NewTopic passwordConfirmSuccessTopic() {
        return TopicBuilder.name(CommonConstants.KAFKA_TOPIC_ACCOUNT_PASSWORD_CHANGE_CONFIRMED).build();
    }

}
