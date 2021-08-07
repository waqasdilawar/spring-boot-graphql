package com.discorp.controller;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.kafka.receiver.KafkaReceiver;
import reactor.kafka.receiver.ReceiverOptions;
import reactor.kafka.receiver.ReceiverRecord;
import reactor.kafka.receiver.internals.ConsumerFactory;
import reactor.kafka.receiver.internals.DefaultKafkaReceiver;

import java.time.Duration;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/test")
public class TestController
{
    @Autowired
    KafkaReceiver<String,String> kafkaReceiver;

    private final KafkaTemplate<Integer, String> template;

    public TestController(KafkaTemplate<Integer, String> template)
    {
        this.template = template;
    }


    @PostMapping
    public void send( @RequestParam String toSend) {
        this.template.send("test_gql", toSend);
    }

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

        return kafkaFlux.checkpoint("Messages are started being consumed").log().doOnNext( r -> r.receiverOffset().acknowledge() )
                .filter((ConsumerRecord stringStringReceiverRecord) -> {
                    System.out.println(stringStringReceiverRecord.value());
                    return true;
                })
                .map(ReceiverRecord::value)
                .checkpoint("Messages are done consumed");
    }

    @GetMapping(value = "/multiple/kafka-events/{userId}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> getMultipleKafkaEvents(@PathVariable String userId) {
        Flux<ReceiverRecord<String,String>> kafkaFlux = new DefaultKafkaReceiver(
                ConsumerFactory.INSTANCE,
                ReceiverOptions.create(this.getProperties(userId)).subscription(Collections.singletonList("test_gql"))
        ).receive();

        return kafkaFlux.checkpoint("Multiple: Messages are started being consumed").log().doOnNext( r -> r.receiverOffset().acknowledge() )
                .filter((ConsumerRecord stringStringReceiverRecord) -> {
                    System.out.println(stringStringReceiverRecord.value());
                    return true;
                })
                .map(ReceiverRecord::value)
                .checkpoint("Multiple: Messages are done consumed");
    }


    private Map<String, Object> getProperties(String userId)
    {
        Map<String, Object> props = new HashMap<>();
        props.put( ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put( ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put( ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put( ConsumerConfig.CLIENT_ID_CONFIG, "sample-client-" + userId + "-" + UUID.randomUUID());
        props.put( ConsumerConfig.GROUP_ID_CONFIG, userId);
        props.put( ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, true);
        return props;
    }
}
