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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import es.ucm.fdi.iw.g01.bayshop.model.Product;
import es.ucm.fdi.iw.g01.bayshop.model.User;
import es.ucm.fdi.iw.g01.bayshop.model.Product.ProductStatus;


@Controller
@RequestMapping("/revisor")
public class ReviewerController {
    private static Logger logger = LogManager.getLogger(ReviewerController.class);

    @Autowired
    private EntityManager entityManager;

    // Metodos de los <form> que son mas especificos de cada Controller
    @GetMapping("/")
    public String revisor(HttpSession session, Model model, @RequestParam(required = false) Integer entero){
        
        List<Product>prod = entityManager.createQuery("select p from Product p where status = 1").getResultList();
        model.addAttribute("prod", prod);
        model.addAttribute("revisar", true);
        model.addAttribute("title", "BayShop | Productos por revisar");
       
        return "index";
    }


    @Transactional
    @GetMapping("/rechazar/{id}")
    public String rechazar(HttpSession session, Model model, @PathVariable long id, @RequestParam(required = false) Integer entero) {
        
        Product target = entityManager.find(Product.class, id);
        target.setStatus(ProductStatus.REJECT);

        return "redirect:/revisor/";
    }


    @Transactional
    @GetMapping("/validar/{id}")
    public String validar(HttpSession session, Model model, @PathVariable long id, @RequestParam(required = false) Integer entero) {

        Product target = entityManager.find(Product.class, id);
        target.setStatus(ProductStatus.ACCEPTED);

        return "redirect:/revisor/";
    }


}