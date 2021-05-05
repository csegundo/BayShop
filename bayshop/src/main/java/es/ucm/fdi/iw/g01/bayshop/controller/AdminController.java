package es.ucm.fdi.iw.g01.bayshop.controller;

import java.util.List;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import es.ucm.fdi.iw.g01.bayshop.model.Product;
import es.ucm.fdi.iw.g01.bayshop.model.User;
import es.ucm.fdi.iw.g01.bayshop.model.User.Role;

@Controller
@RequestMapping("/admin")
public class AdminController {
    private static Logger logger = LogManager.getLogger(AdminController.class);

    @Autowired 
	private EntityManager entityManager;

	@GetMapping(value = { "/", "" })
    public String admin(HttpSession session, Model model, @RequestParam(required = false) Integer entero) {
		User userSess = (User) session.getAttribute("u");

        List<Product> users = entityManager.createQuery("select u from User u where id <> :actual").setParameter("actual", userSess.getId()).getResultList();
        List<Product> products = entityManager.createQuery("select p from Product p").getResultList();
        model.addAttribute("users", users);
        model.addAttribute("products", products);
        model.addAttribute("title", "BayShop | Administrador");
       
        return "admin";
    }

    @DeleteMapping("/api/deleteAccount/{id}")
	@ResponseBody
	@Transactional
	public String deleteAccount(HttpSession session, Model model, @PathVariable long id){
		try{
			User userSess = (User) session.getAttribute("u");

			if(userSess.hasRole(Role.ADMIN) && id != userSess.getId()){
				User target = entityManager.find(User.class, id);
				entityManager.remove(target);
			}
			logger.warn("IDDDDDDDDDDDDD");
			logger.warn(id);

			return "{\"success\":true}";
		} catch(Exception e){
			return "{\"success\":false}";
		}
	}

	@PostMapping("api/toggleUserStatus")
	@ResponseBody
	@Transactional
	public Integer toggleUserStatus(HttpSession session, Model model, @RequestParam long id, @RequestParam boolean enable){ // activa/desactiva un usuario
		logger.warn("ENTROOOOOOOOOOO");
		logger.warn(id);
		logger.warn(enable);
		
		return 1;
	}
}
