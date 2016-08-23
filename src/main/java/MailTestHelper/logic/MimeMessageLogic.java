package MailTestHelper.logic;

import MailTestHelper.errors.ApplicationException;
import MailTestHelper.errors.NotFoundException;
import MailTestHelper.persistent.MailEntity;
import MailTestHelper.persistent.MailRepository;
import MailTestHelper.response.Attachment;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


import javax.mail.*;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by kenji on 2016/07/08.
 */
@Component
@Slf4j
public class MimeMessageLogic {
    public static final String MULTIPART_ALTERNATIVE = "multipart/alternative";
    public static final String TEXT_HTML = "text/html";
    public static final String TEXT_PLAIN = "text/plain";
    @Autowired
    MailRepository mailRepotiroty;

    private Session session = Session.getDefaultInstance(new java.util.Properties(), null);


    public String getSource(String id){
        val mail = mailRepotiroty.findOne(id);
        try {
            return IOUtils.toString(new ByteArrayInputStream(mail.getMessage()), "US-ASCII");
        } catch (IOException e) {
            throw new ApplicationException("Failed to convert from poj to byte array.", e);
        }
    }

    public List<MailTestHelper.response.Message> getMessageList(){
        val mails = mailRepotiroty.findAll();
        return mails.stream().map(m -> {
            MailTestHelper.response.Message message = new MailTestHelper.response.Message();
            message.setId(m.getId());
            message.setSubject(m.getSubject());
            message.setFrom(m.getFrom());
            message.setTo(m.getTo());
            message.setDate(m.getDate());
            return message;
        }).collect(Collectors.toList());
    }

    public MailTestHelper.response.Message getMessage(String id){
        val mail = mailRepotiroty.findOne(id);
        return convertMailEntityToMessageDetail(mail);
    }

    public Attachment getAttachment(String id, String cid) throws IOException, MessagingException {
        val mail = mailRepotiroty.findOne(id);
        val message = convertMailEntityToMessageDetail(mail);
        val att =  message.getAttachments().stream().filter(a -> a.getContentId().equals(cid)).findFirst();
        att.orElseThrow(() -> (new NotFoundException("This message does not have such content.")));
        return att.get();
    }

    private List<Attachment> getAttachments(MimeMessage m) throws MessagingException, IOException {
        val attachments = new ArrayList<Attachment>();
        if(m.isMimeType(MULTIPART_ALTERNATIVE)){
            val mp = (Multipart)m.getContent();
            for(int i = 0; i < mp.getCount(); i++){
                if(Part.ATTACHMENT.equals(mp.getBodyPart(i).getDisposition()) &&
                        mp.getBodyPart(i) instanceof MimeBodyPart){
                    val mbp = (MimeBodyPart)mp.getBodyPart(i);
                    val att = new Attachment();
                    att.setContentId(mbp.getContentID());
                    att.setData(IOUtils.toByteArray(mbp.getInputStream()));
                    att.setContentType(mbp.getContentType());
                    att.setFileName(mbp.getFileName());
                    attachments.add(att);
                }
            }
        }
        return attachments;
    }

    private String getTextPart(MimeMessage m, String partName) throws MessagingException, IOException {
        if(m.isMimeType(partName)) {
            return (String)m.getContent();
        }else if(m.isMimeType(MULTIPART_ALTERNATIVE)){
            val mp = (Multipart)m.getContent();
            for(int i = 0; i < mp.getCount(); i++){
                val bp = mp.getBodyPart(i);
                if(bp.isMimeType(partName)){
                    return (String)bp.getContent();
                }
            }
        }
        return null;
    }

    public MailTestHelper.response.Message convertMailEntityToMessageDetail(MailEntity me){
        try {
            val m = new MimeMessage(session, new ByteArrayInputStream(me.getMessage()));
            val message = new MailTestHelper.response.Message();
            // The summary of email
            message.setId(me.getId());
            message.setTo(me.getTo());
            message.setFrom(me.getFrom());
            message.setSubject(me.getSubject());
            message.setDate(me.getDate());
            // The detail of email
            Enumeration<Header> headers = m.getAllHeaders();
            message.setHeaders(Collections.list(headers).stream().collect(Collectors.toList()));
            message.setHtml(getTextPart(m, TEXT_HTML));
            message.setText(getTextPart(m, TEXT_PLAIN));
            message.setAttachments(getAttachments(m));
            return message;
        } catch (MessagingException | IOException e) {
            log.error("generating Message is failed", e);
            throw new ApplicationException("Failed to convert MailEntity to Message.", e);
        }
    }

    public List<MailEntity> createMimeEntryFromInputStream(InputStream is) throws IOException, MessagingException {
        val rawData = IOUtils.toByteArray(is);
        val mimeMessage = new MimeMessage(session, new ByteArrayInputStream(rawData));
        val subject = mimeMessage.getSubject();
        // TODO allow registering multiple from address
        val from = mimeMessage.getFrom()[0].toString();
        val date = mimeMessage.getSentDate();

        return Arrays.asList(mimeMessage.getAllRecipients()).stream().map(to ->{
            MailEntity mailEntity = new MailEntity();
            mailEntity.setMessage(rawData);
            mailEntity.setTo(to.toString());
            mailEntity.setSubject(subject);
            mailEntity.setFrom(from);
            mailEntity.setDate(date);
            log.info("register an entity. to={}", to);
            return mailEntity;
        }).collect(Collectors.toList());
    }

    public void delete(String id){
        mailRepotiroty.delete(id);
        log.info("deleted from data base. id={}", id);
    }


}
