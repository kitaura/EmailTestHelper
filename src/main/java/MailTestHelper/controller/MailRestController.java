package MailTestHelper.controller;

import MailTestHelper.response.Message;
import MailTestHelper.logic.MimeMessageLogic;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import java.io.IOException;
import java.util.List;


/**
 * Created by kenji on 2016/07/05.
 */
@RestController
@RequestMapping("/messages")
@Slf4j
public class MailRestController {
    @Autowired
    MimeMessageLogic mimeMessageLogic;


    @RequestMapping(value = "")
    public Page<Message> list(@RequestParam(value = "pageNum", defaultValue = "0") int pageNum,
                                @RequestParam(value = "searchKey") String searchKey) {
        return mimeMessageLogic.getMessageList(pageNum, searchKey);
    }

    @RequestMapping(value = "/{id}")
    public Message detail(@PathVariable String id) {
        return mimeMessageLogic.getMessage(id);
    }

    @RequestMapping(value = "/{id}.text")
    public String textMessage(@PathVariable String id) {
        return mimeMessageLogic.getMessage(id).getText();
    }

    @RequestMapping(value = "/{id}/{cid:.+}")
    public HttpEntity<byte[]> attachment(@PathVariable String id, @PathVariable String cid) throws IOException, MessagingException {
        log.info("id={}, cid={}", id, cid);
        val att = mimeMessageLogic.getAttachment(id, cid);

        val headers = new HttpHeaders();
        headers.setContentType(MediaType.valueOf(att.getContentType()));
        headers.setContentLength(att.getData().length);
        headers.setContentDispositionFormData("attachment", att.getFileName());

        return new HttpEntity<byte[]>(att.getData(), headers);
    }


    @RequestMapping(value = "/{id}.html")
    public String htmlMessage(@PathVariable String id) {
        return mimeMessageLogic.getMessage(id).getHtml();
    }

    //    @RequestMapping(value = "/{id}.source")
//    public String source(@PathVariable String id) {
//        return mimeMessageLogic.getSource(id);
//    }
    @RequestMapping(value = "/{id}.source")
    public HttpEntity<String> source(@PathVariable String id) {
        val headers = new HttpHeaders();
        headers.setContentType(MediaType.TEXT_PLAIN);

        return new HttpEntity<String>(mimeMessageLogic.getSource(id), headers);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public Message delete(@PathVariable("id") String id) {
        val m = mimeMessageLogic.getMessage(id);
        mimeMessageLogic.delete(id);
        return m;
    }

}
