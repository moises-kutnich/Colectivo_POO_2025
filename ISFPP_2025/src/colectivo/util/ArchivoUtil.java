package colectivo.util;

import java.io.*;
import java.util.*;

/**
 * Utilidad genérica para lectura de archivos de texto.
 */
public class ArchivoUtil {

    public static List<String> leerArchivo(String ruta) {
        List<String> lineas = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(ruta))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                if (!linea.trim().isEmpty()) {
                    lineas.add(linea);
                }
            }
        } catch (IOException e) {
            System.err.println("Error al leer el archivo: " + ruta);
        }
        return lineas;
    }
}
