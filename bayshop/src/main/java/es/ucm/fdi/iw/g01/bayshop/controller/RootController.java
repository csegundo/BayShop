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
        return "index.html";
    }

    @GetMapping("/login")
    public String login() {
        return "/templates/login.html";
    }
}
