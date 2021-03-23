package es.ucm.fdi.iw.g01.bayshop.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lombok.Data;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.springframework.beans.factory.annotation.Value;

@Entity
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String email;
    private String password;

    @Value("true")
    private Boolean enabled;
    
    private String username;
    private int baypoints;
    private String rol;

    @OneToMany
    @JoinColumn(name = "user_id")
    private List<Product> products = new ArrayList<>();

    @ManyToMany(mappedBy = "userWish")
    private List<Product> productWish = new ArrayList<>();

    @OneToMany
    @JoinColumn(name = "user_sales_id")
    private List<Sale> sales = new ArrayList<>();


	@OneToMany
	@JoinColumn(name = "sender_id")
	private List<Message> sent = new ArrayList<>();
	@OneToMany
	@JoinColumn(name = "recipient_id")	
	private List<Message> received = new ArrayList<>();	


}
