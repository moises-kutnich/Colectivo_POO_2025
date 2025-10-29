package colectivo.dao;

import colectivo.config.SourceType;

/**
 * Fábrica abstracta de DAOs (genérica).
 */
public abstract class DAOFactory {

    public static DAOFactory getDAOFactory(SourceType type) {
        if (type == null) return null;
        switch (type) {
            case TEXT:
                return new colectivo.secuencial.TextDAOFactory();
            case DATABASE:
            default:
                return null;
        }
    }

    // Los getters concretos los define la implementación TextDAOFactory
}
