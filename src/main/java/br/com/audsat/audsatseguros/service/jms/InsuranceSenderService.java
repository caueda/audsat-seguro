package br.com.audsat.audsatseguros.service.jms;

import br.com.audsat.audsatseguros.config.JmsConfig;
import br.com.audsat.audsatseguros.domain.Insurance;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Slf4j
@Service
public class InsuranceSenderService {

    private final JmsTemplate jmsTemplate;
    public void sendMessage(Insurance insurance) {
        jmsTemplate.convertAndSend(JmsConfig.MY_QUEUE, insurance);
        log.info("Message sent.");
    }
}
