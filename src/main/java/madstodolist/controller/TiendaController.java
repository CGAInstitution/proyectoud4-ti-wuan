package madstodolist.controller;

import madstodolist.model.Producto;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@Controller
public class TiendaController {

    @GetMapping("/Tienda")
    public String mostrarTienda(HttpSession session, Model model) {
        List<Producto> carrito = (List<Producto>) session.getAttribute("carrito");

        if (carrito == null) {
            carrito = new ArrayList<>();
        }

        session.setAttribute("carrito", carrito);
        session.setAttribute("numeroCesta", carrito.size());

        model.addAttribute("numeroCesta", carrito.size() < 9 ? carrito.size() : "+9");

        return "index";
    }


}
