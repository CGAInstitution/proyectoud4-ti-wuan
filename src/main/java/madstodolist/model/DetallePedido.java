package madstodolist.model;

import javax.persistence.*;

@Entity
@Table(name = "detalle_pedido")
public class DetallePedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long pedidoId;

    @OneToOne
    @MapsId
    @JoinColumn(name = "pedido_id")
    private Pedido pedido;

    @Column(name = "direccion_envio", nullable = false, length = 255)
    private String direccionEnvio;

    @Enumerated(EnumType.STRING)
    @Column(name = "metodo_pago", nullable = false)
    private MetodoPago metodoPago;

    // Getters y Setters

    public enum MetodoPago {
        TARJETA, PAYPAL, TRANSFERENCIA;

        public static MetodoPago fromString(String metodo) {
            switch (metodo.toUpperCase()) {
                case "TARJETA":
                    return TARJETA;
                case "PAYPAL":
                    return PAYPAL;
                case "TRANSFERENCIA":
                    return TRANSFERENCIA;
                default:
                    throw new IllegalArgumentException("Método de pago no válido: " + metodo);
            }
        }
    }

    public DetallePedido(Long pedidoId, Pedido pedido, String direccionEnvio, MetodoPago metodoPago) {
        this.pedidoId = pedidoId;
        this.pedido = pedido;
        this.direccionEnvio = direccionEnvio;
        this.metodoPago = metodoPago;
    }

    public DetallePedido(Pedido pedido, String direccionEnvio, MetodoPago metodoPago) {
        this.pedido = pedido;
        this.direccionEnvio = direccionEnvio;
        this.metodoPago = metodoPago;
    }

    public DetallePedido() {
    }

    public Long getPedidoId() {
        return pedidoId;
    }

    public void setPedidoId(Long pedidoId) {
        this.pedidoId = pedidoId;
    }

    public Pedido getPedido() {
        return pedido;
    }

    public void setPedido(Pedido pedido) {
        this.pedido = pedido;
    }

    public String getDireccionEnvio() {
        return direccionEnvio;
    }

    public void setDireccionEnvio(String direccionEnvio) {
        this.direccionEnvio = direccionEnvio;
    }

    public MetodoPago getMetodoPago() {
        return metodoPago;
    }

    public void setMetodoPago(MetodoPago metodoPago) {
        this.metodoPago = metodoPago;
    }


}
