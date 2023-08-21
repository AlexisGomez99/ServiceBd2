package ar.unrn.tp.modelo;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import java.util.Objects;
@Entity
@Getter
@Setter
public class ProductoVendido {
    @Id
    @GeneratedValue
    private Long id;
    private String codigo;
    private String descripcion;
    @OneToOne
    private Categoria categoria;
    private double precio;
    @OneToOne
    private Marca marca;

    public ProductoVendido(String codigo, String descripcion, Categoria categoria, double precio, Marca marca) {
        this.codigo = codigo;
        this.descripcion = descripcion;
        this.categoria = categoria;
        this.precio = precio;
        this.marca = marca;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ProductoVendido)) return false;
        ProductoVendido that = (ProductoVendido) o;
        return getCodigo() == that.getCodigo() && Double.compare(that.getPrecio(), getPrecio()) == 0 && getDescripcion().equals(that.getDescripcion()) && getCategoria() == that.getCategoria() && getMarca().equals(that.getMarca());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getCodigo(), getDescripcion(), getCategoria(), getPrecio(), getMarca());
    }
}
