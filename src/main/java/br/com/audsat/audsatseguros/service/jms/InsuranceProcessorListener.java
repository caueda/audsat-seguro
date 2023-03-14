package br.com.audsat.audsatseguros.service.jms;

import br.com.audsat.audsatseguros.config.JmsConfig;
import br.com.audsat.audsatseguros.domain.Insurance;
import jakarta.jms.Destination;
import jakarta.jms.JMSException;
import jakarta.jms.Session;
import jakarta.jms.TextMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.jms.support.JmsHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class InsuranceProcessorListener {

    private final JmsTemplate jmsTemplate;

    @JmsListener(destination = JmsConfig.MY_QUEUE)
    public void listen(@Payload Insurance insurance,
                       @Headers MessageHeaders messageHeaders, Message message) {
        log.info("I got your message {}", insurance);
    }

    @JmsListener(destination=JmsConfig.MY_SEND_RECEIVE_QUEUE)
    public void listenForInsurance(@Payload Insurance insurance,
                                   @Headers MessageHeaders messageHeaders,
                                   Message message) {

        String returnMessage = String.format("Insurance %s was processed successfully.", insurance.getId());
        jmsTemplate.send((Destination) message.getHeaders().get(JmsHeaders.REPLY_TO),
                session -> session.createTextMessage(returnMessage));
    }
}
