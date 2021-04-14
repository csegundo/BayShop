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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import es.ucm.fdi.iw.g01.bayshop.model.Product;
import es.ucm.fdi.iw.g01.bayshop.model.Sale;
import es.ucm.fdi.iw.g01.bayshop.model.User;

// Vistas principales de nuestra aplicacion
@Controller
public class RootController {
    private static Logger logger = LogManager.getLogger(RootController.class);

    @Autowired
    private EntityManager entityManager;

    @GetMapping(value = { "/", "", "/home", "/index" })
    public String index(HttpSession session, Model model, @RequestParam(required = false) Integer entero){
        List<Product> prod = entityManager.createQuery("select p from Product p where status = 0").getResultList();

        model.addAttribute("prod", prod);
        model.addAttribute("revisar", false);
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
       List<Product> user = entityManager.createQuery("select u from User u").getResultList();
       List<Product> prod = entityManager.createQuery("select p from Product p where status = 0").getResultList();
        model.addAttribute("user", user);
        //logger.info("usuairoo-------------------------------------------------");
        model.addAttribute("prod", prod);
        model.addAttribute("title", "BayShop | Administrador");
       
        return "admin";
    }
    

    @GetMapping("/perfil/{id}")
    public String profile(HttpSession session, Model model, @PathVariable long id) {
		User actual = entityManager.find(User.class, id);
        
		model.addAttribute("title", "BayShop | Mi perfil");
		model.addAttribute("user", actual);

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

    @GetMapping("/compra/{id}")
    public String buy(HttpSession session, Model model, @PathVariable long id) {
        Product p = entityManager.find(Product.class, id);

        model.addAttribute("title", "BayShop | Resumen de compra");
        model.addAttribute("p", p);

        return "compra";
    }

    @PostMapping("/compra/{idP}/{idS}/{idB}")
    @Transactional
    public String buyProduct(HttpSession session, Model model, @PathVariable("idP") long idP, @PathVariable("idS") long idS, @PathVariable("idB") long idB){
        logger.warn("ID PRODUCTO", idP);
        logger.warn("ID BUYER", idB);
        logger.warn("ID SELLER", idS);

        // User buyer  = entityManager.find(User.class, idB);
        // User seller = entityManager.find(User.class, idS);
        // Sale sale   = new Sale();

        // Product product = entityManager.find(Product.class, idP);

        // sale.setBuyer(buyer);
        // sale.setSeller(seller);


        // return "redirect:/";
        return "redirect:/compra/3"; // solo de prueba con propositos de debuguear
    }
}
