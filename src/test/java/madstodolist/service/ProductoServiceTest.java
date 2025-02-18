package madstodolist.service;

import madstodolist.dto.CategoriaData;
import madstodolist.dto.ProductoData;
import madstodolist.model.Producto;
import madstodolist.repository.InventarioRepository;
import madstodolist.repository.ProductoRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS) // Permite usar @BeforeAll y @AfterAll sin static
@Transactional
public class ProductoServiceTest {

    @Autowired
    ProductoRepository productoRepository;

    @Autowired
    ProductoService productoService;

    private Long testProductoId;
    private int productosIniciales; // antes del test

    @BeforeAll
    void setUp() {
        // Contar los productos preexistentes antes de agregar el de prueba
        productosIniciales = (int) productoRepository.count();
        assertTrue(productosIniciales >= 6, "Debe haber al menos 6 productos preexistentes en la BD.");

        // Crear un producto de prueba antes de ejecutar los tests
        ProductoData producto = new ProductoData();
        producto.setNombre("Producto de Prueba");
        producto.setDescripcion("Descripción del producto de prueba.");
        producto.setPrecio(29.99);
        producto.setImagenUrl("https://example.com/prueba.jpg");

        CategoriaData categoria = new CategoriaData();
        categoria.setId(1L);

        ProductoData nuevoProducto = productoService.addProduct(producto, categoria, 10);

        // Verificar que el producto fue creado y obtener su ID
        Optional<Producto> productoGuardado = productoRepository.findByNombre("Producto de Prueba").stream().findFirst();
        if (productoGuardado.isPresent()) {
            testProductoId = productoGuardado.get().getId();
            System.out.println("Producto de prueba creado con ID: " + testProductoId);
        } else {
            throw new RuntimeException("Error: No se pudo crear el producto de prueba.");
        }
    }

    @AfterAll
    void tearDown() {
        // Solo eliminar el producto de prueba si fue creado correctamente
        if (testProductoId != null) {
            productoService.deleteProduct(testProductoId);
        }
    }

    @Test
    public void testDeleteProduct() {
        Assertions.assertNotNull(testProductoId, "El ID del producto de prueba no debe ser null.");

        productoService.deleteProduct(testProductoId);
        Optional<Producto> producto = productoRepository.findById(testProductoId);
        assertThat(producto).isEmpty();
    }

    @Test
    public void testAddProduct() {
        Assertions.assertNotNull(testProductoId, "El ID del producto de prueba no debe ser null.");

        List<Producto> productos = productoRepository.findAll();
        assertThat(productos.size()).isEqualTo(productosIniciales + 1);
        assertThat(productos.stream().anyMatch(p -> p.getId().equals(testProductoId))).isTrue();
    }
}
