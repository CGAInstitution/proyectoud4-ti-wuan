package madstodolist.service;

import madstodolist.dto.PedidoData;
import madstodolist.model.*;
import madstodolist.repository.CategoriaRepository;
import madstodolist.repository.PedidoRepository;
import madstodolist.repository.UsuarioRepository;
import madstodolist.repository.InventarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PedidoService {

    private final PedidoRepository pedidoRepository;
    private final UsuarioRepository usuarioRepository;
    private final CategoriaRepository categoriaRepository;
    private final InventarioRepository inventarioRepository;

    public PedidoService(PedidoRepository pedidoRepository, UsuarioRepository usuarioRepository, CategoriaRepository categoriaRepository, InventarioRepository inventarioRepository) {
        this.pedidoRepository = pedidoRepository;
        this.usuarioRepository = usuarioRepository;
        this.categoriaRepository = categoriaRepository;
        this.inventarioRepository = inventarioRepository;
    }

    @Transactional
    public void actualizarTotal(Long pedidoId) {
        Pedido pedido = pedidoRepository.findById(pedidoId).orElseThrow(() -> new RuntimeException("Pedido no encontrado"));
        double nuevoTotal = pedido.calcularTotal();
        pedidoRepository.actualizarTotalPedido(pedidoId, nuevoTotal);
    }

    @Transactional
    public List<String> crearPedido(PedidoData pedidoData, Long usuarioId) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findById(usuarioId);
        if (usuarioOpt.isEmpty()) {
            throw new RuntimeException("Usuario no encontrado.");
        }

        Usuario usuario = usuarioOpt.get();
        List<Producto> productos = pedidoData.getProductos();

        List<String> mensajesDeError = verificarStock(productos);
        if (!mensajesDeError.isEmpty()) {
            return mensajesDeError;
        }

        Pedido nuevoPedido = new Pedido();
        nuevoPedido.setFecha(pedidoData.getFecha());
        nuevoPedido.setEstado(pedidoData.getEstado());
        nuevoPedido.setTotal(productos.stream().mapToDouble(Producto::getPrecio).sum());
        nuevoPedido.setUsuario(usuario);

        DetallePedido detalle = new DetallePedido();
        detalle.setPedido(nuevoPedido);
        detalle.setDireccionEnvio(pedidoData.getDetallePedido().getDireccionEnvio());
        detalle.setMetodoPago(pedidoData.getDetallePedido().getMetodoPago());
        nuevoPedido.setDetallePedido(detalle);

        Map<Long, Long> conteoProductos = productos.stream()
                .collect(Collectors.groupingBy(Producto::getId, Collectors.counting()));

        List<PedidoProducto> pedidoProductos = conteoProductos.entrySet().stream().map(entry -> {
            Long productoId = entry.getKey();
            int cantidad = entry.getValue().intValue();

            Producto producto = productos.stream()
                    .filter(p -> p.getId().equals(productoId))
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("Producto no encontrado en la lista"));

            PedidoProducto pedidoProducto = new PedidoProducto();
            pedidoProducto.setPedido(nuevoPedido);
            Categoria categoria = categoriaRepository.findById(producto.getCategoria().getId())
                    .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));
            pedidoProducto.setProducto(new Producto(producto.getId(), producto.getNombre(),
                    producto.getDescripcion(), producto.getPrecio(), producto.getImagenUrl(),
                    categoria));
            pedidoProducto.setCantidad(cantidad);
            return pedidoProducto;
        }).collect(Collectors.toList());

        nuevoPedido.setPedidoProductos(pedidoProductos);

        pedidoRepository.save(nuevoPedido);
        pedidoRepository.flush();


        return List.of("Pedido creado con éxito.");
    }

    private List<String> verificarStock(List<Producto> productos) {
        List<String> mensajesDeError = new ArrayList<>();

        // Verificar si el stock es suficiente para cada producto
        for (Producto producto : productos) {
            Inventario inventario = inventarioRepository.findByProductoId(producto.getId())
                    .orElseThrow(() -> new RuntimeException("Inventario no encontrado para el producto"));

            // Verificar si el stock es suficiente
            int cantidadPedida = (int) productos.stream().filter(p -> p.getId().equals(producto.getId())).count();
            if (inventario.getCantidad() < cantidadPedida) {
                // Si el stock es insuficiente, agregar un mensaje de error
                int cantidadFaltante = cantidadPedida - inventario.getCantidad();
                mensajesDeError.add("No hay suficiente stock para el producto '" + producto.getNombre() +
                        "'. Faltan " + cantidadFaltante + " unidades.");
            }
        }

        return mensajesDeError;
    }

    private void reducirStockPedido(List<PedidoProducto> pedidoProductos) {
        for (PedidoProducto pedidoProducto : pedidoProductos) {
            Long productoId = pedidoProducto.getProducto().getId();
            int cantidadComprada = pedidoProducto.getCantidad();

            // Buscar el inventario del producto
            Inventario inventario = inventarioRepository.findByProductoId(productoId)
                    .orElseThrow(() -> new RuntimeException("Inventario no encontrado para el producto"));

            // Reducir la cantidad en el inventario
            inventario.setCantidad(inventario.getCantidad() - cantidadComprada);
            inventarioRepository.save(inventario);
        }
    }
}
