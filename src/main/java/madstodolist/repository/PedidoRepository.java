package madstodolist.repository;

import madstodolist.model.Pedido;
import madstodolist.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.List;

public interface PedidoRepository extends JpaRepository<Pedido, Long> {

    // Método para obtener todos los pedidos de un usuario
    List<Pedido> findByUsuario(Usuario usuario);

    @Transactional
    @Modifying
    @Query("UPDATE Pedido p SET p.total = :total WHERE p.id = :id")
    void actualizarTotalPedido(Long id, double total);
}

