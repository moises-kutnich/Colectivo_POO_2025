package colectivo.util;

/**
 * Utilidades para manejo de cadenas de texto.
 */
public class StringUtil {

    public static boolean esVacio(String texto) {
        return texto == null || texto.trim().isEmpty();
    }

    public static String capitalizar(String texto) {
        if (esVacio(texto)) return texto;
        return texto.substring(0, 1).toUpperCase() + texto.substring(1).toLowerCase();
    }

    public static String[] dividir(String texto, String separador) {
        return texto == null ? new String[0] : texto.split(separador);
    }
}

