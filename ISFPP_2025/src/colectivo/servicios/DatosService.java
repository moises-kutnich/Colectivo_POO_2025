package colectivo.servicios;

import colectivo.modelo.*;
import colectivo.secuencial.*;
import colectivo.dao.*;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Servicio centralizado para la carga de datos de paradas, líneas, tramos y frecuencias.
 * Se apoya en los DAOs y en el archivo de configuración del sistema (config.properties).
 */
public class DatosService {

    private static final Logger logger = LogManager.getLogger(DatosService.class);

    private final ParadaSecuencialDAO paradaDao;
    private final LineaSecuencialDAO lineaDao;
    private final TramoSecuencialDAO tramoDao;
    private final FrecuenciaSecuencialDAO frecuenciaDao;

    private final Properties config;

    /**
     * Constructor: inicializa los DAOs y carga la configuración del sistema.
     */
    public DatosService() {
        this.paradaDao = new ParadaSecuencialDAO();
        this.lineaDao = new LineaSecuencialDAO();
        this.tramoDao = new TramoSecuencialDAO();
        this.frecuenciaDao = new FrecuenciaSecuencialDAO();
        this.config = new Properties();

        cargarConfiguracion();
    }

    /**
     * Carga el archivo de configuración (config.properties).
     */
    private void cargarConfiguracion() {
        try (FileInputStream fis = new FileInputStream("config/config.properties")) {
            config.load(fis);
            logger.info("Archivo de configuración cargado correctamente.");
        } catch (IOException e) {
            logger.error("Error al cargar el archivo de configuración config.properties", e);
            throw new RuntimeException("No se pudo cargar la configuración del sistema.", e);
        }
    }

    /**
     * Carga todas las paradas desde el DAO.
     */
    public List<Parada> cargarParadas() {
        try {
            logger.debug("Cargando paradas desde archivo...");
            Map<Integer, Parada> mapa = paradaDao.buscarTodos();
            return new ArrayList<>(mapa.values());
        } catch (Exception e) {
            logger.error("Error al cargar las paradas.", e);
            return Collections.emptyList();
        }
    }

    /**
     * Carga todas las líneas desde el DAO.
     */
    public List<Linea> cargarLineas() {
        try {
            logger.debug("Cargando líneas desde archivo...");
            Map<String, Linea> mapa = lineaDao.buscarTodos();
            return new ArrayList<>(mapa.values());
        } catch (Exception e) {
            logger.error("Error al cargar las líneas.", e);
            return Collections.emptyList();
        }
    }

    /**
     * Carga todos los tramos desde el DAO.
     */
    public List<Tramo> cargarTramos() {
        try {
            logger.debug("Cargando tramos desde archivo...");
            Map<String, Tramo> mapa = tramoDao.buscarTodos();
            return new ArrayList<>(mapa.values());
        } catch (Exception e) {
            logger.error("Error al cargar los tramos.", e);
            return Collections.emptyList();
        }
    }

    /**
     * Carga todas las frecuencias desde el DAO.
     */
    public List<Frecuencia> cargarFrecuencias() {
        try {
            logger.debug("Cargando frecuencias desde archivo...");
            return frecuenciaDao.buscarTodos();
        } catch (Exception e) {
            logger.error("Error al cargar las frecuencias.", e);
            return Collections.emptyList();
        }
    }

    /**
     * Devuelve una lista unificada de todas las paradas presentes en los tramos.
     */
    public List<Parada> cargarTodasLasParadas() {
        List<Tramo> tramos = cargarTramos();
        Set<Parada> paradas = new HashSet<>();

        for (Tramo t : tramos) {
            if (t.getOrigen() != null) paradas.add(t.getOrigen());
            if (t.getDestino() != null) paradas.add(t.getDestino());
        }

        return new ArrayList<>(paradas);
    }

    /**
     * Devuelve los identificadores de líneas (IDs únicos).
     */
    public List<String> obtenerIdsDeLineas() {
        List<Tramo> tramos = cargarTramos();
        return tramos.stream()
                .map(t -> t.getLinea() != null ? t.getLinea().getId() : null)
                .filter(Objects::nonNull)
                .distinct()
                .collect(Collectors.toList());
    }
}
