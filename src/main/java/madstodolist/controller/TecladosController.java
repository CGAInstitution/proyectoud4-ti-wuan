package madstodolist.controller;

import madstodolist.model.Producto;
import madstodolist.repository.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@Controller
public class TecladosController {

    @Autowired
    private ProductoRepository productoRepo;

    @GetMapping("/Tienda/Teclados")
    public String mostrarTeclados(HttpSession session, Model model) {
        List<Producto> productos = productoRepo.findByCategoria_Nombre("Teclados");
        model.addAttribute("productos", productos);

        List<Producto> carrito = (List<Producto>) session.getAttribute("carrito");
        session.getAttribute("numeroCesta");
        model.addAttribute("numeroCesta", carrito.size() < 9 ? carrito.size() : "+9");
        return "teclados";
    }

    @GetMapping("/Tienda/Teclados/AgregarAlCarrito")
    public String agregarAlCarrito(@RequestParam("productoId") Long productoId, HttpSession session, Model model) {
        Producto producto = productoRepo.findById(productoId).orElse(null);

        if (producto != null) {
            List<Producto> carrito = (List<Producto>) session.getAttribute("carrito");
            if (carrito == null) {
                carrito = new ArrayList<>();
            }
            carrito.add(producto);
            session.setAttribute("carrito", carrito);
        }

        return "redirect:/Tienda/Teclados";
    }
}
