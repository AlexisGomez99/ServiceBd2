package ar.unrn.tp.jpa.servicios;

import ar.unrn.tp.api.ClienteService;
import ar.unrn.tp.excepciones.EmailException;
import ar.unrn.tp.excepciones.NotNullException;
import ar.unrn.tp.excepciones.NotNumException;
import ar.unrn.tp.modelo.Cliente;
import ar.unrn.tp.modelo.Tarjeta;
import org.junit.jupiter.api.AfterAll;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
public class Clientes implements ClienteService {

    private final EntityManagerFactory emf;

    public Clientes(EntityManagerFactory emf) {
        this.emf = emf;
    }

    @Override
    public void agregarCliente(String nombre, String apellido, String dni, String email) {
        inTransactionExecute((em) -> {

            TypedQuery<Cliente> q = em.createQuery("SELECT c FROM Cliente c WHERE c.dni = :dni", Cliente.class);
            q.setParameter("dni", dni);
            List<Cliente> clientes = q.getResultList();

            if (!clientes.isEmpty())
                throw new RuntimeException("Ya existe un usuario con ese dni");
            Cliente cliente= null;
            try {
                cliente = cliente = new Cliente(nombre,apellido,dni,email);
            } catch (NotNumException e) {
                throw new RuntimeException(e);
            } catch (EmailException e) {
                throw new RuntimeException(e);
            }

            em.persist(cliente);
        });
    }
    @Override
    public void modificarCliente(Long idCliente, String nombre,String apellido, String dni, String email) {
        inTransactionExecute((em) -> {

            Cliente cliente = em.find(Cliente.class,idCliente);
            if(cliente != null) {
                cliente.setCliente(nombre,apellido,dni,email);
                em.persist(cliente);
            }
            else
                throw new RuntimeException("El cliente no existe");





        });
    }
    @Override
    public void eliminarCliente(Long idCliente){
        inTransactionExecute((em) -> {
            Cliente cliente = em.getReference(Cliente.class, idCliente);
            em.remove(cliente);
        });
    }

    @Override
    public void agregarTarjeta(Long idCliente, String nro, String marca) {
        inTransactionExecute((em) -> {

            Cliente cliente = em.find(Cliente.class, idCliente);
            if (cliente != null) {
                Tarjeta tarjeta = new Tarjeta(nro, marca);
                cliente.setTarjeta(tarjeta);
            }
            else {
                System.out.println("El cliente no existe");
            }

            em.persist(cliente);
        });
    }

    @Override
    public List listarTarjetas(Long idCliente) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        List<Tarjeta> tarjetas = new ArrayList<>();
        tx.begin();
        Cliente cliente=em.getReference(Cliente.class,idCliente);
        tarjetas= cliente.getTarjetas();
        tx.commit();
        return tarjetas;
    }






    public void inTransactionExecute(Consumer<EntityManager> bloqueDeCodigo) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();
            bloqueDeCodigo.accept(em);
            tx.commit();
        } catch (Exception e) {
            tx.rollback();
            throw e;
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }

    @AfterAll
    public void tearDown() {
        emf.close();
    }


}


