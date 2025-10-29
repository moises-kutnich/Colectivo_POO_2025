package colectivo.servicio;

import colectivo.secuencial.TramoSecuencialDAO;
import colectivo.secuencial.FrecuenciaSecuencialDAO;
import colectivo.modelo.*;
import colectivo.util.MensajeUtil;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.FileReader;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Servicio para cargar datos desde los DAOs secuenciales.
 * Adaptado al modelo donde Tramo contiene ids (String) de linea/origen/destino.
 */
public class DatosService {

    private static final Logger logger = LogManager.getLogger(DatosService.class);

    private final TramoSecuencialDAO tramoDao;
    private final FrecuenciaSecuencialDAO frecuenciaDao;

    public DatosService() {
        this.tramoDao = new TramoSecuencialDAO();
        this.frecuenciaDao = new FrecuenciaSecuencialDAO();
    }

    /** Devuelve la ruta absoluta al archivo dentro de /doc */
    private String ruta(String nombreArchivo) {
        return Paths.get(System.getProperty("user.dir"), "doc", nombreArchivo).toString();
    }

    /** Carga todos los tramos desde el DAO (mapa clave -> Tramo) */
    public List<Tramo> cargarTramos() {
        try {
            Map<String, Tramo> mapa = tramoDao.buscarTodos();
            logger.info("Se cargaron {} tramos.", mapa.size());
            return new ArrayList<>(mapa.values());
        } catch (Exception e) {
            logger.error("Error al cargar tramos.", e);
            MensajeUtil.mostrarError("Error de carga", "No se pudieron cargar los tramos.");
            return Collections.emptyList();
        }
    }

    /** Carga todas las frecuencias desde el DAO */
    public List<Frecuencia> cargarFrecuencias() {
        try {
            List<Frecuencia> lista = frecuenciaDao.buscarTodos();
            logger.info("Se cargaron {} frecuencias.", lista.size());
            return lista;
        } catch (Exception e) {
            logger.error("Error al cargar frecuencias.", e);
            MensajeUtil.mostrarError("Error de carga", "No se pudieron cargar las frecuencias.");
            return Collections.emptyList();
        }
    }

    /**
     * Lee las paradas desde el archivo doc/parada_PM.txt (o ruta relativa)
     * Si el archivo no contiene una parada que aparece en tramos, se crea una Parada mínima con id como nombre.
     */
    public List<Parada> cargarParadas() {
        List<Parada> lista = new ArrayList<>();
        String ruta = ruta("parada_PM.txt");

        try (BufferedReader br = new BufferedReader(new FileReader(ruta))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                linea = linea.trim();
                if (linea.isEmpty()) continue;
                String[] partes = Arrays.stream(linea.split(";"))
                        .map(String::trim)
                        .filter(s -> !s.isEmpty())
                        .toArray(String[]::new);
                if (partes.length >= 2) {
                    String id = partes[0];
                    String nombre = partes[1];
                    lista.add(new Parada(id, nombre));
                } else {
                    logger.warn("Línea inválida en archivo de paradas (omitida): {}", linea);
                }
            }
            logger.info("Se leyeron {} paradas desde archivo.", lista.size());
        } catch (Exception e) {
            logger.error("No se pudo leer " + ruta, e);
            MensajeUtil.mostrarError("Error de lectura", "No se pudo leer el archivo de paradas: " + ruta);
        }
        return lista;
    }

    /**
     * Devuelve todas las paradas combinando las del archivo y las referenciadas en tramos.
     * Si una parada aparece en tramos pero no en el archivo, se crea con id como nombre.
     */
    public List<Parada> cargarTodasLasParadas() {
        try {
            // 1) Paradas desde archivo
            List<Parada> desdeArchivo = cargarParadas();
            Map<String, Parada> mapa = new LinkedHashMap<>();
            for (Parada p : desdeArchivo) {
                mapa.put(p.getId(), p);
            }

            // 2) Agregar paradas que aparecen en tramos pero no en archivo
            List<Tramo> tramos = cargarTramos();
            for (Tramo t : tramos) {
                String idO = null;
                String idD = null;
                try {
                    idO = t.getIdParadaOrigen();
                    idD = t.getIdParadaDestino();
                } catch (Exception ex) {
                    // por seguridad, ignora si Tramo no tiene esos getters
                    logger.debug("Tramo sin getters idParadaOrigen/idParadaDestino: {}", t);
                }
                if (idO != null && !mapa.containsKey(idO)) {
                    mapa.put(idO, new Parada(idO, idO)); // nombre provisional = id
                }
                if (idD != null && !mapa.containsKey(idD)) {
                    mapa.put(idD, new Parada(idD, idD));
                }
            }

            logger.info("Total paradas combinadas: {}", mapa.size());
            return new ArrayList<>(mapa.values());
        } catch (Exception e) {
            logger.error("Error al combinar paradas.", e);
            MensajeUtil.mostrarError("Error", "No se pudieron obtener las paradas.");
            return Collections.emptyList();
        }
    }

    /**
     * Crea la lista de líneas (obteniendo ids desde los tramos).
     * Genera objetos Linea simples (id = nombre si no hay nombre).
     */
    public List<Linea> cargarLineas() {
        try {
            List<Tramo> tramos = cargarTramos();
            List<String> ids = tramos.stream()
                    .map(t -> {
                        try { return t.getIdLinea(); } catch (Exception ex) { return null; }
                    })
                    .filter(Objects::nonNull)
                    .distinct()
                    .collect(Collectors.toList());

            List<Linea> lineas = ids.stream().map(id -> new Linea(id, id)).collect(Collectors.toList());
            logger.info("Total líneas creadas: {}", lineas.size());
            return lineas;
        } catch (Exception e) {
            logger.error("Error al crear líneas.", e);
            MensajeUtil.mostrarError("Error", "No se pudieron obtener las líneas.");
            return Collections.emptyList();
        }
    }
}
