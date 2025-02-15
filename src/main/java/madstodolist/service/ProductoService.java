package madstodolist.service;

import madstodolist.dto.ProductoData;
import madstodolist.model.Producto;
import madstodolist.repository.ProductoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class ProductoService {

    private final ProductoRepository productoRepository;

    public ProductoService(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
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
}
