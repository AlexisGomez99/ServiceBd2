package ar.unrn.tp.modelo;
import ar.unrn.tp.excepciones.NotNullException;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
@Getter
@Setter
public class Carrito {
    private List<Promocion> promocions;
    private List<Producto> productoList;
    private Cliente cliente;
    private Tarjeta tarjeta;
    public Carrito( List<Promocion> listPromociones) {
        this.promocions = listPromociones;
        this.productoList = new ArrayList<>();
    }


    public void asociarCliente(Cliente cliente){
        this.cliente= cliente;
    }
    public void asociarTarjeta(Tarjeta tarjeta){
        this.tarjeta=tarjeta;
    }
    public void agregarProducto(Producto prod){
        productoList.add(prod);
    }
    public void agregarProductos(List<Producto> prods){
        this.productoList=prods;
    }




    public double calcularDescuento() throws NotNullException {
        return this.totalSinDescuento()- this.aplicarDescuento();
    }

    private double aplicarDescuento() throws NotNullException {
        ServicioTarjetaWeb servicio= new ServicioTarjetaWeb();
        double total= 0;
        if (servicio.esValida(tarjeta)){
            total = this.promocions.stream()
                    .mapToDouble(prom -> prom.aplicarDescuento(this.productoList, this.tarjeta))
                    .sum();
        }else {
            throw new NotNullException("Tarjeta");
        }
        return total;
    }

    public Venta comprarListado() throws NotNullException {
        double total= this.calcularDescuento();
        Venta venta= new Venta(LocalDate.now(), this.cliente,this.productoList,total);
        return venta;
    }

    public double totalSinDescuento() {
        double total = 0;
        if(this.productoList != null) {
            for (Producto prod : this.productoList) {
                total = total + prod.getPrecio();
            }
        }
        return total;
    }
}
