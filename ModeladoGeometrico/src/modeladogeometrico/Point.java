/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modeladogeometrico;

/**
 *
 * @author karma
 */
public class Point {

    // definición de coordenadas en double, mayor precisión
    public double x, y;

    /**
     * Constructor de la clase. Inicializa el objeto asignando las coordenadas
     * recibidas a los atributos internos.
     */
    public Point(double x, double y) {
        this.x = x; // 'this' diferencia el atributo de la clase del parámetro recibido 
        this.y = y;
    }

    /**
     * Implementa la fórmula del Producto Cruz. Calcula el Producto Cruz entre
     * los vectores (A - esta instancia) y (B - esta instancia). Es equivalente
     * a la función cross(a, b) del documento. long long cross(const pt &a,
     * const pt &b) const { return (a-*this).cross(b-*this); } Esta operación
     * sustituye la fórmula de la recta (ax + by + c). El resultado nos dirá la
     * orientación: si es positivo, negativo o cero.
     */
    public double crossProduct(Point a, Point b) {
        // Esta es la implementación de (a-this) x (b-this)
        // Realiza la resta de vectores y el producto cruz en una sola línea
        return (a.x - this.x) * (b.y - this.y) - (a.y - this.y) * (b.x - this.x);
    }

    /**
     * Calcula el cuadrado de la distancia, útil para comparar longitudes sin
     * usar raíces.
     */
    public double sqrLen() {
        return this.x * this.x + this.y * this.y;
    }

    public Point minus(Point other) {
        return new Point(this.x - other.x, this.y - other.y);
    }
}
