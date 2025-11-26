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

    // =================================================================
    // Operaciones de Rotación
    // =================================================================

    // Rotación Simple a la Derecha
    private NodoAVL rotacionDerecha(NodoAVL y) {
        NodoAVL x = y.izquierda;
        NodoAVL T2 = x.derecha;

        // Realizar rotación
        x.derecha = y;
        y.izquierda = T2;

        // Actualizar alturas
        y.altura = 1 + Math.max(obtenerAltura(y.izquierda), obtenerAltura(y.derecha));
        x.altura = 1 + Math.max(obtenerAltura(x.izquierda), obtenerAltura(x.derecha));

        return x; // Nueva raíz del subárbol
    }

    // Rotación Simple a la Izquierda
    private NodoAVL rotacionIzquierda(NodoAVL x) {
        NodoAVL y = x.derecha;
        NodoAVL T2 = y.izquierda;

        // Realizar rotación
        y.izquierda = x;
        x.derecha = T2;

        // Actualizar alturas
        x.altura = 1 + Math.max(obtenerAltura(x.izquierda), obtenerAltura(x.derecha));
        y.altura = 1 + Math.max(obtenerAltura(y.izquierda), obtenerAltura(y.derecha));

        return y; // Nueva raíz del subárbol
    }

    // =================================================================
    // 4. Implementación. Inventario con Búsqueda Rápida (Insertar)
    // =================================================================
    public NodoAVL insertar(NodoAVL nodo, int clave, Map<String, Object> datos) {
        // 1. Inserción normal de BST
        if (nodo == null) {
            return new NodoAVL(clave, datos);
        }

        if (clave < nodo.clave) {
            nodo.izquierda = insertar(nodo.izquierda, clave, datos);
        } else if (clave > nodo.clave) {
            nodo.derecha = insertar(nodo.derecha, clave, datos);
        } else {
            return nodo; // Claves duplicadas no permitidas
        }

        // 2. Actualizar altura del nodo ancestro actual
        nodo.altura = 1 + Math.max(obtenerAltura(nodo.izquierda), obtenerAltura(nodo.derecha));

        // 3. Obtener el factor de balanceo y balancear si es necesario
        int balance = obtenerBalance(nodo);

        // Caso Izquierda-Izquierda (LL): Rotación Derecha
        if (balance > 1 && clave < nodo.izquierda.clave) {
            return rotacionDerecha(nodo);
        }

        // Caso Derecha-Derecha (RR): Rotación Izquierda
        if (balance < -1 && clave > nodo.derecha.clave) {
            return rotacionIzquierda(nodo);
        }

        // Caso Izquierda-Derecha (LR): Rotación Izquierda, luego Derecha
        if (balance > 1 && clave > nodo.izquierda.clave) {
            nodo.izquierda = rotacionIzquierda(nodo.izquierda);
            return rotacionDerecha(nodo);
        }

        // Caso Derecha-Izquierda (RL): Rotación Derecha, luego Izquierda
        if (balance < -1 && clave < nodo.derecha.clave) {
            nodo.derecha = rotacionDerecha(nodo.derecha);
            return rotacionIzquierda(nodo);
        }

        return nodo; // Retorna el nodo sin cambios si está balanceado
    }

    public void insertar(int clave, Map<String, Object> datos) {
        this.raiz = insertar(this.raiz, clave, datos);
    }

    // =================================================================
    // Carga de CSV para AVL (Usando el archivo inventario_avl.csv)
    // =================================================================
    public void cargarDesdeCSV(String rutaArchivo) {
        System.out.println("\n-> Cargando Inventario AVL desde: " + rutaArchivo + "...");
        String linea;
        String separadorCSV = ",";
        int productosCargados = 0;

        try (BufferedReader br = new BufferedReader(new FileReader(rutaArchivo))) {
            br.readLine(); // Salta la línea de cabecera

            while ((linea = br.readLine()) != null) {
                String[] datos = linea.split(separadorCSV);
                if (datos.length >= 4) {
                    // Mapeo de columnas: ID_Producto, Nombre, Stock, Ubicacion
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
        } catch (FileNotFoundException e) {
            System.err.println("ERROR: Archivo no encontrado: " + rutaArchivo);
        } catch (IOException e) {
            System.err.println("ERROR de lectura del archivo: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.err.println("ERROR de formato: Las columnas numéricas deben ser enteros válidos.");
        }
    }

    // =================================================================
    // 5. Consulta. Verificación Rápida de Stock/Cliente (Búsqueda)
    // =================================================================
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

    // =================================================================
    // 6. Recorridos Ordenados (Reportes de Inventario)
    // =================================================================

    

    // Preorden
    public void preorden(NodoAVL nodo) {
        if (nodo != null) {
            System.out.print(nodo.clave + " ");
            preorden(nodo.izquierda);
            preorden(nodo.derecha);
        }
    }
    public String getPreordenReporte() {
        StringBuilder sb = new StringBuilder();
        recorrerPreorden(this.raiz, sb);
        return sb.toString();
    }

    private void recorrerPreorden(NodoAVL nodo, StringBuilder sb) {
    if (nodo != null) {
        sb.append("ID: ").append(nodo.clave)
                .append(", Nombre: ").append(nodo.datos.get("nombre"))
                .append(", Stock: ").append(nodo.datos.get("stock"))
                .append("\n");
        
        recorrerPreorden(nodo.izquierda, sb);
        recorrerPreorden(nodo.derecha, sb);
    }
}

    // Inorden (Ordenado y Ascendente)
    public void inorden(NodoAVL nodo) {
        if (nodo != null) {
            inorden(nodo.izquierda);
            System.out.println("ID: " + nodo.clave + ", Nombre: " + nodo.datos.get("nombre") + ", Stock: " + nodo.datos.get("stock"));
            inorden(nodo.derecha);
        }
    }
    // Método auxiliar para generar el reporte Inorden como String
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
    // Postorden
    public void postorden(NodoAVL nodo) {
        if (nodo != null) {
            postorden(nodo.izquierda);
            postorden(nodo.derecha);
            System.out.print(nodo.clave + " ");
        }
    }
    // Método auxiliar para generar el reporte Inorden como String
    public String getPostordenReporte() {
        StringBuilder sb = new StringBuilder();
        recorrerPostorden(this.raiz, sb);
        return sb.toString();
    }

    private void recorrerPostorden(NodoAVL nodo, StringBuilder sb) {
    if (nodo != null) {
        recorrerPostorden(nodo.izquierda, sb);
        recorrerPostorden(nodo.derecha, sb);
        
        sb.append("ID: ").append(nodo.clave)
                .append(", Nombre: ").append(nodo.datos.get("nombre"))
                .append(", Stock: ").append(nodo.datos.get("stock"))
                .append("\n");
    }
}
}