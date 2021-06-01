package es.ucm.fdi.iw.g01.bayshop.controller;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class BayShopErrorController implements ErrorController {

    @RequestMapping(value = "/error")
    public String handleError(HttpServletRequest request, Model model){
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        model.addAttribute("title", "BayShop | Error");

        if (status != null) {
            Integer statusCode = Integer.valueOf(status.toString());

            if(statusCode == HttpStatus.NOT_FOUND.value()) {
                return "errors/error-404.html";
            }
            else if(statusCode == HttpStatus.INTERNAL_SERVER_ERROR.value()) {
                return "errors/error-500.html";
            }
            else if(statusCode == HttpStatus.UNAUTHORIZED.value()) {
                return "errors/error-401.html";
            }
        }
        return "errors/error.html";
    }

    @Override
    public String getErrorPath() {
        return "/error";
    }
}
