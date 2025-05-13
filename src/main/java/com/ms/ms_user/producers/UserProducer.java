package com.ms.ms_user.producers;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import com.ms.ms_user.dtos.UserCreatedEventDTO;
import com.ms.ms_user.dtos.UserRequestDTO;

import org.springframework.beans.factory.annotation.Value;

@Component
public class UserProducer {

    final RabbitTemplate rabbitTemplate;

    @Value(value = "${broker.queue.email.name}")
    private String queueKey;

    public UserProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void publishEmailMessage(UserRequestDTO userDataDTO) {
        UserCreatedEventDTO event = new UserCreatedEventDTO();
        event.setId(userDataDTO.getUserId());
        event.setEmail(userDataDTO.getEmail());
        event.setName(userDataDTO.getName());

        rabbitTemplate.convertAndSend(queueKey, event);
    }

}