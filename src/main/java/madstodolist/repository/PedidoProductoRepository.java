package madstodolist.repository;

import madstodolist.model.PedidoProducto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PedidoProductoRepository extends JpaRepository<PedidoProducto, Long> {
}
