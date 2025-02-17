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
            PedidoService pedidoService, //
            InventarioRepository inventarioRepo  // Inyectamos el repositorio de Inventario
            // Inyectamos el servicio// Inyectamos el servicio
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

            Categoria categoria2 = new Categoria();
            categoria2.setNombre("Camisetas");
            categoria2.setDescripcion("Camisetas de personajes de League of Legends.");
            categoriaRepo.save(categoria2);

            Categoria categoria3 = new Categoria();
            categoria3.setNombre("Teclados");
            categoria3.setDescripcion("Teclados de League of Legends.");
            categoriaRepo.save(categoria3);

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

            Producto producto3 = new Producto();
            producto3.setNombre("Camiseta de Jinx");
            producto3.setDescripcion("Camiseta de Jinx, personaje de 'League of Legends'.");
            producto3.setPrecio(19.99);
            producto3.setImagenUrl("https://example.com/jinx.jpg");
            producto3.setCategoria(categoria2);
            productoRepo.save(producto3);

            Producto producto4 = new Producto();
            producto4.setNombre("Camiseta de Teemo");
            producto4.setDescripcion("Camiseta de Teemo, personaje de 'League of Legends'.");
            producto4.setPrecio(19.99);
            producto4.setImagenUrl("https://example.com/teemo.jpg");
            producto4.setCategoria(categoria2);
            productoRepo.save(producto4);

            Producto producto5 = new Producto();
            producto5.setNombre("Teclado de Yasuo");
            producto5.setDescripcion("Teclado de Yasuo, personaje de 'League of Legends'.");
            producto5.setPrecio(99.99);
            producto5.setImagenUrl("https://example.com/yasuo-teclado.jpg");
            producto5.setCategoria(categoria3);
            productoRepo.save(producto5);

            Producto producto6 = new Producto();
            producto6.setNombre("Teclado de Jinx");
            producto6.setDescripcion("Teclado de Jinx, personaje de 'League of Legends'.");
            producto6.setPrecio(99.99);
            producto6.setImagenUrl("https://example.com/jinx-teclado.jpg");
            producto6.setCategoria(categoria3);
            productoRepo.save(producto6);


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

            // 7️⃣ Insertar productos en el inventario
            inventarioRepo.save(new Inventario(producto, 3)); // Figura de Ahri
            inventarioRepo.save(new Inventario(producto2, 2)); // Figura de Yasuo
            inventarioRepo.save(new Inventario(producto3, 200)); // Camiseta de Jinx
            inventarioRepo.save(new Inventario(producto4, 180)); // Camiseta de Teemo
            inventarioRepo.save(new Inventario(producto5, 50));  // Teclado de Yasuo
            inventarioRepo.save(new Inventario(producto6, 70));  // Teclado de Jinx

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
