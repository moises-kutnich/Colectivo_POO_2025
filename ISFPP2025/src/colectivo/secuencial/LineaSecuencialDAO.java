package colectivo.secuencial;

import colectivo.dao.LineaDAO;
import colectivo.modelo.Linea;
import colectivo.config.Config;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.util.*;

public class LineaSecuencialDAO implements LineaDAO {
    private static final Logger logger = LogManager.getLogger(LineaSecuencialDAO.class);
    private final Map<String, Linea> cache = new LinkedHashMap<>();
    private boolean cargado = false;

    @Override
    public Map<String, Linea> buscarTodos() {
        if (cargado) return cache;
        String ruta = Config.getInstance().get("ruta.lineas", "doc/linea_PM.txt");
        try (BufferedReader br = new BufferedReader(new FileReader(ruta))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;
                String[] parts = Arrays.stream(line.split(";")).map(String::trim).toArray(String[]::new);
                if (parts.length < 1) continue;
                String id = parts[0];
                String nombre = parts.length > 1 ? parts[1] : id;
                cache.put(id, new Linea(id, nombre));
            }
            cargado = true;
            logger.info("Lineas cargadas: {}", cache.size());
        } catch (FileNotFoundException e) {
            logger.error("Archivo lineas no encontrado: " + ruta, e);
        } catch (IOException e) {
            logger.error("Error leyendo lineas: " + ruta, e);
        }
        return cache;
    }
}
