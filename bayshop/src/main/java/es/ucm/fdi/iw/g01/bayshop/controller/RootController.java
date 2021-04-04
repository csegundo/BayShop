package es.ucm.fdi.iw.g01.bayshop.controller;

import java.util.Date;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;
import javax.persistence.EntityManager;

import org.apache.commons.logging.Log;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
// import org.hibernate.annotations.common.util.impl.Log.logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import es.ucm.fdi.iw.g01.bayshop.model.Product;

// Vistas principales de nuestra aplicacion
@Controller
public class RootController {
    private static Logger logger = LogManager.getLogger(RootController.class);

    @Autowired
    private EntityManager entityManager;

    public static class Producto {
        
        private String nombre;//
        private String marca;//
        private String color;//
        private String talla;//
        private Double precio;//
        private String estado;//
        private String descripcion;//
        private Date fecha;//
        private String vendedor;//


        public Producto(String nombre,String marca, String color, String talla, Double precio, String estado, String descripcion) {
            this.nombre = nombre;
            this.vendedor = "Rick";
            this.marca = marca;
            this.color = color;
            this.talla = talla;
            this.precio = precio;
            this.estado = estado;
            this.descripcion = descripcion;
            //this.fecha = fecha;
        }

        public String getNombre() { return nombre; }
        public String getMarca() { return marca; }
        public String getVendedor() {return vendedor;}
        public String getColor() {return color;}
        public String getTalla() {return talla;}
        public Double getPrecio() {return precio;}
        public String getEstado() {return estado;}
        public String getDescripcion() {return descripcion;}
        public Date getFecha() {return fecha;}
    }
    
    Producto prod [] = new Producto []{
        new Producto("Camiseta chula", "Adidas", "Rojo", "XL", 23.45, "Nueva", "La vendo porque me queda grande"),
        new Producto("Camiseta fea", "Reebok", "Rojo", "XL", 30.25, "Nueva", "La vendo porque me queda grande"),
        new Producto("Camiseta fina", "Adidas", "Rojo", "XS", 18.25, "Nueva", "La vendo porque me queda pequeña"),
        new Producto("Albornoz", "Adidas", "Rojo", "XL", 30.25, "Nuevo", "La vendo porque me queda grande"),
        new Producto("Abrigo", "Nike", "Rojo", "XL", 30.25, "Nueva", "La vendo porque me queda grande"),
        new Producto("Chandal", "Adidas", "Rojo", "XL", 30.25, "Nueva", "La vendo porque me queda grande"),
        new Producto("Gorra", "P&B", "Rojo", "XL", 30.25, "Nueva", "La vendo porque me queda pequeña")
    };


    @GetMapping(value = { "/", "", "/home", "/index" })
    public String index(HttpSession session, Model model, @RequestParam(required = false) Integer entero){
        List<Product> prod = entityManager.createQuery("select p from Product p").getResultList();

        model.addAttribute("prod", prod);
        model.addAttribute("title", "BayShop | Todos los productos");
       
        return "index";
    }

    @GetMapping("/login")
    public String login(HttpSession session, Model model, @RequestParam(required = false) Integer entero) {
        model.addAttribute("title", "BayShop | Iniciar sesión");
        return "login";
    }

    @GetMapping("/register")
    public String register(HttpSession session, Model model, @RequestParam(required = false) Integer entero) {
        model.addAttribute("title", "BayShop | Crear cuenta");
        return "register";
    }

    @GetMapping(value = { "/admin/", "/admin" })
    public String admin(HttpSession session, Model model, @RequestParam(required = false) Integer entero) {
        model.addAttribute("title", "BayShop | ADMINISTRADOR");
        return "admin";
    }

    @GetMapping(value = { "/revisor/", "/revisor" })
    public String revisor(HttpSession session, Model model, @RequestParam(required = false) Integer entero) {
        model.addAttribute("title", "BayShop | REVISOR");
        return "revisor";
    }

    @GetMapping("/producto/id")
    public String product_id(HttpSession session, Model model, @RequestParam(required = false) Integer entero) {
        model.addAttribute("title", "BayShop | Producto <ID>");
        model.addAttribute("user", "validador");

        Producto p = new Producto("Camiseta chula", "Adidas", "Rojo", "XL", 23.45, "Nueva", "La vendo porque me queda grande");
        model.addAttribute("p", p);
        model.addAttribute("prod", prod);

        return "producto";
    }

    @GetMapping(value = { "/producto/crear", "/producto/crear/" })
    public String productCreate(HttpSession session, Model model, @RequestParam(required = false) Integer entero) {
        model.addAttribute("title", "BayShop | Crear producto");
        return "productoCrear";
    }

    @GetMapping("/perfil/id")
    public String profile(HttpSession session, Model model, @RequestParam(required = false) Integer entero) {
        model.addAttribute("title", "BayShop | Mi perfil");
        return "perfil";
    }

    @GetMapping(value = { "/mensajes/", "/mensajes" })
    public String allMessages(HttpSession session, Model model, @RequestParam(required = false) Integer entero) {
        model.addAttribute("title", "BayShop | Mis mensajes");
        return "mensajes";
    }

    @GetMapping("/mensajes/idU/idP")
    public String message(HttpSession session, Model model, @RequestParam(required = false) Integer entero) {
        model.addAttribute("title", "BayShop | Mensaje <ID>");
        return "mensaje";
    }

    @GetMapping(value = { "/compra/", "/compra" })
    public String buy(HttpSession session, Model model, @RequestParam(required = false) Integer entero) {
        model.addAttribute("title", "BayShop | Resumen de compra");
        return "compra";
    }
}
