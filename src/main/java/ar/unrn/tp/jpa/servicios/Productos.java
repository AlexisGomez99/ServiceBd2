package ar.unrn.tp.jpa.servicios;

import ar.unrn.tp.api.ProductoService;
import ar.unrn.tp.excepciones.NotNullException;
import ar.unrn.tp.modelo.Categoria;
import ar.unrn.tp.modelo.Cliente;
import ar.unrn.tp.modelo.Marca;
import ar.unrn.tp.modelo.Producto;
import org.junit.jupiter.api.AfterAll;

import javax.persistence.*;
import java.util.List;
import java.util.function.Consumer;

public class Productos implements ProductoService {

    private final EntityManagerFactory emf;

    public Productos() {
        emf = Persistence.createEntityManagerFactory("objectdb:myDbTestFile.tmp;drop");
    }


    @Override
    public void crearProducto(String codigo, String descripcion, double precio, Long IdCategoría, Long IdMarca) {
        inTransactionExecute((em) -> {
            Categoria categoria = em.getReference(Categoria.class,IdCategoría);
            Marca marca = em.getReference(Marca.class,IdMarca);
            Producto producto= null;
            try {
                producto = new Producto(codigo,descripcion,precio, categoria,marca);
            } catch (NotNullException e) {
                throw new RuntimeException(e);
            }

            em.persist(producto);
        });
    }

    @Override
    public void modificarProducto(Long idProducto, String codigo, String descripcion, double precio, Long IdCategoría, Long IdMarca) {
        inTransactionExecute((em) -> {

            Producto producto = em.getReference(Producto.class, idProducto);

            if (!producto.getCodigo().equalsIgnoreCase(codigo) && codigo != null)
                producto.setCodigo(codigo);
            if (!producto.getDescripcion().equalsIgnoreCase(descripcion) && descripcion != null)
                producto.setDescripcion(descripcion);
            if ( producto.getPrecio() == precio && precio >0)
                producto.setPrecio(precio);

            Categoria categoria= em.getReference(Categoria.class,IdCategoría);
            if (!categoria.equals(producto.getCategoria()) && IdCategoría !=null){
                producto.setCategoria(categoria);
            }

            Marca marca= em.getReference(Marca.class,IdMarca);
            if (!marca.equals(producto.getCategoria()) && IdCategoría !=null){
                producto.setMarca(marca);
            }

            em.persist(producto);
        });
    }

    @Override
    public List listarProductos() {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        List<Producto> productos;
        tx.begin();
        TypedQuery<Producto> q = em.createQuery("select p from Producto p", Producto.class);
        productos = q.getResultList();
        tx.commit();
        return productos;
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
