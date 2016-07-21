package MailTestHelper;

import MailTestHelper.logic.MyMessageHandlerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.subethamail.smtp.server.SMTPServer;

@SpringBootApplication
public class MailTestHelperApplication {


    public static void main(String[] args) {
        SpringApplication.run(MailTestHelperApplication.class, args);
    }

    @Autowired
    MyMessageHandlerFactory myFactory;

    @Value("${app.smtp.port}")
    private int smtpPort;

    @Bean
    SMTPServer smtpServer(){
        SMTPServer smtpServer = new SMTPServer(myFactory);
        smtpServer.setPort(smtpPort);
        return smtpServer;
    }
}
