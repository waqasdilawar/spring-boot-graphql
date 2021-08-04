package com.discorp.controller;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.kafka.receiver.KafkaReceiver;
import reactor.kafka.receiver.ReceiverRecord;

import java.time.Duration;

@RestController
@RequestMapping("/test")
public class TestController
{
    @Autowired
    KafkaReceiver<String,String> kafkaReceiver;

    @GetMapping(value = "/events", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Boolean> getProductEvents() {
        return Flux.interval(Duration.ofSeconds(1))
                .map(val ->
                        true
                );
    }

    @GetMapping(value = "/kafka-events", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> getKafkaEvents() {
        Flux<ReceiverRecord<String,String>> kafkaFlux = kafkaReceiver.receive();

        return kafkaFlux.log().doOnNext( r -> r.receiverOffset().acknowledge() )
                .filter((ConsumerRecord stringStringReceiverRecord) -> {
                    System.out.println(stringStringReceiverRecord.value());
                    return true;
                })
                .map(ReceiverRecord::value);
    }


}
