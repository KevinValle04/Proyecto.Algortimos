import java.util.*;

public class DijkstraRecursivo {

    public static Map<String, Double> dijkstraRecursivo(GrafoLogistica grafo, String origen) {
        Map<String, Double> distancias = new HashMap<>();

        // Inicializar distancias a infinito para todos
        for (String nodo : grafo.getListaAdyacencia().keySet()) {
            distancias.put(nodo, Double.POSITIVE_INFINITY);
        }
        distancias.put(origen, 0.0);

        System.out.println("\n--- Ejecutando Dijkstra Recursivo (Origen: " + origen + ") ---");

        // Llamada inicial para procesar el primer nodo y sus descendientes
        // Esta implementación recursiva es didáctica, pero menos eficiente que la iterativa.
        procesarNodoRecursivo(grafo, distancias);
        return distancias;
    }

    private static void procesarNodoRecursivo(GrafoLogistica grafo, Map<String, Double> distancias) {

        // Enfoque: Encontrar el nodo no "procesado" (no visitado completamente) con la menor distancia actual.
        String proximoNodo = encontrarNodoMinimoYMarcar(distancias, grafo.getListaAdyacencia().keySet());

        // Caso base: No quedan nodos alcanzables o todos han sido visitados.
        if (proximoNodo == null || distancias.get(proximoNodo) == Double.POSITIVE_INFINITY) {
            return;
        }

        String actual = proximoNodo;
        double distU = distancias.get(actual);

        // Relajación
        List<Arista> vecinos = grafo.getListaAdyacencia().get(actual);
        if (vecinos != null) {
            for (Arista arista : vecinos) {
                String v = arista.destino;
                double peso = arista.costo;
                double nuevaDistancia = distU + peso;

                if (nuevaDistancia < distancias.getOrDefault(v, Double.POSITIVE_INFINITY)) {
                    distancias.put(v, nuevaDistancia);
                }
            }
        }

        // Llamada recursiva
        procesarNodoRecursivo(grafo, distancias);
    }

    // Función auxiliar: Busca el nodo con la distancia mínima y lo "marca" (simulado con Double.MAX_VALUE)
    private static String encontrarNodoMinimoYMarcar(Map<String, Double> distancias, Set<String> nodos) {
        double minD = Double.POSITIVE_INFINITY;
        String nodoMinimo = null;

        for (String nodo : nodos) {
            if (distancias.containsKey(nodo) && distancias.get(nodo) < minD) {
                minD = distancias.get(nodo);
                nodoMinimo = nodo;
            }
        }

        // Marcar el nodo como "procesado" para que no sea seleccionado de nuevo en la recursión
        if (nodoMinimo != null) {
            // Es importante usar la distancia original para la relajación antes de cambiar el valor.
            distancias.put(nodoMinimo, Double.MAX_VALUE);
        }
        return nodoMinimo;
    }
}