# 2026_ITESS_TICS_PDM_NCBVC

Repositorio de programas de la materia Planificación de Movimientos 2026

# 🎯 Detector de Punto en Polígono Convexo

**Una herramienta interactiva de geometría computacional desarrollada en JavaFX.**

Este proyecto permite construir polígonos convexos con precisión matemática y determinar si un punto específico se encuentra dentro o fuera del área delimitada, proporcionando una demostración visual basada en la teoría de áreas.

---

## 🛠️ Características Principales

- **Entrada Dual**: Define vértices mediante clics en el lienzo o ingresando coordenadas exactas por teclado.
- **Plano Cartesiano Real**: Soporte para coordenadas positivas y negativas con el origen $(0,0)$ centrado en la pantalla.
- **Visualización Dinámica**: Cuadrícula de referencia y etiquetas automáticas para cada punto generado.
- **Demostración Matemática**: Resaltado del triángulo de prueba y cálculo de áreas en tiempo real para verificar la pertenencia.

---

## 🧠 Lógica Matemática

El programa utiliza un algoritmo optimizado de **Búsqueda Binaria sobre Ángulos** con una complejidad de $O(\log n)$.

### 1. Triangulación en Abanico

El polígono se descompone en $n-2$ triángulos partiendo de un punto ancla $p_0$ (el vértice con el menor valor de $y$).

[Image of polygon triangulation diagram]

### 2. Validación por Suma de Áreas

Para demostrar si el punto $P$ está dentro de la "rebanada" (triángulo) seleccionada $ABC$, se comparan dos valores:

- $S_1$: Área del triángulo original del polígono.
- $S_2$: Suma de las áreas de los tres triángulos formados entre el punto de prueba y los vértices de la rebanada ($PAB + PBC + PCA$).

$$S_1 = S_2 \implies \text{Punto ADENTRO}$$
$$S_1 < S_2 \implies \text{Punto AFUERA}$$

---

## 🏗️ Arquitectura del Sistema

El proyecto sigue una estructura de **Programación Orientada a Objetos (POO)** dividida en tres componentes clave:

| Clase                         | Responsabilidad                                                                               |
| :---------------------------- | :-------------------------------------------------------------------------------------------- |
| **`Point.java`**              | Representa coordenadas $(x, y)$ y gestiona operaciones vectoriales como el **Producto Cruz**. |
| **`Polygon.java`**            | Almacena los vértices, asegura el sentido anti-horario y ejecuta el algoritmo de pertenencia. |
| **`ModeladoGeometrico.java`** | Gestiona la interfaz JavaFX, la entrada del usuario y el renderizado en el `Canvas`.          |

---

## 🚀 Cómo usar el programa

1.  **Añadir Vértices**: Escribe las coordenadas $X, Y$ y presiona **Añadir Vértice** (o haz clic directamente en el plano).
2.  **Cerrar Polígono**: Presiona el botón **Cerrar Polígono** para
