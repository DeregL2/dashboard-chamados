package br.umc.dashboardchamados.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AuthController {

    @GetMapping("/login")
    public String paginaLogin() {
        return "auth/login";
    }

    @GetMapping("/403")
    public String acessoNegado() {
        return "error/403";
    }
}
