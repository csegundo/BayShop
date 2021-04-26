package es.ucm.fdi.iw.g01.bayshop.model;

import java.math.BigDecimal;
import java.util.ArrayList;

import java.util.List;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;


import java.time.LocalDateTime;

@Entity
@Data
public class Product {
    
    public static enum ProductStatus{
        ACCEPTED,   // 0
        PENDING,    // 1
        REJECT,     // 2
        SOLD        // 3
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String name;
    private String description;
    private LocalDateTime creationDate;
    private BigDecimal price;
    private String size;
    private String brand;
    private ProductStatus status;
    private String categories;
    private Boolean enabled;

    @ManyToOne
    private User user;

    @ManyToMany
    private List<User> userWish = new ArrayList<>();

    @ManyToOne
    private Sale sale;
}