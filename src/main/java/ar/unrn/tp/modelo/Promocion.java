package ar.unrn.tp.modelo;

import ar.unrn.tp.excepciones.DateException;
import ar.unrn.tp.excepciones.InvalidCardException;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Setter
@Getter
public abstract class Promocion {
    @Id
    @GeneratedValue
    private Long id;
    protected LocalDate fechaInicio;
    protected LocalDate fechaFin;
    protected double descuento;

    public Promocion(LocalDate fechaInicio, LocalDate fechaFin, double descuento) throws DateException {

        if(!fechaInicio.isBefore(fechaFin))
            throw new DateException();

        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.descuento = descuento;
    }


    protected abstract double aplicarDescuento(List<Producto> productos, String tarjeta);

    protected double getTotal(List<Producto> productos) {
        return 0;
    }
}
