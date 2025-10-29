package colectivo.secuencial;

import colectivo.dao.DAOFactory;
import colectivo.dao.TramoDAO;
import colectivo.dao.ParadaDAO;
import colectivo.dao.LineaDAO;

/**
 * Fábrica de DAOs para archivos de texto (secuencial).
 */
public class TextDAOFactory extends DAOFactory {

    @Override
    public TramoDAO getTramoDAO() {
        return new TramoSecuencialDAO();
    }

    @Override
    public ParadaDAO getParadaDAO() {
        return new ParadaSecuencialDAO();
    }

    @Override
    public LineaDAO getLineaDAO() {
        return new LineaSecuencialDAO();
    }
}
