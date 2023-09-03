package ar.unrn.tp.servicios;

import ar.unrn.tp.api.ClienteService;
import ar.unrn.tp.excepciones.EmailException;
import ar.unrn.tp.excepciones.NotNullException;
import ar.unrn.tp.excepciones.NotNumException;
import ar.unrn.tp.jpa.servicios.Clientes;
import ar.unrn.tp.modelo.Cliente;
import ar.unrn.tp.modelo.Tarjeta;
import org.junit.Assert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.List;
import java.util.function.Consumer;

import static org.junit.Assert.assertTrue;

public class ClienteServiceTest {
    private static final String UNIT_NAME = "objectdb:test.tmp;drop";
    private static ClienteService clientes;
    private EntityManagerFactory emf;

    @BeforeEach
    public void setUp() {
        emf = Persistence.createEntityManagerFactory(UNIT_NAME);

        clientes = new Clientes(emf);
        clientes.agregarCliente("Alexis", "Gómez", "42456256", "ralexisge@gmail.com");
    }

    @Test
    public void verificarCreacionDeCliente() {
        inTransactionExecute((em) -> {
            Cliente cliente = em.find(Cliente.class, 1L);
            Cliente clienteCopia = null;
            try {
                clienteCopia = new Cliente("Alexis","Gómez","42456256","ralexisge@gmail.com");
                clienteCopia.setId(1L);
            } catch (NotNumException e) {
                throw new RuntimeException(e);
            } catch (EmailException e) {
                throw new RuntimeException(e);
            }
            Assert.assertTrue(cliente.equals(clienteCopia));

        });
    }

    @Test
    public void verificarCuandoSeModificaUnCliente() {

        clientes.modificarCliente(1L, "Alexis", "Espinoza", "42456256", "ralexisge@gmail.com");
        inTransactionExecute(
                (em) -> {
                    Cliente cliente = em.find(Cliente.class, 1L);

                    Cliente clienteCopia = null;
                    try {
                        clienteCopia = new Cliente("Alexis","Espinoza","42456256","ralexisge@gmail.com");
                        Assert.assertTrue(cliente.equals(clienteCopia));
                    } catch (NotNumException e) {
                        throw new RuntimeException(e);
                    } catch (EmailException e) {
                        throw new RuntimeException(e);
                    }

                }
        );
    }

    @Test
    public void verificarCuandoSeAgregaUnaTarjetaAUnCliente() {
        clientes.agregarTarjeta(1L, "123456", "MemeCard");

        inTransactionExecute(
                (em) -> {
                    Cliente cliente = em.find(Cliente.class, 1L);
                    Assert.assertTrue(cliente.getTarjetas().size() > 0);
                }
        );
    }

    @Test
    public void verificarQueLaListaDeTarjetasNoEstaVacia() {
        clientes.agregarTarjeta(1L, "123456", "MemeCard");

        inTransactionExecute(
                (em) -> {
                    List<Tarjeta> tarjetas = clientes.listarTarjetas(1L);
                    Assert.assertTrue(!tarjetas.isEmpty());
                }
        );
    }

    @Test
    public void verificarQueElClienteNoExiste() {
        RuntimeException exception = Assertions.assertThrows(RuntimeException.class, () -> {
            clientes.modificarCliente(5L,"Alex", "Gómez", "42456256", "ralexis@gmail.com");
        });

        Assertions.assertTrue(exception.getMessage().contains("El cliente no existe"));
    }

    @Test
    public void verificarQueElDniExisteAlCrearUnCliente() {
        RuntimeException exception = Assertions.assertThrows(RuntimeException.class, () -> {
            clientes.agregarCliente("Alexis", "Espinoza", "42456256", "ralexisge@gmail.com");
        });
        Assertions.assertTrue(exception.getMessage().contains("Ya existe un usuario con ese dni"));
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
