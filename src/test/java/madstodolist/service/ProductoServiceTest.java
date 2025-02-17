package madstodolist.service;

import madstodolist.dto.CategoriaData;
import madstodolist.dto.ProductoData;
import madstodolist.model.Producto;
import madstodolist.repository.InventarioRepository;
import madstodolist.repository.ProductoRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Sql(scripts = "/clean-db.sql")
public class ProductoServiceTest {

    @Autowired
    ProductoRepository productoRepository;

    @Autowired
    InventarioRepository inventarioRepository;

    @Autowired
    ProductoService productoService;

    void addProductoBD() {
        ProductoData producto = new ProductoData();
        producto.setId(10L);
        producto.setNombre("Producto Ejemplo");
        producto.setDescripcion("Descripción del producto");
        producto.setPrecio(10.0);
        producto.setImagenUrl("camiseta_lol.jpg");
        CategoriaData categoria = new CategoriaData();
        categoria.setId(1L);
        ProductoData nuevoProducto = productoService.addProduct(producto, categoria, 10);
        System.out.println(nuevoProducto.getNombre() + " " + nuevoProducto.getDescripcion() + " " + nuevoProducto.getPrecio() + " " + nuevoProducto.getImagenUrl());
    }

    @Test
    public void deleteProduct() {
        addProductoBD();
        productoService.deleteProduct(10L);
        Optional<Producto> producto = productoRepository.findById(10L);
        assertThat(producto).isEmpty();
    }

    @Test
    public void addProduct() {
        addProductoBD();
        Optional<Producto> producto = productoRepository.findById(10L);
        assertThat(producto).isNotEmpty();
    }
}
