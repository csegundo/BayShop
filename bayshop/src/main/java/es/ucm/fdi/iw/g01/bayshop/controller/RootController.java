package es.ucm.fdi.iw.g01.bayshop.controller;

import java.util.Date;

import javax.servlet.http.HttpSession;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class RootController {
    private static Logger logger = LogManager.getLogger(RootController.class);

    public static class Producto {
        
        private String nombre;
        private String marca;
        private String color;
        private String talla;
        private Double precio;
        private String estado;
        private String descripcion;
        private Date fecha;
        private String vendedor;


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

        model.addAttribute("prod", prod);
        model.addAttribute("title", "BayShop | Todos los productos");
        return "index";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/register")
    public String register() {
        return "register";
    }

    @GetMapping("/admin/")
    public String admin() {
        return "admin";
    }

    @GetMapping(value = { "/revisor/", "/revisor" })
    public String revisor() {
        return "revisor";
    }

    @GetMapping("/producto/id")
    public String product_id(Model model) {
        
        model.addAttribute("user", "validador");

        Producto p = new Producto("Camiseta chula", "Adidas", "Rojo", "XL", 23.45, "Nueva", "La vendo porque me queda grande");
        model.addAttribute("p", p);
        model.addAttribute("prod", prod);

        return "producto";
    }

    @GetMapping(value = { "/producto/crear", "/producto/crear/" })
    public String productCreate() {
        return "producto_crear";
    }

    @GetMapping("/perfil/id")
    public String profile() {
        return "perfil";
    }

    @GetMapping(value = { "/mensajes/", "/mensajes" })
    public String allMessages() {
        return "mensajes";
    }

    @GetMapping("/mensajes/idU/idP")
    public String message() {
        return "mensaje";
    }

    @GetMapping(value = { "/compra/", "/compra" })
    public String buy() {
        return "compra";
    }
}
