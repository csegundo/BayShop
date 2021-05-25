package es.ucm.fdi.iw.g01.bayshop.controller;

import java.util.List;
import java.util.Map;

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

    @DeleteMapping("/api/deleteAccount")
	@ResponseBody
	@Transactional
	public String deleteAccount(HttpSession session, Model model, @RequestBody Map<String, String> json){
		try{
			Long id = Long.parseLong(json.get("id"));
			User userSess = (User) session.getAttribute("u");

			if(userSess.hasRole(Role.ADMIN) && id != userSess.getId()){
				User target = entityManager.find(User.class, id);
				entityManager.remove(target);
			}

			return "{\"success\":true}";
		} catch(Exception e){
			return "{\"success\":false}";
		}
	}

	@PostMapping("api/toggleUserStatus")
	@ResponseBody
	@Transactional
	public String toggleUserStatus(HttpSession session, Model model, @RequestBody Map<String, String> json){ // activa/desactiva un usuario
		Long id = Long.parseLong(json.get("id"));
		Boolean enable = json.get("enable").equals("true");

		User target = entityManager.find(User.class, id);

		target.setEnabled((byte)(enable ? 1 : 0));
		entityManager.persist(target);
		
		return "{\"success\":true}";
	}

	@DeleteMapping("/api/deleteProduct")
	@ResponseBody
	@Transactional
	public String deleteProduct(HttpSession session, Model model, @RequestBody Map<String, String> json){
		try{
			Long id = Long.parseLong(json.get("id"));
			User userSess = (User) session.getAttribute("u");

			if(userSess.hasRole(Role.ADMIN) && id != userSess.getId()){
				Product target = entityManager.find(Product.class, id);
				entityManager.remove(target);
			}

			return "{\"success\":true}";
		} catch(Exception e){
			return "{\"success\":false}";
		}
	}

	@PostMapping("/api/updateRoles")
	@ResponseBody
	@Transactional
	public String updateRolesUser(HttpSession session, Model model, @RequestBody Map<String, String> json){
		String roles = json.get("roles");
		User user = entityManager.find(User.class, Long.parseLong(json.get("user")));

		user.setRoles(roles);
		entityManager.persist(user);

		return "{\"success\":true}";
	}


	@PostMapping("api/toggleProductStatus")
	@ResponseBody
	@Transactional
	public String toggleProductStatus(HttpSession session, Model model, @RequestBody Map<String, String> json){ // activa/desactiva un producto
		Long id = Long.parseLong(json.get("id"));
		Boolean enable = json.get("enable").equals("true");

		Product target = entityManager.find(Product.class, id);

		target.setEnabled(enable ? true : false);
		entityManager.persist(target);
		
		return "{\"success\":true}";
	}
}
