package colectivo.modelo;

/**
 * Entidad Linea: identifica una línea de colectivo (id y nombre/numero).
 */
public class Linea {
    private String id;      // ej "60" o "L1"
    private String nombre;  // opcional, puede repetirse con id

    public Linea(String id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    @Override
    public String toString() {
        return nombre != null && !nombre.isEmpty() ? nombre : id;
    }
}
