package ar.unrn.tp.modelo;

import ar.unrn.tp.excepciones.EmailException;
import ar.unrn.tp.excepciones.NotNullException;
import ar.unrn.tp.excepciones.NotNumException;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Getter
@Setter
@Entity
public class Cliente {
    @Id
    @GeneratedValue
    private Long id;
    private String nombre;
    private String apellido;
    private String dni;
    private String email;
    @OneToMany(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    private List<Tarjeta> tarjetas;

    public Cliente(String nombre, String apellido, String dni, String email) throws NotNullException, NotNumException, EmailException {
        Pattern pattern = Pattern.compile("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");
        if (nombre == null)
            throw new NotNullException("nombre");
        if (apellido == null)
            throw new NotNullException("apellido");
        if (email == null)
            throw new NotNullException("email");
        if (dni == null)
            throw new NotNullException("DNI");
        if (!isNumeric(dni))
            throw new NotNumException();


        Matcher mather = pattern.matcher(email);
        if (mather.find() == false)
            throw new EmailException();
        this.nombre = nombre;
        this.apellido = apellido;
        this.dni = dni;
        this.email = email;
    }
    public void agregarTarjeta(Tarjeta tarjeta){
        this.tarjetas.add(tarjeta);
    }

    public Tarjeta obtenerTarjeta(int index){
        return tarjetas.get(index);
    }
    private static boolean isNumeric(String cadena){
        try {
            Integer.parseInt(cadena);
            return true;
        } catch (NumberFormatException nfe){
            return false;
        }
    }
    public void setTarjeta(Tarjeta tarjeta) {
        if(tarjetas!=null) {
            agregarTarjeta(tarjeta);
        }else {
         this.tarjetas = new ArrayList<>();
         agregarTarjeta(tarjeta);
        }
    }
}
