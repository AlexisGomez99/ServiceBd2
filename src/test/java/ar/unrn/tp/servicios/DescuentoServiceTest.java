package ar.unrn.tp.servicios;

import ar.unrn.tp.api.DescuentoService;
import ar.unrn.tp.excepciones.DateException;
import ar.unrn.tp.jpa.servicios.Descuentos;
import ar.unrn.tp.modelo.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.function.Consumer;
import static org.junit.Assert.assertTrue;

public class DescuentoServiceTest {

    private static final String UNIT_NAME = "objectdb:test.tmp;drop";
    private static DescuentoService descuentoService;
    private EntityManagerFactory emf;
    @BeforeEach
    public void setUp() {
        emf = Persistence.createEntityManagerFactory(UNIT_NAME);

        descuentoService = new Descuentos(emf);
    }

    @Test
    public void verificarQueSeCreoUnDescuentoParaProducto() {

        LocalDate fechaInicio = LocalDate.now().minusDays(2);
        LocalDate fechaFin = LocalDate.now().plusDays(2);

        descuentoService.crearDescuento("Acme", fechaInicio, fechaFin, 0.5);

        inTransactionExecute((em) -> {

            TypedQuery<PromProducto> query = em.createQuery("select p from PromProducto p", PromProducto.class);
            PromProducto promocionProducto =  query.getSingleResult();
            Promocion descN= null;
            try {
                descN = new PromProducto("Acme",fechaInicio,fechaFin,0.5);
            } catch (DateException e) {
                throw new RuntimeException(e);
            }
            assertTrue(promocionProducto.equals(descN));
        });
    }

    @Test
    public void verificarQueSeCreeUnDescuentoParaUnaVenta() {
        LocalDate fechaInicio = LocalDate.now().minusDays(2);
        LocalDate fechaFin = LocalDate.now().plusDays(2);

        descuentoService.crearDescuentoSobreTotal("Visa", fechaInicio, fechaFin, 0.8);

        inTransactionExecute((em) -> {

            TypedQuery<PromTarjeta> query = em.createQuery("select p from PromTarjeta p", PromTarjeta.class);
            PromTarjeta promocionProducto =  query.getSingleResult();
            Promocion descN= null;
            try {
                descN = new PromTarjeta("Visa",fechaInicio,fechaFin,0.8);
            } catch (DateException e) {
                throw new RuntimeException(e);
            }
            assertTrue(promocionProducto.equals(descN));
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
            if (em != null && em.isOpen())
                em.close();
        }
    }

    @AfterEach
    public void tearDown() {
        emf.close();
    }
}
