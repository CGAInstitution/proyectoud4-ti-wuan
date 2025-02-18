package madstodolist.service;

import madstodolist.model.*;
import madstodolist.repository.*;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Date;

@Service
@Profile("dev") // Se ejecuta solo si el perfil activo es 'dev'
public class InitDbService {

    private final UsuarioRepository usuarioRepository;
    private final CategoriaRepository categoriaRepository;
    private final ProductoRepository productoRepository;
    private final PedidoRepository pedidoRepository;
    private final PedidoProductoRepository pedidoProductoRepository;
    private final InventarioRepository inventarioRepository;
    private final PedidoService pedidoService;

    public InitDbService(UsuarioRepository usuarioRepository,
                         CategoriaRepository categoriaRepository,
                         ProductoRepository productoRepository,
                         PedidoRepository pedidoRepository,
                         PedidoProductoRepository pedidoProductoRepository,
                         InventarioRepository inventarioRepository,
                         PedidoService pedidoService) {
        this.usuarioRepository = usuarioRepository;
        this.categoriaRepository = categoriaRepository;
        this.productoRepository = productoRepository;
        this.pedidoRepository = pedidoRepository;
        this.pedidoProductoRepository = pedidoProductoRepository;
        this.inventarioRepository = inventarioRepository;
        this.pedidoService = pedidoService;
    }

//    @PostConstruct
//    public void initDatabase() {
//        Usuario usuarioAdmin = new Usuario("user@ua");
//        usuarioAdmin.setNombre("Usuario Ejemplo");
//        usuarioAdmin.setPassword("123");
//        usuarioAdmin.setFechaNacimiento(new Date(2004, 5, 5));
//        usuarioAdmin.setAdministrador(true);
//        usuarioRepository.save(usuarioAdmin);
//
//        Usuario usuario = new Usuario();
//        usuario.setNombre("Juan Pérez");
//        usuario.setEmail("prueba@gmail.com");
//        usuario.setPassword("123");
//        usuario.setFechaNacimiento(new Date(1997, 5, 5));
//        usuario.setAdministrador(false);
//        usuarioRepository.save(usuario);
//
//        Categoria categoria = new Categoria("Figuras", "Figuras de personajes de League of Legends.");
//        categoriaRepository.save(categoria);
//
//        Categoria categoria2 = new Categoria("Camisetas", "Camisetas de personajes de League of Legends.");
//        categoriaRepository.save(categoria2);
//
//        Categoria categoria3 = new Categoria("Teclados", "Teclados de League of Legends.");
//        categoriaRepository.save(categoria3);
//
//        Producto producto = new Producto("Figura de Ahri", "Figura coleccionable de Ahri.", 49.99, "figuraAhri.jpg", categoria);
//        productoRepository.save(producto);
//
//        Producto producto2 = new Producto("Figura de Yasuo", "Figura coleccionable de Yasuo.", 59.99, "FiguraYasuo.jpg", categoria);
//        productoRepository.save(producto2);
//
//        Producto producto3 = new Producto("Camiseta de Jinx", "Camiseta de Jinx.", 19.99, "CamiJinx.jpg", categoria2);
//        productoRepository.save(producto3);
//
//        Producto producto4 = new Producto("Camiseta de Teemo", "Camiseta de Teemo.", 19.99, "CamiSeta.jpg", categoria2);
//        productoRepository.save(producto4);
//
//        Producto producto5 = new Producto("Teclado de Yasuo", "Teclado de Yasuo.", 99.99, "TecladoMancos.jpg", categoria3);
//        productoRepository.save(producto5);
//
//        Producto producto6 = new Producto("Teclado de Jinx", "Teclado de Jinx.", 99.99, "TecladoJinx.jpg", categoria3);
//        productoRepository.save(producto6);
//
//        Pedido pedido = new Pedido(usuario, new Date(), Pedido.EstadoPedido.PENDIENTE);
//        pedido.setDetallePedido(new DetallePedido(pedido, "Calle Falsa 123", DetallePedido.MetodoPago.TARJETA));
//        pedidoRepository.save(pedido);
//
//        Pedido pedido2 = new Pedido(usuario, new Date(), Pedido.EstadoPedido.PENDIENTE);
//        pedido2.setDetallePedido(new DetallePedido(pedido2, "Calle Falsa 456", DetallePedido.MetodoPago.PAYPAL));
//        pedidoRepository.save(pedido2);
//
//        pedidoProductoRepository.save(new PedidoProducto(pedido, producto, 2));
//        pedidoProductoRepository.save(new PedidoProducto(pedido, producto2, 1));
//        pedidoProductoRepository.save(new PedidoProducto(pedido2, producto2, 3));
//        pedidoProductoRepository.save(new PedidoProducto(pedido2, producto, 4));
//
//        pedidoService.actualizarTotal(pedido.getId());
//        pedidoService.actualizarTotal(pedido2.getId());
//
//        inventarioRepository.save(new Inventario(producto, 3));
//        inventarioRepository.save(new Inventario(producto2, 2));
//        inventarioRepository.save(new Inventario(producto3, 200));
//        inventarioRepository.save(new Inventario(producto4, 180));
//        inventarioRepository.save(new Inventario(producto5, 50));
//        inventarioRepository.save(new Inventario(producto6, 70));
//    }
}