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


@Entity
@Data
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String name;
    private String description;
    
    private Date date;
    @OneToMany
    @JoinColumn(name="product_id")
    private List<Product_images> images = new ArrayList<>();
    private Float price;
    private String size;
    private String brand;

    //@ManyToOne
    //private User user;

    
}
    
