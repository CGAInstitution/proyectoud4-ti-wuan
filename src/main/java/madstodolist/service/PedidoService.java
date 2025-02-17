package madstodolist.service;

import madstodolist.dto.PedidoData;
import madstodolist.model.*;
import madstodolist.repository.CategoriaRepository;
import madstodolist.repository.PedidoRepository;
import madstodolist.repository.UsuarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PedidoService {

    private final PedidoRepository pedidoRepository;
    private final UsuarioRepository usuarioRepository;
    private final CategoriaRepository categoriaRepository;

    public PedidoService(PedidoRepository pedidoRepository, UsuarioRepository usuarioRepository, CategoriaRepository categoriaRepository) {
        this.pedidoRepository = pedidoRepository;
        this.usuarioRepository = usuarioRepository;
        this.categoriaRepository = categoriaRepository;
    }

    @Transactional
    public void actualizarTotal(Long pedidoId) {
        Pedido pedido = pedidoRepository.findById(pedidoId).orElseThrow(() -> new RuntimeException("Pedido no encontrado"));
        double nuevoTotal = pedido.calcularTotal();
        pedidoRepository.actualizarTotalPedido(pedidoId, nuevoTotal);
    }

    @Transactional
    public void crearPedido(PedidoData pedidoData, Long usuarioId) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findById(usuarioId);
        if (usuarioOpt.isEmpty()) {
            throw new RuntimeException("Usuario no encontrado.");
        }

        Usuario usuario = usuarioOpt.get();

        List<Producto> productos = pedidoData.getProductos();
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

        // Guardar el pedido con sus relaciones
        pedidoRepository.save(nuevoPedido);
        pedidoRepository.flush();
    }

}
