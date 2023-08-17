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
public class Categoria {

    @Id
    @GeneratedValue
    private Long id;
    private String nombreCategoria;

    public Categoria(String categoria){
        this.nombreCategoria= categoria;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Categoria)) return false;
        Categoria categoria = (Categoria) o;
        return getNombreCategoria().equals(categoria.getNombreCategoria());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getNombreCategoria());
    }
}
