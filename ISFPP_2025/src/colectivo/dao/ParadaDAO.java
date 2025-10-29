package colectivo.dao;

import java.util.Map;
import colectivo.modelo.Parada;

/**
 * Interfaz DAO de Parada: devuelve todas las paradas en un Map id->Parada.
 */
public interface ParadaDAO {
    Map<String, Parada> buscarTodos();
}
