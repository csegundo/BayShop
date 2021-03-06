package es.ucm.fdi.iw.g01.bayshop.controller;

import java.math.BigDecimal;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import es.ucm.fdi.iw.g01.bayshop.LocalData;
import es.ucm.fdi.iw.g01.bayshop.model.User;
import es.ucm.fdi.iw.g01.bayshop.model.User.Role;

@Controller()
@RequestMapping("/user")
public class UserController {
    private static Logger logger = LogManager.getLogger(UserController.class);

    @Autowired 
	private EntityManager entityManager;
	
	@Autowired
	private LocalData localData;
	
	@Autowired
	private PasswordEncoder passwordEncoder;

    /**
	 * Tests a raw (non-encoded) password against the stored one.
	 * @param rawPassword to test against
 	 * @param encodedPassword as stored in a user, or returned y @see{encodePassword}
	 * @return true if encoding rawPassword with correct salt (from old password)
	 * matches old password. That is, true if the password is correct  
	 */
	public boolean passwordMatches(String rawPassword, String encodedPassword) {
		return passwordEncoder.matches(rawPassword, encodedPassword);
	}

    /**
	 * Encodes a password, so that it can be saved for future checking. Notice
	 * that encoding the same password multiple times will yield different
	 * encodings, since encodings contain a randomly-generated salt.
	 * @param rawPassword to encode
	 * @return the encoded password (typically a 60-character string)
	 * for example, a possible encoding of "test" is 
	 * {bcrypt}$2y$12$XCKz0zjXAP6hsFyVc8MucOzx6ER6IsC1qo5zQbclxhddR1t6SfrHm
	 */
	public String encodePassword(String rawPassword) {
		return passwordEncoder.encode(rawPassword);
	}

    // Metodos de los <form> que son mas especificos de cada Controller
    @PostMapping("/api/create")
    @Transactional
    public String create(@ModelAttribute User newUser, @RequestParam(required=false) String pass2, Model model, HttpSession session, @RequestParam("photo") MultipartFile photo){
		logger.warn(newUser.getUsername());
		logger.warn(pass2);
	
		if(!newUser.getPassword().equals(pass2)){
			return "redirect:/register";
		}
		
		newUser.setPassword(this.encodePassword(newUser.getPassword()));
		newUser.setEnabled((byte)1);
		newUser.setRoles("USER"); // register normal solo USER normales
		newUser.setBaypoints(0);
		newUser.setDinero(new BigDecimal(0));

		entityManager.persist(newUser);
		entityManager.flush();

		File img = localData.getFile("user", Long.toString(newUser.getId()));

		if(photo.isEmpty()){
			logger.warn("REGISTER IMAGE EMPTY");
		} else{
			try (BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(img))){
				byte[] bytes = photo.getBytes();
				stream.write(bytes);
			} catch (Exception e) {
				logger.warn("ERROR UPLOADING REGISTER IMAGE");
			}
		}

        return "redirect:/login";
    }

	@PostMapping("/userNameChange/{id}")
	@Transactional
	@ResponseBody
	public String changeUserName(HttpSession session, @PathVariable long id, @RequestBody Map<String, String> json){
		User userSess = (User) session.getAttribute("u");
		User user = entityManager.find(User.class, id);

		// Permisos para cambiar nombre de usuario
		if(userSess.getId() == id || userSess.hasRole(Role.ADMIN)){
			user.setUsername(json.get("username"));

			entityManager.persist(user);

			return "{\"success\":true,\"message\":\"Nombre de usuario cambiado con exito\"}";
		} else{
			return "{\"success\":false,\"message\":\"No tienes permiso para cambiar el nombre de usuario\"}";
		}
	}

	@PostMapping("/api/changePassword")
	@Transactional
	@ResponseBody
	public String changeUserPass(HttpSession session, @RequestBody Map<String, String> json){
		User userSess = entityManager.find(User.class, ((User) session.getAttribute("u")).getId());
		String oldPass = json.get("oldPass");
		String newPass = json.get("newPass");
		String confirm = json.get("confirm");
		logger.warn("-------------------------- ");
		logger.warn(passwordMatches(oldPass, userSess.getPassword()));
		logger.warn("-------------------------- ");

		if(!passwordMatches(oldPass, userSess.getPassword()) || !newPass.equalsIgnoreCase(confirm)){
			return "{\"success\":false,\"message\":\"Introduce bien las contrase??as\"}";
		}

		if(passwordMatches(newPass, userSess.getPassword())){
			return "{\"success\":false,\"message\":\"La nueva contrase??a no puede ser igual a la anterior\"}";
		}

		userSess.setPassword(this.encodePassword(newPass));
		session.removeAttribute("u");
		session.setAttribute("u", userSess);

		entityManager.persist(userSess);
		entityManager.flush();

		return "{\"success\":true,\"message\":\"Contrase??a cambiada con ??xito\"}";
	}

	@PostMapping("/api/changeUsername")
	@Transactional
	@ResponseBody
	public String changeUsername(HttpSession session, @RequestBody Map<String, String> json){
		User userSess = entityManager.find(User.class, ((User) session.getAttribute("u")).getId());
		String password = json.get("passwd");
		String newUsername = json.get("username");
		Integer exists = entityManager.createNamedQuery("User.hasUsername").setParameter("username", newUsername).getFirstResult();

		if(!passwordMatches(password, userSess.getPassword())){
			return "{\"success\":false,\"message\":\"Contrase??a incorrecta\"}";
		}
		if(userSess.getUsername().equalsIgnoreCase(newUsername)){
			return "{\"success\":false,\"message\":\"El nuevo nombre de usuario no puede ser igual al actual\"}";
		}
		if(exists >= 1){
			return "{\"success\":false,\"message\":\"El nombre de usuario ya existe\"}";
		}

		userSess.setUsername(newUsername);
		session.removeAttribute("u");
		session.setAttribute("u", userSess);

		entityManager.persist(userSess);
		entityManager.flush();

		return "{\"success\":true,\"message\":\"Nombre de usuario cambiado con ??xito\"}";
	}

	@PostMapping("/api/deleteAccount")
	@Transactional
	public String changeUsername(HttpSession session, HttpServletRequest request, HttpServletResponse response){
		User userSess = (User) session.getAttribute("u");
		userSess.setEnabled((byte)0);

		entityManager.persist(userSess);
		entityManager.flush();

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if(auth != null){
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }

		return "redirect:/";
	}

	// edit profile template (user session)
	@GetMapping("/edit")
	public String editProfile(HttpSession session, Model model) {
		User userSess = (User) session.getAttribute("u");
		model.addAttribute("u", userSess);

		return "editProfile";
	}

	// edit profile template (cualquier usuario)
	@GetMapping("/edit/{id}")
	public String editUserProfile(HttpSession session, Model model, @PathVariable long id) {
		User userSess = (User) session.getAttribute("u");

		if(id == userSess.getId() || userSess.hasRole(Role.ADMIN)){
			User user = id != userSess.getId() ? entityManager.find(User.class, id) : userSess;
			model.addAttribute("user", user);
		} else{
			model.addAttribute("user", userSess);
		}

		return "editUserProfile";
	}

	// GET y POST de imagenes de perfil
	@GetMapping("/api/photo/{id}")
	public StreamingResponseBody getPhoto(@PathVariable long id, Model model) throws IOException{
		File file = localData.getFile("user", Long.toString(id));
		InputStream in;

		if (file.exists()) {
			in = new BufferedInputStream(new FileInputStream(file));
		} else {
			in = new BufferedInputStream(getClass().getClassLoader().getResourceAsStream("static/img/default-user.png"));
		}
		return new StreamingResponseBody() {
			@Override
			public void writeTo(OutputStream os) throws IOException {
				FileCopyUtils.copy(in, os);
			}
		};
	}

	@PostMapping("/api/photo/{id}")
	@ResponseBody
	public String postPhoto(HttpServletResponse response, @RequestParam("photo") MultipartFile photo, @PathVariable("id") String id, Model model, HttpSession session) throws IOException{
		User userSess = (User) session.getAttribute("u");
		User userParam = entityManager.find(User.class, Long.parseLong(id));

		if(userSess.getId() != userParam.getId() && !userSess.hasRole(Role.ADMIN)){
			return "{\"success\":false,\"message\":\"No tienes permiso para realizar esta acci??n\"}";
		}

		File img = localData.getFile("user", id);

		if(photo.isEmpty()){
			logger.warn("UPDATE IMAGE EMPTY");
		} else{
			try (BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(img))){
				byte[] bytes = photo.getBytes();
				stream.write(bytes);
			} catch (Exception e) {
				logger.warn("ERROR UPLOADING UPDATE IMAGE");
				return "{\"success\":false,\"message\":\"Error al actualizar la imagen\"}";
			}
		}

		return "{\"success\":true,\"message\":\"Imagen de perfil actualizada con ??xito\"}";
	}
}
