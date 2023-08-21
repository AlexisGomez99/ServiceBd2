package ar.unrn.tp.modelo;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Getter
@Setter
@Entity
public class Tarjeta {

    @Id
    @GeneratedValue
    private Long id;
    private String numTarjeta;
    private String nombre;

    public Tarjeta(String numTarjeta,String nombre) {
        this.numTarjeta = numTarjeta;
        this.nombre = nombre;
    }

    public boolean equals(String tarjeta){
        boolean x= false;
        if (this.nombre.equalsIgnoreCase(tarjeta))
            x= true;
        return x;

    }

}
