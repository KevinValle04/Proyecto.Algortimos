import java.util.*;

public class DijkstraRecursivo {

    // Conocer los visitados
    private static Set<String> visitados = new HashSet<>();

    public static Map<String, Double> dijkstraRecursivo(GrafoLogistica grafo, String origen) {
        Map<String, Double> distancias = new HashMap<>();
        visitados.clear(); // Limpiar

        // Inicializar
        for (String nodo : grafo.getListaAdyacencia().keySet()) {
            distancias.put(nodo, Double.POSITIVE_INFINITY);
        }
        distancias.put(origen, 0.0);

        System.out.println("\n--- Ejecutando Dijkstra Recursivo (Origen: " + origen + ") ---");

        procesarNodoRecursivo(grafo, distancias);
        return distancias;
    }

    private static void procesarNodoRecursivo(GrafoLogistica grafo, Map<String, Double> distancias) {
        // Buscamos el nodo con menor distancia que NO haya sido visitado aún
        String actual = encontrarNodoMinimo(distancias);

        if (actual == null) 
            return; 

        visitados.add(actual); // Marcar como visitado sin borrar su distancia

        double distU = distancias.get(actual);
        
        // Si la distancia es infinito, no se pueden llegar a mas nodos
        if (distU == Double.POSITIVE_INFINITY) return;

        // Relajación
        List<Arista> vecinos = grafo.getListaAdyacencia().get(actual);
        if (vecinos != null) {
            for (Arista arista : vecinos) {
                String v = arista.destino;
                double peso = arista.costo;
                if (!visitados.contains(v)) {
                    double nuevaDistancia = distU + peso;
                    if (nuevaDistancia < distancias.get(v)) {
                        distancias.put(v, nuevaDistancia);
                    }
                }
            }
        }

        // Llamada recursiva
        procesarNodoRecursivo(grafo, distancias);
    }

    private static String encontrarNodoMinimo(Map<String, Double> distancias) {
        double minD = Double.POSITIVE_INFINITY;
        String nodoMinimo = null;

        for (Map.Entry<String, Double> entry : distancias.entrySet()) {
            String nodo = entry.getKey();
            Double dist = entry.getValue();

            // Solo consideramos los NO visitados
            if (!visitados.contains(nodo) && dist < minD) {
                minD = dist;
                nodoMinimo = nodo;
            }
        }
        return nodoMinimo;
    }
}