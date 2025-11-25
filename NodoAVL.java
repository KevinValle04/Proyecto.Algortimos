import java.util.Map;

public class NodoAVL {
    public int clave; // ID_Producto
    public Map<String, Object> datos; // Nombre, Stock, Ubicacion
    public NodoAVL izquierda;
    public NodoAVL derecha;
    public int altura; // Para el balanceo

    public NodoAVL(int clave, Map<String, Object> datos) {
        this.clave = clave;
        this.datos = datos;
        this.izquierda = null;
        this.derecha = null;
        this.altura = 1; // Un nuevo nodo siempre tiene altura 1
    }
}