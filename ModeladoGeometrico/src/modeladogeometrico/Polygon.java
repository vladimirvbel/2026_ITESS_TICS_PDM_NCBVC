package modeladogeometrico;

import java.util.*;

public class Polygon {

    private List<Point> vertices;
    private List<Point> seq;      // Vectores p_i - p_0
    private Point p0;             // Punto ancla
    public Point debugP0, debugV1, debugV2; // Para guardar el triángulo actual
    public double debugS1, debugS2; // Áreas para demostración

    public Polygon(List<Point> puntos) {
        this.vertices = new ArrayList<>(puntos);
        asegurarSentidoAntiHorario(); // 0. Fundamental para que funcione la lógica
        this.seq = new ArrayList<>();
        prepare();
    }

    private void asegurarSentidoAntiHorario() {
        double areaSignada = 0;
        for (int i = 0; i < vertices.size(); i++) {
            Point cur = vertices.get(i);
            Point next = vertices.get((i + 1) % vertices.size());
            // En un plano matemático (Y arriba), area > 0 es Anti-Horario
            areaSignada += (cur.x * next.y - next.x * cur.y);
        }
        if (areaSignada < 0) {
            Collections.reverse(vertices);
        }
    }

    private void prepare() {
        int n = vertices.size();
        int pos = 0;
        // 1. Buscamos el punto p0 (punto con menor Y, luego menor X)
        for (int i = 1; i < n; i++) {
            if (vertices.get(i).y < vertices.get(pos).y
                    || (vertices.get(i).y == vertices.get(pos).y && vertices.get(i).x < vertices.get(pos).x)) {
                pos = i;
            }
        }
        Collections.rotate(vertices, -pos);
        this.p0 = vertices.get(0);

        for (int i = 1; i < n; i++) {
            seq.add(vertices.get(i).minus(p0));
        }
    }

    public boolean isInside(Point p) {
        Point query = p.minus(p0);
        int n = vertices.size();

        // Guardamos p0 para el dibujo
        this.debugP0 = p0;

        if (productoCruzVectores(seq.get(0), query) < -1e-9
                || productoCruzVectores(seq.get(n - 2), query) > 1e-9) {
            return false;
        }

        int l = 0, r = n - 2;
        while (r - l > 1) {
            int mid = (l + r) / 2;
            if (productoCruzVectores(seq.get(mid), query) >= 0) {
                l = mid;
            } else {
                r = mid;
            }
        }

        // Guardamos los vértices del triángulo analizado
        this.debugV1 = vertices.get(l + 1);
        this.debugV2 = vertices.get(l + 2);

        return pointInTriangle(debugP0, debugV1, debugV2, p);
    }

    // Auxiliar: Producto cruz de dos vectores (u_x * v_y - u_y * v_x)
    private double productoCruzVectores(Point a, Point b) {
        return a.x * b.y - a.y * b.x;
    }

    private boolean pointInTriangle(Point a, Point b, Point c, Point p) {
        // Área del triángulo del polígono (S1)
        this.debugS1 = Math.abs(areaTriangulo(a, b, c));

        // Suma de las áreas de los sub-triángulos (S2)
        this.debugS2 = Math.abs(areaTriangulo(p, a, b))
                + Math.abs(areaTriangulo(p, b, c))
                + Math.abs(areaTriangulo(p, c, a));

        // El punto está dentro si el área total es igual a la suma de las partes
        return Math.abs(debugS1 - debugS2) < 1e-7;
    }

    private double areaTriangulo(Point a, Point b, Point c) {
        return (a.x * (b.y - c.y) + b.x * (c.y - a.y) + c.x * (a.y - b.y)) / 2.0;
    }

}
