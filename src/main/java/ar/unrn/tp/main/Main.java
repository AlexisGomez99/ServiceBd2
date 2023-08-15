package ar.unrn.tp.main;


import ar.unrn.tp.api.ClienteService;
import ar.unrn.tp.excepciones.EmailException;
import ar.unrn.tp.excepciones.NotNullException;
import ar.unrn.tp.excepciones.NotNumException;
import ar.unrn.tp.jpa.servicios.Clientes;
import ar.unrn.tp.modelo.Tarjeta;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) throws EmailException, NotNullException, NotNumException {
        ClienteService clientes= new Clientes();

        Tarjeta memeCard= new Tarjeta(1L,2020,"ACTIVO",20000,"MemeCard");
        List<Tarjeta> tarjetas= new ArrayList<>();
        tarjetas.add(memeCard);

        clientes.agregarCliente(1L,"Alexis","Gómez","42456256","ralexisge@gmail.com",tarjetas);
    }
}
