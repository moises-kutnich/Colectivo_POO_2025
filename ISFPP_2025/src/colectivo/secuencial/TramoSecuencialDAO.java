package colectivo.secuencial;

import colectivo.dao.TramoDAO;
import colectivo.modelo.Tramo;
import colectivo.config.Config;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class TramoSecuencialDAO implements TramoDAO {
    private static final Logger logger = LogManager.getLogger(TramoSecuencialDAO.class);
    private final Map<String, Tramo> cache = new LinkedHashMap<>();
    private boolean cargado = false;

    @Override
    public Map<String, Tramo> buscarTodos() {
        if (cargado) return cache;
        String ruta = Config.getInstance().get("ruta.tramos", "doc/tramo_PM.txt");

        try (BufferedReader br = new BufferedReader(new FileReader(ruta))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;
                String[] parts = Arrays.stream(line.split(";")).map(String::trim).filter(s -> !s.isEmpty()).toArray(String[]::new);
                if (parts.length < 4) {
                    logger.warn("Línea de tramo inválida (omitida): {}", line);
                    continue;
                }
                try {
                    String idOrigen = parts[0];
                    String idDestino = parts[1];
                    int duracion = Integer.parseInt(parts[2]);
                    String idLinea = parts[3];
                    Tramo t = new Tramo(idLinea, idOrigen, idDestino, duracion);
                    cache.put(t.getId(), t);
                } catch (NumberFormatException e) {
                    logger.warn("Duración inválida en tramo (omitido): {}", line);
                }
            }
            cargado = true;
            logger.info("Tramos cargados: {}", cache.size());
        } catch (FileNotFoundException e) {
            logger.error("Archivo tramos no encontrado: " + ruta, e);
        } catch (IOException e) {
            logger.error("Error leyendo tramos: " + ruta, e);
        }

        // retorno del map (clave: t.getId() ya es "linea-origen-destino")
        return cache;
    }
}
