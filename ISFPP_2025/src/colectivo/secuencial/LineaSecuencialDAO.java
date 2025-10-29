package colectivo.secuencial;

import colectivo.dao.LineaDAO;
import colectivo.modelo.Linea;
import colectivo.util.ArchivoUtil;
import colectivo.config.Config;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;

/**
 * DAO secuencial para líneas.
 */
public class LineaSecuencialDAO implements LineaDAO {

    private static final Logger logger = LogManager.getLogger(LineaSecuencialDAO.class);

    private final Map<String, Linea> cacheLineas = new LinkedHashMap<>();
    private boolean cargado = false;

    @Override
    public Map<String, Linea> buscarTodos() {
        if (cargado) {
            logger.debug("Líneas ya cargadas, devolviendo caché.");
            return cacheLineas;
        }
        cargado = true;

        String ruta = Config.getInstance().get("ruta.lineas", "doc/linea_PM.txt");
        logger.info("Leyendo líneas desde {}", ruta);

        List<String> lineas = ArchivoUtil.leerArchivo(ruta);
        for (String linea : lineas) {
            try {
                String[] partes = linea.split(";");
                if (partes.length >= 2) {
                    String id = partes[0].trim();
                    String nombre = partes[1].trim();
                    cacheLineas.put(id, new Linea(id, nombre));
                } else {
                    logger.warn("Línea inválida en archivo de líneas: {}", linea);
                }
            } catch (Exception e) {
                logger.warn("Error procesando línea '{}': {}", linea, e.getMessage());
            }
        }

        logger.info("Total de líneas cargadas: {}", cacheLineas.size());
        return cacheLineas;
    }
}
