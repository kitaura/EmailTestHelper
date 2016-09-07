package MailTestHelper.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by kenji on 2016/07/06.
 */
@Data
public class Message {
    private String id;
    private String to;
    private String subject;
    private String from;
    private Date date;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String text;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String html;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Map<String, String> headers;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<Attachment> attachments;

}
