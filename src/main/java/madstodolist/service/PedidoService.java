package madstodolist.service;

import madstodolist.model.Pedido;
import madstodolist.repository.PedidoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PedidoService {

    private final PedidoRepository pedidoRepository;

    public PedidoService(PedidoRepository pedidoRepository) {
        this.pedidoRepository = pedidoRepository;
    }

    @Transactional
    public void actualizarTotal(Long pedidoId) {
        Pedido pedido = pedidoRepository.findById(pedidoId).orElseThrow(() -> new RuntimeException("Pedido no encontrado"));
        double nuevoTotal = pedido.calcularTotal();
        pedidoRepository.actualizarTotalPedido(pedidoId, nuevoTotal);
    }
}
