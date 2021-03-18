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
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;


@Entity
@Data
public class Product {
    
    public static enum ProductStatus{
        ACCEPTED,
        PENDING,
        REJECT,
        SOLD,
        DELETED
    }

    public static enum Category{
        SHIRT,
        HOODIE,
        JEANS,
        SHOES,
        OTHER
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String name;
    private String description;
    
    private Date date;
    @OneToMany
    @JoinColumn(name = "product_id")
    private List<Product_images> images = new ArrayList<>();
    private Float price;
    private String size;
    private String brand;
    private ProductStatus status;
    private Category category;

    @ManyToOne
    private User user;

    @ManyToMany
    private List<User> userWish = new ArrayList<>();

    @ManyToOne
    private Sales sales;
}