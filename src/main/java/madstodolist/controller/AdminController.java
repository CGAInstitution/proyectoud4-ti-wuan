package madstodolist.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpSession;

@Controller
public class AdminController {

    @GetMapping("/admin")
    public String mostrarTienda(HttpSession session) {
        session.getAttribute("userId");
        return "admin";
    }
}
