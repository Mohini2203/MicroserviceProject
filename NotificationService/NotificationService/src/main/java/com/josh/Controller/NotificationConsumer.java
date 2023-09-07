package com.josh.Controller;

import io.dapr.Topic;
import io.dapr.client.domain.CloudEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/notification")
public class NotificationConsumer {
    private static final Logger log = LoggerFactory.getLogger(NotificationConsumer.class);

    @Topic(name = "employee-department", pubsubName = "employee-pub-sub")
    @PostMapping(path = "/consumer")


    public Mono<Void> getConsume(@RequestBody(required = false) CloudEvent<String> cloudEvent) {
        return Mono.fromRunnable(() -> {
            try {
                log.info("subscriber received"+cloudEvent.getData());
            }catch (Exception e){
                throw  new RuntimeException(e);
            }
        });

    }
}
