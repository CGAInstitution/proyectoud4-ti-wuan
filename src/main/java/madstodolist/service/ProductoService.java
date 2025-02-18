package madstodolist.service;

import madstodolist.dto.CategoriaData;
import madstodolist.dto.ProductoData;
import madstodolist.model.Categoria;
import madstodolist.model.Inventario;
import madstodolist.model.Producto;
import madstodolist.repository.InventarioRepository;
import madstodolist.repository.ProductoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class ProductoService {

    private final ProductoRepository productoRepository;

    private final InventarioRepository inventarioRepository;

    public ProductoService(ProductoRepository productoRepository, InventarioRepository inventarioRepository) {
        this.productoRepository = productoRepository;
        this.inventarioRepository = inventarioRepository;
    }

    public Optional<Producto> obtenerProductoPorId(Long id) {
        return productoRepository.findById(id);
    }

    public List<ProductoData> findAll() {
        List<Producto> productos = StreamSupport
                .stream(productoRepository.findAll().spliterator(), false)
                .collect(Collectors.toList());
        return productos.stream().map(producto -> new ProductoData(producto.getId(),producto.getNombre(),producto.getDescripcion(),
                producto.getPrecio(), producto.getImagenUrl(), producto.getCategoria().getId())).collect(Collectors.toList());
    }

    public void deleteProduct(Long id) {
        try {
            Optional<Producto> producto = productoRepository.findById(id);
            if (producto.isPresent()) {
                productoRepository.delete(producto.get());
            }
        }catch (Exception e){
            System.out.println("Error al borrar el producto");
        }
    }

    public ProductoData addProduct(ProductoData productoData, CategoriaData categoriaData, int cantidad) {
        try {
            Producto producto = new Producto();
            producto.setNombre(productoData.getNombre());
            producto.setDescripcion(productoData.getDescripcion());
            producto.setPrecio(productoData.getPrecio());
            producto.setImagenUrl(productoData.getImagenUrl());
            Categoria categoria = new Categoria();
            categoria.setId(categoriaData.getId());
            producto.setCategoria(categoria);
            productoRepository.save(producto);
            Inventario inventario = new Inventario();
            inventario.setCantidad(cantidad);
            inventario.setProducto(producto);
            inventarioRepository.save(inventario);
            return productoData;
        }catch (Exception e){
            System.out.println("Error al añadir el producto");
            return null;
        }
    }

    public ProductoData modProduct(ProductoData productoData, CategoriaData categoriaData, int cantidad) {
        try {
            Optional<Producto> producto = productoRepository.findByNombre(productoData.getNombre());
            Optional<Inventario> inventario = inventarioRepository.findByProductoId(producto.get().getId());
            if (producto.isPresent()) {
                producto.get().setNombre(productoData.getNombre());
                producto.get().setDescripcion(productoData.getDescripcion());
                producto.get().setPrecio(productoData.getPrecio());
                producto.get().setImagenUrl(productoData.getImagenUrl());
                Categoria categoria = new Categoria();
                categoria.setId(categoriaData.getId());
                producto.get().setCategoria(categoria);
                productoRepository.save(producto.get());
                inventario.get().setCantidad(cantidad);
                inventarioRepository.save(inventario.get());
            }
            return productoData;
        }catch (Exception e){
            System.out.println("Error al modificar el producto");
            return null;
        }
    }

    public void restoreProduct(String nombre, int cantidad) {
        try {
            Optional<Producto> producto = productoRepository.findByNombre(nombre);
            Optional<Inventario> inventario = inventarioRepository.findByProductoId(producto.get().getId());
            if (producto.isPresent()) {
                inventario.get().setCantidad(cantidad);
                inventarioRepository.save(inventario.get());
            }
        }catch (Exception e){
            System.out.println("Error al restaurar el producto");
        }
    }
}
