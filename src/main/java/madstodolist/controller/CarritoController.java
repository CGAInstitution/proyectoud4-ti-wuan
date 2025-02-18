package madstodolist.controller;

import madstodolist.dto.PedidoData;
import madstodolist.model.DetallePedido;
import madstodolist.model.Inventario;
import madstodolist.model.Pedido;
import madstodolist.model.Producto;
import madstodolist.repository.InventarioRepository;
import madstodolist.service.PedidoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.*;


@Controller
@RequestMapping("/Tienda/Cesta")
public class CarritoController {

    private final PedidoService pedidoService;
    private final InventarioRepository inventarioRepository;

    public CarritoController(PedidoService pedidoService, InventarioRepository inventarioRepository) {
        this.pedidoService = pedidoService;
        this.inventarioRepository = inventarioRepository;
    }

    @GetMapping
    public String mostrarCarrito(HttpSession session, Model model) {
        session.getAttribute("userId");
        List<Producto> carrito = (List<Producto>) session.getAttribute("carrito");
        if (carrito == null) {
            carrito = new ArrayList<>();
        }
        double total = carrito.stream().mapToDouble(Producto::getPrecio).sum();
        total = Math.round(total * 100.0) / 100.0;

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
    public String finalizarCompra(@RequestParam String direccion,
                                  @RequestParam String metodoPago,
                                  Model model,
                                  HttpSession session) {
        List<Producto> carrito = (List<Producto>) session.getAttribute("carrito");
        Long usuarioId = (Long) session.getAttribute("userId");

        if (carrito == null || carrito.isEmpty()) {
            model.addAttribute("mensajesDeError", List.of("El carrito está vacío."));
            return "cesta";
        }

        if (usuarioId == null) {
            model.addAttribute("mensajesDeError", List.of("Usuario no autenticado."));
            return "cesta";
        }

        Map<Long, Integer> cantidadProductos = new HashMap<>();
        for (Producto producto : carrito) {
            cantidadProductos.put(producto.getId(), cantidadProductos.getOrDefault(producto.getId(), 0) + 1);
        }

        Map<String, Integer> productosSinStock = new HashMap<>();

        for (Map.Entry<Long, Integer> entry : cantidadProductos.entrySet()) {
            Long productoId = entry.getKey();
            int cantidadEnCarrito = entry.getValue();

            Optional<Inventario> inventarioOpt = inventarioRepository.findByProductoId(productoId);
            int stockDisponible = inventarioOpt.map(Inventario::getCantidad).orElse(0);

            if (cantidadEnCarrito > stockDisponible) {
                productosSinStock.put(inventarioOpt.map(i -> i.getProducto().getNombre()).orElse("Producto desconocido"), stockDisponible);
            }
        }

        if (!productosSinStock.isEmpty()) {
            List<String> mensajesDeError = new ArrayList<>();
            for (Map.Entry<String, Integer> entry : productosSinStock.entrySet()) {
                mensajesDeError.add("No hay suficiente stock de '" + entry.getKey() + "'. Puedes comprar hasta " + Math.max(entry.getValue(), 0) + " unidades.");
            }
            model.addAttribute("mensajesDeError", mensajesDeError);
            model.addAttribute("carrito", carrito);
            model.addAttribute("total", carrito.stream().mapToDouble(Producto::getPrecio).sum());
            return "cesta";
        }

        PedidoData pedidoData = new PedidoData();
        pedidoData.setFecha(new Date());
        pedidoData.setEstado(Pedido.EstadoPedido.PENDIENTE);
        pedidoData.setTotal(carrito.stream().mapToDouble(Producto::getPrecio).sum());
        pedidoData.setPedidos(carrito);

        DetallePedido detalle = new DetallePedido();
        detalle.setDireccionEnvio(direccion);
        detalle.setMetodoPago(DetallePedido.MetodoPago.fromString(metodoPago.toUpperCase()));
        pedidoData.setDetallePedido(detalle);

        pedidoService.crearPedido(pedidoData, usuarioId);

        for (Map.Entry<Long, Integer> entry : cantidadProductos.entrySet()) {
            Optional<Inventario> inventarioOpt = inventarioRepository.findByProductoId(entry.getKey());
            inventarioOpt.ifPresent(inventario -> {
                inventario.setCantidad(inventario.getCantidad() - entry.getValue());
                inventarioRepository.save(inventario);
            });
        }

        session.removeAttribute("carrito");
        return "redirect:/Tienda";
    }
}