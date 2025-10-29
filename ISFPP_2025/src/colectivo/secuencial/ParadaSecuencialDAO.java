package colectivo.secuencial;

import colectivo.dao.ParadaDAO;
import colectivo.modelo.Parada;
import colectivo.util.ArchivoUtil;
import colectivo.config.Config;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;

/**
 * DAO secuencial para Parada. Devuelve Map<String, Parada>.
 */
public class ParadaSecuencialDAO implements ParadaDAO {

    private static final Logger logger = LogManager.getLogger(ParadaSecuencialDAO.class);

    private final Map<String, Parada> cacheParadas = new LinkedHashMap<>();
    private boolean cargado = false;

    @Override
    public Map<String, Parada> buscarTodos() {
        if (cargado) {
            logger.debug("Paradas ya cargadas, devolviendo caché.");
            return cacheParadas;
        }
        cargado = true;

        String ruta = Config.getInstance().get("ruta.paradas", "doc/parada_PM.txt");
        logger.info("Leyendo paradas desde {}", ruta);

        List<String> lineas = ArchivoUtil.leerArchivo(ruta);
        for (String linea : lineas) {
            try {
                String[] partes = Arrays.stream(linea.split(";"))
                        .map(String::trim)
                        .filter(s -> !s.isEmpty())
                        .toArray(String[]::new);
                if (partes.length >= 2) {
                    String id = partes[0];
                    String nombre = partes[1];
                    cacheParadas.put(id, new Parada(id, nombre));
                } else {
                    logger.warn("Línea inválida en paradas (omitida): {}", linea);
                }
            } catch (Exception e) {
                logger.warn("Error procesando línea de paradas '{}': {}", linea, e.getMessage());
            }
        }

        logger.info("Total de paradas cargadas: {}", cacheParadas.size());
        return cacheParadas;
    }
}
