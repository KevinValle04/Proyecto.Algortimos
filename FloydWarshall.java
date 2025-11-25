import java.util.*;

public class FloydWarshall {

    /**
     * Implementación de Floyd-Warshall para encontrar el camino más corto entre todos los pares.
     * @param grafo El GrafoLogistica.
     * @return La matriz de distancias mínimas (Map<String, Map<String, Double>>).
     */
    public static Map<String, Map<String, Double>> floydWarshall(GrafoLogistica grafo) {
        Set<String> nodos = grafo.getListaAdyacencia().keySet();
        Map<String, Map<String, Double>> dist = new HashMap<>();

        // 1. Inicialización de la matriz de distancias
        for (String u : nodos) {
            dist.put(u, new HashMap<>());
            for (String v : nodos) {
                if (u.equals(v)) {
                    dist.get(u).put(v, 0.0);
                } else {
                    dist.get(u).put(v, Double.POSITIVE_INFINITY);
                }
            }
        }

        // Cargar las distancias directas (aristas)
        for (String origen : nodos) {
            List<Arista> aristas = grafo.getListaAdyacencia().get(origen);
            if (aristas != null) {
                for (Arista arista : aristas) {
                    dist.get(origen).put(arista.destino, arista.costo);
                }
            }
        }

        System.out.println("\n--- Ejecutando Floyd-Warshall (Análisis Total) ---");

        // 2. Algoritmo de Programación Dinámica O(V^3)
        for (String k : nodos) {
            for (String i : nodos) {
                for (String j : nodos) {
                    double distIK = dist.get(i).get(k);
                    double distKJ = dist.get(k).get(j);

                    if (distIK != Double.POSITIVE_INFINITY && distKJ != Double.POSITIVE_INFINITY) {
                        double nuevaDistancia = distIK + distKJ;
                        if (nuevaDistancia < dist.get(i).get(j)) {
                            dist.get(i).put(j, nuevaDistancia);
                        }
                    }
                }
            }
        }
        return dist;
    }
}