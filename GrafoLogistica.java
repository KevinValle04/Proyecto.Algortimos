import java.util.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * Grafo Dirigido Ponderado implementado con Lista de Adyacencia.
 * Modela la red de Centros de Distribución y rutas de transporte.
 */
public class GrafoLogistica {
    // Mapa: { Origen -> Lista de Aristas (Destino, Costo) }
    private Map<String, List<Arista>> listaAdyacencia;

    public GrafoLogistica() {
        this.listaAdyacencia = new HashMap<>();
    }

    /** Agrega un nodo al grafo si no existe. */
    public void agregarNodo(String nombre) {
        listaAdyacencia.putIfAbsent(nombre, new LinkedList<>());
    }

    /** Agrega una arista dirigida y ponderada. */
    public void agregarArista(String origen, String destino, double costo) {
        agregarNodo(origen);
        agregarNodo(destino);
        listaAdyacencia.get(origen).add(new Arista(destino, costo));
    }

    /** Obtiene la lista de adyacencia completa. */
    public Map<String, List<Arista>> getListaAdyacencia() {
        return listaAdyacencia;
    }

    /** Carga la estructura del grafo desde un archivo CSV. */
    public void cargarDesdeCSV(String rutaArchivo) {
        System.out.println("\n-> Cargando Grafo de Logística desde: " + rutaArchivo + "...");
        String linea;
        String separadorCSV = ",";

        try (BufferedReader br = new BufferedReader(new FileReader(rutaArchivo))) {
            br.readLine(); // Saltar cabecera
            while ((linea = br.readLine()) != null) {
                String[] datos = linea.split(separadorCSV);
                if (datos.length >= 3) {
                    agregarArista(datos[0].trim(), datos[1].trim(), Double.parseDouble(datos[2].trim()));
                }
            }
            System.out.println("-> Carga de Grafo completada. " + listaAdyacencia.size() + " Nodos cargados.");
        } catch (Exception e) {
            throw new RuntimeException("ERROR al cargar Grafo desde CSV: " + e.getMessage());
        }
    }
}