package MailTestHelper;

/**
 * Created by kenji on 2016/07/05.
 */
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import org.subethamail.smtp.server.SMTPServer;

//https://springframework.guru/running-code-on-spring-boot-startup/
@Component
@Slf4j
public class ContextRefreshedListener implements ApplicationListener<ContextRefreshedEvent>{

    private EventHolderBean eventHolderBean;

    @Autowired
    public void setEventHolderBean(EventHolderBean eventHolderBean) {
        this.eventHolderBean = eventHolderBean;
    }

    @Autowired private SMTPServer smtpServer;


    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        log.info("Context Event Received. SMTP server start!");
        smtpServer.start();
        eventHolderBean.setEventFired(true);
    }
}
