package com.discorp.resolver;

import com.coxautodev.graphql.tools.GraphQLSubscriptionResolver;
import org.reactivestreams.Publisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.kafka.receiver.KafkaReceiver;
import reactor.kafka.receiver.ReceiverRecord;

@Component
public class KafkaSubscriptionResolver implements GraphQLSubscriptionResolver
{
    @Autowired
    KafkaReceiver<String,String> kafkaReceiver;

    public Publisher<String> getMessages(String in) {
        Flux<ReceiverRecord<String,String>> kafkaFlux = kafkaReceiver.receive();

        return kafkaFlux.log().doOnNext( r -> r.receiverOffset().acknowledge() )
                .map(ReceiverRecord::value);
    }
}
