package br.com.audsat.audsatseguros.service.jms;

import br.com.audsat.audsatseguros.config.JmsConfig;
import br.com.audsat.audsatseguros.domain.Insurance;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class InsuranceProcessorListener {
    @JmsListener(destination = JmsConfig.MY_QUEUE)
    public void listen(@Payload Insurance insurance,
                       @Headers MessageHeaders messageHeaders, Message message) {
        log.info("I got your message {}", insurance);
    }
}
