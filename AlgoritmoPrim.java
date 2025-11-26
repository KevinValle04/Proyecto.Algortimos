import java.util.*;

// ==========================================
// Clases Auxiliares para el Resultado
// ==========================================

// Clase que representa una conexión elegida en el árbol
class AristaMST {
    public String origen;
    public String destino;
    public double peso;

    public AristaMST(String origen, String destino, double peso) {
        this.origen = origen;
        this.destino = destino;
        this.peso = peso;
    }
}

// Clase contenedora que devuelve el algoritmo
class ResultadoMST {
    private List<AristaMST> aristas = new ArrayList<>();
    private double costoTotal = 0.0;

    public void agregarArista(String origen, String destino, double peso) {
        aristas.add(new AristaMST(origen, destino, peso));
        costoTotal += peso;
    }

    public List<AristaMST> getAristas() {
        return aristas;
    }

    public double getCostoTotal() {
        return costoTotal;
    }
}

// ==========================================
// Lógica Principal de Prim
// ==========================================

public class AlgoritmoPrim {

    /**
     * Algoritmo de Prim modificado para retornar el detalle de rutas.
     */
    public static ResultadoMST algoritmoPrim(GrafoLogistica grafo, String origen) {
        ResultadoMST resultado = new ResultadoMST();
        
        Set<String> nodos = grafo.getListaAdyacencia().keySet();
        
        // Mapas para el seguimiento
        Map<String, Double> costosMinimos = new HashMap<>(); 
        Map<String, String> padres = new HashMap<>(); // CLAVE: guarda de dónde venimos (Hijo -> Padre)
        Set<String> nodosEnMST = new HashSet<>();
        PriorityQueue<EstadoRuta> pq = new PriorityQueue<>();

        // 1. Inicialización
        for (String nodo : nodos) {
            costosMinimos.put(nodo, Double.POSITIVE_INFINITY);
        }

        costosMinimos.put(origen, 0.0);
        pq.add(new EstadoRuta(origen, 0.0));

        System.out.println("\n--- Ejecutando Prim con Detalle de Rutas ---");

        // 2. Proceso Principal
        while (!pq.isEmpty() && nodosEnMST.size() < nodos.size()) {
            EstadoRuta actual = pq.poll();
            String u = actual.nodo;
            double costoArista = actual.distancia;

            // Si ya procesamos este nodo, lo saltamos
            if (nodosEnMST.contains(u)) {
                continue;
            }

            // --- AQUÍ ESTÁ EL CAMBIO CLAVE ---
            // Si el nodo tiene un padre registrado, guardamos esa conexión en el resultado
            if (padres.containsKey(u)) {
                String padre = padres.get(u);
                resultado.agregarArista(padre, u, costoArista);
            }

            // Marcar como visitado
            nodosEnMST.add(u);

            // 3. Revisar vecinos
            List<Arista> vecinos = grafo.getListaAdyacencia().get(u);
            if (vecinos != null) {
                for (Arista arista : vecinos) {
                    String v = arista.destino;
                    double peso = arista.costo;

                    // Si el vecino no está en el MST y esta ruta es más barata que la que conocíamos
                    if (!nodosEnMST.contains(v) && peso < costosMinimos.getOrDefault(v, Double.POSITIVE_INFINITY)) {
                        costosMinimos.put(v, peso);
                        padres.put(v, u); // Guardamos que llegamos a 'v' desde 'u'
                        pq.add(new EstadoRuta(v, peso));
                    }
                }
            }
        }

        return resultado;
    }
}