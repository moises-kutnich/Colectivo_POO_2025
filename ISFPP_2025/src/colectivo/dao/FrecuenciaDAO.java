package colectivo.dao;

import java.util.List;
import colectivo.modelo.Frecuencia;

/**
 * Interface DAO para manejar acceso a datos de Frecuencia.
 */
public interface FrecuenciaDAO {
    List<Frecuencia> findAll();
}