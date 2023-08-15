package ar.unrn.tp.jpa.servicios;

import ar.unrn.tp.api.VentaService;
import org.junit.jupiter.api.AfterEach;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.List;
import java.util.function.Consumer;
public class Ventas implements VentaService {


    private EntityManagerFactory emf;

    public Ventas() {
        emf = Persistence.createEntityManagerFactory("objectdb:myDbTestFile.tmp;drop");
    }

    @Override
    public void realizarVenta(Long idCliente, List<Long> productos, Long idTarjeta) {


    }

    @Override
    public float calcularMonto(List<Long> productos, Long idTarjeta) {
        return 0;
    }

    @Override
    public List ventas() {
        return null;
    }



    public void inTransactionExecute(Consumer<EntityManager> bloqueDeCodigo) throws Exception {
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
            if (em != null && em.isOpen())
                em.close();
        }
    }

    @AfterEach
    public void tearDown() {
        emf.close();
    }
}
