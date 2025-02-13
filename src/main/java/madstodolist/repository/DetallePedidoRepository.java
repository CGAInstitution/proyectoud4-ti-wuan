package madstodolist.repository;

import madstodolist.model.DetallePedido;
import madstodolist.model.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DetallePedidoRepository extends JpaRepository<DetallePedido, Long> {
}
