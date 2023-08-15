package ar.unrn.tp.jpa.servicios;

import ar.unrn.tp.api.ClienteService;
import ar.unrn.tp.excepciones.EmailException;
import ar.unrn.tp.excepciones.NotNullException;
import ar.unrn.tp.excepciones.NotNumException;
import ar.unrn.tp.modelo.Cliente;
import ar.unrn.tp.modelo.Tarjeta;
import org.junit.jupiter.api.AfterAll;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.List;
import java.util.function.Consumer;
public class Clientes implements ClienteService {

    private EntityManagerFactory emf;

    public Clientes() {
        emf = Persistence.createEntityManagerFactory("objectdb:myDbTestFile.tmp;drop");
    }

    @Override
    public void agregarCliente(Long id,String nombre, String apellido, String dni, String email, List<Tarjeta> tarjetas) {
        inTransactionExecute((em) -> {
            Cliente cliente= null;
            try {
                cliente = new Cliente(id,nombre,apellido,dni,email);
                cliente.setTarjeta(tarjetas);
            } catch (NotNullException e) {
                throw new RuntimeException(e);
            } catch (NotNumException e) {
                throw new RuntimeException(e);
            } catch (EmailException e) {
                throw new RuntimeException(e);
            }
            em.persist(cliente);
        });
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


