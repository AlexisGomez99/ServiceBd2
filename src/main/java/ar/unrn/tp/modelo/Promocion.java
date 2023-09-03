package ar.unrn.tp.modelo;

import ar.unrn.tp.excepciones.DateException;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@MappedSuperclass
@Inheritance(strategy = InheritanceType.JOINED)
@Setter
@Getter
@NoArgsConstructor
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


    protected abstract double aplicarDescuento(List<Producto> productos, Tarjeta tarjeta);

    protected double getTotal(List<Producto> productos) {
        return 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Promocion)) return false;
        Promocion promocion = (Promocion) o;
        return Double.compare(promocion.getDescuento(), getDescuento()) == 0 && getFechaInicio().equals(promocion.getFechaInicio()) && getFechaFin().equals(promocion.getFechaFin());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getFechaInicio(), getFechaFin(), getDescuento());
    }
}
