import java.io.FileNotFoundException;
import java.util.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class ArbolAVL {
    public NodoAVL raiz;

    public ArbolAVL() {
        this.raiz = null;
    }

    private int obtenerAltura(NodoAVL nodo) {
        return (nodo == null) ? 0 : nodo.altura;
    }

    private int obtenerBalance(NodoAVL nodo) {
        return (nodo == null) ? 0 : obtenerAltura(nodo.izquierda) - obtenerAltura(nodo.derecha);
    }


    //Obtener el balance
    // Rotacion 
    private NodoAVL rotacionDerecha(NodoAVL y) {
        NodoAVL x = y.izquierda;
        NodoAVL T2 = x.derecha;

        // hacer la rotacion
        x.derecha = y;
        y.izquierda = T2;

        // Actualizar alturas
        y.altura = 1 + Math.max(obtenerAltura(y.izquierda), obtenerAltura(y.derecha));
        x.altura = 1 + Math.max(obtenerAltura(x.izquierda), obtenerAltura(x.derecha));

        return x; // nueva raiz del subarbol
    }

    // Rotacion a la izquierda simple
    private NodoAVL rotacionIzquierda(NodoAVL x) {
        NodoAVL y = x.derecha;
        NodoAVL T2 = y.izquierda;

        // hacer la rotacion
        y.izquierda = x;
        x.derecha = T2;

        // Actualizar alturas
        x.altura = 1 + Math.max(obtenerAltura(x.izquierda), obtenerAltura(x.derecha));
        y.altura = 1 + Math.max(obtenerAltura(y.izquierda), obtenerAltura(y.derecha));

        return y; // nueva raiz del subarbol
    }

    //Metodo de insercion 
    public NodoAVL insertar(NodoAVL nodo, int clave, Map<String, Object> datos) {
        if (nodo == null) {
            return new NodoAVL(clave, datos);
        }

        if (clave < nodo.clave) {
            nodo.izquierda = insertar(nodo.izquierda, clave, datos);
        } else if (clave > nodo.clave) {
            nodo.derecha = insertar(nodo.derecha, clave, datos);
        } else {
            return nodo; // Claves duplicadas no 
        }

        // actualizar altura del nodo
        nodo.altura = 1 + Math.max(obtenerAltura(nodo.izquierda), obtenerAltura(nodo.derecha));

        // obtenr factor de balanceo
        int balance = obtenerBalance(nodo);

        // Caso Izquierda-Izquierda (LL)
        if (balance > 1 && clave < nodo.izquierda.clave) {
            return rotacionDerecha(nodo);
        }

        // Caso Derecha-Derecha (RR)
        if (balance < -1 && clave > nodo.derecha.clave) {
            return rotacionIzquierda(nodo);
        }

        // Caso Izquierda-Derecha (LR)
        if (balance > 1 && clave > nodo.izquierda.clave) {
            nodo.izquierda = rotacionIzquierda(nodo.izquierda);
            return rotacionDerecha(nodo);
        }

        // Caso Derecha-Izquierda (RL)
        if (balance < -1 && clave < nodo.derecha.clave) {
            nodo.derecha = rotacionDerecha(nodo.derecha);
            return rotacionIzquierda(nodo);
        }

        return nodo; // si esta balanceado
    }

    public void insertar(int clave, Map<String, Object> datos) {
        this.raiz = insertar(this.raiz, clave, datos);
    }


    public void cargarDesdeCSV(String rutaArchivo) {
        String linea;
        String separadorCSV = ",";
        int productosCargados = 0;

        try (BufferedReader br = new BufferedReader(new FileReader(rutaArchivo))) {
            br.readLine(); // salta linea

            while ((linea = br.readLine()) != null) {
                String[] datos = linea.split(separadorCSV);
                if (datos.length >= 4) {
                    // columnas
                    int clave = Integer.parseInt(datos[0].trim());

                    Map<String, Object> itemDatos = new HashMap<>();
                    itemDatos.put("nombre", datos[1].trim());
                    itemDatos.put("stock", Integer.parseInt(datos[2].trim()));
                    itemDatos.put("ubicacion", datos[3].trim());

                    this.raiz = insertar(this.raiz, clave, itemDatos);
                    productosCargados++;
                }
            }
        } catch (FileNotFoundException e) {
            System.err.println("ERROR: Archivo no encontrado: " + rutaArchivo);
        } catch (IOException e) {
            System.err.println("ERROR de lectura del archivo: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.err.println("ERROR de formato: Las columnas numéricas deben ser enteros válidos.");
        }
    }

    //Metodos para busqueda
    public NodoAVL buscar(int clave) {
        return buscar(this.raiz, clave);
    }

    private NodoAVL buscar(NodoAVL nodo, int clave) {
        if (nodo == null || nodo.clave == clave) {
            return nodo;
        }

        if (clave < nodo.clave) {
            return buscar(nodo.izquierda, clave);
        } else {
            return buscar(nodo.derecha, clave);
        }
    }


    //Recorridos
    // Preorden
    public void preorden(NodoAVL nodo) {
        if (nodo != null) {
            preorden(nodo.izquierda);
            preorden(nodo.derecha);
        }
    }

    // Inorden (Ordenado y Ascendente)
    public void inorden(NodoAVL nodo) {
        if (nodo != null) {
            inorden(nodo.izquierda);
            inorden(nodo.derecha);
        }
    }
   
    // Postorden
    public void postorden(NodoAVL nodo) {
        if (nodo != null) {
            postorden(nodo.izquierda);
            postorden(nodo.derecha);
        }
    }

/**
 * Meotod para obtener los datos y pner en la tabla
 */
public List<Map<String, String>> obtenerRecorridoEnLista(String tipoRecorrido) {
    List<Map<String, String>> listaDatos = new ArrayList<>();
    recolectarDatosRecursivo(this.raiz, listaDatos, tipoRecorrido);
    return listaDatos;
}

private void recolectarDatosRecursivo(NodoAVL nodo, List<Map<String, String>> lista, String tipo) {
    if (nodo == null) return;

    // Preorden: Raiz primero
    if (tipo.equals("PREORDEN")) agregarNodoALista(nodo, lista);

    recolectarDatosRecursivo(nodo.izquierda, lista, tipo);

    // Inorden: Raiz en medio
    if (tipo.equals("INORDEN")) agregarNodoALista(nodo, lista);

    recolectarDatosRecursivo(nodo.derecha, lista, tipo);

    // Postorden: Raiz en final
    if (tipo.equals("POSTORDEN")) agregarNodoALista(nodo, lista);
}

private void agregarNodoALista(NodoAVL nodo, List<Map<String, String>> lista) {
    Map<String, String> fila = new HashMap<>();
    // conversion de los datos obtenidos a string
    fila.put("ID", String.valueOf(nodo.clave));
    
    
    fila.put("PRODUCTO", nodo.datos.getOrDefault("nombre", "Sin Nombre").toString());
    fila.put("STOCK", nodo.datos.getOrDefault("stock", "0").toString());
    fila.put("UBICACIÓN", nodo.datos.getOrDefault("ubicacion", "N/A").toString());

    lista.add(fila);
}

}