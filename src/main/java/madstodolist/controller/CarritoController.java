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
    public String mostrarCarrito( Model model) {
        List<Producto> carrito = productoService.obtenerProductos();
        double total = carrito.stream().mapToDouble(Producto::getPrecio).sum();

        model.addAttribute("carrito", carrito);
        model.addAttribute("total", total);
        return "cesta";
    }

    @GetMapping("/agregar/{id}")
    public String agregarAlCarrito(@PathVariable Long id, HttpSession session) {
        List<Producto> carrito = productoService.obtenerProductos();
        if (carrito == null) {
            carrito = new ArrayList<>();
        }

        Optional<Producto> productoOpt = productoService.obtenerProductoPorId(id);
        productoOpt.ifPresent(carrito::add);

        session.setAttribute("carrito", carrito);
        return "cesta";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminarDelCarrito(@PathVariable(value = "id") Long id, Model model) {
        List<Producto> carrito = productoService.obtenerProductos();
        if (carrito != null) {
            carrito.removeIf(producto -> producto.getId().equals(id));
        }
        double total = carrito.stream().mapToDouble(Producto::getPrecio).sum();
        model.addAttribute("carrito", carrito);
        model.addAttribute("total", total);
        return "cesta";
    }

    @GetMapping("/checkout")
    public String finalizarCompra(HttpSession session) {
        session.removeAttribute("carrito");
        return "redirect:/Tienda";
    }
}