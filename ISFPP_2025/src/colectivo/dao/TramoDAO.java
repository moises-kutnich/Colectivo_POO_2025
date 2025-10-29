package colectivo.dao;

import java.util.Map;
import colectivo.modelo.Tramo;

public interface TramoDAO {
    /**
     * Devuelve mapa clave -> Tramo. Clave sugerida: "origen-destino" o tr.getId().
     */
    Map<String, Tramo> buscarTodos();
}
