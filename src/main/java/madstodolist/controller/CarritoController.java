package madstodolist.controller;

import madstodolist.model.Pedido;
import madstodolist.model.Producto;
import madstodolist.service.ProductoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/Tienda/Cesta")
public class CarritoController {

    private final ProductoService productoService;

    public CarritoController(ProductoService productoService) {
        this.productoService = productoService;
    }

    @GetMapping
    public String mostrarCarrito(HttpSession session, Model model) {
        List<Producto> carrito = (List<Producto>) session.getAttribute("carrito");
        if (carrito == null) {
            carrito = new ArrayList<>();
        }
        double total = carrito.stream().mapToDouble(Producto::getPrecio).sum();

        model.addAttribute("carrito", carrito);
        model.addAttribute("total", total);
        return "cesta";
    }

    @GetMapping("/agregar/{id}")
    public String agregarAlCarrito(@PathVariable Long id, HttpSession session) {
        List<Producto> carrito = (List<Producto>) session.getAttribute("carrito");
        if (carrito == null) {
            carrito = new ArrayList<>();
        }

        Optional<Producto> productoOpt = productoService.obtenerProductoPorId(id);
        productoOpt.ifPresent(carrito::add);

        session.setAttribute("carrito", carrito);
        return "redirect:/Tienda/Cesta";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminarDelCarrito(@PathVariable Long id, HttpSession session) {
        List<Producto> carrito = (List<Producto>) session.getAttribute("carrito");

        if (carrito != null && !carrito.isEmpty()) {
            // Buscar el primer producto con el id igual al que se pasa como parámetro
            Producto productoAEliminar = null;
            for (Producto producto : carrito) {
                if (producto.getId().equals(id)) {
                    productoAEliminar = producto;
                    break; // Solo eliminar la primera ocurrencia
                }
            }

            // Si se encontró un producto a eliminar, lo eliminamos
            if (productoAEliminar != null) {
                carrito.remove(productoAEliminar);
            }
        }

        session.setAttribute("carrito", carrito);
        return "redirect:/Tienda/Cesta";
    }




    @GetMapping("/checkout")
    public String finalizarCompra(HttpSession session) {
        session.removeAttribute("carrito");
        return "redirect:/Tienda";
    }
}