package colectivo.interfaz;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import colectivo.modelo.Parada;
import colectivo.modelo.Recorrido;
import colectivo.negocio.GestionColectivos;
import colectivo.util.MensajeUtil;

import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Controlador para la vista de consulta de recorridos.
 */
public class ConsultaController {

    private static final Logger logger = LogManager.getLogger(ConsultaController.class);

    @FXML private ComboBox<Parada> comboOrigen;
    @FXML private ComboBox<Parada> comboDestino;
    @FXML private ComboBox<String> comboHora;
    @FXML private ComboBox<String> comboMinuto;
    @FXML private ComboBox<String> comboDia;

    private GestionColectivos gestor;

    public void setGestor(GestionColectivos gestor) {
        this.gestor = gestor;
        inicializarControles();
    }

    @FXML
    public void initialize() {
        logger.info("Inicializando ComboBoxes de hora, minuto y día.");

        for (int i = 0; i < 24; i++) comboHora.getItems().add(String.format("%02d", i));
        for (int i = 0; i < 60; i++) comboMinuto.getItems().add(String.format("%02d", i));

        comboDia.setItems(FXCollections.observableArrayList(
                "Lunes", "Martes", "Miércoles", "Jueves", "Viernes", "Sábado", "Domingo"
        ));
        comboDia.getSelectionModel().selectFirst();
    }

    private void inicializarControles() {
        try {
            List<Parada> paradas = gestor.obtenerParadas();
            comboOrigen.setItems(FXCollections.observableArrayList(paradas));
            comboDestino.setItems(FXCollections.observableArrayList(paradas));
            logger.info("Paradas cargadas correctamente: {} disponibles.", paradas.size());
        } catch (Exception e) {
            MensajeUtil.mostrarError("Error de carga", "No se pudieron cargar las paradas.");
            logger.error("Error cargando paradas en ComboBoxes.", e);
        }
    }

    @FXML
    private void realizarConsulta() {
        logger.info("Usuario inició una consulta.");

        Parada origen = comboOrigen.getValue();
        Parada destino = comboDestino.getValue();
        String hora = comboHora.getValue();
        String minuto = comboMinuto.getValue();
        String dia = comboDia.getValue();

        if (origen == null || destino == null || hora == null || minuto == null || dia == null) {
            MensajeUtil.mostrarAdvertencia("Datos incompletos", "Por favor, complete todos los campos.");
            logger.warn("Consulta cancelada: campos incompletos.");
            return;
        }

        String horaCompleta = hora + ":" + minuto;
        try {
            List<List<Recorrido>> resultados = gestor.realizarConsulta(origen, destino, dia, horaCompleta);

            if (resultados.isEmpty()) {
                MensajeUtil.mostrarInfo("Sin resultados", "No hay recorridos disponibles.");
                logger.warn("Consulta sin resultados entre {} y {}", origen, destino);
            } else {
                MensajeUtil.mostrarInfo("Consulta exitosa",
                        "Se encontraron " + resultados.size() + " posibles recorridos.");
                logger.info("Consulta exitosa con {} resultados.", resultados.size());
            }

        } catch (Exception e) {
            MensajeUtil.mostrarError("Error", "Error durante la consulta: " + e.getMessage());
            logger.error("Excepción en realizarConsulta()", e);
        }
    }
}
