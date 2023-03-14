package br.com.audsat.audsatseguros.service.jms;

import br.com.audsat.audsatseguros.config.JmsConfig;
import br.com.audsat.audsatseguros.domain.Insurance;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.jms.JMSException;
import jakarta.jms.Message;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Slf4j
@Service
public class InsuranceSenderService {

    private final JmsTemplate jmsTemplate;
    private final ObjectMapper objectMapper;
    public void sendMessage(Insurance insurance) {
        jmsTemplate.convertAndSend(JmsConfig.MY_QUEUE, insurance);
        log.info("Message sent.");
    }

    public void sendAndReceiveMessage(Insurance insurance) throws JMSException {
        var returnedMessage = jmsTemplate.sendAndReceive(JmsConfig.MY_SEND_RECEIVE_QUEUE, session -> {
            try {
                Message insuranceMessage = session.createTextMessage(objectMapper.writeValueAsString(insurance));
                insuranceMessage.setStringProperty("_type", Insurance.class.getName());

                log.info("Sending the message {}", insuranceMessage);

                return insuranceMessage;
            } catch(Exception e) {
                throw new JMSException("Boom");
            }
        });
        log.info(returnedMessage.getBody(String.class));
    }
}
