package com.patria.apps.helper;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KafkaProducerService {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public <T> String send(String topic, T message) {
        kafkaTemplate.send(topic, message);
        return "Success send message!";
    }
    
}
