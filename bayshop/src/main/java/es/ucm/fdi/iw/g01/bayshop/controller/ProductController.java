package es.ucm.fdi.iw.g01.bayshop.controller;

import java.util.List;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import es.ucm.fdi.iw.g01.bayshop.LocalData;
import es.ucm.fdi.iw.g01.bayshop.model.Product;
import es.ucm.fdi.iw.g01.bayshop.model.User;

@Controller
@RequestMapping("producto")
public class ProductController {
    private static Logger logger = LogManager.getLogger(ProductController.class);

    @Autowired
	private EntityManager entityManager;
	
	@Autowired
	private LocalData localData;
	
	@Autowired
	private PasswordEncoder passwordEncoder;

    // @PostMapping("/create")
    // @Transactional
    // public String createProduct(HttpServletResponse response, @ModelAttribute User newProduct, Model model, HttpSession session){
    //     logger.debug("NUEVO PRODUCTO");
    //     return "index";
    // }


    @Transactional
    @PostMapping("/create")
    public String createProduct(@ModelAttribute Product newProduct, Model model, HttpSession session){
        logger.warn("NUEVO PRODUCTO");
        
        // entityManager.persist(newProduct);

        return "redirect:/index";
    }

    @GetMapping(value = { "/crear", "/crear/" })
    public String productCreate(HttpSession session, Model model, @RequestParam(required = false) Integer entero) {
        model.addAttribute("title", "BayShop | Crear producto");
        return "productoCrear";
    }




    @Transactional
    @GetMapping("/{id}")
    public String product_id(HttpSession session, @PathVariable long id, Model model, @RequestParam(required = false) Integer entero) {
        Product p = entityManager.find(Product.class, id);
        List<Product> prod = entityManager.createQuery("select e from Product e").getResultList();
    
        model.addAttribute("p", p);
        model.addAttribute("prod", prod);
        // model.addAttribute("title", "BayShop | " + p.getName());
        model.addAttribute("title", "BayShop | Producto " + p.getId());

        return "producto";
    }

}
