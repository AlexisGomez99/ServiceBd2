package ar.unrn.tp.api;

import ar.unrn.tp.modelo.Tarjeta;

import java.util.List;

public interface ClienteService {

    void agregarCliente(Long id,String nombre, String apellido, String dni, String email, List<Tarjeta> tarjetas);

}
