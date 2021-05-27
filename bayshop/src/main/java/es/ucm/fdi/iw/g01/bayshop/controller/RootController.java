package es.ucm.fdi.iw.g01.bayshop.controller;

import java.util.Date;
import java.math.BigDecimal;
import java.math.MathContext;
import java.net.URLDecoder;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

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
import org.springframework.web.bind.annotation.RequestBody;
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
        
        List<Product> prod = entityManager.createQuery("select p from Product p where status = 0 and enabled = TRUE").getResultList();
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
        
        String queryUserProd = "select p from Product p where status = 0 and USER_ID = :userId";
        List<Product> userProd = entityManager.createQuery(queryUserProd).setParameter("userId", id).getResultList();

        // Consulta que no funciona y no sé por qué, es como se debe hacer
        //String queryUserCompras = "select p from Product p JOIN Sale ON Product.SALE_ID = Sale.ID where status = 0 and USER_ID = :userId";
        // Consulta a modo de placeholder
        String queryUserCompras = "select p from Product p where status = 0 and USER_ID = :userId";
        List<Product> userCompras = entityManager.createQuery(queryUserCompras).setParameter("userId", idSession).getResultList();

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
        String redirect = "/";

        List<Product> list = new ArrayList<>();
        Product product = entityManager.find(Product.class, p);
        list.add(product);

        // repartir Baypoints a ambas partes
        buyer.setBaypoints(buyer.getBaypoints() + ((int) Math.round(0.2 * product.getPrice().intValue()) * 50));
        seller.setBaypoints(seller.getBaypoints() + ((int) Math.round(0.2 * product.getPrice().intValue()) * 50));

        // añadir y quitar dinero
        if(buyer.getDinero().doubleValue() < product.getPrice().doubleValue()){
            // no tiene dinero suficiente => se le deja a 0 y se simula un pago
            buyer.setDinero(new BigDecimal(0));
            redirect += "pasarela/" + String.valueOf(product.getPrice().doubleValue() - buyer.getDinero().doubleValue());
        } else{
            buyer.setDinero(new BigDecimal(buyer.getDinero().doubleValue() - product.getPrice().doubleValue(), MathContext.DECIMAL64));
        }

        seller.setDinero(new BigDecimal(seller.getDinero().doubleValue() + product.getPrice().doubleValue(), MathContext.DECIMAL64));

        sale.setBuyer(buyer);
        sale.setSeller(seller);
        sale.setProducts(list);
        sale.setSaleDate(LocalDateTime.now());

        // persist
        entityManager.persist(sale);
        entityManager.flush();
        entityManager.persist(buyer);
        entityManager.flush();
        entityManager.persist(seller);
        entityManager.flush();

        session.setAttribute("baypoints", buyer.getBaypoints());
        session.setAttribute("dinero", buyer.getDinero());

        product.setStatus(ProductStatus.SOLD);
        product.setSale(sale);
        entityManager.persist(product);

        return "redirect:" + redirect;
    }

    // Simula una interfaz de pago para cuando quieres comprar algo y no tienes el dinero suficiente
    @GetMapping("/pasarela/{price}")
    public String pasarelaPago(Model model, @PathVariable Double price){
        model.addAttribute("title", "BayShop | Pasarela de pago");
        model.addAttribute("price", price);
        return "pasarela";
    }

    @PostMapping("/retirarDinero")
    @Transactional
    public String retirarDinero(HttpSession session, @RequestParam String money){
        User actual = (User) session.getAttribute("u");
        actual.setDinero(new BigDecimal(actual.getDinero().doubleValue() - Double.parseDouble(money), MathContext.DECIMAL64));
        session.setAttribute("dinero", actual.getDinero());
        return "redirect:/";
    }
}
