import java.util.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * Implementación del Árbol Binario de Búsqueda Balanceado (AVL) para Inventario.
 */
public class ArbolAVL {
    public NodoAVL raiz;

    private int obtenerAltura(NodoAVL nodo) {
        return (nodo == null) ? 0 : nodo.altura;
    }

    private int obtenerBalance(NodoAVL nodo) {
        return (nodo == null) ? 0 : obtenerAltura(nodo.izquierda) - obtenerAltura(nodo.derecha);
    }

    // Rotaciones requeridas para el balanceo AVL
    private NodoAVL rotacionDerecha(NodoAVL y) {
        NodoAVL x = y.izquierda;
        NodoAVL T2 = x.derecha;
        x.derecha = y;
        y.izquierda = T2;
        // Actualizar alturas
        y.altura = 1 + Math.max(obtenerAltura(y.izquierda), obtenerAltura(y.derecha));
        x.altura = 1 + Math.max(obtenerAltura(x.izquierda), obtenerAltura(x.derecha));
        return x;
    }

    private NodoAVL rotacionIzquierda(NodoAVL x) {
        NodoAVL y = x.derecha;
        NodoAVL T2 = y.izquierda;
        y.izquierda = x;
        x.derecha = T2;
        // Actualizar alturas
        x.altura = 1 + Math.max(obtenerAltura(x.izquierda), obtenerAltura(x.derecha));
        y.altura = 1 + Math.max(obtenerAltura(y.izquierda), obtenerAltura(y.derecha));
        return y;
    }

    /** Inserta la clave y realiza las rotaciones para mantener el balanceo O(log n). */
    public NodoAVL insertar(NodoAVL nodo, int clave, Map<String, Object> datos) {
        // 1. Inserción normal de BST
        if (nodo == null) return new NodoAVL(clave, datos);
        if (clave < nodo.clave) nodo.izquierda = insertar(nodo.izquierda, clave, datos);
        else if (clave > nodo.clave) nodo.derecha = insertar(nodo.derecha, clave, datos);
        else return nodo; // Claves duplicadas no permitidas

        // 2. Actualizar altura
        nodo.altura = 1 + Math.max(obtenerAltura(nodo.izquierda), obtenerAltura(nodo.derecha));

        // 3. Balanceo
        int balance = obtenerBalance(nodo);
        if (balance > 1 && clave < nodo.izquierda.clave) return rotacionDerecha(nodo); // Caso LL
        if (balance < -1 && clave > nodo.derecha.clave) return rotacionIzquierda(nodo); // Caso RR
        if (balance > 1 && clave > nodo.izquierda.clave) { // Caso LR
            nodo.izquierda = rotacionIzquierda(nodo.izquierda);
            return rotacionDerecha(nodo);
        }
        if (balance < -1 && clave < nodo.derecha.clave) { // Caso RL
            nodo.derecha = rotacionDerecha(nodo.derecha);
            return rotacionIzquierda(nodo);
        }
        return nodo;
    }

    /** Carga datos de inventario (ID_Producto como clave) desde un archivo CSV. */
    public void cargarDesdeCSV(String rutaArchivo) {
        System.out.println("\n-> Cargando Inventario AVL desde: " + rutaArchivo + "...");
        int productosCargados = 0;

        try (BufferedReader br = new BufferedReader(new FileReader(rutaArchivo))) {
            br.readLine(); // Saltar cabecera
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] datos = linea.split(",");
                if (datos.length >= 4) {
                    int clave = Integer.parseInt(datos[0].trim());
                    Map<String, Object> itemDatos = new HashMap<>();
                    itemDatos.put("nombre", datos[1].trim());
                    itemDatos.put("stock", Integer.parseInt(datos[2].trim()));
                    itemDatos.put("ubicacion", datos[3].trim());

                    this.raiz = insertar(this.raiz, clave, itemDatos);
                    productosCargados++;
                }
            }
            System.out.println("-> Carga de Inventario AVL completada. " + productosCargados + " productos cargados.");
        } catch (Exception e) {
            throw new RuntimeException("ERROR al cargar Inventario desde CSV: " + e.getMessage());
        }
    }

    /** Realiza una Búsqueda estándar en el BST, tiempo O(log n) garantizado por AVL. */
    public NodoAVL buscar(int clave) {
        return buscar(this.raiz, clave);
    }

    private NodoAVL buscar(NodoAVL nodo, int clave) {
        if (nodo == null || nodo.clave == clave) return nodo;
        if (clave < nodo.clave) return buscar(nodo.izquierda, clave);
        else return buscar(nodo.derecha, clave);
    }

    /** Genera el reporte de inventario ordenado (Inorden) como un String. */
    public String getInordenReporte() {
        StringBuilder sb = new StringBuilder();
        recorrerInorden(this.raiz, sb);
        return sb.toString();
    }

    private void recorrerInorden(NodoAVL nodo, StringBuilder sb) {
        if (nodo != null) {
            recorrerInorden(nodo.izquierda, sb);
            sb.append("ID: ").append(nodo.clave)
                    .append(", Nombre: ").append(nodo.datos.get("nombre"))
                    .append(", Stock: ").append(nodo.datos.get("stock"))
                    .append("\n");
            recorrerInorden(nodo.derecha, sb);
        }
    }
}