package colectivo.secuencial;

import colectivo.dao.FrecuenciaDAO;
import colectivo.modelo.Frecuencia;
import colectivo.config.Config;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.util.*;

public class FrecuenciaSecuencialDAO implements FrecuenciaDAO {
    private static final Logger logger = LogManager.getLogger(FrecuenciaSecuencialDAO.class);
    private final List<Frecuencia> cache = new ArrayList<>();
    private boolean cargado = false;

    @Override
    public List<Frecuencia> buscarTodos() {
        if (cargado) return cache;
        String ruta = Config.getInstance().get("ruta.frecuencias", "doc/frecuencia_PM.txt");
        try (BufferedReader br = new BufferedReader(new FileReader(ruta))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;
                String[] parts = Arrays.stream(line.split(";")).map(String::trim).toArray(String[]::new);
                if (parts.length < 5) {
                    logger.warn("Línea de frecuencia con menos de 5 campos (omitida): {}", line);
                    continue;
                }
                try {
                    String idLinea = parts[0];
                    String horaInicio = parts[1];
                    String horaFin = parts[2];
                    int intervalo = Integer.parseInt(parts[3]);
                    int tipoDia = Integer.parseInt(parts[4]);
                    cache.add(new Frecuencia(idLinea, horaInicio, horaFin, intervalo, tipoDia));
                } catch (Exception e) {
                    logger.warn("Línea de frecuencia malformada (omitida): {} -> {}", line, e.getMessage());
                }
            }
            cargado = true;
            logger.info("Frecuencias cargadas: {}", cache.size());
        } catch (FileNotFoundException e) {
            logger.error("Archivo frecuencias no encontrado: " + ruta, e);
        } catch (IOException e) {
            logger.error("Error leyendo frecuencias: " + ruta, e);
        }
        return cache;
    }
}
