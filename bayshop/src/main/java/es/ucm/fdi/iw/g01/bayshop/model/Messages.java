package es.ucm.fdi.iw.g01.bayshop.model;

import java.util.ArrayList;
import java.util.Date;
import lombok.Data;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity
@Data
public class Messages {
    public static enum MessageStatus{
        DELETED,
        READ,
        UNREAD
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String body;
    private Date timestamp;
    private MessageStatus status;

    @ManyToOne
    // @JoinColumn(name = "emisor_id")
    private User user_emisor;

    @ManyToOne
    // @JoinColumn(name = "receiver_id")
    private User user_receiver;
}
    
