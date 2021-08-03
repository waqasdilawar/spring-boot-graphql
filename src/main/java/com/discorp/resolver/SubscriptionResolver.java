package com.discorp.resolver;

import com.coxautodev.graphql.tools.GraphQLSubscriptionResolver;
import org.reactivestreams.Publisher;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.Random;

@Component
public class SubscriptionResolver implements GraphQLSubscriptionResolver
{

    public Publisher<Boolean> getRandomValue(String symbol) {

        Random random = new Random();
        return Flux.interval(Duration.ofSeconds(1))
                .map(num -> random.nextBoolean());
    }
}
