package es.ucm.fdi.iw.g01.bayshop.controller;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import es.ucm.fdi.iw.g01.bayshop.model.Message;
import es.ucm.fdi.iw.g01.bayshop.model.User;

@Controller
@RequestMapping("/mensajes")
public class MessageController {
    private static Logger logger = LogManager.getLogger(MessageController.class);

    @Autowired
	private EntityManager entityManager;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

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
    @Transactional
    public String viewMessage(HttpSession session, Model model, @RequestParam long id) {
        long userSess = ((User) session.getAttribute("u")).getId();
        logger.warn("ENTROOOOOOOOOOOOO");
        logger.warn(id);

        // El usuario es el que envia o recibe
        Message message = entityManager.find(Message.class, id);
        if(message != null && (message.getSender().getId() == userSess || message.getRecipient().getId() == userSess)){
            // Si no tiene fecha de lectura entonces se le pone SOLO si es el que recibe
            if(message.getDateRead() == null && message.getRecipient().getId() == userSess){
                message.setDateRead(LocalDateTime.now());
                entityManager.persist(message);
            }

            model.addAttribute("msg", message);
            return "mensaje";
        } else{
            return "redirect:errors/401";
        }
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
    @ResponseBody
    // public String createMessage(HttpSession session, Model model, @RequestParam long dest, @RequestParam String msg) {
    public String createMessage(HttpSession session, Model model, @RequestBody Map<String, String> jsonParam) {
        long dest = Long.parseLong(jsonParam.get("dest"));
        String msg = jsonParam.get("msg");

        long userSess   = (long) ((User) session.getAttribute("u")).getId();
        User sender     = entityManager.find(User.class, userSess);
        User receiver   = entityManager.find(User.class, dest);

        Message message = new Message();
        message.setRecipient(receiver);
        message.setSender(sender);
        message.setDateSent(LocalDateTime.now());
        message.setText(msg);

        entityManager.persist(message);

        ObjectMapper mapper = new ObjectMapper();
        ObjectNode rootNode = mapper.createObjectNode();
        rootNode.put("text", sender.getUsername() + " envia mensaje a " + receiver.getUsername());
        String json = "";
        try {
            json = mapper.writeValueAsString(rootNode);
        } catch (Exception e) {
            return "{\"success\":false,\"message\":\"Mensaje enviado pero no recibido con WS\"}";
        }
        messagingTemplate.convertAndSend("/user/" + receiver.getUsername() + "/queue/updates", json);

        // return "redirect:/mensajes";
        return "{\"success\":true,\"message\":\"Mensaje enviado con exito\"}";
    }

    @DeleteMapping("/api/delete/{id}")
    @Transactional
    @ResponseBody
    public String deleteMessage(HttpSession session, Model model, @PathVariable long id){
        logger.warn("ENTROOOOOOOOOOOOO");
        logger.warn(id);
        long userSess = ((User) session.getAttribute("u")).getId();
        Message message = entityManager.find(Message.class, id);

        // Solo puede borrar el mensaje si es quien lo recibe
        if(message != null && message.getRecipient().getId() == userSess){
            entityManager.remove(message);
            
            return "{\"success\":true}";
        } else{
            return "{\"success\":false}";
        }
    }
}
