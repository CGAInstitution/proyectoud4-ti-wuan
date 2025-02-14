package madstodolist;

import madstodolist.model.*;
import madstodolist.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import java.math.BigDecimal;
import java.util.Date;

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
            DetallePedidoRepository detallePedidoRepo
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
            categoria.setNombre("Figuras de colección");
            categoria.setDescripcion("Figuras de personajes de League of Legends.");
            categoriaRepo.save(categoria);

            // 3️⃣ Crear un producto
            Producto producto = new Producto();
            producto.setNombre("Figura de Ahri");
            producto.setDescripcion("Figura coleccionable de Ahri.");
            producto.setPrecio(new Double("49.99"));
            producto.setImagenUrl("https://example.com/ahri.jpg");
            producto.setCategoria(categoria);
            productoRepo.save(producto);

            Producto producto2 = new Producto();
            producto2.setNombre("Figura de Yasuo");
            producto2.setDescripcion("Figura coleccionable de Yasuo, personaje de 'League of Legends'.");
            producto2.setPrecio(new Double("59.99"));
            producto2.setImagenUrl("https://example.com/yasuo.jpg");
            producto2.setCategoria(categoria);
            productoRepo.save(producto2);



            // 4️⃣ Crear un pedido
            Pedido pedido = new Pedido();
            pedido.setUsuario(usuario);
            pedido.setFecha(new Date());
            pedido.setEstado(Pedido.EstadoPedido.PENDIENTE);
            pedido.setDetallePedido(new DetallePedido(1L, pedido, "Calle Falsa 123", DetallePedido.MetodoPago.TARJETA));
            pedido.setTotal(pedido.calcularTotal());
            pedidoRepo.save(pedido);

            Pedido pedido2 = new Pedido();
            pedido2.setUsuario(usuario);
            pedido2.setFecha(new Date());
            pedido2.setEstado(Pedido.EstadoPedido.PENDIENTE);
            pedido2.setDetallePedido(new DetallePedido(2L, pedido2, "Calle Falsa 456", DetallePedido.MetodoPago.PAYPAL));
            pedido2.setTotal(pedido2.calcularTotal());
            pedidoRepo.save(pedido2);


            // 5️⃣ Asociar el pedido con el producto
            PedidoProducto pedidoProducto = new PedidoProducto();
            pedidoProducto.setPedido(pedido);
            pedidoProducto.setProducto(producto);
            pedidoProducto.setCantidad(2);
            pedidoProductoRepo.save(pedidoProducto);

            PedidoProducto pedidoProducto2 = new PedidoProducto();
            pedidoProducto2.setPedido(pedido);
            pedidoProducto2.setProducto(producto2);
            pedidoProducto2.setCantidad(1);
            pedidoProductoRepo.save(pedidoProducto2);

            PedidoProducto pedidoProducto3 = new PedidoProducto();
            pedidoProducto3.setPedido(pedido2);
            pedidoProducto3.setProducto(producto2);
            pedidoProducto3.setCantidad(3);
            pedidoProductoRepo.save(pedidoProducto3);

            PedidoProducto pedidoProducto4 = new PedidoProducto();
            pedidoProducto4.setPedido(pedido2);
            pedidoProducto4.setProducto(producto);
            pedidoProducto4.setCantidad(4);
            pedidoProductoRepo.save(pedidoProducto4);

            // ✅ Validar datos guardados
            System.out.println("Usuarios en BD: " + usuarioRepo.count());
            System.out.println("Categorías en BD: " + categoriaRepo.count());
            System.out.println("Productos en BD: " + productoRepo.count());
            System.out.println("Pedidos en BD: " + pedidoRepo.count());
            System.out.println("Pedidos-Productos en BD: " + pedidoProductoRepo.count());
        };
    }
}
