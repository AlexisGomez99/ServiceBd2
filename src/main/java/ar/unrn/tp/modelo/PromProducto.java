package ar.unrn.tp.modelo;

import ar.unrn.tp.excepciones.DateException;

import javax.persistence.Entity;
import java.time.LocalDate;

import java.util.List;
@Entity
public class PromProducto extends Promocion{
    private String marca;

    public PromProducto(String marca, LocalDate fechaInicio, LocalDate fechaFin, double descuento) throws DateException {
        super(fechaInicio,fechaFin,descuento);
        this.marca = marca;

    }

    @Override
    public double aplicarDescuento(List<Producto> productos, Tarjeta tarjeta) {
        LocalDate hoy= LocalDate.now();
        double total=0;
        for (Producto prod : productos){

            if(hoy.isBefore(this.fechaFin) && hoy.isAfter(this.fechaInicio) && prod.esDeMarca(marca)){
                total = total + (prod.getPrecio() * descuento);
            }
        }
        return total;
    }
    public double getTotal(List<Producto> productos) {
        double total = 0;
        if(productos != null) {
            for (Producto prod : productos) {
                total = total + prod.getPrecio();
            }
        }
        return total;
    }

}
