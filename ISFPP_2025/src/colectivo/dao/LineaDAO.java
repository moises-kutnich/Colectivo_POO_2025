package colectivo.dao;

import java.util.Map;
import colectivo.modelo.Linea;

/**
 * Interfaz DAO: devuelve todas las lineas en un Map (clave id -> Linea)
 */
public interface LineaDAO {
    Map<String, Linea> buscarTodos();
}
