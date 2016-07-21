package MailTestHelper.logic;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.subethamail.smtp.MessageContext;
import org.subethamail.smtp.MessageHandler;
import org.subethamail.smtp.MessageHandlerFactory;

@Service
public class MyMessageHandlerFactory implements MessageHandlerFactory {

    @Autowired
    MessageHandler messageHandler;

    public MessageHandler create(MessageContext ctx) {
//        messageHandler.setMessageContext(ctx);
        return messageHandler;
    }

}
