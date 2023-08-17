package ar.unrn.tp.main;


import ar.unrn.tp.api.CategoriaService;
import ar.unrn.tp.api.ClienteService;
import ar.unrn.tp.api.ProductoService;
import ar.unrn.tp.excepciones.EmailException;
import ar.unrn.tp.excepciones.NotNullException;
import ar.unrn.tp.excepciones.NotNumException;
import ar.unrn.tp.jpa.servicios.Categorias;
import ar.unrn.tp.jpa.servicios.Clientes;
import ar.unrn.tp.jpa.servicios.Productos;
import ar.unrn.tp.modelo.Categoria;
import ar.unrn.tp.modelo.Tarjeta;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) throws EmailException, NotNullException, NotNumException {
        ClienteService clientes= new Clientes();
        List<Tarjeta> tarjetas;

        clientes.agregarCliente("Alexis","Gómez","42456256","ralexisge@gmail.com");
        clientes.agregarCliente("Santiago","Fernandez","43456825","sjf@gmail.com");

        clientes.modificarCliente(1L,null,"Espinoza",null,null);

        clientes.agregarTarjeta(1L,"2020 5645 2356 2354","Visa");
        clientes.agregarTarjeta(1L,"6061 2682 3265 5632","MemeCard");
        clientes.agregarTarjeta(1L,"2525 2323 6524 6541","Uala");
        clientes.agregarTarjeta(2L,"6061 2682 7275 7565","MemeCard");

        tarjetas = clientes.listarTarjetas(1L);

        for (Tarjeta t: tarjetas){
            System.out.println(t.getNombre()+" -> "+t.getNumTarjeta());
        }

        CategoriaService categorias = new Categorias();

        categorias.crearCategoria("ROPA DEPORTIVA");
        categorias.crearCategoria("ROPA DE INVIERNO");
        categorias.crearCategoria("ROPA DE VERANO");
        categorias.crearCategoria("CALZADO DEPORTIVO");
        categorias.crearCategoria("CALZADO DE INVIERNO");
        categorias.crearCategoria("CALZADO DE VERANO");

        List<Categoria> listaCategorias=categorias.listarCategorias();

        for (Categoria c: listaCategorias){
            System.out.println(c.getNombreCategoria());
        }
        categorias.modificarCategoria(7L, "ROPA DE DEPORTE");
        categorias.modificarCategoria(10L,"ROPA DE DEPORTE");
        listaCategorias=categorias.listarCategorias();

        for (Categoria c: listaCategorias){
            System.out.println(c.getNombreCategoria());
        }

        ProductoService productos= new Productos();

        productos.crearProducto("2323","Buena calidad remera",500, 7L,"Nike");
    }
}
