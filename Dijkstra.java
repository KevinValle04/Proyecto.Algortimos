import java.util.*;

public class Dijkstra {

    /**
     * Implementación estándar e eficiente del Algoritmo de Dijkstra.
     * @param grafo El GrafoLogistica a analizar.
     * @param origen El nodo de inicio (CD de despacho).
     * @return Un mapa con la distancia más corta del origen a todos los demás nodos.
     */
    public static Map<String, Double> dijkstraIterativo(GrafoLogistica grafo, String origen) {
        Map<String, Double> distancias = new HashMap<>();
        PriorityQueue<EstadoRuta> pq = new PriorityQueue<>();

        // 1. Inicialización
        for (String nodo : grafo.getListaAdyacencia().keySet()) {
            distancias.put(nodo, Double.POSITIVE_INFINITY);
        }
        distancias.put(origen, 0.0);
        pq.add(new EstadoRuta(origen, 0.0));

        System.out.println("\n--- Ejecutando Dijkstra Iterativo (Origen: " + origen + ") ---");

        // 2. Proceso principal
        while (!pq.isEmpty()) {
            EstadoRuta actual = pq.poll();
            String u = actual.nodo;
            double distU = actual.distancia;

            if (distU > distancias.get(u)) {
                continue; // Ruta ya encontrada que es más corta
            }

            // 3. Relajación de aristas
            List<Arista> vecinos = grafo.getListaAdyacencia().get(u);
            if (vecinos != null) {
                for (Arista arista : vecinos) {
                    String v = arista.destino;
                    double peso = arista.costo;
                    double nuevaDistancia = distU + peso;

                    if (nuevaDistancia < distancias.getOrDefault(v, Double.POSITIVE_INFINITY)) {
                        distancias.put(v, nuevaDistancia);
                        pq.add(new EstadoRuta(v, nuevaDistancia));
                    }
                }
            }
        }
        return distancias;
    }
}