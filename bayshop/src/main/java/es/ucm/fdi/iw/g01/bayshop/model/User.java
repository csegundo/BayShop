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

@Entity
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String email;
    private String password;
    // Ruta del fichero donde esta la imagen de perfil, no hace falta guardarlo en una tabla user_images pues solo va a tener una
    private String image;
    private String username;
    private int baypoints;
    private String rol;

    @OneToMany
    @JoinColumn(name = "user_id")
    private List<Product> products = new ArrayList<>();

    @OneToMany
    @JoinColumn(name = "user_message_id")
    private List<Messages> messages = new ArrayList<>();

    @ManyToMany(mappedBy = "userWish")
    private List<Product> productWish = new ArrayList<>();
}
