package ar.unrn.tp.servicios;

import ar.unrn.tp.api.ClienteService;
import ar.unrn.tp.api.DescuentoService;
import ar.unrn.tp.api.ProductoService;
import ar.unrn.tp.api.VentaService;
import ar.unrn.tp.jpa.servicios.Clientes;
import ar.unrn.tp.jpa.servicios.Descuentos;
import ar.unrn.tp.jpa.servicios.Productos;
import ar.unrn.tp.jpa.servicios.Ventas;
import ar.unrn.tp.modelo.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class VentaServiceTest {

    private static final String UNIT_NAME = "objectdb:test.tmp;drop";
    private static VentaService ventaService;
    private static ClienteService clienteService;
    private static DescuentoService descuentoService;
    private static ProductoService productoService;

    private EntityManagerFactory emf;

    @BeforeEach
    public void setUp() {

        emf = Persistence.createEntityManagerFactory(UNIT_NAME);

        ventaService = new Ventas(emf);
        clienteService = new Clientes(emf);
        productoService = new Productos(emf);
        descuentoService = new Descuentos(emf);

        inTransactionExecute((em) -> {
            Categoria categoria = new Categoria("Ropa deportiva");
            Marca marca = new Marca("Nike");

            em.persist(categoria);
            em.persist(marca);
        });

        clienteService.agregarCliente("Alexis", "Gómez", "42456256", "ralexisge@gmail.com");

        productoService.crearProducto("777", "Remera Crossfit", 15000, 1L, 2L);
        productoService.crearProducto("555", "Pantalon corto", 11000, 1L, 2L);

        clienteService.agregarTarjeta(3L, "34595465465454", "Visa");

        descuentoService.crearDescuentoSobreTotal("Visa", LocalDate.now().minusDays(2), LocalDate.now().plusDays(2), 0.08);
        descuentoService.crearDescuento("Nike", LocalDate.now().minusDays(2), LocalDate.now().plusDays(2), 0.05);
    }

    @Test
    public void verificarQueSeCreoUnaVenta() throws Exception {
        List<Producto> productos = productoService.listarProductos();

        List<Long> idsProductos = new ArrayList<>();

        productos.forEach(p -> {
            idsProductos.add(p.getId());
        });

        ventaService.realizarVenta(3L, idsProductos, 6L);

        inTransactionExecute((em) -> {
            Venta venta = em.find(Venta.class, 9L);
            assertNotNull(venta);
        });
    }

    @Test
    public void verificarQueElClienteEsInvalidoEnVenta() {
        List<Producto> productos = productoService.listarProductos();

        List<Long> idsProductos = new ArrayList<>();

        productos.forEach(p -> {
            idsProductos.add(p.getId());
        });

        RuntimeException exception = Assertions.assertThrows(RuntimeException.class, () -> {
            ventaService.realizarVenta(65L, idsProductos, 6L);
        });

        Assertions.assertTrue(exception.getMessage().contains("El cliente no existe"));
    }

    @Test
    public void varificarQueLaTarjetaNoEsValidaEnVenta() {
        List<Producto> productos = productoService.listarProductos();

        List<Long> idsProductos = new ArrayList<>();

        productos.forEach(p -> {
            idsProductos.add(p.getId());
        });

        RuntimeException exception = Assertions.assertThrows(RuntimeException.class, () -> {
            ventaService.realizarVenta(3L, idsProductos, 65L);
        });

        Assertions.assertTrue(exception.getMessage().contains("No existe la tarjeta solicitada"));
    }

    @Test
    public void verificarQueLaListaTieneProductosEnVenta() {
        List<Long> idsProductos = new ArrayList<>();

        RuntimeException exception = Assertions.assertThrows(RuntimeException.class, () -> {
            ventaService.realizarVenta(3L, idsProductos, 6L);
        });

        Assertions.assertTrue(exception.getMessage().contains("No hay productos para esta lista"));
    }

    @Test
    public void verificarMontoCorrecto() throws Exception {
        List<Producto> productos = productoService.listarProductos();

        List<Long> idsProductos = new ArrayList<>();

        productos.forEach(p -> {
            idsProductos.add(p.getId());
        });
        Assertions.assertEquals(ventaService.calcularMonto(idsProductos, 6L), 22620);
    }

    @Test
    public void verificarQueLaListaTieneProductos() {
        List<Long> idsProductos = new ArrayList<>();

        RuntimeException exception = Assertions.assertThrows(RuntimeException.class, () -> {
            ventaService.calcularMonto(idsProductos, 6L);
        });

        Assertions.assertTrue(exception.getMessage().contains("No hay productos para esta lista"));
    }

    @Test
    public void verificarQueLaTarjetaEsValidaParaLaPromocion() {
        List<Producto> productos = productoService.listarProductos();

        List<Long> idsProductos = new ArrayList<>();

        productos.forEach(p -> {
            idsProductos.add(p.getId());
        });

        RuntimeException exception = Assertions.assertThrows(RuntimeException.class, () -> {
            ventaService.calcularMonto(idsProductos, 15L);
        });

        Assertions.assertTrue(exception.getMessage().contains("La tarjeta solicitada no existe"));
    }

    @Test
    public void verificarQueLaListaDeVentasNoEsVacia() throws Exception {
        List<Producto> productos = productoService.listarProductos();

        List<Long> idsProductos = new ArrayList<>();

        productos.forEach(p -> {
            idsProductos.add(p.getId());
        });

        ventaService.realizarVenta(3L, idsProductos, 6L);

        List<Venta> ventas = ventaService.ventas();

        assertTrue(!ventas.isEmpty());
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
