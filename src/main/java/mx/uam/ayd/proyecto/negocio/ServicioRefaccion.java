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
    //HU-12 Recupera refaccion para mostrar en innventario
    public List<Refaccion> getRefaccion(){
        return refaccionRepository.findAll();
    }

    //HU-12 Envia refaccion editada
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

    //HU-14 Buscar refaccion para agregar a Cotizacion
    public List<Refaccion> buscarRefaccion(Integer idPieza) {
        List<Refaccion> resultado = new ArrayList<>();
    
        if (idPieza == null) {
            return resultado; 
        }
    
        // findById devuelve un Optional. Lo evaluamos para ver si existe.
        refaccionRepository.findById(idPieza).ifPresent(refaccion -> {
            resultado.add(refaccion); // Si se encontró, la metemos a la lista
        });
    
        return resultado;
    }
    


}