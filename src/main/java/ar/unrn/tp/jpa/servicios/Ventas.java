package ar.unrn.tp.jpa.servicios;

import ar.unrn.tp.api.VentaService;
import ar.unrn.tp.excepciones.NotNullException;
import ar.unrn.tp.modelo.*;
import org.junit.jupiter.api.AfterEach;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
public class Ventas implements VentaService {


    private final EntityManagerFactory emf;

    public Ventas() {
        emf = Persistence.createEntityManagerFactory("objectdb:myDbTestFile.tmp;drop");
    }

    @Override
    public void realizarVenta(Long idCliente, List<Long> productos, Long idTarjeta) throws Exception {
        inTransactionExecute((em) -> {

            List<Promocion> promocions = new ArrayList<>();

            TypedQuery<PromTarjeta> t = em.createQuery("select p from PromTarjeta p", PromTarjeta.class);
            promocions.addAll(t.getResultList());
            TypedQuery<PromProducto> p = em.createQuery("select p from PromProducto p", PromProducto.class);
            promocions.addAll(p.getResultList());

            Cliente cliente = em.find(Cliente.class,idCliente);
            Tarjeta tarjeta = em.find(Tarjeta.class,idTarjeta);
            boolean existeTarjeta=false;
            if (cliente !=null && tarjeta !=null && productos != null) {
                for (Tarjeta card : cliente.getTarjetas()) {
                    if (card.getNumTarjeta().equalsIgnoreCase(tarjeta.getNumTarjeta())) {
                        existeTarjeta = true;
                        break;
                    }
                }
                if (existeTarjeta) {
                    List<Producto> productoRecuperados = new ArrayList<>();
                    for (Long i : productos) {
                        Producto prod = em.getReference(Producto.class, i);
                        productoRecuperados.add(prod);
                    }
                    Carrito carrito = new Carrito(promocions);
                    carrito.asociarTarjeta(tarjeta);
                    carrito.asociarCliente(cliente);
                    carrito.agregarProductos(productoRecuperados);
                    Venta venta;
                    try {
                        venta = carrito.comprarListado();
                        em.persist(venta);
                    } catch (NotNullException e) {
                        throw new RuntimeException(e);
                    }
                } else {
                    System.out.println("La tarjeta no pertenece al cliente");
                }
            }
        });

    }

    @Override
    public double calcularMonto(List<Long> productos, Long idTarjeta) throws Exception {
        double total=0;

        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        if (productos!=null) {
            tx.begin();
            List<Promocion> promocions = new ArrayList<>();

            TypedQuery<PromTarjeta> t = em.createQuery("select p from PromTarjeta p", PromTarjeta.class);
            promocions.addAll(t.getResultList());
            TypedQuery<PromProducto> p = em.createQuery("select p from PromProducto p", PromProducto.class);
            promocions.addAll(p.getResultList());

            Tarjeta tarjeta = em.find(Tarjeta.class, idTarjeta);
            if (tarjeta !=null) {
                List<Producto> productoRecuperados = new ArrayList<>();
                for (Long i : productos) {
                    Producto prod = em.getReference(Producto.class, i);
                    productoRecuperados.add(prod);
                }
                Carrito carrito = new Carrito(promocions);
                carrito.asociarTarjeta(tarjeta);
                carrito.agregarProductos(productoRecuperados);
                total = carrito.calcularDescuento();
            }
            tx.commit();
        }
        return total;
    }

    @Override
    public List ventas() {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        List<Venta> ventas = new ArrayList<>();

        TypedQuery<Venta> t = em.createQuery("select v from Venta v", Venta.class);
        ventas.addAll(t.getResultList());

        tx.commit();
        return ventas;
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
