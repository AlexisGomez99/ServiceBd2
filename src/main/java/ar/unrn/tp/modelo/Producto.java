package ar.unrn.tp.modelo;

import ar.unrn.tp.excepciones.NotNullException;
import ar.unrn.tp.excepciones.NotNumException;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class Producto {


    private int codigo;
    private String descripcion;
    private Categoria categoria;
    private double precio;
    private Marca marca;

    public Producto(int codigo, String descripcion, Categoria categoria, double precio, Marca marca) throws NotNullException {
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

    public double verificarDescontar(Marca marcaN, double descuento) {
        LocalDate hoy= LocalDate.now();
        double desc=0;
        if(marcaN.equals(this.marca)){
            desc = (precio * descuento);
        }
        return desc;
    }
}
