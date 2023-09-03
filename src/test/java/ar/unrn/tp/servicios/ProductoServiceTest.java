package ar.unrn.tp.servicios;

import ar.unrn.tp.api.ProductoService;
import ar.unrn.tp.excepciones.NotNullException;
import ar.unrn.tp.jpa.servicios.Productos;
import ar.unrn.tp.modelo.Categoria;
import ar.unrn.tp.modelo.Marca;
import ar.unrn.tp.modelo.Producto;
import ar.unrn.tp.modelo.Tarjeta;
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

public class ProductoServiceTest {
    private static final String UNIT_NAME = "objectdb:test.tmp;drop";
    private static ProductoService productoService;
    private EntityManagerFactory emf;
    private Categoria categoria;
    private Marca marca;

    @BeforeEach
    public void setUp() {
        emf = Persistence.createEntityManagerFactory(UNIT_NAME);

        categoria = new Categoria("Deportes");
        marca = new Marca("Nike");


        inTransactionExecute((em) -> {
            em.persist(categoria);
            em.persist(marca);
        });

        productoService = new Productos(emf);
        productoService.crearProducto("777", "Producto de prueba", 55, 1L, 2L);
        productoService.crearProducto("888", "Producto de prueba 2", 75, 1L, 2L);
    }

    @Test
    public void verificarQueSeCreoUnProducto() {
        inTransactionExecute((em) -> {
            Producto producto = em.find(Producto.class, 3L);
            try {
                Producto prod= new Producto("777", "Producto de prueba",55,new Categoria("Deportes"),new Marca("Nike"));
                assertTrue(producto.equals(prod));
            } catch (NotNullException e) {
                throw new RuntimeException(e);
            }

        });
    }

    @Test
    public void verificarQueLaMarcaYCategoriaExista() {
        RuntimeException exception = Assertions.assertThrows(RuntimeException.class, () -> {
            productoService.crearProducto("777", "Producto de prueba", 55, 12L, 55L);
        });

        Assertions.assertTrue(exception.getMessage().contains("La marca o categoria no es valida."));
    }

    @Test
    public void verificarQueSeModificoUnProducto() {
        productoService.modificarProducto(3L, "555", "Producto de prueba", 55, 1L,2L);

        inTransactionExecute((em) -> {
            Producto producto = em.find(Producto.class, 3L);
            Producto prod= null;
            try {
                prod = new Producto("555", "Producto de prueba",55,new Categoria("Deportes"),new Marca("Nike"));
                assertTrue(producto.equals(prod));
            } catch (NotNullException e) {
                throw new RuntimeException(e);
            }

        });
    }

    @Test
    public void verificarQueElProductoExiste() {
        RuntimeException exception = Assertions.assertThrows(RuntimeException.class, () -> {
            productoService.modificarProducto(312L, "555", "Prueba modificada", 60, 1L,2L);
        });

        Assertions.assertTrue(exception.getMessage().contains("El producto no existe"));
    }

    @Test
    public void verificarQueLaListaDeProductoNoEstaVacia() {
        inTransactionExecute(
                (em) -> {
                    List<Tarjeta> productos = productoService.listarProductos();
                    assertTrue(!productos.isEmpty());
                }
        );
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
