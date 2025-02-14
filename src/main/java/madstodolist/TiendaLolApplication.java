package madstodolist;

import madstodolist.model.*;
import madstodolist.repository.*;
import madstodolist.service.PedidoService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@SpringBootApplication
public class TiendaLolApplication {

    public static void main(String[] args) {
        SpringApplication.run(TiendaLolApplication.class, args);
    }

    @Bean
    CommandLineRunner run(
            UsuarioRepository usuarioRepo,
            CategoriaRepository categoriaRepo,
            ProductoRepository productoRepo,
            PedidoRepository pedidoRepo,
            PedidoProductoRepository pedidoProductoRepo,
            DetallePedidoRepository detallePedidoRepo,
            PedidoService pedidoService // Inyectamos el servicio
    ) {
        return args -> {
            // 1️⃣ Crear un usuario
            Usuario usuario = new Usuario();
            usuario.setNombre("Juan Pérez");
            usuario.setEmail("prueba@gmail.com");
            usuario.setPassword("123");
            usuario.setAdministrador(false);
            usuarioRepo.save(usuario);

            // 2️⃣ Crear una categoría
            Categoria categoria = new Categoria();
            categoria.setNombre("Figuras");
            categoria.setDescripcion("Figuras de personajes de League of Legends.");
            categoriaRepo.save(categoria);

            // 3️⃣ Crear productos
            Producto producto = new Producto();
            producto.setNombre("Figura de Ahri");
            producto.setDescripcion("Figura coleccionable de Ahri.");
            producto.setPrecio(49.99);
            producto.setImagenUrl("https://example.com/ahri.jpg");
            producto.setCategoria(categoria);
            productoRepo.save(producto);

            Producto producto2 = new Producto();
            producto2.setNombre("Figura de Yasuo");
            producto2.setDescripcion("Figura coleccionable de Yasuo, personaje de 'League of Legends'.");
            producto2.setPrecio(59.99);
            producto2.setImagenUrl("https://example.com/yasuo.jpg");
            producto2.setCategoria(categoria);
            productoRepo.save(producto2);

            // 4️⃣ Crear pedidos
            Pedido pedido = new Pedido();
            pedido.setUsuario(usuario);
            pedido.setFecha(new Date());
            pedido.setEstado(Pedido.EstadoPedido.PENDIENTE);
            pedido.setDetallePedido(new DetallePedido(1L, pedido, "Calle Falsa 123", DetallePedido.MetodoPago.TARJETA));
            pedidoRepo.save(pedido);

            Pedido pedido2 = new Pedido();
            pedido2.setUsuario(usuario);
            pedido2.setFecha(new Date());
            pedido2.setEstado(Pedido.EstadoPedido.PENDIENTE);
            pedido2.setDetallePedido(new DetallePedido(2L, pedido2, "Calle Falsa 456", DetallePedido.MetodoPago.PAYPAL));
            pedidoRepo.save(pedido2);

            // 5️⃣ Asociar pedidos con productos
            pedidoProductoRepo.save(new PedidoProducto(1L, pedido, producto, 2));
            pedidoProductoRepo.save(new PedidoProducto(2L, pedido, producto2, 1));
            pedidoProductoRepo.save(new PedidoProducto(3L, pedido2, producto2, 3));
            pedidoProductoRepo.save(new PedidoProducto(4L, pedido2, producto, 4));

            // 6️⃣ Actualizar total de los pedidos
            pedidoService.actualizarTotal(pedido.getId());
            pedidoService.actualizarTotal(pedido2.getId());

            // ✅ Validar datos guardados
            System.out.println("Usuarios en BD: " + usuarioRepo.count());
            System.out.println("Categorías en BD: " + categoriaRepo.count());
            System.out.println("Productos en BD: " + productoRepo.count());
            System.out.println("Pedidos en BD: " + pedidoRepo.count());
            System.out.println("Pedidos-Productos en BD: " + pedidoProductoRepo.count());

            // ✅ Validar total de los pedidos
            System.out.println("Total Pedido 1: " + pedidoRepo.findById(pedido.getId()).get().getTotal());
            System.out.println("Total Pedido 2: " + pedidoRepo.findById(pedido2.getId()).get().getTotal());

            // Validación adicional para comprobar eliminación de productos del carrito

            // Simular un carrito
            List<Producto> carrito = new ArrayList<>();
            carrito.add(producto);
            carrito.add(producto2);

            // Mostrar el carrito antes de eliminar
            System.out.println("Carrito antes de eliminar:");
            carrito.forEach(p -> System.out.println(p.getNombre()));

            // Eliminar un producto del carrito
            Long idAEliminar = producto.getId(); // Eliminar la "Figura de Ahri"
            carrito.removeIf(p -> p.getId().equals(idAEliminar));

            // Mostrar el carrito después de eliminar
            System.out.println("Carrito después de eliminar:");
            carrito.forEach(p -> System.out.println(p.getNombre()));

            // ✅ Validar el tamaño de la lista de productos
            System.out.println("Total productos en BD: " + productoRepo.count());
            List<Producto> productos = productoRepo.findAll();
            System.out.println("Productos en BD:" + productos.size());
        };
    }

}
