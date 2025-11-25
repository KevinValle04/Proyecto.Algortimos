import java.util.*;

public class AlgoritmoPrim {

    /**
     * Implementación del Algoritmo de Prim para encontrar el Árbol de Expansión Mínimo (MST).
     * Nota: Prim asume un grafo NO dirigido. Si la red es dirigida, Prim solo
     * encontrará el MST si se consideran las rutas en ambas direcciones.
     * @param grafo El GrafoLogistica (tratado como no dirigido para esta aplicación).
     * @return El costo total del MST.
     */
    public static double algoritmoPrim(GrafoLogistica grafo, String origen) {
        Set<String> nodos = grafo.getListaAdyacencia().keySet();
        Map<String, Double> costosMinimos = new HashMap<>(); // Distancia al MST
        Set<String> nodosEnMST = new HashSet<>();
        PriorityQueue<EstadoRuta> pq = new PriorityQueue<>(); // Similar a Dijkstra, pero prioriza el borde más barato

        // 1. Inicialización
        for (String nodo : nodos) {
            costosMinimos.put(nodo, Double.POSITIVE_INFINITY);
        }

        costosMinimos.put(origen, 0.0);
        pq.add(new EstadoRuta(origen, 0.0));

        double costoTotalMST = 0.0;

        System.out.println("\n--- Ejecutando Algoritmo de Prim (Origen: " + origen + ") ---");

        // 2. Proceso Principal
        while (!pq.isEmpty() && nodosEnMST.size() < nodos.size()) {
            EstadoRuta actual = pq.poll();
            String u = actual.nodo;
            double costoU = actual.distancia;

            if (nodosEnMST.contains(u)) {
                continue;
            }

            // Agregamos el nodo al MST y sumamos su costo
            nodosEnMST.add(u);
            costoTotalMST += costoU;

            // 3. Actualización de costos a los vecinos
            List<Arista> vecinos = grafo.getListaAdyacencia().get(u);
            if (vecinos != null) {
                for (Arista arista : vecinos) {
                    String v = arista.destino;
                    double peso = arista.costo;

                    if (!nodosEnMST.contains(v) && peso < costosMinimos.getOrDefault(v, Double.POSITIVE_INFINITY)) {
                        costosMinimos.put(v, peso);
                        pq.add(new EstadoRuta(v, peso));
                    }
                }
            }
        }

        // Verifica si todos los nodos están conectados
        if (nodosEnMST.size() < nodos.size()) {
            System.err.println("Advertencia: El grafo no es conexo. MST parcial encontrado.");
            // El costo total incluye solo el componente conexo encontrado.
        }

        return costoTotalMST;
    }
}