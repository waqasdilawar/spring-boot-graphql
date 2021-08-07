package com.discorp;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import reactor.kafka.receiver.KafkaReceiver;
import reactor.kafka.receiver.ReceiverOptions;
import reactor.kafka.receiver.internals.ConsumerFactory;
import reactor.kafka.receiver.internals.DefaultKafkaReceiver;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@SpringBootApplication
public class TestGraphQLApplication
{
	public static void main(String[] args) {
		SpringApplication.run(TestGraphQLApplication.class, args);
	}


	@Bean
	public KafkaReceiver kafkaReceiver()
	{
		return new DefaultKafkaReceiver(
				ConsumerFactory.INSTANCE,
				ReceiverOptions.create(getProperties()).subscription(Collections.singletonList("test_gql"))
		);
	}

	private Map<String, Object> getProperties()
	{
		Map<String, Object> props = new HashMap<>();
		props.put( ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
		props.put( ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
		props.put( ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
		props.put( ConsumerConfig.CLIENT_ID_CONFIG, "sample-client-1-asa2");
		props.put( ConsumerConfig.GROUP_ID_CONFIG, "sample-group-1-sasas");
		props.put( ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, true);
		return props;
	}
}
