package colectivo.aplicacion;

import javafx.application.Application;
import colectivo.interfaz.InterfazJavaFX;

// LÍNEAS DE LOG4J: Importaciones
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Clase principal del proyecto (Entry Point). 
 * Su única responsabilidad es lanzar la implementación de la UI.
 */
public class AplicacionPrincipal {
    
    // LÍNEA DE LOG4J: Instancia del Logger (para esta clase)
    private static final Logger logger = LogManager.getLogger(AplicacionPrincipal.class);
    
    public static void main(String[] args) {
        
        // USO DEL LOGGER: Registrar el inicio de la aplicación
        logger.info("El sistema de colectivos ha iniciado su secuencia de arranque.");
        
        try {
            // Lanza la aplicación JavaFX
            Application.launch(InterfazJavaFX.class, args);
            
            // USO DEL LOGGER: Registrar si el lanzamiento fue exitoso
            logger.info("Interfaz JavaFX lanzada exitosamente.");
            
        } catch (Exception e) {
            // USO DEL LOGGER: Si el lanzamiento falla, registrar el error CRÍTICO
            logger.fatal("ERROR CRÍTICO: Falló el lanzamiento de la Interfaz JavaFX.", e);
            // FATAL se usa para errores que impiden que el programa continúe
        }
    }
}