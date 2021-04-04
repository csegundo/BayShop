package es.ucm.fdi.iw.g01.bayshop.controller;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import es.ucm.fdi.iw.g01.bayshop.LocalData;
import es.ucm.fdi.iw.g01.bayshop.model.User;

@Controller
@RequestMapping("/mensajes")
public class MessageController {
    private static Logger logger = LogManager.getLogger(MessageController.class);

    @Autowired 
	private EntityManager entityManager;

    @GetMapping("/")
	public String getMessages(Model model, HttpSession session) {
		model.addAttribute("user", entityManager.createQuery(
			"SELECT u FROM User u").getResultList());
		return "messages";
	}

    @GetMapping(path = "/received", produces = "application/json")
	@Transactional // para no recibir resultados inconsistentes
	@ResponseBody  // para indicar que no devuelve vista, sino un objeto (jsonizado)
	public List<String> retrieveMessages(HttpSession session) {
		long userId = ((User)session.getAttribute("u")).getId();		
		User u = entityManager.find(User.class, userId);
		//log.info("Generating message list for user {} ({} messages)", 
		//		u.getUsername(), u.getReceived().size());
		return  u.getReceived().stream().map(Transferable::toTransfer).collect(Collectors.toList());
	}	

    
    // Metodos de los <form> que son mas especificos de cada Controller
}
