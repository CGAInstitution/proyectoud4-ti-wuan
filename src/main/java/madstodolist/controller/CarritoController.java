package madstodolist.controller;

import madstodolist.model.Pedido;
import madstodolist.model.Producto;
import madstodolist.service.PedidoService;
import madstodolist.service.ProductoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/Tienda/Cesta")
public class CarritoController {

    private final ProductoService productoService;
    private final PedidoService pedidoService;

    public CarritoController(ProductoService productoService, PedidoService pedidoService) {
        this.productoService = productoService;
        this.pedidoService = pedidoService;
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

    @GetMapping("/eliminar/{id}")
    public String eliminarDelCarrito(@PathVariable Long id, HttpSession session) {
        List<Producto> carrito = (List<Producto>) session.getAttribute("carrito");

        if (carrito != null && !carrito.isEmpty()) {
            Producto productoAEliminar = null;
            for (Producto producto : carrito) {
                if (producto.getId().equals(id)) {
                    productoAEliminar = producto;
                    break; // Solo eliminar la primera ocurrencia
                }
            }

            if (productoAEliminar != null) {
                carrito.remove(productoAEliminar);
            }
        }

        session.setAttribute("carrito", carrito);
        return "redirect:/Tienda/Cesta";
    }


    @PostMapping("/checkout")
    public String finalizarCompra(@RequestParam String direccion, @RequestParam String metodoPago, Model model, HttpSession session) {
        List<Producto> carrito = (List<Producto>) session.getAttribute("carrito");

        if (carrito == null || carrito.isEmpty()) {
            model.addAttribute("mensaje", "El carrito está vacío.");
            return "redirect:/Tienda/Cesta";
        }


        session.removeAttribute("carrito");

        return "redirect:/Tienda";
    }
}