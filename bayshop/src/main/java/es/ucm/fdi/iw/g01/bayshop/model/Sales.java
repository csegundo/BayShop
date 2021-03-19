package es.ucm.fdi.iw.g01.bayshop.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import lombok.Data;

@Entity
@Data
public class Sales {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    // @OneToMany
    // @JoinColumn(name = "sales_id")
    @OneToMany(mappedBy = "sales")
    private List<Product> product = new ArrayList<>();

    @ManyToOne
    private User user_buyer;

    @ManyToOne
    private User user_seller;
}