package es.ucm.fdi.iw.g01.bayshop.controller;

import javax.servlet.http.HttpSession;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class RootController {
    private static Logger logger = LogManager.getLogger(RootController.class);

    @GetMapping("/") 
    public String index(HttpSession session, Model model, @RequestParam(required = false) Integer entero){
        return "index";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/register")
    public String register() {
        return "register";
    }

    @GetMapping("/admin/")
    public String admin() {
        return "admin";
    }

    @GetMapping("/revisor/")
    public String revisor() {
        return "revisor";
    }

    @GetMapping("/producto/id")
    public String product_id() {
        return "producto";
    }

    @GetMapping("/producto/crear")
    public String productCreate() {
        return "producto_crear";
    }

    @GetMapping("/perfil/id")
    public String profile() {
        return "perfil";
    }

    @GetMapping("/mensajes/")
    public String allMessages() {
        return "mensajes";
    }

    @GetMapping("/mensajes/idU/idP")
    public String message() {
        return "mensaje";
    }

    @GetMapping("/compra/")
    public String buy() {
        return "compra";
    }
}
