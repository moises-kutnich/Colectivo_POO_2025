package colectivo.secuencial;

import colectivo.modelo.Frecuencia;
import colectivo.util.ArchivoUtil;
import colectivo.config.Config;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;

/**
 * DAO secuencial para Frecuencia.
 * Lee archivo configurado y carga solo una vez (bandera).
 *
 * Formato por línea esperado (ejemplo): idLinea;horaInicio;horaFin;intervalo;tipoDia
 */
public class FrecuenciaSecuencialDAO {

    private static final Logger logger = LogManager.getLogger(FrecuenciaSecuencialDAO.class);

    private final List<Frecuencia> cacheFrecuencias = new ArrayList<>();
    private boolean cargado = false;

    /**
     * Devuelve la lista de frecuencias. Carga desde archivo solo la primera vez.
     */
    public List<Frecuencia> buscarTodos() {
        if (cargado) {
            logger.debug("Frecuencias ya cargadas, devolviendo caché ({} items).", cacheFrecuencias.size());
            return cacheFrecuencias;
        }
        cargado = true;

        String ruta = Config.getInstance().get("ruta.frecuencias", "doc/frecuencia_PM.txt");
        logger.info("Cargando frecuencias desde {}", ruta);

        List<String> lineas = ArchivoUtil.leerArchivo(ruta);
        for (String linea : lineas) {
            try {
                String[] partes = Arrays.stream(linea.split(";"))
                        .map(String::trim)
                        .filter(s -> !s.isEmpty())
                        .toArray(String[]::new);

                if (partes.length < 5) {
                    logger.warn("Línea de frecuencia con menos de 5 campos (omitida): {}", linea);
                    continue;
                }

                String idLinea = partes[0];
                String horaInicio = partes[1];
                String horaFin = partes[2];
                int intervalo = Integer.parseInt(partes[3]);
                int tipoDia = Integer.parseInt(partes[4]);

                Frecuencia f = new Frecuencia(idLinea, horaInicio, horaFin, intervalo, tipoDia);
                cacheFrecuencias.add(f);

            } catch (NumberFormatException nfe) {
                logger.warn("Formato numérico inválido en línea de frecuencia (omitida): {} -> {}", linea, nfe.getMessage());
            } catch (Exception e) {
                logger.warn("Error procesando línea de frecuencia '{}': {}", linea, e.getMessage());
            }
        }

        logger.info("Total de frecuencias cargadas: {}", cacheFrecuencias.size());
        return cacheFrecuencias;
    }
}
