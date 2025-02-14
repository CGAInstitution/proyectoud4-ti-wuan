package madstodolist.model;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "pedidos")
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @Temporal(TemporalType.DATE)
    @Column(nullable = false)
    private Date fecha;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoPedido estado = EstadoPedido.PENDIENTE;

    @OneToOne(mappedBy = "pedido", cascade = CascadeType.ALL)
    private DetallePedido detallePedido;

    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<PedidoProducto> pedidoProductos;

    @Column(nullable = false)
    private double total;

    // Getters y Setters

    public enum EstadoPedido {
        PENDIENTE, ENVIADO, ENTREGADO
    }

    public Pedido(Long id, Usuario usuario, Date fecha, EstadoPedido estado, DetallePedido detallePedido, List<PedidoProducto> pedidoProductos) {
        this.id = id;
        this.usuario = usuario;
        this.fecha = fecha;
        this.estado = estado;
        this.detallePedido = detallePedido;
        this.pedidoProductos = pedidoProductos;
        this.total = calcularTotal();
    }

    public Pedido() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public EstadoPedido getEstado() {
        return estado;
    }

    public void setEstado(EstadoPedido estado) {
        this.estado = estado;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public DetallePedido getDetallePedido() {
        return detallePedido;
    }

    public void setDetallePedido(DetallePedido detallePedido) {
        this.detallePedido = detallePedido;
    }

    public List<PedidoProducto> getPedidoProductos() {
        return pedidoProductos;
    }

    public void setPedidoProductos(List<PedidoProducto> pedidoProductos) {
        this.pedidoProductos = pedidoProductos;
        this.total = calcularTotal();
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public double calcularTotal() {
        if (pedidoProductos == null) return 0.0; // Retorna 0 si la lista es null
        return pedidoProductos.stream()
                .mapToDouble(pp -> pp.getCantidad() * pp.getProducto().getPrecio())
                .sum();
    }
}
