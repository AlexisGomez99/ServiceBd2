package ar.unrn.tp.modelo;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
@Entity
@Getter
@Setter
public class Venta {
    @Id
    @GeneratedValue
    private Long id;
    private LocalDate fechaVenta;
    private Cliente cliente;
    @OneToMany(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    private List<ProductoVendido> listaProductos;
    private double totalPagado;




    public Venta(LocalDate fechaVenta, Cliente cliente, List<Producto> listaProductos, double montoTotal) {
        this.fechaVenta = fechaVenta;
        this.cliente = cliente;
        this.listaProductos = this.convertProd(listaProductos);
        this.totalPagado = montoTotal;
    }

    private List<ProductoVendido> convertProd(List<Producto> listaProductos){
        List<ProductoVendido> list = new ArrayList<>();
        for (Producto prod: listaProductos){
            list.add(new ProductoVendido(prod.getCodigo(),prod.getDescripcion(),prod.getCategoria(),prod.getPrecio(),prod.getMarca()));
        }

        return list;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Venta)) return false;
        Venta venta = (Venta) o;
        return Double.compare(venta.getTotalPagado(), getTotalPagado()) == 0 && getFechaVenta().equals(venta.getFechaVenta()) && getCliente().equals(venta.getCliente()) && getListaProductos().equals(venta.getListaProductos());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getFechaVenta(), getCliente(), getListaProductos(), getTotalPagado());
    }
}
