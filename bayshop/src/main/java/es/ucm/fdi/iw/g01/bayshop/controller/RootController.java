package es.ucm.fdi.iw.g01.bayshop.controller;

import java.util.Date;
import java.net.URLDecoder;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;
import javax.persistence.EntityManager;

import org.apache.commons.logging.Log;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
// import org.hibernate.annotations.common.util.impl.Log_.logger;
// import org.hibernate.annotations.common.util.impl.Log.logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import es.ucm.fdi.iw.g01.bayshop.model.Product;
import es.ucm.fdi.iw.g01.bayshop.model.Sale;
import es.ucm.fdi.iw.g01.bayshop.model.User;
import es.ucm.fdi.iw.g01.bayshop.model.Product.ProductStatus;

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

    @GetMapping(value = { "/buscar", "/buscar/" })
    public String search(HttpSession session, Model model, @RequestParam String query){
        List<Product> prod = entityManager.createNamedQuery("Product.searchQuery").setParameter("query", "%" + query + "%").getResultList();

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

    @GetMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if(auth != null){
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
        return "redirect:/login?logout";
    }

    @GetMapping("/register")
    public String register(HttpSession session, Model model, @RequestParam(required = false) Integer entero) {
        model.addAttribute("title", "BayShop | Crear cuenta");
        return "register";
    }
    

    @GetMapping("/perfil/{id}")
    public String profile(HttpSession session, Model model, @PathVariable long id) {
		User actual = entityManager.find(User.class, id);
        long idSession = ((User)session.getAttribute("u")).getId();
        // Faltarían los where para coger los del usuario concreto
        List<Product> userProd = entityManager.createQuery("select p from Product p where status = 0").getResultList();
        List<Product> userCompras = entityManager.createQuery("select p from Product p where status = 0").getResultList();
        
        if(id == idSession){
            model.addAttribute("title", "BayShop | Mi perfil");
        }else{
            model.addAttribute("title", "BayShop | Perfil de " + (String)actual.getUsername());
        }
		model.addAttribute("user", actual);
        model.addAttribute("userProd", userProd);
        model.addAttribute("userCompras", userCompras);
        return "perfil";
    }

    // @GetMapping("/mensajes/idU/idP")
    // public String message(HttpSession session, Model model, @RequestParam(required = false) Integer entero) {
    //     model.addAttribute("title", "BayShop | Mensaje <ID>");
    //     return "mensaje";
    // }

    @GetMapping("/compra/{id}")
    public String buy(HttpSession session, Model model, @PathVariable long id) {
        Product p = entityManager.find(Product.class, id);

        model.addAttribute("title", "BayShop | Resumen de compra");
        model.addAttribute("p", p);

        return "compra";
    }

    @PostMapping("/compra")
    @Transactional
    public String buyProduct(HttpSession session, Model model,
        @RequestParam(value="buyer", required=true) long b,
        @RequestParam(value="seller", required=true) long s,
        @RequestParam(value="product", required=true) long p
    ){
        User buyer  = entityManager.find(User.class, b);
        User seller = entityManager.find(User.class, s);
        Sale sale   = new Sale();

        List<Product> list = new ArrayList<>();
        Product product = entityManager.find(Product.class, p);
        list.add(product);

        sale.setBuyer(buyer);
        sale.setSeller(seller);
        sale.setProducts(list);
        sale.setTimestamp(LocalDateTime.now());
        // persist
        entityManager.persist(sale);
        entityManager.flush();

        product.setStatus(ProductStatus.SOLD);
        entityManager.persist(product);

        return "redirect:/";
        // return "redirect:/compra/3"; // solo de prueba con propositos de debuguear
    }
}
