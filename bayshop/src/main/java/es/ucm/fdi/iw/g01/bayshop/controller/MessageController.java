package es.ucm.fdi.iw.g01.bayshop.controller;

import javax.servlet.http.HttpSession;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/mensajes")
public class MessageController {
    private static Logger logger = LogManager.getLogger(MessageController.class);

    // Metodos de los <form> que son mas especificos de cada Controller
}
