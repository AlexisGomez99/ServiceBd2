package ar.unrn.tp.modelo;

import ar.unrn.tp.excepciones.DateException;

import java.time.LocalDate;

import java.util.List;

public class DescProducto extends Promocion{
    private Marca marca;

    public DescProducto(LocalDate fechaInicio, LocalDate fechaFin, Marca marca, double descuento) throws DateException {
        super(fechaInicio,fechaFin,descuento);
        this.marca = marca;

    }

    @Override
    public double aplicarDescuento(List<Producto> productos,String tarjeta) {
        LocalDate hoy= LocalDate.now();
        double total=0;
        for (Producto prod : productos){

            if(hoy.isBefore(this.fechaFin) && hoy.isAfter(this.fechaInicio)){
                total = total + prod.verificarDescontar(marca,descuento);
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
