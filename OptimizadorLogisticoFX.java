import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import java.util.Collections;
import java.util.Map;

/**
 * Clase principal con la Interfaz Gráfica de Usuario (JavaFX) para el Optimizador Logístico.
 */
public class OptimizadorLogisticoFX extends Application {

    private GrafoLogistica logisticaGrafo = null;
    private ArbolAVL inventarioAVL = null;

    private TextArea areaSalida;
    private TextField txtRutaGrafo;
    private TextField txtRutaAVL;
    private TextField txtParametroOrigen;
    private TextField txtParametroBusqueda;
    private Label lblStatus;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Proyecto Final de algoritmos");

        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #f4f6f7;");

        root.setTop(crearHeader());
        root.setLeft(crearSidebarFijo()); // Panel de control izquierdo fijo
        root.setCenter(crearAreaResultados());


        Scene scene = new Scene(root, 1280, 800);

        scene.getStylesheets().add(getClass().getResource("styles.css") != null
                ? getClass().getResource("styles.css").toExternalForm()
                : "data:text/css," + obtenerEstilosCSS());

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /** Crea la barra superior con el título de la aplicación. */
    private HBox crearHeader() {
        HBox header = new HBox();
        header.setPadding(new Insets(15, 25, 15, 25));
        header.setAlignment(Pos.CENTER_LEFT);
        header.setStyle("-fx-background-color: #2c3e50;");

        Label lblTitulo = new Label("OPTIMIZADOR LOGÍSTICO");
        lblTitulo.setTextFill(Color.WHITE);
        lblTitulo.setFont(Font.font("Segoe UI", FontWeight.BOLD, 22));

        Label lblSubtitulo = new Label("  |  Graph Explorer");
        lblSubtitulo.setTextFill(Color.web("#bdc3c7"));
        lblSubtitulo.setFont(Font.font("Segoe UI", FontWeight.NORMAL, 18));

        header.getChildren().addAll(lblTitulo, lblSubtitulo);
        return header;
    }

    /** Crea la barra lateral izquierda con los 3 paneles de control fijos. */
    private VBox crearSidebarFijo() {
        VBox sidebarContent = new VBox(20);
        sidebarContent.setPadding(new Insets(15));

        sidebarContent.getChildren().addAll(
                crearSeccionFija("1. Carga de Datos", crearContenidoDatos()),
                crearSeccionFija("2. Rutas y Transporte", crearContenidoGrafos()),
                crearSeccionFija("3. Gestión de Inventario", crearContenidoAVL()),
                crearSeccionFija("4. Recorridos ordenados",crearContenidoRecorridoOrdenados())

        );

        ScrollPane scrollPane = new ScrollPane(sidebarContent);
        scrollPane.setFitToWidth(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setStyle("-fx-background: #ecf0f1; -fx-border-color: #dcdcdc; -fx-border-width: 0 1 0 0;");
        scrollPane.setPrefWidth(340);

        VBox sidebarContainer = new VBox(scrollPane);
        VBox.setVgrow(scrollPane, Priority.ALWAYS);
        return sidebarContainer;
    }

    /** Auxiliar para envolver cada sección fija con un título. */
    private VBox crearSeccionFija(String titulo, Node contenido) {
        VBox seccion = new VBox(10);

        Label lblTitulo = new Label(titulo);
        lblTitulo.getStyleClass().add("section-header");

        seccion.getChildren().addAll(lblTitulo, contenido);

        Separator sep = new Separator();
        sep.setPadding(new Insets(10, 0, 0, 0));
        seccion.getChildren().add(sep);

        return seccion;
    }

    /** Contenido de la sección de carga de archivos. */
    private VBox crearContenidoDatos() {
        VBox box = new VBox(10);

        VBox group1 = new VBox(5);
                group1.getChildren().addAll(new Label("Archivo de Rutas (.csv):"), txtRutaGrafo = new TextField("C:\\Users\\jalex\\Documents\\NEWSEMESTER\\Algoritmos\\ProyectoAlgoritmos\\Proyecto-Algoritmos\\Copia de red_logistica_grafo.csv"));


        VBox group2 = new VBox(5);
                group2.getChildren().addAll(new Label("Archivo de Inventario (.csv):"), txtRutaAVL = new TextField("C:\\Users\\jalex\\Documents\\NEWSEMESTER\\Algoritmos\\ProyectoAlgoritmos\\Proyecto-Algoritmos\\Copia de inventario_avl.csv"));


        Button btnCargar = new Button("Cargar Base de Datos");
        btnCargar.getStyleClass().add("btn-primary");
        btnCargar.setMaxWidth(Double.MAX_VALUE);
        btnCargar.setOnAction(e -> cargarDatos());

        box.getChildren().addAll(group1, group2, btnCargar);
        return box;
    }

    /** Contenido de la sección de algoritmos de Grafos. */
    private VBox crearContenidoGrafos() {
        VBox box = new VBox(10);

        VBox groupOrigen = new VBox(5);
        groupOrigen.getChildren().addAll(new Label("Centro de Origen:"), txtParametroOrigen = new TextField(""));

        Button btnDijkstra = new Button("Ruta Más Corta (Dijkstra)");
        btnDijkstra.getStyleClass().add("btn-action");
        btnDijkstra.setMaxWidth(Double.MAX_VALUE);
        btnDijkstra.setOnAction(e -> ejecutarDijkstra());

        Button btnFloyd = new Button("Conectividad Total (Floyd)");
        btnFloyd.getStyleClass().add("btn-action");
        btnFloyd.setMaxWidth(Double.MAX_VALUE);
        btnFloyd.setOnAction(e -> ejecutarFloydWarshall());

        Button btnPrim = new Button("Red Costo Mínimo (Prim)");
        btnPrim.getStyleClass().add("btn-action");
        btnPrim.setMaxWidth(Double.MAX_VALUE);
        btnPrim.setOnAction(e -> ejecutarPrim());

        box.getChildren().addAll(groupOrigen, new Label("Algoritmos:"), btnDijkstra, btnFloyd, btnPrim);
        return box;
    }

    private VBox crearContenidoRecorridoOrdenados(){
    VBox box = new VBox(10);
    
    Button btnReporte = new Button("Reporte (Inorden)");
        btnReporte.getStyleClass().add("btn-action");
        btnReporte.setMaxWidth(Double.MAX_VALUE);
        btnReporte.setOnAction(e -> ejecutarRecorridoInorden());

        Button btnReporte2 = new Button("Reporte (postorden)");
        btnReporte2.getStyleClass().add("btn-action");
        btnReporte2.setMaxWidth(Double.MAX_VALUE);
        btnReporte2.setOnAction(e -> ejecutarRecorridoPostorden());

        Button btnReporte3 = new Button("Reporte (preorden)");
        btnReporte3.getStyleClass().add("btn-action");
        btnReporte3.setMaxWidth(Double.MAX_VALUE);
        btnReporte3.setOnAction(e -> ejecutarRecorridoPreorden());

        box.getChildren().addAll(btnReporte, btnReporte2  , btnReporte3);
        return box;
    }

    /** Contenido de la sección de gestión de Inventario AVL. */
    private VBox crearContenidoAVL() {
        VBox box = new VBox(10);

        VBox groupBusqueda = new VBox(5);
        groupBusqueda.getChildren().addAll(new Label("ID Producto:"), txtParametroBusqueda = new TextField(""));

        Button btnBuscar = new Button("Consultar Stock");
        btnBuscar.getStyleClass().add("btn-action");
        btnBuscar.setMaxWidth(Double.MAX_VALUE);
        btnBuscar.setOnAction(e -> ejecutarBusquedaAVL());

        box.getChildren().addAll(groupBusqueda, new Label("Operaciones:"), btnBuscar);
        return box;
    }

    /** Crea el área central de resultados. */
    private VBox crearAreaResultados() {
        VBox box = new VBox();
        box.setPadding(new Insets(20));
        box.setStyle("-fx-background-color: white;");

        Label lblTitulo = new Label("Resultados");
        lblTitulo.setFont(Font.font("Segoe UI", FontWeight.BOLD, 16));
        lblTitulo.setTextFill(Color.web("#34495e"));
        lblTitulo.setPadding(new Insets(0, 0, 10, 0));

        areaSalida = new TextArea();
        areaSalida.setEditable(false);
        areaSalida.setFont(Font.font("Consolas", 14));
        areaSalida.setStyle("-fx-control-inner-background: #fdfdfd; -fx-border-color: #dcdcdc;");
        areaSalida.setText("->Carga los archivos CSV<-");

        VBox.setVgrow(areaSalida, Priority.ALWAYS);

        box.getChildren().addAll(lblTitulo, areaSalida);
        return box;
    }


    /** Carga los datos de Grafo y AVL desde los archivos CSV. */
    private void cargarDatos() {
        areaSalida.clear();
        areaSalida.appendText("--->Cargando archivos con los datos\n");

        // 1. Carga Grafo
        String rutaGrafo = txtRutaGrafo.getText();
        logisticaGrafo = new GrafoLogistica();
        try {
            logisticaGrafo.cargarDesdeCSV(rutaGrafo);
            areaSalida.appendText("--->Datos cargados\n");
        } catch (Exception e) {
            areaSalida.appendText("---->No se pudieron obtener los datos" + e.getMessage() + "\n");
        }

        // 2. Carga AVL
        String rutaAVL = txtRutaAVL.getText();
        inventarioAVL = new ArbolAVL();
        try {
            inventarioAVL.cargarDesdeCSV(rutaAVL);
            areaSalida.appendText("--->Datos cargados\n");
        } catch (Exception e) {
            areaSalida.appendText("---->No se pudieron obtener los datos" + e.getMessage() + "\n");
        }

        areaSalida.appendText("--->Datos cargados correctamente\n");
    }

    /** Ejecuta el algoritmo de Dijkstra (Ruta más corta desde un origen). */
    private void ejecutarDijkstra() {
        if (!validarGrafo()) return;
        String origen = txtParametroOrigen.getText().trim();

        if (!logisticaGrafo.getListaAdyacencia().containsKey(origen)) {
            alertarError("Origen no válido", "El nodo '" + origen + "' no existe en la red actual.");
            return;
        }

        Map<String, Double> resultados = Dijkstra.dijkstraIterativo(logisticaGrafo, origen);

        StringBuilder sb = new StringBuilder();
        sb.append("=== DIJKSTRA ===\n");
        sb.append("Origen: ").append(origen).append("\n\n");
        sb.append(String.format("%-20s %-10s\n", "DESTINO", "TIEMPO(H)"));
        sb.append("----------------------------------\n");

        resultados.forEach((destino, costo) -> {
            String costoStr = (costo == Double.POSITIVE_INFINITY) ? "N/A" : String.format("%.2f", costo);
            sb.append(String.format("%-20s %-10s\n", destino, costoStr));
        });

        areaSalida.setText(sb.toString());
    }

    /** Ejecuta el algoritmo de Floyd-Warshall (Costos mínimos entre todos los pares). */
    private void ejecutarFloydWarshall() {
        if (!validarGrafo()) return;

        Map<String, Map<String, Double>> resultados = FloydWarshall.floydWarshall(logisticaGrafo);

        StringBuilder sb = new StringBuilder();
        sb.append("=== FLOYD-WARSHALL - Matriz adyaciencia===\n");
        java.util.List<String> nodos = new java.util.ArrayList<>(logisticaGrafo.getListaAdyacencia().keySet());
        Collections.sort(nodos);

        // Cabecera formateada
        sb.append(String.format("%-15s", "ORIG\\DEST"));
        for (String n : nodos) sb.append(String.format("%-10s", n.length() > 8 ? n.substring(0,8) : n));
        sb.append("\n");

        // Filas formateadas
        for (String i : nodos) {
            sb.append(String.format("%-15s", i.length() > 14 ? i.substring(0,14) : i));
            for (String j : nodos) {
                double val = resultados.getOrDefault(i, Collections.emptyMap()).getOrDefault(j, Double.POSITIVE_INFINITY);
                String valStr = (val == Double.POSITIVE_INFINITY) ? "X" : (val == 0 ? "-" : String.format("%.1f", val));
                sb.append(String.format("%-10s", valStr));
            }
            sb.append("\n");
        }

        areaSalida.setText(sb.toString());
    }

    private void ejecutarPrim() {
        if (!validarGrafo()) return;
        String origen = txtParametroOrigen.getText().trim();

        if (!logisticaGrafo.getListaAdyacencia().containsKey(origen)) {
            alertarError("Origen no válido", "El nodo '" + origen + "' no existe en la red.");
            return;
        }

        // NOTA: AlgoritmoPrim.algoritmoPrim debe actualizarse para devolver 'ResultadoMST'
        // (Ver paso 2 más abajo)
        ResultadoMST resultado = AlgoritmoPrim.algoritmoPrim(logisticaGrafo, origen);

        StringBuilder sb = new StringBuilder();
        sb.append("=== PRIM ===\n");        
        // Encabezado de la tabla (Similar a Dijkstra)
        sb.append(String.format("%-20s %-20s %-10s\n", "DESDE", "HASTA", "COSTO"));
        sb.append("----------------------------------------------------\n");

        // Iterar sobre las aristas obtenidas
        for (AristaMST arista : resultado.getAristas()) {
            sb.append(String.format("%-20s %-20s %-10s\n", 
                arista.origen, 
                arista.destino, 
                String.format("%.2f", arista.peso)
            ));
        }

        sb.append("----------------------------------------------------\n");
        sb.append("Costo Total de Interconexión (MST): " + String.format("%.2f", resultado.getCostoTotal()) + " unidades.\n");
        
        areaSalida.setText(sb.toString());
    }

    /** Ejecuta la búsqueda de un producto por ID en el Árbol AVL. */
    private void ejecutarBusquedaAVL() {
        if (!validarAVL()) return;

        try {
            int id = Integer.parseInt(txtParametroBusqueda.getText().trim());
            NodoAVL nodo = inventarioAVL.buscar(id);

            StringBuilder sb = new StringBuilder();
            sb.append("=== STOCK ===\n\n");
            if (nodo != null) {
                sb.append("Estado: ENCONTRADO\n");
                sb.append("ID Producto : ").append(nodo.clave).append("\n");
                sb.append("Descripción : ").append(nodo.datos.get("nombre")).append("\n");
                sb.append("Existencia  : ").append(nodo.datos.get("stock")).append(" unidades\n");
                sb.append("Ubicación   : ").append(nodo.datos.get("ubicacion")).append("\n");
            } else {
                sb.append("Estado: NO ENCONTRADO\n");
                sb.append("El producto con ID ").append(id).append(" no existe en la base de datos.\n");
            }
            areaSalida.setText(sb.toString());

        } catch (NumberFormatException e) {
            alertarError("Formato Inválido", "El ID del producto debe ser un número entero.");
        }
    }

    /** Genera el reporte de inventario ordenado usando el recorrido Inorden del AVL. */
    private void ejecutarRecorridoInorden() {
        if (!validarAVL()) return;

        String reporte = inventarioAVL.getInordenReporte();
        areaSalida.setText("=== REPORTE DE INVENTARIO (Inorden) ===\n\n" + reporte);
    }

    private void ejecutarRecorridoPostorden()
    {
        if(!validarAVL())
            return;

        String reporte = inventarioAVL.getPostordenReporte();
        areaSalida.setText("=== REPORTE DE INVENTARIO (Postorden) ===\n\n" + reporte);
    }

    private void ejecutarRecorridoPreorden()
    {
        if(!validarAVL())
            return;

        String reporte = inventarioAVL.getPreordenReporte();
        areaSalida.setText("=== REPORTE DE INVENTARIO (Preorden) ===\n\n" + reporte);
    }
    
    // Validaciones
    private boolean validarGrafo() {
        if (logisticaGrafo == null || logisticaGrafo.getListaAdyacencia().isEmpty()) {
            alertarError("Datos no cargados", "La base de datos de Rutas (Grafo) no ha sido inicializada.\nPor favor cargue los archivos en la sección 1.");
            return false;
        }
        return true;
    }

    private boolean validarAVL() {
        if (inventarioAVL == null || inventarioAVL.raiz == null) {
            alertarError("Datos no cargados", "La base de datos de Inventario (AVL) no ha sido inicializada.\nPor favor cargue los archivos en la sección 1.");
            return false;
        }
        return true;
    }

    /** Muestra un cuadro de diálogo de error. */
    private void alertarError(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    /** Retorna el CSS embebido para los estilos de la GUI. */
    private String obtenerEstilosCSS() {
        return
                ".root { -fx-font-family: 'Segoe UI', sans-serif; }" +
                        ".label { -fx-text-fill: #2c3e50; }" +
                        ".text-field { -fx-background-radius: 4; -fx-border-color: #bdc3c7; -fx-border-radius: 4; -fx-padding: 6; }" +
                        ".text-field:focused { -fx-border-color: #3498db; }" +

                        ".section-header { -fx-font-size: 15px; -fx-font-weight: bold; -fx-text-fill: #34495e; -fx-padding: 5 0 5 0; }" +

                        ".btn-primary { -fx-background-color: #27ae60; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 4; -fx-cursor: hand; }" +
                        ".btn-primary:hover { -fx-background-color: #2ecc71; }" +

                        ".btn-action { -fx-background-color: #2980b9; -fx-text-fill: white; -fx-background-radius: 4; -fx-cursor: hand; -fx-padding: 8 15 8 15; }" +
                        ".btn-action:hover { -fx-background-color: #3498db; }" +
                        ".btn-action:pressed { -fx-background-color: #2573a7; }";
    }
}