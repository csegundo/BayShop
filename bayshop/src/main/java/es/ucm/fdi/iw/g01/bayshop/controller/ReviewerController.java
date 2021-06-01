package es.ucm.fdi.iw.g01.bayshop.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;
import javax.persistence.EntityManager;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import es.ucm.fdi.iw.g01.bayshop.model.Product;
import es.ucm.fdi.iw.g01.bayshop.model.Product.ProductStatus;


@Controller
@RequestMapping("/revisor")
public class ReviewerController {
    private static Logger logger = LogManager.getLogger(ReviewerController.class);

    @Autowired
    private EntityManager entityManager;

    @GetMapping("/")
    public String revisor(HttpSession session, Model model, @RequestParam(required = false) Integer entero){
        
        List<Product>prod = entityManager.createQuery("select p from Product p where status = 1").getResultList();
        model.addAttribute("prod", prod);
        model.addAttribute("revisar", true);
        model.addAttribute("title", "BayShop | Productos por revisar");
       
        return "index";
    }

    
	@PostMapping("/api/toggleProductValidation")
	@ResponseBody
	@Transactional
	public String toggleProductValidation(HttpSession session, Model model, @RequestBody Map<String, String> json){ // valida/rechaza un producto
		
        Long id = Long.parseLong(json.get("id"));
		Boolean validated = json.get("enable").equals("true");
		Product target = entityManager.find(Product.class, id);

		target.setStatus(validated ? ProductStatus.ACCEPTED : ProductStatus.REJECT);
		entityManager.persist(target);
		
		return "{\"success\":true}";
	}



}