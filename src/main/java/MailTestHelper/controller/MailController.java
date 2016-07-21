package MailTestHelper.controller;

import MailTestHelper.response.Attachment;
import MailTestHelper.response.Message;
import MailTestHelper.logic.MimeMessageLogic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.mail.MessagingException;
import java.io.IOException;
import java.util.List;


/**
 * Created by kenji on 2016/07/05.
 */
@RestController
@RequestMapping("/messages")
public class MailController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    MimeMessageLogic mimeMessageLogic;

    @RequestMapping(value = "/a")
    public List<Message> list() {
        return mimeMessageLogic.getMessageList();
    }

    @RequestMapping(value = "/{id}.json")
    public Message detail(@PathVariable String id) {
        return mimeMessageLogic.getMessage(id);
    }

    @RequestMapping(value = "/{id}.text")
    public String textMessage(@PathVariable String id) {
        return mimeMessageLogic.getMessage(id).getText();
    }

    @RequestMapping(value = "/{id}/{cid:.+}")
    public HttpEntity<byte[]> attachment(@PathVariable String id, @PathVariable String cid) throws IOException, MessagingException {
        logger.info("id={}, cid={}", id, cid);
        Attachment att = mimeMessageLogic.getAttachment(id, cid);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.valueOf(att.getContentType()));
        headers.setContentLength(att.getData().length);
        headers.setContentDispositionFormData("attachment", att.getFileName());

        return new HttpEntity<byte[]>(att.getData(), headers);
    }


    @RequestMapping(value = "/{id}.html")
    public String htmlMessage(@PathVariable String id) {
        return mimeMessageLogic.getMessage(id).getHtml();
    }

    @RequestMapping(value = "/{id}.source")
    public String source(@PathVariable String id) {
        return mimeMessageLogic.getSource(id);
    }

}
