package colectivo.modelo;

/**
 * Recorrido: representa una opción mostrable al usuario.
 */
public class Recorrido {
    private String linea;
    private String horaSalida; // "HH:mm"
    private int duracionMinutos;
    private String horaLlegada; // "HH:mm"
    private String conexion;

    public Recorrido(String linea, String horaSalida, int duracionMinutos, String horaLlegada, String conexion) {
        this.linea = linea;
        this.horaSalida = horaSalida;
        this.duracionMinutos = duracionMinutos;
        this.horaLlegada = horaLlegada;
        this.conexion = conexion;
    }

    public String getLinea() { return linea; }
    public String getHoraSalida() { return horaSalida; }
    public String getDuracion() { return duracionMinutos + " min"; }
    public int getDuracionMinutos() { return duracionMinutos; }
    public String getHoraLlegada() { return horaLlegada; }
    public String getConexion() { return conexion; }

    @Override
    public String toString() {
        return String.format("%s | %s → %s (%d min) [%s]", linea, horaSalida, horaLlegada, duracionMinutos, conexion);
    }
}
