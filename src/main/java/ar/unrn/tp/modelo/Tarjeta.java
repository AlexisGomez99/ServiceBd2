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
    private int numTarjeta;
    private String estado;
    private double saldo;
    private String nombre;

    public Tarjeta(int numTarjeta, String estado, double saldo, String nombre) {
        this.numTarjeta = numTarjeta;
        this.estado = estado;
        this.saldo = saldo;
        this.nombre = nombre;
    }
    public Tarjeta(Long id, int numTarjeta, String estado, double saldo, String nombre) {
        this.id = id;
        this.numTarjeta = numTarjeta;
        this.estado = estado;
        this.saldo = saldo;
        this.nombre = nombre;
    }

    public void agregar(double monto){
        this.saldo=this.saldo+monto;
    }
    public void descontar(double monto){
        this.saldo=this.saldo-monto;
    }

    public double getTotal(){
        return this.saldo;
    }
}
