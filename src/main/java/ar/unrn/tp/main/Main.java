package ar.unrn.tp.main;


import ar.unrn.tp.api.*;
import ar.unrn.tp.excepciones.EmailException;
import ar.unrn.tp.excepciones.NotNullException;
import ar.unrn.tp.excepciones.NotNumException;
import ar.unrn.tp.jpa.servicios.*;
import ar.unrn.tp.modelo.Categoria;
import ar.unrn.tp.modelo.Promocion;
import ar.unrn.tp.modelo.Tarjeta;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) throws Exception {
        ClienteService clientes= new Clientes();

        clientes.agregarCliente("Alexis","Gómez","42456256","ralexisge@gmail.com");
        clientes.agregarCliente("Santiago","Fernandez","43456825","sjf@gmail.com");

        clientes.modificarCliente(1L,null,"Espinoza",null,null);

        clientes.agregarTarjeta(1L,"2020 5645 2356 2354","Visa");
        clientes.agregarTarjeta(1L,"6061 2682 3265 5632","MemeCard");
        clientes.agregarTarjeta(1L,"2525 2323 6524 6541","Uala");
        clientes.agregarTarjeta(2L,"6061 2682 7275 7565","MemeCard");

        CategoriaService categorias = new Categorias();

        categorias.crearCategoria("ROPA DEPORTIVA");
        categorias.crearCategoria("ROPA DE INVIERNO");
        categorias.crearCategoria("ROPA DE VERANO");
        categorias.crearCategoria("CALZADO DEPORTIVO");
        categorias.crearCategoria("CALZADO DE INVIERNO");
        categorias.crearCategoria("CALZADO DE VERANO");

        categorias.modificarCategoria(7L, "ROPA DE DEPORTE");
        categorias.modificarCategoria(10L,"ROPA DE DEPORTE");

        ProductoService productos= new Productos();
        MarcaService marcas= new Marcas();
        marcas.crearMarca("Nike");
        marcas.crearMarca("Adiddas");
        marcas.crearMarca("Puma");
        productos.crearProducto("1","Buena calidad remera",500, 7L,13L);
        productos.crearProducto("2","Buena calidad zapatillas",300, 10L,13L);
        productos.modificarProducto(16L,"3","Remera",500, 8L,15L);

        DescuentoService descuentos = new Descuentos();

        descuentos.crearDescuento("Nike", LocalDate.now().minusDays(3),LocalDate.now().plusMonths(1),0.05);
        descuentos.crearDescuentoSobreTotal("Visa", LocalDate.now().minusDays(3),LocalDate.now().plusMonths(1),0.08);

        VentaService ventas= new Ventas();
        List<Long> prods= new ArrayList<>();
        prods.add(16L);
        prods.add(17L);
        ventas.realizarVenta(1L,prods,3L);

        //Como se maneja los id cuando los recupero
        //Cuando recupero las promociones no puedo usar aplicardescuentos en el main, solo me deja en el carrito
        //Como manejar la herencia de descuentos
        //Cuando se hereda se crean las tablas extra
        //

    }
}
