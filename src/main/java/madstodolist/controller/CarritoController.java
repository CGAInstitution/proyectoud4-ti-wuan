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
            carrito.add(new Producto(1L, "Producto 1", "Desc producto 1", 10.0, "", null));
            carrito.add(new Producto(2L, "Producto 2", "Desc producto 2", 10.0, "", null));
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
        return "cesta";
    }

    @GetMapping("/cesta/eliminar/{id}")
    public String eliminarDelCarrito(@PathVariable(value = "id") Long id, Model model) {
        System.out.println("Hola");
        List<Producto> carrito = new ArrayList<>();
        carrito.add(productoService.obtenerProductoPorId(1L).get());
        carrito.add(productoService.obtenerProductoPorId(2L).get());
        if (carrito != null) {
            carrito.removeIf(producto -> producto.getId().equals(id));
        }

        model.addAttribute("carrito", carrito);
        return "cesta";
    }

    @GetMapping("/cesta/checkout")
    public String finalizarCompra(HttpSession session) {
        session.removeAttribute("carrito");
        return "redirect:/Tienda";
    }
}