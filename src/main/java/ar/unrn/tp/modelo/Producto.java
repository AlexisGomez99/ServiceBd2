package ar.unrn.tp.modelo;

import ar.unrn.tp.excepciones.NotNullException;
import ar.unrn.tp.excepciones.NotNumException;
import lombok.Getter;
import lombok.Setter;

import javax.jdo.annotations.Unique;
import javax.persistence.*;
import java.time.LocalDate;
@Setter
@Getter
@Entity
public class Producto {

    @Id
    @GeneratedValue
    private Long id;
    @Unique
    private String codigo;
    private String descripcion;

    @OneToOne
    private Categoria categoria;
    private double precio;
    @OneToOne
    private Marca marca;

    public Producto(String codigo, String descripcion, double precio, Categoria categoria , Marca marca) throws NotNullException {
        if (descripcion == null)
            throw new NotNullException("descripcion");
        if (precio < 0)
            throw new NotNullException("precio");
        if (categoria == null)
            throw new NotNullException("categoria");

        this.codigo = codigo;
        this.descripcion = descripcion;
        this.categoria = categoria;
        this.precio = precio;
        this.marca = marca;
    }

    public boolean esDeMarca(String marcaN) {
        boolean x=false;
        if(this.marca.getNombre().equalsIgnoreCase(marcaN)){
            x = true;
        }
        return x;
    }
}
