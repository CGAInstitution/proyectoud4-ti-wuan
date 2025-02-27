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
public class FigurasController {

    @Autowired
    private ProductoRepository productoRepo;

    @GetMapping("/Tienda/Figuras")
    public String mostrarFiguras(HttpSession session, Model model) {
        // Obtener todos los productos de la categoría "Figuras"
        List<Producto> productos = productoRepo.findByCategoria_Nombre("Figuras");

        // Pasar los productos al modelo
        model.addAttribute("productos", productos);

        // Mostrar la cantidad de productos en el carrito
        List<Producto> carrito = (List<Producto>) session.getAttribute("carrito");

        session.getAttribute("numeroCesta");
        model.addAttribute("numeroCesta", carrito.size() < 9 ? carrito.size() : "+9");

        return "figuras";
    }

    @GetMapping("/Tienda/Figuras/AgregarAlCarrito")
    public String agregarAlCarrito(@RequestParam("productoId") Long productoId, HttpSession session) {
        // Buscar el producto por ID
        Producto producto = productoRepo.findById(productoId).orElse(null);

        if (producto != null) {
            // Obtener el carrito de la sesión (si existe)
            List<Producto> carrito = (List<Producto>) session.getAttribute("carrito");

            // Si el carrito no existe, lo creamos
            if (carrito == null) {
                carrito = new ArrayList<>();
            }

            // Agregar el producto al carrito
            carrito.add(producto);

        }

        // Redirigir a la vista de figuras
        return "redirect:/Tienda/Figuras";
    }
}
