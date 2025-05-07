package com.ms.ms_user.producers;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import com.ms.ms_user.dtos.EmailRequestDTO;
import com.ms.ms_user.dtos.UserRequestDTO;

import org.springframework.beans.factory.annotation.Value;

@Component
public class UserProducer {

    final RabbitTemplate rabbitTemplate;

    @Value(value = "${broker.queue.email.name}")
    private String routingKey;

    public UserProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void publishEmailMessage(UserRequestDTO userDataDTO) {
        EmailRequestDTO emailToSendToQueue = new EmailRequestDTO();
        emailToSendToQueue.setUserId(userDataDTO.getUserId());
        emailToSendToQueue.setEmailTo(userDataDTO.getEmail());
        emailToSendToQueue.setSubject("O seu cadastro foi realizado com sucesso!");
        emailToSendToQueue.setContent("Obrigado por se cadastrar na nossa plataforma!");

        rabbitTemplate.convertAndSend("", routingKey, emailToSendToQueue);
    }

}
