/**
 * Clase auxiliar para la PriorityQueue de Dijkstra y Prim.
 * Almacena el nodo y la distancia/costo acumulado.
 */
public class EstadoRuta implements Comparable<EstadoRuta> {
    public String nodo;
    public double distancia;

    public EstadoRuta(String nodo, double distancia) {
        this.nodo = nodo;
        this.distancia = distancia;
    }

    /** Compara dos estados priorizando la menor distancia. */
    @Override
    public int compareTo(EstadoRuta otro) {
        return Double.compare(this.distancia, otro.distancia);
    }
}