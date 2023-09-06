package com.josh.Service;

import io.dapr.client.DaprClient;
import io.dapr.client.DaprClientBuilder;
import io.dapr.client.domain.Metadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static java.util.Collections.singletonMap;


@Service
public class NotificationService {

    private static final Logger log = LoggerFactory.getLogger(NotificationService.class);

    @Autowired
    private DaprClient daprClient;


    public void sendNotification(String message) {
        String MESSAGE_TTL_IN_SECONDS = "1000";
        String TOPIC_NAME = "employee-department";
        String PUBSUB_NAME = "employee-pub-sub";

        try {
           // DaprClient client = new DaprClientBuilder().build();

           // Thread.sleep(5000);
            daprClient.publishEvent(
                    PUBSUB_NAME,
                    TOPIC_NAME,
                    message).block();
                    //singletonMap(Metadata.TTL_IN_SECONDS, MESSAGE_TTL_IN_SECONDS)



            log.info("Published notification: " + message);
        } catch (Exception e) {
            log.error("Error publishing notification: " + e.getMessage(), e);
        }
    }
}


