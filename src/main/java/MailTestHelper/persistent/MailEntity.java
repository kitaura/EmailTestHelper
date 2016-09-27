package MailTestHelper.persistent;

import javax.persistence.*;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import java.util.Date;

/**
 * Created by kenji on 2016/07/04.
 */
@Entity
@Data
public class MailEntity {
    @Id
    @GeneratedValue(generator="id")
    @GenericGenerator(name = "id", strategy = "uuid2")
    private String id;

    private String subject;
    @Column(name = "from_add")
    private String from;
    private String to;
    private Date date;

    @Lob
    private byte[] message;
    private String searchKey;

}
