package ar.unrn.tp.modelo;

import lombok.Getter;

import java.util.Objects;

@Getter
public class Marca {
    private String nombre;

    public Marca(String nombre) {
        this.nombre = nombre;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Marca)) return false;
        Marca marca = (Marca) o;
        return Objects.equals(getNombre(), marca.getNombre());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getNombre());
    }
}
