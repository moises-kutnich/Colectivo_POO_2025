package colectivo.conexion;

/**
 * // NUEVO - Clase de conexión genérica
 * Esta versión es una base para cuando se implemente la conexión a base de datos (PostgreSQL)
 * En la segunda entrega no hace nada, pero deja la estructura lista.
 */
public class Conexion {

    private static Conexion instance;

    // Constructor privado → patrón Singleton
    private Conexion() {
        // En esta entrega no hay conexión real (solo estructura)
    }

    public static synchronized Conexion getInstance() {
        if (instance == null) {
            instance = new Conexion();
        }
        return instance;
    }

    // Método simulado (solo imprime, por ahora)
    public void conectar() {
        System.out.println("Simulación de conexión (sin BD).");
    }

    public void desconectar() {
        System.out.println("Desconexión simulada.");
    }
}
