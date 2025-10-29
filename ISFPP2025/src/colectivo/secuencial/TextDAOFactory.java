package colectivo.secuencial;

import colectivo.dao.*;

public class TextDAOFactory extends colectivo.dao.DAOFactory {

    public TramoDAO getTramoDAO() {
        return new TramoSecuencialDAO();
    }

    public ParadaDAO getParadaDAO() {
        return new ParadaSecuencialDAO();
    }

    public LineaDAO getLineaDAO() {
        return new LineaSecuencialDAO();
    }

    public FrecuenciaDAO getFrecuenciaDAO() {
        return new FrecuenciaSecuencialDAO();
    }
}
