package colectivo.modelo;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

/**
 * Frecuencia: frecuencia de paso para una linea en un rango horario y por tipo de día.
 * horaInicio/horaFin: formato "HH:mm"
 * intervalo: minutos entre unidades
 * tipoDia: 1=hábil,6=sábado,7=domingo/feriado
 */
public class Frecuencia {
    private String idLinea;
    private String horaInicio; // "HH:mm"
    private String horaFin;    // "HH:mm"
    private int intervalo;     // minutos
    private int tipoDia;       // 1=habil,6=sab,7=dom/feriado

    private static final DateTimeFormatter FORMATO = DateTimeFormatter.ofPattern("HH:mm");

    public Frecuencia(String idLinea, String horaInicio, String horaFin, int intervalo, int tipoDia) {
        this.idLinea = idLinea;
        this.horaInicio = horaInicio;
        this.horaFin = horaFin;
        this.intervalo = intervalo;
        this.tipoDia = tipoDia;
    }

    public String getIdLinea() { return idLinea; }
    public String getHoraInicio() { return horaInicio; }
    public String getHoraFin() { return horaFin; }
    public int getIntervalo() { return intervalo; }
    public int getTipoDia() { return tipoDia; }

    /** Devuelve LocalTime de inicio */
    public LocalTime getHoraInicioLocalTime() {
        try { return LocalTime.parse(horaInicio, FORMATO); }
        catch (Exception e) { return null; }
    }

    /** Devuelve LocalTime de fin */
    public LocalTime getHoraFinLocalTime() {
        try { return LocalTime.parse(horaFin, FORMATO); }
        catch (Exception e) { return null; }
    }

    /** Devuelve el intervalo en minutos (alias más claro) */
    public int getMinutosEntreColectivos() { return intervalo; }

    /** Indica si una hora (LocalTime) cae dentro del rango de esta frecuencia (inclusive inicio/fin). */
    public boolean incluyeHora(LocalTime hora) {
        LocalTime hi = getHoraInicioLocalTime();
        LocalTime hf = getHoraFinLocalTime();
        if (hi == null || hf == null || hora == null) return false;

        // caso rango normal (ej 06:00 - 23:00)
        if (!hf.isBefore(hi)) {
            return !hora.isBefore(hi) && !hora.isAfter(hf);
        } else {
            // caso intervalo que pasa por medianoche (ej 22:00 - 04:00)
            return !hora.isBefore(hi) || !hora.isAfter(hf);
        }
    }

    @Override
    public String toString() {
        return String.format("Frec[%s %s-%s cada %d min tipo:%d]", idLinea, horaInicio, horaFin, intervalo, tipoDia);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Frecuencia)) return false;
        Frecuencia f = (Frecuencia) o;
        return Objects.equals(idLinea, f.idLinea)
                && Objects.equals(horaInicio, f.horaInicio)
                && Objects.equals(horaFin, f.horaFin)
                && intervalo == f.intervalo
                && tipoDia == f.tipoDia;
    }

    @Override
    public int hashCode() {
        return Objects.hash(idLinea, horaInicio, horaFin, intervalo, tipoDia);
    }
}
