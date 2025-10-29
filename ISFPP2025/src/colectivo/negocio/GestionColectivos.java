package colectivo.negocio;

import colectivo.modelo.*;
import colectivo.servicios.DatosService;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * GestionColectivos tolerante a distintas formas de Calculo/Tramo/Recorrido.
 * Intenta crear Calculo con los datos (si existe constructor) o con el constructor vacío.
 * Intenta invocar calcularRecorrido probando distintas firmas.
 *
 * Devuelve siempre List<List<Recorrido>> (convierte si el método original devuelve otro tipo).
 */
public class GestionColectivos {

    private static final Logger logger = LogManager.getLogger(GestionColectivos.class);

    private final DatosService datosService;
    private final Object calculo; // instancia de Calculo (tipo dinámico)
    private final Class<?> calculoClass;

    public GestionColectivos() {
        this.datosService = new DatosService();

        // Cargar datos para intentar construir Calculo si existe constructor con parámetros
        List<Tramo> tramos = datosService.cargarTramos();
        List<Linea> lineas = datosService.cargarLineas();
        List<Parada> paradas = datosService.cargarTodasLasParadas();
        List<Frecuencia> frecuencias = datosService.cargarFrecuencias();

        // Intento de instanciar Calculo de forma flexible
        Object calcInstance = null;
        Class<?> cls = null;
        try {
            cls = Class.forName("colectivo.negocio.Calculo");
        } catch (Exception e) {
            // intenta paquete anterior si existe
            try {
                cls = Class.forName("colectivo.logica.Calculo");
            } catch (Exception ex) {
                logger.error("No se encontró la clase Calculo en paquetes esperados.", ex);
            }
        }

        if (cls != null) {
            // intentamos constructores posibles (4 lists, 3 lists, 0)
            Constructor<?>[] ctors = cls.getConstructors();
            Exception lastEx = null;
            for (Constructor<?> ctor : ctors) {
                try {
                    Class<?>[] pts = ctor.getParameterTypes();
                    if (pts.length == 4) {
                        calcInstance = ctor.newInstance(tramos, lineas, paradas, frecuencias);
                        break;
                    } else if (pts.length == 3) {
                        // try (tramos, paradas, frecuencias) or (tramos, lineas, paradas)
                        try {
                            calcInstance = ctor.newInstance(tramos, paradas, frecuencias);
                            break;
                        } catch (Exception e) {
                            calcInstance = ctor.newInstance(tramos, lineas, paradas);
                            break;
                        }
                    } else if (pts.length == 0) {
                        // default constructor
                        calcInstance = ctor.newInstance();
                        break;
                    }
                } catch (Exception e) {
                    lastEx = e;
                }
            }
            if (calcInstance == null) {
                // fallback: try newInstance from default constructor reflectively
                try {
                    calcInstance = cls.getDeclaredConstructor().newInstance();
                } catch (Exception e) {
                    logger.error("No se pudo instanciar Calculo con ninguna firma conocida.", lastEx != null ? lastEx : e);
                }
            }
        }

        this.calculo = calcInstance;
        this.calculoClass = (calcInstance != null) ? calcInstance.getClass() : null;

        logger.info("Gestor inicializado. Calculo disponible: {}", calculoClass != null ? calculoClass.getName() : "NO");
    }

    public List<Linea> obtenerLineas() {
        return datosService.cargarLineas();
    }

    public List<Parada> obtenerParadas() {
        return datosService.cargarTodasLasParadas();
    }

    public List<Parada> obtenerParadasPorLinea(String idLinea) {
        // Delegar a Calculo si tiene el método, sino derivar desde DatosService
        try {
            if (calculoClass != null) {
                Method m = null;
                try {
                    m = calculoClass.getMethod("obtenerParadasPorLinea", String.class);
                } catch (NoSuchMethodException ignored) {}
                if (m != null) {
                    Object r = m.invoke(calculo, idLinea);
                    if (r instanceof List) return (List<Parada>) r;
                }
            }
        } catch (Exception e) {
            logger.debug("No se pudo delegar obtenerParadasPorLinea a Calculo: {}", e.getMessage());
        }
        // fallback: derivar desde tramos
        List<Tramo> tramos = datosService.cargarTramos();
        Set<String> seen = new LinkedHashSet<>();
        List<Parada> resultado = new ArrayList<>();
        List<Parada> todas = datosService.cargarTodasLasParadas();
        for (Tramo t : tramos) {
            String idO = safeGetTramoIdParadaOrigen(t);
            String idD = safeGetTramoIdParadaDestino(t);
            if (safeGetTramoIdLinea(t) == null || !safeGetTramoIdLinea(t).equals(idLinea)) continue;
            if (idO != null && !seen.contains(idO)) {
                Parada p = buscarParadaPorId(todas, idO);
                if (p != null) { resultado.add(p); seen.add(idO); }
            }
            if (idD != null && !seen.contains(idD)) {
                Parada p = buscarParadaPorId(todas, idD);
                if (p != null) { resultado.add(p); seen.add(idD); }
            }
        }
        return resultado;
    }

    /**
     * Método público que la interfaz llama. Intenta invocar distintos
     * calcularRecorrido(...) disponibles en la clase Calculo.
     * Devuelve siempre List<List<Recorrido>> (conversión si es necesario).
     */
    public List<List<Recorrido>> realizarConsulta(Parada origen, Parada destino, String dia, String hora) {
        if (calculo == null) {
            throw new IllegalStateException("La lógica de cálculo no está disponible.");
        }

        // Preparar argumentos comunes
        Object[] possibleArgs;
        DayOfWeek diaDW = null;
        try {
            diaDW = DayOfWeek.valueOf(dia.trim().toUpperCase());
        } catch (Exception ignored) {}

        // Intentos de firmas (ordenadas por probabilidad)
        List<Object[]> attempts = new ArrayList<>();
        // (Parada, Parada, String dia, String hora)
        attempts.add(new Object[]{origen, destino, dia, hora});
        // (Parada, Parada, DayOfWeek, String)
        attempts.add(new Object[]{origen, destino, diaDW, hora});
        // (Parada, Parada, String dia, LocalTime)
        LocalTime lt = null;
        try { lt = LocalTime.parse(hora); } catch (Exception ignored) {}
        if (lt != null) attempts.add(new Object[]{origen, destino, dia, lt});
        // (Parada, Parada, int diaInt, String hora)
        int diaInt = mapDiaToInt(dia);
        attempts.add(new Object[]{origen, destino, diaInt, hora});

        // Otros intentos menos comunes: (String origenId, String destinoId, ...)
        attempts.add(new Object[]{origen != null ? origen.getId() : null, destino != null ? destino.getId() : null, dia, hora});

        Exception lastEx = null;
        for (Object[] args : attempts) {
            try {
                Method method = findMethodByNameAndArgs(calculoClass, "calcularRecorrido", args);
                if (method != null) {
                    Object res = method.invoke(calculo, args);
                    List<List<Recorrido>> converted = convertirAListaRecorridos(res);
                    if (converted != null) return converted;
                }
            } catch (Exception e) {
                lastEx = e;
                logger.debug("Intento calcularRecorrido con firma {} falló: {}", Arrays.toString(argTypes(args)), e.getMessage());
            }
        }

        // Si llegamos acá, no se pudo invocar con ninguna firma conocida
        logger.error("No se pudo invocar calcularRecorrido con ninguna firma conocida. Última excepción: {}", lastEx != null ? lastEx.getMessage() : "N/A");
        throw new RuntimeException("Calculo.calcularRecorrido no disponible con firmas esperadas.", lastEx);
    }

    // ---------- Helpers --------------

    private static String[] argTypes(Object[] args) {
        return Arrays.stream(args).map(a -> a == null ? "null" : a.getClass().getSimpleName()).toArray(String[]::new);
    }

    private static int mapDiaToInt(String dia) {
        if (dia == null) return 1;
        switch (dia.trim().toUpperCase()) {
            case "SABADO": case "SÁBADO": return 6;
            case "DOMINGO": return 7;
            default: return 1;
        }
    }

    private Method findMethodByNameAndArgs(Class<?> cls, String name, Object[] args) {
        Method[] methods = cls.getMethods();
        for (Method m : methods) {
            if (!m.getName().equals(name)) continue;
            Class<?>[] pts = m.getParameterTypes();
            if (pts.length != args.length) continue;
            boolean ok = true;
            for (int i = 0; i < pts.length; i++) {
                Object a = args[i];
                if (a == null) continue; // permitimos nulls
                if (!wrapPrimitive(pts[i]).isAssignableFrom(a.getClass())) { ok = false; break; }
            }
            if (ok) return m;
        }
        return null;
    }

    private static Class<?> wrapPrimitive(Class<?> c) {
        if (!c.isPrimitive()) return c;
        if (c == int.class) return Integer.class;
        if (c == long.class) return Long.class;
        if (c == boolean.class) return Boolean.class;
        if (c == double.class) return Double.class;
        if (c == float.class) return Float.class;
        if (c == short.class) return Short.class;
        if (c == byte.class) return Byte.class;
        if (c == char.class) return Character.class;
        return c;
    }

    /**
     * Convierte el resultado de la invocación en List<List<Recorrido>> si es posible.
     * - Si ya es List<List<Recorrido>> devuelve directamente.
     * - Si es List<Recorrido> lo envuelve en listas singleton.
     * - Si es List<Tramo> convierte cada Tramo en Recorrido mínimo (sin horas exactas).
     */
    @SuppressWarnings("unchecked")
    private List<List<Recorrido>> convertirAListaRecorridos(Object res) {
        if (res == null) return Collections.emptyList();
        if (res instanceof List) {
            List<?> list = (List<?>) res;
            if (list.isEmpty()) return new ArrayList<>();
            Object first = list.get(0);
            // ya es lista de listas?
            if (first instanceof List) {
                return (List<List<Recorrido>>) res;
            }
            // es lista de Recorrido?
            if (first instanceof Recorrido) {
                List<List<Recorrido>> out = new ArrayList<>();
                for (Object o : list) out.add(Collections.singletonList((Recorrido) o));
                return out;
            }
            // es lista de Tramo?
            if (first instanceof Tramo) {
                List<List<Recorrido>> out = new ArrayList<>();
                for (Object o : list) {
                    Tramo t = (Tramo) o;
                    String idLinea = safeGetTramoIdLinea(t);
                    int dur = safeGetTramoDuracion(t);
                    Recorrido r = new Recorrido(idLinea != null ? idLinea : "?", "??:??", dur, "??:??", "Directo");
                    out.add(Collections.singletonList(r));
                }
                return out;
            }
        }
        return null;
    }

    // Métodos seguros para extraer info de Tramo (soporta distintas implementaciones)
    private static String safeGetTramoIdParadaOrigen(Tramo t) {
        try {
            // try getIdParadaOrigen
            Method m = t.getClass().getMethod("getIdParadaOrigen");
            Object o = m.invoke(t);
            return o != null ? o.toString() : null;
        } catch (Exception ignored) {}
        try {
            Method m = t.getClass().getMethod("getOrigen");
            Object o = m.invoke(t);
            if (o instanceof Parada) return ((Parada) o).getId();
            return o != null ? o.toString() : null;
        } catch (Exception ignored) {}
        return null;
    }

    private static String safeGetTramoIdParadaDestino(Tramo t) {
        try {
            Method m = t.getClass().getMethod("getIdParadaDestino");
            Object o = m.invoke(t);
            return o != null ? o.toString() : null;
        } catch (Exception ignored) {}
        try {
            Method m = t.getClass().getMethod("getDestino");
            Object o = m.invoke(t);
            if (o instanceof Parada) return ((Parada) o).getId();
            return o != null ? o.toString() : null;
        } catch (Exception ignored) {}
        return null;
    }

    private static String safeGetTramoIdLinea(Tramo t) {
        try {
            Method m = t.getClass().getMethod("getIdLinea");
            Object o = m.invoke(t);
            return o != null ? o.toString() : null;
        } catch (Exception ignored) {}
        try {
            Method m = t.getClass().getMethod("getLinea");
            Object o = m.invoke(t);
            if (o instanceof Linea) return ((Linea) o).getId();
            return o != null ? o.toString() : null;
        } catch (Exception ignored) {}
        return null;
    }

    private static int safeGetTramoDuracion(Tramo t) {
        try {
            Method m = t.getClass().getMethod("getDuracion");
            Object o = m.invoke(t);
            if (o instanceof Number) return ((Number) o).intValue();
            return Integer.parseInt(o.toString());
        } catch (Exception ignored) {}
        return 0;
    }

    private Parada buscarParadaPorId(List<Parada> lista, String id) {
        if (id == null) return null;
        for (Parada p : lista) if (id.equals(p.getId())) return p;
        return null;
    }
}
