package es.ucm.fdi.iw.g01.bayshop.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;

import lombok.Data;

// @Entity
// @Data
public class Wishlist {
    // @Id
    // @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private long id_user;
    private List<Product> id_products = new ArrayList<>();

}