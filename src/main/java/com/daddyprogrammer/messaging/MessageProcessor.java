package com.daddyprogrammer.messaging;

import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSAsync;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.aws.messaging.core.QueueMessagingTemplate;
import org.springframework.cloud.aws.messaging.listener.SqsMessageDeletionPolicy;
import org.springframework.cloud.aws.messaging.listener.annotation.SqsListener;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class MessageProcessor {

    private final String queueName = "webtoon-event-collect-dev";
    private QueueMessagingTemplate queueMessagingTemplate;

    @Autowired
    public MessageProcessor(AmazonSQS amazonSqs) {
        this.queueMessagingTemplate = new QueueMessagingTemplate((AmazonSQSAsync) amazonSqs);
    }

    public void send(String data) {
        Message<String> message = MessageBuilder.withPayload(data).build();
        queueMessagingTemplate.send(queueName, message);
    }

    @SqsListener(value = queueName, deletionPolicy = SqsMessageDeletionPolicy.NEVER)
    public void receive(String message) throws Exception {
        log.info("Event : {}", message);
        Thread.sleep(1000 * 5
        );
    }
}
