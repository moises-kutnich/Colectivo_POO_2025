package colectivo.interfaz;

import colectivo.aplicacion.CoordinadorApp;
import colectivo.modelo.Parada;
import colectivo.modelo.Recorrido;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Locale; // Importar Locale
import java.util.stream.Collectors;

public class ResultadoController {

    @FXML private TableView<Recorrido> tblSegmentos;
    @FXML private TableColumn<Recorrido, String> colLinea;
    @FXML private TableColumn<Recorrido, String> colParadas;
    @FXML private TableColumn<Recorrido, String> colSalida;
    @FXML private TableColumn<Recorrido, String> colDur;
    @FXML private TableColumn<Recorrido, String> colHoraLlegada;
    @FXML private TableColumn<Recorrido, String> colEspera;
    @FXML private WebView mapView;

    private MainController mainController;
    private CoordinadorApp coordinador;
    private LocalTime horaConsulta;
    private WebEngine webViewEngine;

    private ObservableList<Recorrido> segmentosLista = FXCollections.observableArrayList();

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    public void setCoordinador(CoordinadorApp coordinador) {
        this.coordinador = coordinador;
    }

    @FXML
    private void initialize() {
        
        if (colLinea != null) {
            colLinea.setCellValueFactory(c -> Bindings.createStringBinding(
                () -> {
                    if (c.getValue().getLinea() == null) {
                        return "Caminando";
                    }
                    if (segmentosLista.size() > 1) {
                        return "Conexión (" + c.getValue().getLinea().getId() + ")";
                    } else {
                        return "Directo (" + c.getValue().getLinea().getId() + ")"; 
                    }
                }
            ));
        }

        if (colParadas != null) {
            colParadas.setCellValueFactory(c -> Bindings.createStringBinding(
                () -> {
                    List<Parada> paradas = c.getValue().getParadas();
                    if (paradas == null || paradas.size() < 2) return "";

                    StringBuilder sb = new StringBuilder();
                    for (int i = 0; i < paradas.size() - 1; i++) {
                        sb.append(paradas.get(i).toString()); 
                        sb.append(" → ");
                        sb.append(paradas.get(i+1).toString()); 
                        
                        if (i < paradas.size() - 2) {
                            sb.append("\n"); 
                        }
                    }
                    return sb.toString();
                }
            ));
        }

        if (colSalida != null) {
            colSalida.setCellValueFactory(c -> Bindings.createStringBinding(
                    () -> c.getValue().getHoraSalida() == null ? "-" : c.getValue().getHoraSalida().toString()
            ));
        }
        
        if (colDur != null) {
            colDur.setCellValueFactory(c -> Bindings.createStringBinding(
                    () -> String.valueOf(c.getValue().getDuracion())
            ));
        }

        if (colHoraLlegada != null) {
            colHoraLlegada.setCellValueFactory(c -> Bindings.createStringBinding(
                () -> {
                    LocalTime salida = c.getValue().getHoraSalida();
                    int duracion = c.getValue().getDuracion();
                    if (salida == null) return "-";
                    return salida.plusMinutes(duracion).toString();
                }
            ));
        }
        
        if (colEspera != null) {
            colEspera.setCellValueFactory(c -> Bindings.createStringBinding(
                () -> {
                    LocalTime salida = c.getValue().getHoraSalida();
                    if (salida == null) return "-";

                    long espera;
                    int indiceFila = segmentosLista.indexOf(c.getValue());

                    if (indiceFila == 0) {
                        if (this.horaConsulta == null) return "-";
                        espera = ChronoUnit.MINUTES.between(this.horaConsulta, salida);
                        
                    } else if (indiceFila > 0) {
                        Recorrido segmentoAnterior = segmentosLista.get(indiceFila - 1);
                        LocalTime llegadaAnterior = segmentoAnterior.getHoraSalida().plusMinutes(segmentoAnterior.getDuracion());
                        espera = ChronoUnit.MINUTES.between(llegadaAnterior, salida);
                        
                    } else {
                        return "-";
                    }

                    if (espera < 0) {
                        espera = espera + 1440; 
                    }
                    
                    return String.valueOf(espera);
                }
            ));
        }
        
        tblSegmentos.setItems(segmentosLista);

        if (mapView != null) {
            webViewEngine = mapView.getEngine();
            mapView.setContextMenuEnabled(false);
        }
    }

    public void setResultados(List<Recorrido> segmentos, LocalTime horaConsulta) {
        this.horaConsulta = horaConsulta;
        
        this.segmentosLista.setAll(segmentos); 
        
        if (webViewEngine != null) {
            webViewEngine.loadContent(generarContenidoMapaHTML(segmentos));
        }
    }

    private String generarContenidoMapaHTML(List<Recorrido> segmentos) {
        StringBuilder coords = new StringBuilder();
        coords.append("[");
        if (!segmentos.isEmpty()) {
            for (Recorrido r : segmentos) {
                for (Parada p : r.getParadas()) {
                    coords.append(String.format(Locale.US, "[%f, %f],", p.getLatitud(), p.getLongitud()));
                }
            }
        }
        coords.append("]");

        return String.format("""
        <!DOCTYPE html>
        <html>
        <head>
          <meta charset="utf-8" />
          <meta name="viewport" content="width=device-width, initial-scale=1.0">
          <link rel="stylesheet" href="https://unpkg.com/leaflet@1.9.4/dist/leaflet.css" />
          <script src="https://unpkg.com/leaflet@1.9.4/dist/leaflet.js"></script>
          <style>html,body,#map{height:100%%;width:100%%;margin:0;padding:0;}</style>
        </head>
        <body>
          <div id="map"></div>
          <script>
            var coords = %s;
            var centro = (coords.length > 0 && coords[0].length > 1) ? coords[0] : [-42.7692, -65.0385];
            var map = L.map('map').setView(centro, 13);
            L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', { maxZoom: 19 }).addTo(map);
            if (coords.length > 0) {
                 L.marker(coords[0]).addTo(map).bindPopup("Salida").openPopup();
                 if (coords.length > 1) {
                     L.marker(coords[coords.length - 1]).addTo(map).bindPopup("Llegada");
                 }
                 var polyline = L.polyline(coords, {color: 'blue'}).addTo(map);
                 map.fitBounds(polyline.getBounds());
            }
          </script>
        </body>
        </html>
        """, coords.toString());
    }

    @FXML
    private void onVolver() {
        if (mainController != null) {
            mainController.mostrarConsulta();
        }
    }
}