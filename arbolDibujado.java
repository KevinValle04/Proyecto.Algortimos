import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class arbolDibujado extends Pane {
    private ArbolAVL arbol;
    private double radio = 25;
    private double vGap = 60;
    
    // Mapear el numero de visita
    private Map<Integer, Integer> ordenVisita = new HashMap<>();

    public arbolDibujado(ArbolAVL arbol) {
        this.arbol = arbol;
        this.setStyle("-fx-background-color: #ecf0f1;");
    }

    /**
     *Se define el orden en el cual se visitaron los campos
     */
    public void setOrdenVisita(List<Integer> listaIdsOrdenados) {
        ordenVisita.clear();
        for (int i = 0; i < listaIdsOrdenados.size(); i++) {
            ordenVisita.put(listaIdsOrdenados.get(i), i + 1);
        }
    }

    public void dibujar() {
        this.getChildren().clear();
        if (arbol.raiz != null) {
            double anchoInicial = this.getWidth();
            dibujarRecursivo(arbol.raiz, anchoInicial / 2, 50, anchoInicial / 4);
        }
    }

    private void dibujarRecursivo(NodoAVL nodo, double x, double y, double hGap) {
        // conexiones entre los grafos
        if (nodo.izquierda != null) {
            double xIzquierda = x - hGap;
            double yIzquierda = y + vGap;
            Line linea = new Line(x, y, xIzquierda, yIzquierda);
            linea.setStroke(Color.GRAY);
            linea.setStrokeWidth(2);
            this.getChildren().add(linea);
            dibujarRecursivo(nodo.izquierda, xIzquierda, yIzquierda, hGap / 2);
        }
        if (nodo.derecha != null) {
            double xDerecha = x + hGap;
            double yDerecha = y + vGap;
            Line linea = new Line(x, y, xDerecha, yDerecha);
            linea.setStroke(Color.GRAY);
            linea.setStrokeWidth(2);
            this.getChildren().add(linea);
            dibujarRecursivo(nodo.derecha, xDerecha, yDerecha, hGap / 2);
        }

        // Nodo principal
        Circle circulo = new Circle(x, y, radio);
        circulo.setFill(Color.web("#D175FF"));
        circulo.setStroke(Color.web("#D175FF"));
        circulo.setStrokeWidth(3);

        Text texto = new Text(String.valueOf(nodo.clave));
        texto.setFill(Color.WHITE);
        texto.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        texto.setX(x - texto.getLayoutBounds().getWidth() / 2);
        texto.setY(y + texto.getLayoutBounds().getHeight() / 4);

        this.getChildren().addAll(circulo, texto);

        // circulo para ver como se ordenan
        if (ordenVisita.containsKey(nodo.clave)) {
            int paso = ordenVisita.get(nodo.clave);
            
            double badgeX = x + 15;
            double badgeY = y - 15;

            Circle badgeBg = new Circle(badgeX, badgeY, 10);
            badgeBg.setFill(Color.web("#e74c3c")); 
            badgeBg.setStroke(Color.WHITE);
            badgeBg.setStrokeWidth(1);

            Text badgeText = new Text(String.valueOf(paso));
            badgeText.setFill(Color.WHITE);
            badgeText.setFont(Font.font("Arial", FontWeight.BOLD, 10));
            badgeText.setX(badgeX - badgeText.getLayoutBounds().getWidth() / 2);
            badgeText.setY(badgeY + badgeText.getLayoutBounds().getHeight() / 4);

            this.getChildren().addAll(badgeBg, badgeText);
        }
    }
}