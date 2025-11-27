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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.stage.FileChooser; 
import java.io.File;

/**
 * Clase principal con la Interfaz Gráfica de Usuario (JavaFX) para el Optimizador Logístico.
 */
public class OptimizadorLogisticoFX extends Application {

    private GrafoLogistica logisticaGrafo = null;
    private ArbolAVL inventarioAVL = null;

    private VBox contenedorResultados; 
    private TextField txtRutaGrafo;
    private TextField txtRutaAVL;
    private TextField txtParametroOrigen;
    private TextField txtParametroBusqueda;
    private CheckBox chkDijkstraRecursivo;

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
        Label lblRuta = new Label("Archivo de Rutas (.csv):");
        
        // Usamos un HBox para poner el texto y el botón uno al lado del otro
        HBox filaGrafo = new HBox(5); 
        txtRutaGrafo = new TextField(); 
       txtRutaGrafo.setPrefWidth(220);
        
        Button btnExaminarGrafo = new Button("📂");
        btnExaminarGrafo.setOnAction(e -> seleccionarArchivo(txtRutaGrafo)); 
        
        filaGrafo.getChildren().addAll(txtRutaGrafo, btnExaminarGrafo);
        group1.getChildren().addAll(lblRuta, filaGrafo);


        VBox group2 = new VBox(5);
        Label lblInv = new Label("Archivo de Inventario (.csv):");
        
        HBox filaAVL = new HBox(5);
        txtRutaAVL = new TextField();
        txtRutaAVL.setPrefWidth(220);

        Button btnExaminarAVL = new Button("📂");
        btnExaminarAVL.setOnAction(e -> seleccionarArchivo(txtRutaAVL)); 

        filaAVL.getChildren().addAll(txtRutaAVL, btnExaminarAVL);
        group2.getChildren().addAll(lblInv, filaAVL);


        Button btnCargar = new Button("Cargar Archivos");
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

        chkDijkstraRecursivo = new CheckBox("Usar Modo Recursivo ");

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

        box.getChildren().addAll(groupOrigen, new Label("Algoritmos:"),chkDijkstraRecursivo , btnDijkstra, btnFloyd, btnPrim);
        return box;
    }

   private VBox crearContenidoRecorridoOrdenados() {
        VBox box = new VBox(10);
        
        //  Inorden
        Button btnReporte = new Button("Ver Estructura (Inorden)");
        btnReporte.getStyleClass().add("btn-action");
        btnReporte.setMaxWidth(Double.MAX_VALUE);
        btnReporte.setOnAction(e -> mostrarArbolVisual("Estructura (Inorden)", "INORDEN"));

        //  Postorden
        Button btnReporte2 = new Button("Ver Estructura (Postorden)");
        btnReporte2.getStyleClass().add("btn-action");
        btnReporte2.setMaxWidth(Double.MAX_VALUE);
        btnReporte2.setOnAction(e -> mostrarArbolVisual("Estructura (Postorden)", "POSTORDEN"));

        //  Preorden
        Button btnReporte3 = new Button("Ver Estructura (Preorden)");
        btnReporte3.getStyleClass().add("btn-action");
        btnReporte3.setMaxWidth(Double.MAX_VALUE);
        btnReporte3.setOnAction(e -> mostrarArbolVisual("Estructura (Preorden)", "PREORDEN"));

        box.getChildren().addAll(btnReporte, btnReporte2, btnReporte3);
        return box;
    }

    /** Contenido de la sección de gestión de Inventario AVL. */
private VBox crearContenidoAVL() {
    VBox box = new VBox(10);

    // Busqueda
    box.getChildren().addAll(new Label("ID Producto:"), txtParametroBusqueda = new TextField(""));
    Button btnBuscar = new Button("Consultar Stock");
    btnBuscar.getStyleClass().add("btn-action");
    btnBuscar.setMaxWidth(Double.MAX_VALUE);
    btnBuscar.setOnAction(e -> ejecutarBusquedaAVL());

    box.getChildren().add(new Separator());

    // Alta
    Label lblAlta = new Label("Nuevo Ingreso:");
    lblAlta.setStyle("-fx-font-weight: bold; -fx-text-fill:");

    TextField txtNewID = new TextField(); txtNewID.setPromptText("Ingresa ID");
    TextField txtNewNom = new TextField(); txtNewNom.setPromptText("Ingresa nombre producto");
    TextField txtNewStock = new TextField(); txtNewStock.setPromptText("Ingresa cantidad");
    TextField txtNewUbi = new TextField(); txtNewUbi.setPromptText("Ingresa ubicacion");

    Button btnInsertar = new Button("Agregar al Inventario");
    btnInsertar.getStyleClass().add("btn-primary");
    btnInsertar.setMaxWidth(Double.MAX_VALUE);

    // insersion
    btnInsertar.setOnAction(e -> {
        if (!validarAVL()) return;
        try {
            int id = Integer.parseInt(txtNewID.getText());
            int stock = Integer.parseInt(txtNewStock.getText());
            String nombre = txtNewNom.getText();
            String ubi = txtNewUbi.getText();

            if (nombre.isEmpty()) { alertarError("Error", "Nombre requerido"); return; }

            Map<String, Object> datos = new HashMap<>();
            datos.put("nombre", nombre);
            datos.put("stock", stock);
            datos.put("ubicacion", ubi);

            inventarioAVL.insertar(id, datos); 

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Éxito");
            alert.setHeaderText(null);
            alert.setContentText("Producto #" + id + " agregado correctamente.");
            alert.showAndWait();

            txtNewID.clear(); txtNewNom.clear(); txtNewStock.clear(); txtNewUbi.clear();
            List<Map<String, String>> datosActualizados = inventarioAVL.obtenerRecorridoEnLista("INORDEN");
            mostrarTablaDin(Arrays.asList("ID", "PRODUCTO", "STOCK", "UBICACIÓN"), FXCollections.observableArrayList(datosActualizados));

        } catch (NumberFormatException ex) {
            alertarError("Error", "ID y Stock deben ser números enteros.");
        }
    });

    box.getChildren().addAll(btnBuscar, lblAlta, txtNewID, txtNewNom, txtNewStock, txtNewUbi, btnInsertar);
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

        contenedorResultados = new VBox();
        VBox.setVgrow(contenedorResultados, Priority.ALWAYS);

        box.getChildren().addAll(lblTitulo,contenedorResultados);

        return box;
    }


    /** Carga los datos de Grafo y AVL desde los archivos CSV. */
    private void cargarDatos() {
        String rutaGrafo = txtRutaGrafo.getText();
        logisticaGrafo = new GrafoLogistica();
        try {
            logisticaGrafo.cargarDesdeCSV(rutaGrafo);
        } catch (Exception e) {
            alertarError("Error Carga Grafo", "No se pudo cargar el archivo de rutas:\n" + e.getMessage());
            e.printStackTrace(); 
        }

        String rutaAVL = txtRutaAVL.getText();
        inventarioAVL = new ArbolAVL();
        try {
            inventarioAVL.cargarDesdeCSV(rutaAVL);
        } catch (Exception e) {
            alertarError("Error Carga Inventario", "No se pudo cargar el archivo de inventario:\n" + e.getMessage());
            e.printStackTrace();
        }
        
        if (inventarioAVL.raiz != null && !logisticaGrafo.getListaAdyacencia().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Carga Exitosa");
            alert.setHeaderText(null);
            alert.setContentText("¡Datos cargados correctamente!");
            alert.showAndWait();
        }
    }

    /** Ejecuta el algoritmo de Dijkstra (Ruta más corta desde un origen). */
    private void ejecutarDijkstra() {
        if (!validarGrafo()) return;

        String origen = txtParametroOrigen.getText().trim();

        if (!logisticaGrafo.getListaAdyacencia().containsKey(origen)) {
            alertarError("Origen no válido", "El nodo '" + origen + "' no existe en la red actual.");
            return;
        }

        Map<String, Double> resultados;

        if (chkDijkstraRecursivo.isSelected()) {
            // Recursivo
            resultados = DijkstraRecursivo.dijkstraRecursivo(logisticaGrafo, origen);
            System.out.println("-> Usando lógica Recursiva");
        } else {
            //Interatico
            resultados = Dijkstra.dijkstraIterativo(logisticaGrafo, origen);
            System.out.println("-> Usando lógica Iterativa");
        }

        ObservableList<Map<String, String>> listaDatos = FXCollections.observableArrayList();

        resultados.forEach((destino, costo) -> {
            Map<String, String> fila = new HashMap<>();
            fila.put("DESTINO", destino);
            fila.put("TIEMPO (H)", (costo == Double.POSITIVE_INFINITY) ? "Inalcanzable" : String.format("%.2f", costo));
            listaDatos.add(fila);
        });

        mostrarTablaDin(Arrays.asList("DESTINO", "TIEMPO (H)"), listaDatos);
    }

    /** Ejecuta el algoritmo de Floyd-Warshall (Costos mínimos entre todos los pares). */
    private void ejecutarFloydWarshall() {
        if (!validarGrafo()) return;

        Map<String, Map<String, Double>> resultados = FloydWarshall.floydWarshall(logisticaGrafo);
        List<String> nodos = new ArrayList<>(logisticaGrafo.getListaAdyacencia().keySet());
        Collections.sort(nodos);

        List<String> columnas = new ArrayList<>();
        columnas.add("ORIGEN \\ DESTINO");
        columnas.addAll(nodos);

        ObservableList<Map<String, String>> listaDatos = FXCollections.observableArrayList();

        for (String origen : nodos) {
            Map<String, String> fila = new HashMap<>();
            fila.put("ORIGEN \\ DESTINO", origen); // Primera columna

            for (String destino : nodos) {
                double val = resultados.get(origen).get(destino);
                String valStr = (val == Double.POSITIVE_INFINITY) ? "X" : (val == 0 ? "-" : String.format("%.1f", val));
                fila.put(destino, valStr);
            }
            listaDatos.add(fila);
        }

        mostrarTablaDin(columnas, listaDatos);
    }

    private void ejecutarPrim() {
        if (!validarGrafo()) return;
        String origen = txtParametroOrigen.getText().trim();
                
        ResultadoMST resultado = AlgoritmoPrim.algoritmoPrim(logisticaGrafo, origen);
        ObservableList<Map<String, String>> listaDatos = FXCollections.observableArrayList();

        for (AristaMST arista : resultado.getAristas()) {
            Map<String, String> fila = new HashMap<>();
            fila.put("DESDE", arista.origen);
            fila.put("HASTA", arista.destino);
            fila.put("COSTO", String.format("%.2f", arista.peso));
            listaDatos.add(fila);
        }
        
        Map<String, String> filaTotal = new HashMap<>();
        filaTotal.put("DESDE", "TOTAL: ");
        filaTotal.put("HASTA", "---");
        filaTotal.put("COSTO", String.format("%.2f", resultado.getCostoTotal()));
        listaDatos.add(filaTotal);

        mostrarTablaDin(Arrays.asList("DESDE", "HASTA", "COSTO"), listaDatos);
    }

    /** Ejecuta la búsqueda de un producto por ID en el Árbol AVL. */
    private void ejecutarBusquedaAVL() {
        if (!validarAVL()) return;

        try {
            int id = Integer.parseInt(txtParametroBusqueda.getText().trim());
            NodoAVL nodo = inventarioAVL.buscar(id);
            
            ObservableList<Map<String, String>> listaDatos = FXCollections.observableArrayList();

            if (nodo != null) {
                Map<String, String> fila = new HashMap<>();
                fila.put("ID", String.valueOf(nodo.clave));
                fila.put("PRODUCTO", nodo.datos.get("nombre").toString());
                fila.put("STOCK", nodo.datos.get("stock").toString());
                fila.put("UBICACIÓN", nodo.datos.get("ubicacion").toString());
                listaDatos.add(fila);
                
                mostrarTablaDin(Arrays.asList("ID", "PRODUCTO", "STOCK", "UBICACIÓN"), listaDatos);
            } else {
                 alertarError("Sin resultados", "El producto con ID " + id + " no existe.");
            }

        } catch (NumberFormatException e) {
            alertarError("Formato Inválido", "El ID debe ser numérico.");
        }
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

    /**
     * Metodo para generar las tablas
     */
    private void mostrarTablaDin(List<String> nombresColumnas, ObservableList<Map<String, String>> datos) {
        contenedorResultados.getChildren().clear(); // Limpiar 

        TableView<Map<String, String>> tabla = new TableView<>(datos);
        tabla.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY); // Ajustar ancho
        VBox.setVgrow(tabla, Priority.ALWAYS);

        for (String colName : nombresColumnas) {
            TableColumn<Map<String, String>, String> col = new TableColumn<>(colName);
            col.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().get(colName)));
            tabla.getColumns().add(col);
        }

        contenedorResultados.getChildren().add(tabla);
    }

    /**
     * Muestra la tabla la pagina y la grafica en el emergente
     */
    private void mostrarArbolVisual(String tituloVentana, String tipoRecorrido) {
        if (!validarAVL()) 
            return;

        List<Map<String, String>> listaDatos = inventarioAVL.obtenerRecorridoEnLista(tipoRecorrido);

        ObservableList<Map<String, String>> datosTabla = FXCollections.observableArrayList(listaDatos);
        
        List<String> columnas = Arrays.asList("ID", "PRODUCTO", "STOCK", "UBICACIÓN");
        mostrarTablaDin(columnas, datosTabla);
        
        Label lblTituloTabla = new Label("Resultados: " + tituloVentana);
        lblTituloTabla.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #2c3e50; -fx-padding: 0 0 10 0;");
        contenedorResultados.getChildren().add(0, lblTituloTabla);

        Stage stageArbol = new Stage();
        stageArbol.setTitle(tituloVentana); 

        arbolDibujado visualizador = new arbolDibujado(inventarioAVL);
        
        List<Integer> idsOrdenados = new ArrayList<>();
        for (Map<String, String> item : listaDatos) {
            try {
                idsOrdenados.add(Integer.parseInt(item.get("ID")));
            } catch (Exception e) { }
        }
        visualizador.setOrdenVisita(idsOrdenados);

        Scene scene = new Scene(visualizador, 900, 700);
        stageArbol.setScene(scene);
        stageArbol.show();

        visualizador.dibujar();
        scene.widthProperty().addListener((obs, oldVal, newVal) -> visualizador.dibujar());
        scene.heightProperty().addListener((obs, oldVal, newVal) -> visualizador.dibujar());
    }

    /**
     * Abrir el explorador de archivos
     */
    private void seleccionarArchivo(TextField campoTexto) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Seleccionar Archivo CSV");
        
        fileChooser.getExtensionFilters().add(
            new FileChooser.ExtensionFilter("Archivos CSV (*.csv)", "*.csv")
        );
        
        File initialDir = new File(System.getProperty("user.home") + "/Documents");
        if (initialDir.exists()) {
            fileChooser.setInitialDirectory(initialDir);
        }

        Stage stage = (Stage) campoTexto.getScene().getWindow();
        File archivoSeleccionado = fileChooser.showOpenDialog(stage);
        
        if (archivoSeleccionado != null) {
            campoTexto.setText(archivoSeleccionado.getAbsolutePath());
        }
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