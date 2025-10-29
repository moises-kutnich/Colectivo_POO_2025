package colectivo.interfaz;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import colectivo.logica.GestionColectivos;

// Log4j2
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Clase principal de la interfaz JavaFX.
 * Carga el archivo MainView.fxml e inyecta el gestor de lógica.
 */
public class InterfazJavaFX extends Application {

    private static final Logger logger = LogManager.getLogger(InterfazJavaFX.class);
    private GestionColectivos gestor;

    @Override
    public void init() throws Exception {
        super.init();
        logger.info("Inicializando InterfazJavaFX...");
        this.gestor = new GestionColectivos();
        logger.info("Gestor de lógica inicializado correctamente.");
    }

    @Override
    public void start(Stage primaryStage) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/colectivo/interfaz/MainView.fxml"));
            Parent root = loader.load();

            // Inyectamos el gestor al controlador principal
            MainController controller = loader.getController();
            controller.setGestor(gestor);

            Scene scene = new Scene(root);
            primaryStage.setTitle("Consultas de Colectivos Urbanos");
            primaryStage.setScene(scene);
            primaryStage.show();

            logger.info("Interfaz principal cargada y mostrada correctamente.");

        } catch (Exception e) {
            logger.fatal("Error al iniciar la interfaz gráfica.", e);
        }
    }
}
