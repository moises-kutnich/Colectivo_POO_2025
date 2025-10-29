package colectivo.dao;

import java.util.Map;
import colectivo.modelo.Tramo;

/**
 * Interfaz DAO: devuelve todos los tramos en un Map clave "origen-destino" -> Tramo
 */
public interface TramoDAO {
    Map<String, Tramo> buscarTodos();
}
