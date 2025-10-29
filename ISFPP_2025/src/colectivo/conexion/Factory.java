package colectivo.conexion;

import colectivo.dao.DAOFactory;
import colectivo.config.SourceType;
import colectivo.secuencial.TextDAOFactory; // ← AGREGA ESTE IMPORT

/**
 * Clase mínima para obtener DAOFactory según tipo de origen.
 */
public class Factory {

    public static DAOFactory getFactoryFor(SourceType type) {
        if (type == null) {
            return new TextDAOFactory(); // ← ya no es necesario escribir el paquete completo
        }
        switch (type) {
            case TEXT:
                return new TextDAOFactory();
            // case DATABASE: return new SqlDAOFactory();
            default:
                return new TextDAOFactory();
        }
    }
}
