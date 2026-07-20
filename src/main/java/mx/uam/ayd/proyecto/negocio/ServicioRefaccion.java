package mx.uam.ayd.proyecto.negocio;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import mx.uam.ayd.proyecto.datos.RefaccionRepository;
import mx.uam.ayd.proyecto.negocio.modelo.Refaccion;

/**
 * Servicio de negocio para la consulta de piezas de taller
 */
@Service
public class ServicioRefaccion {

    private static final Logger log = LoggerFactory.getLogger(ServicioRefaccion.class);

    private final RefaccionRepository refaccionRepository;

    @Autowired
    public ServicioRefaccion(RefaccionRepository refaccionRepository) {
        this.refaccionRepository = refaccionRepository;
    }

    public List<Refaccion> getRefaccion(){
        return refaccionRepository.findAll();
    }

    public boolean enviarDatos(Integer id, String nombre, float precio, int existencias){
        Refaccion refaccion = refaccionRepository.findById(id).orElse(null);

        if (refaccion != null){
            refaccion.setNombre(nombre);
            refaccion.setPrecio(precio);
            refaccion.setExistencia(existencias);
            refaccionRepository.save(refaccion);
            return true;
        } else{
            return false;
        }
    }
}