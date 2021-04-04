package es.ucm.fdi.iw.g01.bayshop.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import lombok.Data;

@Entity
@Data
public class Sale {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private LocalDateTime timestamp;

    @OneToMany
    @JoinColumn(name = "sale_id")
    private List<Product> products = new ArrayList<>();

    @ManyToOne
    private User buyer;

    @ManyToOne
    private User seller;
}