package es.ucm.fdi.iw.g01.bayshop.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
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
public class Message {
    public static enum MessageStatus{
        DELETED,
        READ,
        UNREAD
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String body;
    private LocalDateTime timestamp;
    private MessageStatus status;

    @ManyToOne
    private User userSender;

    @ManyToOne
    private User userReceiver;
}
    
