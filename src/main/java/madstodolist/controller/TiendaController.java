package madstodolist.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpSession;

@Controller
public class TiendaController {

    @GetMapping("/Tienda")
    public String mostrarTienda(HttpSession session) {
            session.getAttribute("userId");
        return "index";
    }


//    @GetMapping("/Tienda/Camisetas")
//    public String mostrarCamisetas(HttpSession session) {
//            session.getAttribute("userId");
//        return "camisetas";
//    }
//
//    @GetMapping("/Tienda/Teclados")
//    public String mostrarTeclados(HttpSession session) {
//            session.getAttribute("userId");
//        return "teclados";
//    }


}
