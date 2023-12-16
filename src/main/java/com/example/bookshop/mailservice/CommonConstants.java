package com.example.bookshop.mailservice;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class CommonConstants {
    public static String KAFKA_TOPIC_ACCOUNT_REGISTRATION;
    public static String KAFKA_TOPIC_ACCOUNT_CONFIRMED;
    public static String KAFKA_TOPIC_ACCOUNT_PASSWORD_CHANGE;
    public static String KAFKA_TOPIC_ACCOUNT_PASSWORD_CHANGE_CONFIRMED;
    public static Long KAFKA_BACKOFF_INTERVAL;
    public static Long KAFKA_BACKOFF_MAX_ATTEMPTS;

    @Value("${constant.kafka.account-registration}")
    public void setKafkaTopicAccountRegistration(String topic) {
        CommonConstants.KAFKA_TOPIC_ACCOUNT_REGISTRATION = topic;
    }

    @Value("${constant.kafka.account-confirm-success}")
    public void setKafkaTopicAccountConfirmed(String topic) {
        CommonConstants.KAFKA_TOPIC_ACCOUNT_CONFIRMED = topic;
    }

    @Value("${constant.kafka.backoff.interval}")
    public void setKafkaBackoffInterval(Long interval) {
        CommonConstants.KAFKA_BACKOFF_INTERVAL = interval;
    }

    @Value("${constant.kafka.backoff.max-attempts}")
    public void setKafkaBackoffMaxAttempts(Long maxAttempts) {
        CommonConstants.KAFKA_BACKOFF_MAX_ATTEMPTS = maxAttempts;
    }

    @Value("${constant.kafka.account-password-change}")
    public void setKafkaTopicAccountPasswordChange(String topic) {
        CommonConstants.KAFKA_TOPIC_ACCOUNT_PASSWORD_CHANGE = topic;
    }

    @Value("${constant.kafka.account-password-change-confirmed}")
    public void setKafkaTopicAccountPasswordChangeConfirmed(String topic) {
        CommonConstants.KAFKA_TOPIC_ACCOUNT_PASSWORD_CHANGE_CONFIRMED = topic;
    }
}
