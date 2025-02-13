package madstodolist.repository;

import madstodolist.model.Pedido;
import madstodolist.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PedidoRepository extends JpaRepository<Pedido, Long> {

    // Método para obtener todos los pedidos de un usuario
    List<Pedido> findByUsuario(Usuario usuario);

}
