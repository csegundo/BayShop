package es.ucm.fdi.iw.g01.bayshop.model;

import java.util.ArrayList;
import java.util.Date;
import lombok.Data;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;

// @Entity
// @Data
public class Messages {
    // @Id
    // @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String body;
    private Date timestamp;
    private long id_emisor; 
    private long id_receptor;


}
    
