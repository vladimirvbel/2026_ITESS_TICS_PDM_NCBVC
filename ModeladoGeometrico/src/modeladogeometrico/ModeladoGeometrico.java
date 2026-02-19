package modeladogeometrico;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import java.util.ArrayList;
import java.util.List;

public class ModeladoGeometrico extends Application {

    private List<Point> puntosTemp = new ArrayList<>();
    private Polygon poligonoReal;
    private boolean poligonoCerrado = false;
    private GraphicsContext gc;

    // Constantes para centrar el plano
    private final double OFFSET_X = 400;
    private final double OFFSET_Y = 300;

    @Override
    public void start(Stage primaryStage) {
        Canvas canvas = new Canvas(800, 600);
        gc = canvas.getGraphicsContext2D();
        dibujarCuadricula();

        TextField campoX = new TextField();
        campoX.setPromptText("X");
        campoX.setPrefWidth(60);
        TextField campoY = new TextField();
        campoY.setPromptText("Y");
        campoY.setPrefWidth(60);

        Button btnAdd = new Button("Añadir Vértice");
        Button btnCerrar = new Button("Cerrar Polígono");
        Button btnProbar = new Button("Probar Punto");
        Button btnLimpiar = new Button("Limpiar");

        // LÓGICA 1: AÑADIR VÉRTICES (Teclado)
        btnAdd.setOnAction(e -> {
            if (poligonoCerrado) {
                return;
            }
            try {
                double xMath = Double.parseDouble(campoX.getText());
                double yMath = Double.parseDouble(campoY.getText());
                Point p = new Point(xMath + OFFSET_X, OFFSET_Y - yMath);
                puntosTemp.add(p);
                dibujarEstado();
            } catch (Exception ex) {
                System.out.println("Error en números");
            }
        });

        // LÓGICA 2: CERRAR
        btnCerrar.setOnAction(e -> {
            if (puntosTemp.size() > 2) {
                poligonoReal = new Polygon(new ArrayList<>(puntosTemp));
                poligonoCerrado = true;
                dibujarEstado();
                System.out.println("Polígono listo.");
            }
        });

        // LÓGICA 3: PROBAR PUNTO ESCRITO
        btnProbar.setOnAction(e -> {
            if (!poligonoCerrado) {
                return;
            }
            try {
                double xMath = Double.parseDouble(campoX.getText());
                double yMath = Double.parseDouble(campoY.getText());
                Point pPrueba = new Point(xMath + OFFSET_X, OFFSET_Y - yMath);

                boolean dentro = poligonoReal.isInside(pPrueba);
                dibujarEstado();
                dibujarDemostracion(pPrueba); // <-- Pasamos el punto de los cuadros de texto
                dibujarPunto(pPrueba, dentro ? Color.GREEN : Color.RED);
            } catch (Exception ex) {
                System.out.println("Error en coordenadas");
            }
        });

        // LÓGICA 4: MOUSE (Interactividad)
        canvas.setOnMouseClicked(event -> {
            Point pClick = new Point(event.getX(), event.getY());
            if (poligonoCerrado) {
                boolean dentro = poligonoReal.isInside(pClick);
                dibujarEstado();
                dibujarDemostracion(pClick);
                dibujarPunto(pClick, dentro ? Color.GREEN : Color.RED);
            }
        });

        btnLimpiar.setOnAction(e -> {
            puntosTemp.clear();
            poligonoCerrado = false;
            poligonoReal = null;
            dibujarEstado();
        });

        // Nuevo botón
        HBox controles = new HBox(10, new Label("X:"), campoX, new Label("Y:"), campoY,
                btnAdd, btnCerrar, btnProbar, btnLimpiar);
        controles.setPadding(new javafx.geometry.Insets(10));
        primaryStage.setScene(new Scene(new VBox(controles, canvas)));
        primaryStage.setTitle("Geometría de Precisión - Demostración Matemática");
        primaryStage.show();
    }

    private void dibujarEstado() {
        gc.clearRect(0, 0, 800, 600);
        dibujarCuadricula();
        for (int i = 0; i < puntosTemp.size(); i++) {
            dibujarPunto(puntosTemp.get(i), Color.BLUE);
            if (i > 0) {
                dibujarLinea(puntosTemp.get(i - 1), puntosTemp.get(i), Color.BLACK);
            }
        }
        if (poligonoCerrado) {
            dibujarLinea(puntosTemp.get(puntosTemp.size() - 1), puntosTemp.get(0), Color.BLACK);
        }
    }

    private void dibujarCuadricula() {
        gc.setStroke(Color.web("#F0F0F0"));
        for (int i = 0; i <= 800; i += 50) {
            gc.strokeLine(i, 0, i, 600);
        }
        for (int i = 0; i <= 600; i += 50) {
            gc.strokeLine(0, i, 800, i);
        }

        // EJES PRINCIPALES (El nuevo 0,0)
        gc.setStroke(Color.GRAY);
        gc.strokeLine(OFFSET_X, 0, OFFSET_X, 600); // Eje Y
        gc.strokeLine(0, OFFSET_Y, 800, OFFSET_Y); // Eje X
        gc.fillText("0,0", OFFSET_X + 5, OFFSET_Y - 5);
    }

    private void dibujarPunto(Point p, Color color) {
        gc.setFill(color);
        gc.fillOval(p.x - 4, p.y - 4, 8, 8);
        // Mostramos la coordenada matemática en pantalla
        String txt = "(" + (int) (p.x - OFFSET_X) + "," + (int) (OFFSET_Y - p.y) + ")";
        gc.fillText(txt, p.x + 5, p.y - 5);
    }

    private void dibujarLinea(Point p1, Point p2, Color color) {
        gc.setStroke(color);
        gc.setLineWidth(2);
        gc.strokeLine(p1.x, p1.y, p2.x, p2.y);
    }

    private void dibujarDemostracion(Point pPrueba) {
        if (poligonoReal == null || poligonoReal.debugV1 == null) {
            return;
        }

        Point p0 = poligonoReal.debugP0;
        Point v1 = poligonoReal.debugV1;
        Point v2 = poligonoReal.debugV2;

        // 1. Dibujar el abanico y el triángulo resaltado
        gc.setStroke(Color.LIGHTBLUE);
        gc.setLineDashes(5);
        for (Point v : puntosTemp) {
            gc.strokeLine(p0.x, p0.y, v.x, v.y);
        }

        gc.setFill(Color.rgb(255, 255, 0, 0.3));
        gc.fillPolygon(new double[]{p0.x, v1.x, v2.x}, new double[]{p0.y, v1.y, v2.y}, 3);
        gc.setLineDashes(0);

        // 2. MOSTRAR CÁLCULOS MATEMÁTICOS EN PANTALLA
        gc.setFill(Color.BLACK);
        gc.setFont(new javafx.scene.text.Font("Arial", 14));

        double s1 = poligonoReal.debugS1;
        double s2 = poligonoReal.debugS2;
        boolean esIgual = Math.abs(s1 - s2) < 1e-7;

        // Cuadro de texto informativo
        gc.fillText("DEMOSTRACIÓN MATEMÁTICA:", 580, 40);
        gc.fillText("Área Triángulo (S1): " + String.format("%.2f", s1), 580, 60);
        gc.fillText("Suma Sub-áreas (S2): " + String.format("%.2f", s2), 580, 80);

        gc.setFill(esIgual ? Color.DARKGREEN : Color.RED);
        gc.fillText(esIgual ? "S1 = S2 → ¡ESTÁ DENTRO!" : "S1 < S2 → ESTÁ FUERA", 580, 105);

        // Dibujar líneas desde el punto de prueba a los vértices del triángulo analizado
        gc.setStroke(Color.GRAY);
        gc.setLineWidth(0.5);
        gc.strokeLine(pPrueba.x, pPrueba.y, p0.x, p0.y);
        gc.strokeLine(pPrueba.x, pPrueba.y, v1.x, v1.y);
        gc.strokeLine(pPrueba.x, pPrueba.y, v2.x, v2.y);
    }

    public static void main(String[] args) {
        launch(args);
        try {
            System.setOut(new java.io.PrintStream(System.out, true, "UTF-8"));
        } catch (java.io.UnsupportedEncodingException e) {
            System.err.println("No se pudo configurar la consola a UTF-8");
        }
    }
}
