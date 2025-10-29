package colectivo.secuencial;

import colectivo.dao.TramoDAO;
import colectivo.modelo.Tramo;
import colectivo.util.ArchivoUtil;
import colectivo.config.Config;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;

/**
 * DAO secuencial para tramos.
 * Usa constructor (String idLinea, String idOrigen, String idDestino, int duracion)
 */
public class TramoSecuencialDAO implements TramoDAO {

    private static final Logger logger = LogManager.getLogger(TramoSecuencialDAO.class);

    private final Map<String, Tramo> cacheTramos = new LinkedHashMap<>();
    private boolean cargado = false;

    @Override
    public Map<String, Tramo> buscarTodos() {
        if (cargado) {
            logger.debug("Tramos ya cargados, devolviendo caché.");
            return cacheTramos;
        }
        cargado = true;

        String ruta = Config.getInstance().get("ruta.tramos", "doc/tramo_PM.txt");
        logger.info("Leyendo tramos desde {}", ruta);

        List<String> lineas = ArchivoUtil.leerArchivo(ruta);
        for (String linea : lineas) {
            try {
                String[] partes = linea.split(";");
                if (partes.length >= 4) {
                    String idOrigen = partes[0].trim();
                    String idDestino = partes[1].trim();
                    int duracion = Integer.parseInt(partes[2].trim());
                    String idLinea = partes[3].trim();

                    Tramo t = new Tramo(idLinea, idOrigen, idDestino, duracion);
                    String clave = idLinea + "-" + idOrigen + "-" + idDestino;
                    cacheTramos.put(clave, t);
                } else {
                    logger.warn("Línea inválida en archivo de tramos: {}", linea);
                }
            } catch (Exception e) {
                logger.warn("Error procesando línea de tramo '{}': {}", linea, e.getMessage());
            }
        }

        logger.info("Total de tramos cargados: {}", cacheTramos.size());
        return cacheTramos;
    }
}
