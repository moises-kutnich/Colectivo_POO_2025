package colectivo.logica;

import colectivo.modelo.*;
import colectivo.servicio.DatosService;

import java.time.DayOfWeek;
import java.util.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Coordinador principal del sistema de colectivos.
 * Conecta la capa de servicio (DAO) con la lógica de cálculo.
 */
public class GestionColectivos {

    private static final Logger logger = LogManager.getLogger(GestionColectivos.class);

    private Calculo calculo;
    private final DatosService datosService;

    // 🔹 Mapa de días en español → DayOfWeek
    private static final Map<String, DayOfWeek> DIA_MAPA = new HashMap<>();
    static {
        DIA_MAPA.put("LUNES", DayOfWeek.MONDAY);
        DIA_MAPA.put("MARTES", DayOfWeek.TUESDAY);
        DIA_MAPA.put("MIÉRCOLES", DayOfWeek.WEDNESDAY);
        DIA_MAPA.put("MIERCOLES", DayOfWeek.WEDNESDAY); // Por si no tiene tilde
        DIA_MAPA.put("JUEVES", DayOfWeek.THURSDAY);
        DIA_MAPA.put("VIERNES", DayOfWeek.FRIDAY);
        DIA_MAPA.put("SÁBADO", DayOfWeek.SATURDAY);
        DIA_MAPA.put("SABADO", DayOfWeek.SATURDAY);
        DIA_MAPA.put("DOMINGO", DayOfWeek.SUNDAY);
    }

    // =========================================================
    // CONSTRUCTOR
    // =========================================================
    public GestionColectivos() {
        this.datosService = new DatosService();

        try {
            // 🔹 Carga inicial de todos los datos
            List<Tramo> tramos = datosService.cargarTramos();
            List<Parada> paradas = datosService.cargarParadas();
            List<Frecuencia> frecuencias = datosService.cargarFrecuencias();
            List<Linea> lineas = datosService.cargarLineas();

            // 🔹 Inicializa el motor de cálculo
            this.calculo = new Calculo(tramos, lineas, paradas, frecuencias);

            logger.info("Gestor de Lógica inicializado correctamente. Se cargaron {} tramos, {} paradas, {} líneas, {} frecuencias.",
                    tramos.size(), paradas.size(), lineas.size(), frecuencias.size());

        } catch (Exception e) {
            logger.fatal("Error crítico al inicializar la lógica de colectivos.", e);
            throw new RuntimeException("No se pudo inicializar la lógica del sistema.", e);
        }
    }

    // =========================================================
    // MÉTODOS PÚBLICOS DE CONSULTA
    // =========================================================

    /** Devuelve todas las líneas cargadas. */
    public List<Linea> obtenerTodasLasLineas() {
        return datosService.cargarLineas();
    }

    /** Devuelve todas las paradas disponibles. */
    public List<Parada> obtenerTodasLasParadas() {
        return datosService.cargarTodasLasParadas();
    }

    /** Devuelve las paradas correspondientes a una línea específica. */
    public List<Parada> obtenerParadasPorLinea(String idLinea) {
        return calculo.obtenerParadasPorLinea(idLinea);
    }

    /**
     * Calcula los posibles recorridos entre origen y destino, según día y hora.
     */
    public List<List<Recorrido>> realizarConsulta(Parada origen, Parada destino, String dia, String hora) {
        try {
            DayOfWeek diaSemana = DIA_MAPA.getOrDefault(
                    dia == null ? "LUNES" : dia.trim().toUpperCase(),
                    DayOfWeek.MONDAY
            );

            logger.info("Consulta solicitada: {} → {} | Día: {} | Hora: {}", 
                        origen != null ? origen.getNombre() : "null",
                        destino != null ? destino.getNombre() : "null",
                        diaSemana, hora);

            List<List<Recorrido>> resultados = calculo.calcularRecorrido(origen, destino, diaSemana, hora);

            logger.info("Consulta completada. Se encontraron {} posibles recorridos.", resultados.size());
            return resultados;

        } catch (Exception e) {
            logger.error("Error al calcular recorrido.", e);
            throw new RuntimeException("Error al realizar la consulta de recorrido.", e);
        }
    }
}
