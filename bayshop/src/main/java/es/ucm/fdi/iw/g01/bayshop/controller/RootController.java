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


        public Producto(String nombre, String marca, String color, String talla, Double precio, String estado, String descripcion) {
            this.nombre = nombre;
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
        public String getColor() {return color;}
        public String getTalla() {return talla;}
        public Double getPrecio() {return precio;}
        public String getEstado() {return estado;}
        public String getDescripcion() {return descripcion;}
        public Date getFecha() {return fecha;}
    }
    
    @GetMapping("/") 
    public String index(HttpSession session, Model model, @RequestParam(required = false) Integer entero){
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

    @GetMapping("/revisor/")
    public String revisor() {
        return "revisor";
    }

    @GetMapping("/producto/id")
    public String product_id(Model model) {
        model.addAttribute("user", "comprador");
        Producto p = new Producto("Camiseta chula", "Adidas", "Rojo", "XL", 23.45, "Nueva", "La vendo porque me queda grande");
        model.addAttribute("p", p);
        return "producto";
    }

    @GetMapping("/producto/crear")
    public String productCreate() {
        return "producto_crear";
    }

    @GetMapping("/perfil/id")
    public String profile() {
        return "perfil";
    }

    @GetMapping("/mensajes/")
    public String allMessages() {
        return "mensajes";
    }

    @GetMapping("/mensajes/idU/idP")
    public String message() {
        return "mensaje";
    }

    @GetMapping("/compra/")
    public String buy() {
        return "compra";
    }
}
