package com.acme.pulsar;

import org.apache.pulsar.client.api.Message;
import org.apache.pulsar.reactive.client.api.MessageResult;
import org.apache.pulsar.reactive.client.api.MessageSpec;
import reactor.core.publisher.Flux;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.pulsar.reactive.config.annotation.ReactivePulsarListener;
import org.springframework.pulsar.reactive.core.ReactivePulsarTemplate;

@SpringBootApplication
public class PulsarApplication {

	public static void main(String[] args) {
		SpringApplication.run(PulsarApplication.class, args);
	}

	@Autowired
	private ReactivePulsarTemplate<User> userTemplate;

	@Autowired
	private UserService userService;

	@Bean
	ApplicationRunner sourceUsersIntoPulsarTopic() {
		return (args) -> {
			userService.multipleUsers()
					.map(MessageSpec::of)
					.as(userTemplate::send)
					.doOnNext((sendResult) -> System.out.println("*** PRODUCE: " + sendResult.getMessageId()))
					.subscribe();
		};
	}

	@ReactivePulsarListener(stream = true)
	Flux<MessageResult<Void>> logUsersFromPulsarTopic(Flux<Message<User>> users) {
		return users.doOnNext((user) -> System.out.println("*** CONSUME: " + user.getValue()))
				.map(MessageResult::acknowledge);
	}

	public record User(String uid, String username) {

	}
}
