package colectivo.servicios;

import colectivo.modelo.*;
import colectivo.secuencial.*;
import colectivo.dao.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * DatosService robusto: maneja DAOs que devuelven Map<String,Parada> y Tramo con id/origen/destino variados.
 */
public class DatosService {

    private static final Logger logger = LogManager.getLogger(DatosService.class);

    private final TramoSecuencialDAO tramoDao;
    private final FrecuenciaSecuencialDAO frecuenciaDao;
    private final ParadaSecuencialDAO paradaDao;
    private final LineaSecuencialDAO lineaDao;

    public DatosService() {
        this.tramoDao = new TramoSecuencialDAO();
        this.frecuenciaDao = new FrecuenciaSecuencialDAO();
        this.paradaDao = new ParadaSecuencialDAO();
        this.lineaDao = new LineaSecuencialDAO();
    }

    private String ruta(String nombreArchivo) {
        return Paths.get(System.getProperty("user.dir"), "doc", nombreArchivo).toString();
    }

    public List<Tramo> cargarTramos() {
        try {
            Map<String, Tramo> mapa = tramoDao.buscarTodos();
            logger.info("Tramos cargados: {}", mapa.size());
            return new ArrayList<>(mapa.values());
        } catch (Exception e) {
            logger.error("Error al cargar tramos", e);
            return Collections.emptyList();
        }
    }

    public List<Frecuencia> cargarFrecuencias() {
        try {
            List<Frecuencia> lista = frecuenciaDao.buscarTodos();
            logger.info("Frecuencias cargadas: {}", lista.size());
            return lista;
        } catch (Exception e) {
            logger.error("Error al cargar frecuencias", e);
            return Collections.emptyList();
        }
    }

    /**
     * Devuelve paradas leídas directamente del DAO (mapa de id->Parada).
     */
    public List<Parada> cargarParadas() {
        try {
            Map<String, Parada> mapa = paradaDao.buscarTodos();
            logger.info("Paradas (DAO) cargadas: {}", mapa.size());
            return new ArrayList<>(mapa.values());
        } catch (Exception e) {
            logger.error("Error al cargar paradas desde DAO", e);
            // fallback: leer archivo básico si hace falta
            return leerParadasDesdeArchivo("parada_PM.txt");
        }
    }

    private List<Parada> leerParadasDesdeArchivo(String nombreArchivo) {
        List<Parada> lista = new ArrayList<>();
        String ruta = ruta(nombreArchivo);
        try (BufferedReader br = new BufferedReader(new FileReader(ruta))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                linea = linea.trim();
                if (linea.isEmpty()) continue;
                String[] p = linea.split(";");
                if (p.length >= 2) lista.add(new Parada(p[0].trim(), p[1].trim()));
            }
            logger.info("Paradas leídas desde archivo: {}", lista.size());
        } catch (Exception e) {
            logger.error("No se pudo leer archivo de paradas: " + ruta, e);
        }
        return lista;
    }

    /**
     * Devuelve todas las paradas combinadas: las del DAO y las referenciadas en tramos (si faltan, las crea).
     */
    public List<Parada> cargarTodasLasParadas() {
        try {
            Map<String, Parada> mapaParadas = new LinkedHashMap<>(paradaDao.buscarTodos());
            List<Tramo> tramos = cargarTramos();

            for (Tramo t : tramos) {
                // intentamos obtener objetos Parada vía reflexión segura (ver GestionColectivos helpers)
                Parada origen = extraerParadaDesdeTramo(t, mapaParadas, true);
                Parada destino = extraerParadaDesdeTramo(t, mapaParadas, false);
                if (origen != null) mapaParadas.putIfAbsent(origen.getId(), origen);
                if (destino != null) mapaParadas.putIfAbsent(destino.getId(), destino);
            }

            logger.info("Total paradas combinadas: {}", mapaParadas.size());
            return new ArrayList<>(mapaParadas.values());
        } catch (Exception e) {
            logger.error("Error al combinar paradas", e);
            return Collections.emptyList();
        }
    }

    private Parada extraerParadaDesdeTramo(Tramo t, Map<String, Parada> mapaParadas, boolean origen) {
        try {
            // 1) intentar getOrigen()/getDestino()
            String metodo = origen ? "getOrigen" : "getDestino";
            try {
                Object o = t.getClass().getMethod(metodo).invoke(t);
                if (o instanceof Parada) return (Parada) o;
                if (o != null) {
                    String id = o.toString();
                    return mapaParadas.getOrDefault(id, new Parada(id, id));
                }
            } catch (NoSuchMethodException ignored) {}

            // 2) intentar getIdParadaOrigen()/getIdParadaDestino()
            String metodoId = origen ? "getIdParadaOrigen" : "getIdParadaDestino";
            try {
                Object o = t.getClass().getMethod(metodoId).invoke(t);
                if (o != null) {
                    String id = o.toString();
                    return mapaParadas.getOrDefault(id, new Parada(id, id));
                }
            } catch (NoSuchMethodException ignored) {}

            // 3) intentar campos alternativos
            return null;
        } catch (Exception e) {
            logger.debug("No se pudo extraer parada desde tramo: {}", e.getMessage());
            return null;
        }
    }

    /**
     * Devuelve las lineas (como objetos Linea).
     * Soporta Tramo con getLinea() o getIdLinea().
     */
    public List<Linea> cargarLineas() {
        try {
            Map<String, Linea> mapa = lineaDao.buscarTodos();
            if (mapa != null && !mapa.isEmpty()) {
                return new ArrayList<>(mapa.values());
            }
            // fallback: derivar desde tramos
            List<Tramo> tramos = cargarTramos();
            List<String> ids = tramos.stream()
                    .map(t -> {
                        try {
                            // try getLinea().getId()
                            Object lineaObj = t.getClass().getMethod("getLinea").invoke(t);
                            if (lineaObj instanceof Linea) return ((Linea) lineaObj).getId();
                        } catch (Exception ignored) {}
                        try {
                            Object id = t.getClass().getMethod("getIdLinea").invoke(t);
                            return id != null ? id.toString() : null;
                        } catch (Exception ignored) {}
                        return null;
                    })
                    .filter(Objects::nonNull)
                    .distinct()
                    .collect(Collectors.toList());
            return ids.stream().map(id -> new Linea(id, id)).collect(Collectors.toList());
        } catch (Exception e) {
            logger.error("Error al cargar lineas", e);
            return Collections.emptyList();
        }
    }
}
