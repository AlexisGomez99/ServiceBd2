package ar.unrn.tp.modelo;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Objects;

@Entity
@Setter
@Getter
public class Marca {
    @Id
    @GeneratedValue
    private Long id;
    private String nombre;

    public Marca(String nombre) {
        this.nombre = nombre;
    }

}
