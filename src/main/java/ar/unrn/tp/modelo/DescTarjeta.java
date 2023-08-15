package ar.unrn.tp.modelo;

import ar.unrn.tp.excepciones.DateException;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

public class DescTarjeta extends Promocion{

    private Tarjeta tarjeta;


    public DescTarjeta(LocalDate fechaInicio, LocalDate fechaFin, Tarjeta tarjeta, double descuento) throws DateException {
        super(fechaInicio,fechaFin,descuento);
        this.tarjeta = tarjeta;
    }


    @Override
    public double aplicarDescuento(List<Producto> productos, String tarjeta) {
        LocalDate hoy= LocalDate.now();
        double total=0;
        if (hoy.isBefore(this.fechaFin) && hoy.isAfter(this.fechaInicio) && this.tarjeta.getNombre().equalsIgnoreCase(tarjeta)){
            total = getTotal(productos);
            total = total * this.descuento;
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
