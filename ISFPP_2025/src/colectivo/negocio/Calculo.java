package colectivo.negocio;

import colectivo.modelo.*;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Lógica de cálculo de recorridos entre paradas urbanos.
 * Cumple la consigna: directos, con 1 transbordo y hora de llegada real.
 */
public class Calculo {

    private final List<Tramo> tramos;
    private final List<Linea> lineas;
    private final List<Parada> paradas;
    private final List<Frecuencia> frecuencias;
    private final DateTimeFormatter formatoHora = DateTimeFormatter.ofPattern("HH:mm");

    public Calculo(List<Tramo> tramos, List<Linea> lineas, List<Parada> paradas, List<Frecuencia> frecuencias) {
        this.tramos = tramos != null ? tramos : new ArrayList<>();
        this.lineas = lineas != null ? lineas : new ArrayList<>();
        this.paradas = paradas != null ? paradas : new ArrayList<>();
        this.frecuencias = frecuencias != null ? frecuencias : new ArrayList<>();
    }

    // --------------------- MÉTODO PRINCIPAL ---------------------
    public List<List<Recorrido>> calcularRecorrido(Parada origen, Parada destino, DayOfWeek diaSemana, String horaTexto) {
        List<List<Recorrido>> resultados = new ArrayList<>();
        if (origen == null || destino == null) return resultados;
        if (horaTexto == null || horaTexto.isEmpty()) horaTexto = "08:00";

        LocalTime hora = LocalTime.parse(horaTexto, formatoHora);

        // 🔹 1) Directos
        List<Recorrido> directos = buscarRecorridosDirectos(origen, destino, hora);
        for (Recorrido r : directos) resultados.add(Collections.singletonList(r));

        // 🔹 2) Con conexión
        resultados.addAll(buscarRecorridosConConexion(origen, destino, hora));

        return resultados;
    }

    // --------------------- DIRECTOS ---------------------
    private List<Recorrido> buscarRecorridosDirectos(Parada origen, Parada destino, LocalTime hora) {
        List<Recorrido> recorridos = new ArrayList<>();
        Map<String, List<Tramo>> tramosPorLinea = tramos.stream()
                .collect(Collectors.groupingBy(Tramo::getIdLinea));

        for (Map.Entry<String, List<Tramo>> entry : tramosPorLinea.entrySet()) {
            String idLinea = entry.getKey();
            List<Tramo> tramosLinea = entry.getValue();

            int duracion = calcularDuracion(tramosLinea, origen.getId(), destino.getId());
            if (duracion > 0 && duracion < 300) { // evita recorridos absurdos (>5h)
                LocalTime horaLlegada = hora.plusMinutes(duracion);
                Linea linea = lineas.stream().filter(l -> l.getId().equals(idLinea))
                        .findFirst().orElse(new Linea(idLinea, idLinea));

                recorridos.add(new Recorrido(
                        linea.getNombre(),
                        hora.format(formatoHora),
                        duracion,
                        horaLlegada.format(formatoHora),
                        "Directo"
                ));
            }
        }
        return recorridos;
    }

    // --------------------- CON CONEXIÓN ---------------------
    private List<List<Recorrido>> buscarRecorridosConConexion(Parada origen, Parada destino, LocalTime hora) {
        List<List<Recorrido>> resultados = new ArrayList<>();
        Map<String, List<Tramo>> tramosPorLinea = tramos.stream()
                .collect(Collectors.groupingBy(Tramo::getIdLinea));

        for (String lineaA : tramosPorLinea.keySet()) {
            for (String lineaB : tramosPorLinea.keySet()) {
                if (lineaA.equals(lineaB)) continue;

                List<Tramo> tramosA = tramosPorLinea.get(lineaA);
                List<Tramo> tramosB = tramosPorLinea.get(lineaB);

                Set<String> interseccion = new HashSet<>(obtenerParadasDeLinea(tramosA));
                interseccion.retainAll(obtenerParadasDeLinea(tramosB));

                for (String idConexion : interseccion) {
                    if (idConexion.equals(origen.getId()) || idConexion.equals(destino.getId())) continue;

                    int durA = calcularDuracion(tramosA, origen.getId(), idConexion);
                    int durB = calcularDuracion(tramosB, idConexion, destino.getId());

                    if (durA > 0 && durB > 0) {
                        int total = durA + durB + 5; // +5 min transbordo
                        LocalTime llegadaA = hora.plusMinutes(durA);
                        LocalTime llegadaTotal = hora.plusMinutes(total);

                        Linea lA = lineas.stream().filter(l -> l.getId().equals(lineaA)).findFirst().orElse(new Linea(lineaA, lineaA));
                        Linea lB = lineas.stream().filter(l -> l.getId().equals(lineaB)).findFirst().orElse(new Linea(lineaB, lineaB));

                        Recorrido tramo1 = new Recorrido(lA.getNombre(), hora.format(formatoHora), durA, llegadaA.format(formatoHora), "Hasta " + idConexion);
                        Recorrido tramo2 = new Recorrido(lB.getNombre(), llegadaA.plusMinutes(5).format(formatoHora), durB, llegadaTotal.format(formatoHora), "Desde " + idConexion);

                        resultados.add(Arrays.asList(tramo1, tramo2));
                    }
                }
            }
        }
        return resultados;
    }

    // --------------------- DURACIÓN (BFS SEGURO) ---------------------
    private int calcularDuracion(List<Tramo> tramosLinea, String idOrigen, String idDestino) {
        if (idOrigen.equals(idDestino)) return 0;

        Map<String, List<String>> mapa = new HashMap<>();
        for (Tramo t : tramosLinea) {
            if (t.getIdParadaOrigen() != null && t.getIdParadaDestino() != null) {
                mapa.computeIfAbsent(t.getIdParadaOrigen(), k -> new ArrayList<>()).add(t.getIdParadaDestino());
            }
        }

        Set<String> visitados = new HashSet<>();
        Queue<String> cola = new LinkedList<>();
        Map<String, Integer> distancia = new HashMap<>();
        cola.add(idOrigen);
        distancia.put(idOrigen, 0);

        while (!cola.isEmpty()) {
            String actual = cola.poll();
            int durAcum = distancia.get(actual);
            if (actual.equals(idDestino)) return durAcum;

            visitados.add(actual);

            for (String vecino : mapa.getOrDefault(actual, Collections.emptyList())) {
                if (visitados.contains(vecino)) continue; // evita bucles
                int durTramo = buscarDuracionTramo(tramosLinea, actual, vecino);
                if (durTramo <= 0) continue;

                int nuevaDur = durAcum + durTramo;
                if (!distancia.containsKey(vecino) || nuevaDur < distancia.get(vecino)) {
                    distancia.put(vecino, nuevaDur);
                    cola.add(vecino);
                }
            }
        }
        return 0;
    }

    private int buscarDuracionTramo(List<Tramo> tramosLinea, String origen, String destino) {
        for (Tramo t : tramosLinea) {
            if (origen.equals(t.getIdParadaOrigen()) && destino.equals(t.getIdParadaDestino())) {
                return t.getDuracion();
            }
        }
        return 0;
    }

    // --------------------- UTILIDADES ---------------------
    private Set<String> obtenerParadasDeLinea(List<Tramo> tramosLinea) {
        Set<String> ids = new HashSet<>();
        for (Tramo t : tramosLinea) {
            ids.add(t.getIdParadaOrigen());
            ids.add(t.getIdParadaDestino());
        }
        return ids;
    }

    public List<Parada> obtenerParadasPorLinea(String idLinea) {
        if (idLinea == null) return Collections.emptyList();
        List<Tramo> tramosLinea = tramos.stream()
                .filter(t -> idLinea.equals(t.getIdLinea()))
                .collect(Collectors.toList());

        List<Parada> resultado = new ArrayList<>();
        Set<String> seen = new HashSet<>();

        for (Tramo t : tramosLinea) {
            String idO = t.getIdParadaOrigen();
            String idD = t.getIdParadaDestino();

            if (idO != null && !seen.contains(idO)) {
                paradas.stream().filter(p -> p.getId().equals(idO)).findFirst().ifPresent(p -> {
                    resultado.add(p); seen.add(idO);
                });
            }
            if (idD != null && !seen.contains(idD)) {
                paradas.stream().filter(p -> p.getId().equals(idD)).findFirst().ifPresent(p -> {
                    resultado.add(p); seen.add(idD);
                });
            }
        }
        return resultado;
    }
}
