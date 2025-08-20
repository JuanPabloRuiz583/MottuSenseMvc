package br.com.fiap.MottuSenseMvc.controller;

import br.com.fiap.MottuSenseMvc.dto.UserCreateDTO;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/register")
    public String register(Model model) {
        model.addAttribute("userCreateDTO", new UserCreateDTO());
        return "register";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }
}