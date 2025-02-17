package madstodolist.dto;

import madstodolist.model.DetallePedido;

import madstodolist.model.Pedido.EstadoPedido;
import madstodolist.model.Producto;

import java.util.Date;
import java.util.List;

public class PedidoData {
    private Long id;
    private Date fecha;
    private EstadoPedido estado;
    private DetallePedido detallePedido;
    private List<Producto> productos;
    private double total;
    private int cantidad;

    public PedidoData() {
    }

    public PedidoData(Long id, Date fecha, EstadoPedido estado, DetallePedido detallePedido, List<Producto> productos, double total) {
        this.id = id;
        this.fecha = fecha;
        this.estado = estado;
        this.detallePedido = detallePedido;
        this.productos = productos;
        this.total = total;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public EstadoPedido getEstado() {
        return estado;
    }

    public void setEstado(EstadoPedido estado) {
        this.estado = estado;
    }

    public DetallePedido getDetallePedido() {
        return detallePedido;
    }

    public void setDetallePedido(DetallePedido detallePedido) {
        this.detallePedido = detallePedido;
    }

    public List<Producto> getProductos() {
        return productos;
    }

    public void setPedidos(List<Producto> productos) {
        this.productos = productos;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }
}