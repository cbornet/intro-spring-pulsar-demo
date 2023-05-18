package com.acme.pulsar;

import java.time.Duration;

import com.acme.pulsar.PulsarApplication.User;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import org.springframework.stereotype.Service;

@Service
public class UserService {

	private final RandomApiClient randomApiClient;

	public UserService(RandomApiClient randomApiClient) {
		this.randomApiClient = randomApiClient;
	}

	User singleUser() {
		return randomApiClient.getUser();
	}

	Flux<User> multipleUsers() {
		return Mono.fromSupplier(randomApiClient::getUser)
				.delaySubscription(Duration.ofSeconds(1))
				.repeat();
	}

}
