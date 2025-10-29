package colectivo.dao;

import colectivo.config.SourceType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Fábrica genérica de DAOs.
 * Devuelve implementaciones concretas según SourceType y tipo de interfaz.
 *
 * Nota: para la entrega 3 se puede mejorar leyendo clases concretas desde config.
 */
public abstract class DAOFactory {
    private static final Logger logger = LogManager.getLogger(DAOFactory.class);

    public static DAOFactory getDAOFactory(SourceType type) {
        if (type == null) type = SourceType.TEXT;
        switch (type) {
            case TEXT:
                // fábrica concreta para archivos
                return new colectivo.secuencial.TextDAOFactory();
            case DATABASE:
                // futuro: return new colectivo.sql.SqlDAOFactory();
            default:
                logger.warn("SourceType desconocido, usando TEXT por defecto");
                return new colectivo.secuencial.TextDAOFactory();
        }
    }

    // Métodos abstractos genéricos para obtener DAO por tipo (recomendado)
    public abstract TramoDAO getTramoDAO();
    public abstract ParadaDAO getParadaDAO();
    public abstract LineaDAO getLineaDAO();
}
