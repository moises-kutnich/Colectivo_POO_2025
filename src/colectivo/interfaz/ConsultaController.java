package colectivo.interfaz;

import colectivo.aplicacion.CoordinadorApp;
import colectivo.modelo.Parada;
import colectivo.modelo.Recorrido;
import colectivo.modelo.Tramo;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable; // <-- 1. IMPORTAR
import javafx.scene.control.*;
import javafx.util.StringConverter;
import javafx.application.Platform; 

import java.net.URL; // <-- 2. IMPORTAR
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

// --- 3. IMPORTAR Log4j ---
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ConsultaController implements Initializable {

    private static final Logger logger = LogManager.getLogger(ConsultaController.class);

    @FXML private ComboBox<Parada> cbOrigen;
    @FXML private ComboBox<Parada> cbDestino;
    @FXML private ChoiceBox<String> cbDiaSemana;
    @FXML private ComboBox<Integer> cbHora;
    @FXML private ComboBox<Integer> cbMinuto;
    @FXML private Button btnCalcular;
    @FXML private TableView<Recorrido> tblPreview;
    @FXML private TableColumn<Recorrido, String> colLinea;
    @FXML private TableColumn<Recorrido, String> colHoraSalida;
    @FXML private TableColumn<Recorrido, String> colDuracion;

    private CoordinadorApp coordinador;
    private MainController mainController;
    private ResourceBundle bundle; 

    public void setCoordinador(CoordinadorApp coordinador) {
        this.coordinador = coordinador;
        cargarParadas();
    }

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.bundle = resources; 
        
        cbDiaSemana.setItems(FXCollections.observableArrayList(
                bundle.getString("dia.lunes") + "(1)", 
                bundle.getString("dia.martes") + "(2)", 
                bundle.getString("dia.miercoles") + "(3)", 
                bundle.getString("dia.jueves") + "(4)", 
                bundle.getString("dia.viernes") + "(5)", 
                bundle.getString("dia.sabado") + "(6)", 
                bundle.getString("dia.domingo") + "(7)"
        ));
        
        cbDiaSemana.getSelectionModel().select(0);
        
        StringConverter<Integer> twoDigits = new StringConverter<>() {
            @Override public String toString(Integer v) { return v == null ? "" : String.format("%02d", v); }
            @Override public Integer fromString(String s) {
                try { return Integer.parseInt(s.trim()); } catch (Exception e) { return null; }
            }
        };
        ObservableList<Integer> horas = FXCollections.observableArrayList(
                IntStream.rangeClosed(1, 23).boxed().collect(Collectors.toList()));
        cbHora.setItems(horas);
        cbHora.setConverter(twoDigits);
        cbHora.getSelectionModel().select(Integer.valueOf(8));
        ObservableList<Integer> minutos = FXCollections.observableArrayList(
                IntStream.rangeClosed(0, 59).boxed().collect(Collectors.toList()));
        cbMinuto.setItems(minutos);
        cbMinuto.setConverter(twoDigits);
        cbMinuto.getSelectionModel().select(Integer.valueOf(0));

        if (colLinea != null) {
            colLinea.setCellValueFactory(c -> javafx.beans.binding.Bindings.createStringBinding(
                    () -> c.getValue().getLinea() == null ? "A PIE" : c.getValue().getLinea().getId()
            ));
        }
        if (colHoraSalida != null) {
            colHoraSalida.setCellValueFactory(c -> javafx.beans.binding.Bindings.createStringBinding(
                    () -> c.getValue().getHoraSalida() == null ? "-" : c.getValue().getHoraSalida().toString()
            ));
        }
        if (colDuracion != null) {
            colDuracion.setCellValueFactory(c -> javafx.beans.binding.Bindings.createStringBinding(
                    () -> String.valueOf(c.getValue().getDuracion())
            ));
        }

        btnCalcular.setOnAction(e -> onCalcular());
    }
    
    private void cargarParadas() {
        if (coordinador == null) return;
        Map<Integer, Parada> mapa = coordinador.getParadas();
        ObservableList<Parada> items = FXCollections.observableArrayList(
                mapa.values().stream()
                        .sorted(Comparator.comparing(p -> Integer.parseInt(p.getId()))) 
                        .collect(Collectors.toList())
        );
        cbOrigen.setItems(items);
        cbDestino.setItems(items);
        if (!items.isEmpty()) {
            cbOrigen.getSelectionModel().select(0);
            cbDestino.getSelectionModel().select(Math.min(1, items.size() - 1));
        }
    }

    private int parseDiaSemana() {
        int idx = cbDiaSemana.getSelectionModel().getSelectedIndex();
        return (idx >= 0 ? idx + 1 : 1);
    }

    private static int idAsInt(Parada p) {
        try {
            return Integer.parseInt(p.getId().trim());
        } catch (Exception ex) {
            throw new IllegalArgumentException("El id de la parada no es numérico: " + p.getId());
        }
    }

    private void onCalcular() {
        
        Parada origen = cbOrigen.getValue();
        Parada destino = cbDestino.getValue();
        if (origen == null || destino == null) {
            new Alert(Alert.AlertType.WARNING, bundle.getString("validacion.faltan.campos")).showAndWait();
            return;
        }
        Integer hSel = cbHora.getValue();
        Integer mSel = cbMinuto.getValue();
        if (hSel == null || mSel == null) {
            new Alert(Alert.AlertType.WARNING, bundle.getString("validacion.faltan.campos")).showAndWait();
            return;
        }
        
        final LocalTime hora = LocalTime.of(hSel, mSel);
        final int diaSemana = parseDiaSemana();
        final int idOrigen = idAsInt(origen);
        final int idDestino = idAsInt(destino);
        final String strOrigenId = origen.getId();

        btnCalcular.setDisable(true);
        btnCalcular.setText("Buscando..."); 
        tblPreview.getItems().clear();

        new Thread(() -> {
            
            List<List<Recorrido>> alternativas;
            try {
                alternativas = coordinador.calcularRecorrido(idOrigen, idDestino, diaSemana, hora);
            } catch (Exception e) {
                logger.error("Error durante el cálculo de recorrido", e);
                alternativas = List.of(); 
                Platform.runLater(() -> {
                    new Alert(Alert.AlertType.ERROR, bundle.getString("buscar.error.msg")).showAndWait();
                });
            }
            
            final List<List<Recorrido>> resultadosFinales = alternativas;

            Platform.runLater(() -> {
                
                if (resultadosFinales == null || resultadosFinales.isEmpty()) {
                    
                    Map<String, Tramo> tramos = coordinador.getTramos();
                    var salientes = tramos.values().stream()
                            .filter(t -> strOrigenId.equals(t.getIdParadaOrigen()) && t.getIdLinea() != null) 
                            .collect(Collectors.toList());
                    String lineas = salientes.stream().map(Tramo::getIdLinea).distinct().sorted()
                            .collect(Collectors.joining(", "));

                    new Alert(Alert.AlertType.INFORMATION,
                            bundle.getString("sin.resultados.msg") + "\n\n" +
                            "Paradas cargadas: " + (coordinador.getParadas()!=null?coordinador.getParadas().size():"-") +
                            " | Tramos cargados: " + (tramos!=null?tramos.size():"-") + "\n" +
                            "Desde la parada de origen hay " + salientes.size() + " tramos salientes.\n" +
                            (lineas.isEmpty() ? "No hay líneas saliendo del origen."
                                              : "Líneas que salen del origen: " + lineas)
                    ).showAndWait();
                    
                } else {
                    tblPreview.getItems().setAll(resultadosFinales.get(0));
                    if (mainController != null && mainController.getResultadoController() != null) {
                        mainController.getResultadoController().setResultados(resultadosFinales.get(0), hora); 
                        mainController.mostrarResultado();
                    }
                }
                
                btnCalcular.setDisable(false);
                btnCalcular.setText(bundle.getString("accion.buscar"));
            });

        }).start(); 
    }
}