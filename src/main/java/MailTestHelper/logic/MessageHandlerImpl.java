package MailTestHelper.logic;

import MailTestHelper.persistent.MailRepository;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.subethamail.smtp.MessageContext;
import org.subethamail.smtp.MessageHandler;
import org.subethamail.smtp.RejectException;

import javax.mail.MessagingException;
import java.io.*;

/**
 * Created by kenji on 2016/07/05.
 */
@Service
@Data
@Slf4j
class MessageHandlerImpl implements MessageHandler {
    @Autowired
    private MailRepository mailRepotiroty;

    @Autowired
    MimeMessageLogic mimeMessageLogic;
//    private MessageContext messageContext;

    public void from(String from) throws RejectException {
        log.info("FROM={}", from);
    }

    public void recipient(String recipient) throws RejectException {
        log.info("RECIPIENT={}", recipient);
    }

    public void data(InputStream data) {
        log.info("DATA");
        try {
            mimeMessageLogic.createMimeEntryFromInputStream(data).forEach(m ->{
                mailRepotiroty.save(m);
            });
            log.info("save an email into persistent repository.");
        } catch (MessagingException e) {
            log.error("generating email is failed", e);
        } catch (IOException e) {
            log.error("generating email is failed", e);
        }
    }

    public void done() {
        log.info("Finished");
    }

}
