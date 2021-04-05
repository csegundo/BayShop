package es.ucm.fdi.iw.g01.bayshop.controller;

import java.util.Date;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;
import javax.persistence.EntityManager;

import org.apache.commons.logging.Log;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
// import org.hibernate.annotations.common.util.impl.Log.logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import es.ucm.fdi.iw.g01.bayshop.model.Product;
import es.ucm.fdi.iw.g01.bayshop.model.User;

// Vistas principales de nuestra aplicacion
@Controller
public class RootController {
    private static Logger logger = LogManager.getLogger(RootController.class);

    @Autowired
    private EntityManager entityManager;

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
        List<Product> users = entityManager.createQuery("select u from Users u").getResultList();
        List<Product> products = entityManager.createQuery("select p from Product p where p.productstatus=:1").getResultList();
        model.addAttribute("users", users);
        model.addAttribute("products", products);
        model.addAttribute("title", "BayShop | Administrador");
        return "admin";
    }

    @PostMapping("/producto/create")
    @Transactional
    public String createProduct(HttpServletResponse response, @ModelAttribute Product newProduct, Model model, HttpSession session){
        logger.debug("NUEVO PRODUCTO");
        
        // entityManager.persist(newProduct);

        return "index";
    }

    @GetMapping(value = { "/revisor/", "/revisor" })
    public String revisor(HttpSession session, Model model, @RequestParam(required = false) Integer entero) {
        model.addAttribute("title", "BayShop | REVISOR");
        return "revisor";
    }

    @GetMapping(value = { "/producto/crear", "/producto/crear/" })
    public String productCreate(HttpSession session, Model model, @RequestParam(required = false) Integer entero) {
        model.addAttribute("title", "BayShop | Crear producto");
        return "productoCrear";
    }

    @GetMapping("/perfil/id")
    public String profile(HttpSession session, Model model, @RequestParam(required = false) Integer entero) {
        model.addAttribute("title", "BayShop | Mi perfil");
        // Falta el where para indicar que son los productos del usuario
        List<Product> userProd = entityManager.createQuery("select p from Product p").getResultList();
        model.addAttribute("userProd", userProd);  
        // Falta el where para indicar que son las compras del usuario
        List<Product> userCompras = entityManager.createQuery("select p from Product p").getResultList();
        model.addAttribute("userCompras", userCompras);

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
