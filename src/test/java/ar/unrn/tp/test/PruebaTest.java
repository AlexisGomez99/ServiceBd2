package ar.unrn.tp.test;
import ar.unrn.tp.excepciones.DateException;
import ar.unrn.tp.excepciones.EmailException;
import ar.unrn.tp.excepciones.NotNullException;
import ar.unrn.tp.excepciones.NotNumException;
import ar.unrn.tp.modelo.*;
import org.junit.Assert;
import org.junit.Test;

import java.time.LocalDate;
import java.util.ArrayList;

import java.util.List;

public class PruebaTest   {
    @Test
    public void montoTotalSinDescuentos() throws EmailException, NotNullException, NotNumException, DateException {

        //Inicializacion
        Marca marca = new Marca("Nike");
        Producto prod1= new Producto("1", "zapatillas", 500,new Categoria("CALZADO"),  marca);
        Producto prod2= new Producto("2", "Buzo", 250,new Categoria("ROPA_DEPORTIVA"),  marca);
        Producto prod3= new Producto("3", "Shorts", 250,new Categoria("ROPA_DEPORTIVA"),  marca);

        Tarjeta tarjeta= new Tarjeta("123","Visa");

        Cliente cliente = new Cliente("Alexis","Gomez","42456256","ralexisge@gmail.com");
        cliente.setTarjeta(tarjeta);
        DescTarjeta descTarjeta= new DescTarjeta(LocalDate.now().plusMonths(1),LocalDate.now().plusMonths(2),tarjeta,0.05);
        DescProducto descProducto= new DescProducto(LocalDate.now().plusMonths(1),LocalDate.now().plusMonths(2),marca,0.08);
        List<Promocion> promociones = new ArrayList<>();
        promociones.add(descTarjeta);
        promociones.add(descProducto);

        Carrito carrito= new Carrito(promociones);
        carrito.asociarCliente(cliente);
        carrito.asociarTarjeta(tarjeta);
        carrito.agregarProducto(prod1);
        carrito.agregarProducto(prod2);
        carrito.agregarProducto(prod3);

        double montoEsperado= 1000;

        //Ejercitacion

        double montoRecibido= carrito.calcularDescuento();
        //Verificacion

        Assert.assertEquals("true",montoEsperado,montoRecibido, 00);
    }
    @Test
    public void montoTotalConDescuentoParaAcme() throws EmailException, NotNullException, NotNumException, DateException {

        //Inicializacion
        Marca marca = new Marca("Acme");
        Producto prod1= new Producto("1", "zapatillas", 500,new Categoria("CALZADO"),  marca);
        Producto prod2= new Producto("2", "Buzo", 250,new Categoria("ROPA_DEPORTIVA"),  marca);
        Producto prod3= new Producto("3", "Shorts", 250,new Categoria("ROPA_DEPORTIVA"),  marca);

        Tarjeta tarjeta= new Tarjeta("123","Visa");

        Cliente cliente = new Cliente("Alexis","Gomez","42456256","ralexisge@gmail.com");

        cliente.setTarjeta(tarjeta);
        DescTarjeta descTarjeta= new DescTarjeta(LocalDate.now().plusMonths(1),LocalDate.now().plusMonths(2),tarjeta,0.05);
        DescProducto descProducto= new DescProducto(LocalDate.now().minusDays(1),LocalDate.now().plusMonths(1),marca,0.08);
        List<Promocion> promociones = new ArrayList<>();
        promociones.add(descTarjeta);
        promociones.add(descProducto);

        Carrito carrito= new Carrito(promociones);
        carrito.asociarCliente(cliente);
        carrito.asociarTarjeta(tarjeta);
        carrito.agregarProducto(prod1);
        carrito.agregarProducto(prod2);
        carrito.agregarProducto(prod3);

        double montoEsperado= 920;

        //Ejercitacion

        double montoRecibido= carrito.calcularDescuento();
        //Verificacion

        Assert.assertEquals("true",montoEsperado,montoRecibido, 00);
    }
    @Test
    public void montoTotalConDescuentoParaVisa() throws EmailException, NotNullException, NotNumException, DateException {

        //Inicializacion
        Marca marca = new Marca("Acme");
        Producto prod1= new Producto("1", "zapatillas", 500,new Categoria("CALZADO"),  marca);
        Producto prod2= new Producto("2", "Buzo", 250,new Categoria("ROPA_DEPORTIVA"),  marca);
        Producto prod3= new Producto("3", "Shorts", 250,new Categoria("ROPA_DEPORTIVA"),  marca);

        Tarjeta tarjeta= new Tarjeta("123","Visa");

        Cliente cliente = new Cliente("Alexis","Gomez","42456256","ralexisge@gmail.com");

        cliente.setTarjeta(tarjeta);
        DescTarjeta descTarjeta= new DescTarjeta(LocalDate.now().minusDays(1),LocalDate.now().plusMonths(2),tarjeta,0.05);
        DescProducto descProducto= new DescProducto(LocalDate.now().plusMonths(1),LocalDate.now().plusMonths(2),marca,0.08);
        List<Promocion> promociones = new ArrayList<>();
        promociones.add(descTarjeta);
        promociones.add(descProducto);

        Carrito carrito= new Carrito(promociones);
        carrito.asociarCliente(cliente);
        carrito.asociarTarjeta(tarjeta);
        carrito.agregarProducto(prod1);
        carrito.agregarProducto(prod2);
        carrito.agregarProducto(prod3);

        double montoEsperado= 950;

        //Ejercitacion

        double montoRecibido= carrito.calcularDescuento();
        //Verificacion

        Assert.assertEquals("true",montoEsperado,montoRecibido, 00);
    }
    @Test
    public void montoTotalConDosDescuentosComarcaMemecard() throws EmailException, NotNullException, NotNumException, DateException {

        //Inicializacion
        Marca marca = new Marca("Comarca");
        Producto prod1= new Producto("1", "zapatillas", 500,new Categoria("CALZADO"),  marca);
        Producto prod2= new Producto("2", "Buzo", 250,new Categoria("ROPA_DEPORTIVA"),  marca);
        Producto prod3= new Producto("3", "Shorts", 250,new Categoria("ROPA_DEPORTIVA"),  marca);

        Tarjeta tarjeta= new Tarjeta("123","MemeCard");

        Cliente cliente = new Cliente("Alexis","Gomez","42456256","ralexisge@gmail.com");

        cliente.setTarjeta(tarjeta);
        DescTarjeta descTarjeta= new DescTarjeta(LocalDate.now().minusDays(1),LocalDate.now().plusMonths(2),tarjeta,0.05);
        DescProducto descProducto= new DescProducto(LocalDate.now().minusDays(1),LocalDate.now().plusMonths(2),marca,0.08);
        List<Promocion> promociones = new ArrayList<>();
        promociones.add(descTarjeta);
        promociones.add(descProducto);

        Carrito carrito= new Carrito(promociones);
        carrito.asociarCliente(cliente);
        carrito.asociarTarjeta(tarjeta);
        carrito.agregarProducto(prod1);
        carrito.agregarProducto(prod2);
        carrito.agregarProducto(prod3);

        double montoEsperado= 870;

        //Ejercitacion

        double montoRecibido= carrito.calcularDescuento();
        //Verificacion

        Assert.assertEquals("true",montoEsperado,montoRecibido, 00);
    }
    @Test
    public void verificarVenta() throws EmailException, NotNullException, NotNumException, DateException {

        //Inicializacion
        Marca marca = new Marca("Comarca");
        Producto prod1= new Producto("1", "zapatillas", 500,new Categoria("CALZADO"),  marca);
        Producto prod2= new Producto("2", "Buzo", 250,new Categoria("ROPA_DEPORTIVA"),  marca);
        Producto prod3= new Producto("3", "Shorts", 250,new Categoria("ROPA_DEPORTIVA"),  marca);

        Tarjeta tarjeta= new Tarjeta("123","MemeCard");

        Cliente cliente = new Cliente("Alexis","Gomez","42456256","ralexisge@gmail.com");

        cliente.setTarjeta(tarjeta);
        DescTarjeta descTarjeta= new DescTarjeta(LocalDate.now().minusDays(1),LocalDate.now().plusMonths(2),tarjeta,0.05);
        DescProducto descProducto= new DescProducto(LocalDate.now().minusDays(1),LocalDate.now().plusMonths(2),marca,0.08);
        List<Promocion> promociones = new ArrayList<>();
        promociones.add(descTarjeta);
        promociones.add(descProducto);

        Carrito carrito= new Carrito(promociones);
        carrito.asociarCliente(cliente);
        carrito.asociarTarjeta(tarjeta);
        carrito.agregarProducto(prod1);
        carrito.agregarProducto(prod2);
        carrito.agregarProducto(prod3);

        //Ejercitacion
        Venta venta= new Venta(LocalDate.now(),cliente,carrito.getProductoList(),870);//Consultar
        Venta orden= carrito.comprarListado();
        //Verificacion
        //consultar comparar con equals dentro de la clase venta
        Assert.assertEquals(true,venta.equals(orden));
    }

    @Test
    public void generarProductoInvalido() throws NotNullException{
        // Inicialización
        Marca acme = new Marca("Acme");
        boolean saltoExcepcion = false;

        // Ejercitación
        try{
            new Producto("1", "Es dinamita",-250.0,new Categoria("CALZADO"),  acme);
        }catch( NotNullException e){
            saltoExcepcion = true;
        }
        // Verificación
        Assert.assertEquals("true",true, saltoExcepcion);

    }
    @Test
    public void generarClienteInvalido() throws NotNullException{
        // Inicialización
        Marca acme = new Marca("Acme");
        boolean saltoExcepcion = false;

        // Ejercitación
        try{
            new Cliente("Alexis",null,null,null);
        }catch(NotNullException | NotNumException | EmailException e){
            saltoExcepcion = true;
        }
        // Verificación
        Assert.assertEquals("true",true, saltoExcepcion);

    }@Test
    public void generarDescuentoInvalido() throws NotNullException{
        // Inicialización
        Marca acme = new Marca("Acme");
        boolean saltoExcepcion = false;

        // Ejercitación
        try{
            DescProducto descProducto= new DescProducto(LocalDate.now().minusDays(1),LocalDate.now().plusMonths(-2),new Marca("Si"),0.08);

        }catch(DateException e){
            saltoExcepcion = true;
        }
        // Verificación
        Assert.assertEquals("true",true, saltoExcepcion);

    }


}
