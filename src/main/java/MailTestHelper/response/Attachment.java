package MailTestHelper.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

/**
 * Created by kenji on 2016/07/11.
 */
@Data
public class Attachment {
    @JsonIgnore
    private byte[] data;
    private String contentId;
    private String contentType;
    private String fileName;
}
