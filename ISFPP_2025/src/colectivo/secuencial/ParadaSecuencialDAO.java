package colectivo.secuencial;

import colectivo.dao.ParadaDAO;
import colectivo.modelo.Parada;
import colectivo.config.Config;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.util.*;

public class ParadaSecuencialDAO implements ParadaDAO {
    private static final Logger logger = LogManager.getLogger(ParadaSecuencialDAO.class);
    private final Map<String, Parada> cache = new LinkedHashMap<>();
    private boolean cargado = false;

    @Override
    public Map<String, Parada> buscarTodos() {
        if (cargado) return cache;
        String ruta = Config.getInstance().get("ruta.paradas", "doc/parada_PM.txt");
        try (BufferedReader br = new BufferedReader(new FileReader(ruta))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;
                String[] parts = Arrays.stream(line.split(";")).map(String::trim).toArray(String[]::new);
                if (parts.length < 2) {
                    logger.warn("Línea de parada inválida (omitida): {}", line);
                    continue;
                }
                String id = parts[0];
                String nombre = parts[1];
                cache.put(id, new Parada(id, nombre));
            }
            cargado = true;
            logger.info("Paradas cargadas: {}", cache.size());
        } catch (FileNotFoundException e) {
            logger.error("Archivo paradas no encontrado: " + ruta, e);
        } catch (IOException e) {
            logger.error("Error leyendo paradas: " + ruta, e);
        }
        return cache;
    }
}
