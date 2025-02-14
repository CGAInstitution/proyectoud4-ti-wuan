package madstodolist.repository;

import madstodolist.model.Producto;
import madstodolist.model.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ProductoRepository extends JpaRepository<Producto, Long> {
    List<Producto> findByCategoria_Nombre(String categoria);
}
