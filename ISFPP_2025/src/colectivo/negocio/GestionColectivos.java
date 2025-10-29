package colectivo.negocio;

import colectivo.modelo.*;
import colectivo.servicios.DatosService;

import java.time.LocalTime;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Clase principal de la lógica del sistema.
 * Gestiona la interacción entre la capa de datos (DAO/Servicio) y la de interfaz.
 */
public class GestionColectivos {

    private static final Logger logger = LogManager.getLogger(GestionColectivos.class);

    private final DatosService datosService;
    private final Calculo calculo;

    /**
     * Constructor: inicializa el servicio de datos y el cálculo de recorridos.
     */
    public GestionColectivos() {
        this.datosService = new DatosService();
        this.calculo = new Calculo();

        logger.info("Gestor de Lógica inicializado. Datos cargados correctamente.");
    }

    /**
     * Devuelve todas las paradas disponibles.
     */
    public List<Parada> obtenerParadas() {
        logger.debug("Solicitando lista de paradas al servicio de datos...");
        return datosService.cargarTodasLasParadas();
    }

    /**
     * Devuelve todas las líneas disponibles.
     */
    public List<Linea> obtenerLineas() {
        logger.debug("Solicitando lista de líneas al servicio de datos...");
        return datosService.cargarLineas();
    }

    /**
     * Calcula el recorrido entre dos paradas dadas, según día y hora.
     *
     * @param origen     Parada de origen
     * @param destino    Parada de destino
     * @param diaSemana  Día seleccionado (ej: "LUNES", "DOMINGO")
     * @param hora       Hora seleccionada
     * @return Lista de tramos o recorridos posibles
     */
    public List<Tramo> calcularRecorrido(Parada origen, Parada destino, String diaSemana, LocalTime hora) {
        try {
            logger.info("Iniciando cálculo de recorrido entre '{}' y '{}' para {} a las {}",
                    origen.getNombre(), destino.getNombre(), diaSemana, hora);

            List<Tramo> resultado = calculo.calcularRecorrido(origen, destino, diaSemana, hora);

            if (resultado.isEmpty()) {
                logger.warn("No se encontraron recorridos entre {} y {}.", origen.getNombre(), destino.getNombre());
            } else {
                logger.info("Cálculo completado: se encontraron {} posibles recorridos.", resultado.size());
            }

            return resultado;

        } catch (Exception e) {
            logger.error("Error al calcular recorrido entre paradas.", e);
            throw new RuntimeException("Error al calcular el recorrido: " + e.getMessage(), e);
        }
    }
}
