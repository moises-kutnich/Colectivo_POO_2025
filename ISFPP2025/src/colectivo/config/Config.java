package colectivo.config;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Configuración general del sistema.
 * Lee el archivo config/config.properties al iniciar el sistema.
 */
public class Config {

    private static final Logger logger = LogManager.getLogger(Config.class);
    private static Config instance;
    private final Properties props;

    private Config() throws IOException {
        props = new Properties();
        String ruta = "config/config.properties";

        try (FileInputStream fis = new FileInputStream(ruta)) {
            props.load(fis);
            logger.info("Archivo de configuración cargado correctamente desde {}", ruta);
        } catch (IOException e) {
            logger.fatal("No se pudo leer el archivo de configuración: {}", ruta);
            throw new IOException("Archivo de configuración no encontrado o ilegible.", e);
        }
    }

    public static synchronized Config getInstance() {
        if (instance == null) {
            try {
                instance = new Config();
            } catch (IOException e) {
                System.err.println("ERROR: No se pudo inicializar la configuración. El sistema se cerrará.");
                System.exit(1);
            }
        }
        return instance;
    }

    public String get(String key) {
        return props.getProperty(key);
    }

    public String get(String key, String defaultValue) {
        return props.getProperty(key, defaultValue);
    }
}
