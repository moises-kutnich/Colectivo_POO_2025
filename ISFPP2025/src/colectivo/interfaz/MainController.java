package colectivo.interfaz;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import colectivo.negocio.GestionColectivos;
import colectivo.util.MensajeUtil;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Controlador del menú principal.
 */
public class MainController {

    private static final Logger logger = LogManager.getLogger(MainController.class);
    private GestionColectivos gestor;

    public void setGestor(GestionColectivos gestor) {
        this.gestor = gestor;
        logger.info("Gestor de lógica inyectado en MainController.");
    }

    @FXML
    private void abrirConsulta(ActionEvent event) {
        logger.info("Usuario seleccionó 'Abrir Consulta'.");

        if (gestor == null) {
            MensajeUtil.mostrarError("Error", "No se pudo inicializar la lógica del sistema.");
            logger.error("Gestor nulo al intentar abrir consulta.");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/colectivo/interfaz/ConsultaView.fxml"));
            Parent root = loader.load();

            ConsultaController controller = loader.getController();
            controller.setGestor(gestor);

            Stage stage = new Stage();
            stage.setTitle("Consulta de Recorridos");
            stage.setScene(new Scene(root));
            stage.show();

            logger.info("Ventana de consulta abierta correctamente.");

        } catch (Exception e) {
            MensajeUtil.mostrarError("Error de interfaz", "No se pudo cargar la ventana de consulta.");
            logger.error("Error al abrir ConsultaView.fxml", e);
        }
    }
}
