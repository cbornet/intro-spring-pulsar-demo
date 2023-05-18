package com.acme.pulsar;

import com.acme.pulsar.PulsarApplication.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "random-api", url = "https://random-data-api.com/api/v2")
public interface RandomApiClient {

    @GetMapping("/users")
    User getUser();
}
