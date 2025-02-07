package madstodolist.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpSession;

@Controller
public class TiendaController {

    @GetMapping("/TiendaController/formLogin")
    public String mostrarFormularioLogin(Model model, HttpSession session) {

        // Opcional: Cerrar sesión si es necesario
        session.invalidate();

        // Redirige a la vista formLogin.html
        return "formLogin";
    }
}
