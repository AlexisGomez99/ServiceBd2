package ar.unrn.tp.jpa.servicios;

import ar.unrn.tp.api.DescuentoService;
import ar.unrn.tp.excepciones.DateException;
import ar.unrn.tp.modelo.*;
import org.junit.jupiter.api.AfterAll;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.function.Consumer;

public class Descuentos implements DescuentoService {

    private final EntityManagerFactory emf;

    public Descuentos(EntityManagerFactory emf) {
        this.emf = emf;
    }

    @Override
    public void crearDescuentoSobreTotal(String marcaTarjeta, LocalDate fechaDesde, LocalDate fechaHasta, double porcentaje) {
        inTransactionExecute((em) -> {
            PromTarjeta descuento;
            try {
                descuento = new PromTarjeta(marcaTarjeta,fechaDesde,fechaHasta,porcentaje);
            } catch (DateException e) {
                throw new RuntimeException(e);
            }

            em.persist(descuento);
        });
    }

    @Override
    public void crearDescuento(String marcaProducto, LocalDate fechaDesde, LocalDate fechaHasta, double porcentaje) {
        inTransactionExecute((em) -> {
            Promocion descuento;
            try {
                descuento = new PromProducto(marcaProducto,fechaDesde,fechaHasta,porcentaje);
            } catch (DateException e) {
                throw new RuntimeException(e);
            }

            em.persist(descuento);
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
