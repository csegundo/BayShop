package es.ucm.fdi.iw.g01.bayshop.controller;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import es.ucm.fdi.iw.g01.bayshop.model.Message;
import es.ucm.fdi.iw.g01.bayshop.model.User;

@Controller
@RequestMapping("/mensajes")
public class MessageController {
    private static Logger logger = LogManager.getLogger(MessageController.class);

    @Autowired
	private EntityManager entityManager;

    @GetMapping(value = { "/", "" })
    public String allMessages(HttpSession session, Model model, @RequestParam(required = false) Integer entero) {
        User userSess = (User) session.getAttribute("u");
        // List<Message> messages = entityManager.createNamedQuery("Message.all").setParameter("uid", userSess.getId()).getResultList();
        List<Message> send = entityManager.createNamedQuery("Message.send").setParameter("uid", userSess.getId()).getResultList();
        List<Message> inbox = entityManager.createNamedQuery("Message.inbox").setParameter("uid", userSess.getId()).getResultList();
        // model.addAttribute("all", messages);
        model.addAttribute("send", send);
        model.addAttribute("inbox", inbox);
        model.addAttribute("title", "BayShop | Todos los mensajes");
        return "mensajes";
    }

    @GetMapping(value = { "/ver", "/ver/" })
    public String viewMessage(HttpSession session, Model model, @RequestParam long id) {
        logger.warn("ENTROOOOOOOOOOOOO");
        logger.warn(id);
        Message message = entityManager.find(Message.class, id);
        model.addAttribute("msg", message);
        return "mensaje";
    }

    @GetMapping(value = { "/nuevo", "/nuevo/" })
    public String newMessage(HttpSession session, Model model, @RequestParam(required = false) Integer entero) {
        User userSess = (User) session.getAttribute("u");
        List<User> users = entityManager.createNamedQuery("User.all").setParameter("uid", userSess.getId()).getResultList();

        model.addAttribute("users", users);
        model.addAttribute("title", "BayShop | Crear mensaje");
        return "crearMensaje";
    }

    @PostMapping("/api/new")
    @Transactional
    public String createMessage(HttpSession session, Model model, @RequestParam long dest, @RequestParam String msg) {
        long userSess   = (long) ((User) session.getAttribute("u")).getId();
        User sender     = entityManager.find(User.class, userSess);
        User receiver   = entityManager.find(User.class, dest);

        Message message = new Message();
        message.setRecipient(receiver);
        message.setSender(sender);
        message.setDateSent(LocalDateTime.now());
        message.setText(msg);

        entityManager.persist(message);

        return "redirect:/mensajes";
    }
}
