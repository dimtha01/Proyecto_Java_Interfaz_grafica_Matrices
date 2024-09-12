package proyecto;

import java.awt.*; // Importa las clases necesarias para manejar gráficos y componentes de la interfaz.
import java.awt.event.*; // Importa las clases para manejar eventos como acciones de botones.
import java.math.BigDecimal; // Importa la clase BigDecimal para manejar números con alta precisión.
import java.math.MathContext; // Importa la clase MathContext para el manejo del contexto matemático en BigDecimal.
import java.text.DecimalFormat; // Importa la clase DecimalFormat para formatear números.
import javax.swing.*; // Importa las clases de la biblioteca Swing para crear la interfaz gráfica.
import java.util.Random; // Importa la clase Random para generar números aleatorios.

public class Ventana extends JFrame {
    // Define constantes para límites de tamaño y rango de las matrices.
    private static final int MAX_TAMANO = 9;
    private static final int MIN_TAMANO = 1;
    private static final int MAX_RANGO = 1000;
    private static final int MIN_RANGO = -1000;
    private static final Font FUENTE = new Font("Arial", Font.PLAIN, 14); // Define la fuente para el texto.

    // Declara los componentes de la interfaz.
    private JLabel label1, label2;
    private JButton buttonGenerar;
    private JTextField textField1, textField2;
    private JTextArea textAreaMatrices, textAreaResultados;
    private JPanel formPanel;
    private int[][] matrizSuma; // Variable para almacenar la matriz resultante de la suma.

    // Constructor de la clase Ventana.
    public Ventana() {
        configurarVentana(); // Configura la ventana principal.
        inicializarComponentes(); // Inicializa los componentes de la interfaz.
        agregarAcciones(); // Agrega las acciones a los botones y campos de texto.
    }

    // Configura los parámetros de la ventana.
    private void configurarVentana() {
        this.setTitle("Interfaz Gráfica"); // Establece el título de la ventana.
        this.setSize(1080, 720); // Establece el tamaño de la ventana.
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Configura el cierre de la ventana.
        this.setResizable(false); // Desactiva la capacidad de redimensionar la ventana.
        this.setLocationRelativeTo(null); // Centra la ventana en la pantalla.
        getContentPane().setLayout(new BorderLayout()); // Define el layout principal de la ventana.
    }

    // Inicializa los componentes de la interfaz.
    private void inicializarComponentes() {
        formPanel = new JPanel(new GridBagLayout()); // Crea un panel con GridBagLayout para organizar los componentes.
        GridBagConstraints gbc = new GridBagConstraints(); // Configura las restricciones para el GridBagLayout.
        gbc.insets = new Insets(10, 10, 10, 10); // Define los márgenes entre los componentes.

        // Configura las etiquetas y campos de texto.
        label1 = crearEtiqueta("Ingrese el tamaño de las Matrices (n x n): ", gbc, 0, 0);
        textField1 = crearCampoTexto(gbc, 1, 0);
        buttonGenerar = crearBoton("Generar Matrices", gbc, 2, 0);
        label2 = crearEtiqueta("Ingrese columna para filtrar positivos: ", gbc, 3, 0);
        textField2 = crearCampoTexto(gbc, 4, 0);

        // Añade el panel al contenedor de la ventana.
        getContentPane().add(formPanel, BorderLayout.NORTH);
        textAreaMatrices = crearTextoArea(15, 50); // Crea un área de texto para mostrar las matrices.
        textAreaResultados = crearTextoArea(10, 50); // Crea un área de texto para mostrar los resultados.

        // Añade scroll a las áreas de texto.
        JScrollPane scrollPaneMatrices = new JScrollPane(textAreaMatrices);
        JScrollPane scrollPaneResultados = new JScrollPane(textAreaResultados);
        getContentPane().add(scrollPaneMatrices, BorderLayout.CENTER); // Ubica el área de matrices en el centro.
        getContentPane().add(scrollPaneResultados, BorderLayout.SOUTH); // Ubica el área de resultados en la parte inferior.
    }

    // Método para crear una etiqueta con texto.
    private JLabel crearEtiqueta(String texto, GridBagConstraints gbc, int x, int y) {
        JLabel etiqueta = new JLabel(texto); // Crea una etiqueta con el texto proporcionado.
        etiqueta.setFont(FUENTE); // Establece la fuente para la etiqueta.
        gbc.gridx = x; // Establece la posición x en el GridBagLayout.
        gbc.gridy = y; // Establece la posición y en el GridBagLayout.
        formPanel.add(etiqueta, gbc); // Añade la etiqueta al panel.
        return etiqueta; // Retorna la etiqueta creada.
    }

    // Método para crear un campo de texto.
    private JTextField crearCampoTexto(GridBagConstraints gbc, int x, int y) {
        JTextField campoTexto = new JTextField(9); // Crea un campo de texto con un tamaño de 9 caracteres.
        campoTexto.setFont(FUENTE); // Establece la fuente para el campo de texto.
        gbc.gridx = x; // Establece la posición x.
        gbc.gridy = y; // Establece la posición y.
        formPanel.add(campoTexto, gbc); // Añade el campo de texto al panel.
        return campoTexto; // Retorna el campo de texto.
    }

    // Método para crear un botón.
    private JButton crearBoton(String texto, GridBagConstraints gbc, int x, int y) {
        JButton boton = new JButton(texto); // Crea un botón con el texto proporcionado.
        boton.setFont(FUENTE); // Establece la fuente para el botón.
        boton.setEnabled(false); // Desactiva el botón inicialmente.
        gbc.gridx = x; // Establece la posición x.
        gbc.gridy = y; // Establece la posición y.
        formPanel.add(boton, gbc); // Añade el botón al panel.
        return boton; // Retorna el botón.
    }

    // Método para crear un área de texto.
    private JTextArea crearTextoArea(int filas, int columnas) {
        JTextArea textoArea = new JTextArea(filas, columnas); // Crea un área de texto con el número de filas y columnas.
        textoArea.setFont(FUENTE); // Establece la fuente para el área de texto.
        return textoArea; // Retorna el área de texto.
    }

    // Método para agregar acciones a los componentes.
    private void agregarAcciones() {
        buttonGenerar.addActionListener(e -> generarMatrices()); // Asocia la acción de generar matrices al botón.
        textField1.addKeyListener(new KeyAdapter() { // Añade un escuchador de eventos de teclado al campo de texto.
            @Override
            public void keyReleased(KeyEvent e) {
                validarEntrada(); // Llama al método para validar la entrada cuando se suelta una tecla.
            }
        });
    }

    // Método para validar la entrada del campo de texto.
    private void validarEntrada() {
        try {
            int n = Integer.parseInt(textField1.getText()); // Intenta convertir el texto del campo a un número entero.
            buttonGenerar.setEnabled(n >= MIN_TAMANO && n <= MAX_TAMANO); // Activa el botón si el número está dentro del rango permitido.
        } catch (NumberFormatException e) {
            buttonGenerar.setEnabled(false); // Desactiva el botón si la entrada no es un número válido.
        }
    }

    // Método para generar las matrices.
    private void generarMatrices() {
        try {
            int n = Integer.parseInt(textField1.getText()); // Convierte el tamaño de la matriz ingresado a entero.
            if (n < MIN_TAMANO || n > MAX_TAMANO) { // Verifica que el tamaño esté dentro del rango permitido.
                mostrarError("El tamaño de la matriz debe ser entre 1 y 9");
                return;
            }

            // Genera dos matrices aleatorias y las suma.
            int[][] matriz1 = generarMatrizAleatoria(n);
            int[][] matriz2 = generarMatrizAleatoria(n);
            matrizSuma = sumarMatrices(matriz1, matriz2);

            // Muestra las matrices generadas y realiza el procesamiento de resultados.
            mostrarMatrices(matriz1, matriz2);
            procesarResultados();

        } catch (NumberFormatException ex) {
            mostrarError("Por favor, ingrese un número válido"); // Muestra un mensaje de error si el tamaño no es válido.
        }
    }

    // Método para mostrar las matrices generadas en el área de texto.
    private void mostrarMatrices(int[][] matriz1, int[][] matriz2) {
        textAreaMatrices.setText(""); // Limpia el área de texto.
        textAreaMatrices.append("Matriz 1:\n");
        mostrarMatrizBonita(matriz1, textAreaMatrices); // Muestra la primera matriz.

        textAreaMatrices.append("\nMatriz 2:\n");
        mostrarMatrizBonita(matriz2, textAreaMatrices); // Muestra la segunda matriz.

        textAreaMatrices.append("\nMatriz Suma (Matriz 1 + Matriz 2):\n");
        mostrarMatrizBonita(matrizSuma, textAreaMatrices); // Muestra la suma de las matrices.
    }

    // Método que procesa los resultados y los muestra en el área de texto.
    private void procesarResultados() {
        int[] diagonalSecundaria = obtenerDiagonalSecundaria(matrizSuma); // Obtiene la diagonal secundaria de la matriz.
        int max = encontrarMaximo(diagonalSecundaria); // Encuentra el valor máximo en la diagonal secundaria.
        int min = encontrarMinimo(diagonalSecundaria); // Encuentra el valor mínimo en la diagonal secundaria.

        BigDecimal potencia = calcularPotencia(new BigDecimal(max), min); // Calcula la potencia del máximo elevado al mínimo.
        String notacionCientifica = notacionCientifica(potencia); // Convierte el resultado a notación científica.
        double promedioUltimaFila = calcularPromedioUltimaFila(); // Calcula el promedio de la última fila.

        // Muestra los resultados en el área de texto.
        textAreaResultados.setText("");
        textAreaResultados.append(crearLinea(90));
        textAreaResultados.append("POTENCIA ENTRE EL NÚMERO MAYOR Y MENOR DE LA DIAGONAL SECUNDARIA\n");
        textAreaResultados.append(crearLinea(90));
        textAreaResultados.append(String.format("Número mayor en la diagonal secundaria: %d\n", max));
        textAreaResultados.append(String.format("Número menor en la diagonal secundaria: %d\n", min));
        textAreaResultados.append(String.format("Resultado de la potencia (%.2f^%d): %s\n", (double) max, min, notacionCientifica));
        textAreaResultados.append(crearLinea(90));
        textAreaResultados.append("NÚMEROS POSITIVOS DE LA COLUMNA\n");
        textAreaResultados.append(crearLinea(90));
        filtrarColumna(); // Filtra los números positivos de la columna seleccionada.
        textAreaResultados.append(crearLinea(90));
        textAreaResultados.append("PROMEDIO DE LA ÚLTIMA FILA\n");
        textAreaResultados.append(crearLinea(90));
        textAreaResultados.append(String.format("Promedio de la última fila (multiplicación): %.2f\n", promedioUltimaFila));
        verificarEsquinasPrimos(); // Verifica si las esquinas de la matriz son números primos.
        textAreaResultados.append(crearLinea(90));
    }

    // Método para calcular una potencia dado una base y un exponente.
    private BigDecimal calcularPotencia(BigDecimal base, int exponente) {
        if (exponente == 0) {
            return BigDecimal.ONE; // Cualquier número elevado a la 0 es 1.
        }

        if (exponente < 0) {
            base = BigDecimal.ONE.divide(base, MathContext.DECIMAL128); // Inverso para exponentes negativos.
            exponente = -exponente; // Hacer el exponente positivo.
        }

        BigDecimal resultado = BigDecimal.ONE;
        for (int i = 0; i < exponente; i++) {
            resultado = resultado.multiply(base); // Multiplica la base por sí misma exponente veces.
        }

        return resultado; // Retorna el resultado de la potencia.
    }

    // Método para formatear un número en notación científica.
    private String notacionCientifica(BigDecimal potencia) {
        DecimalFormat formatoCientifico = new DecimalFormat("0.###E0"); // Configura el formato en notación científica.
        String resultadoCientifico = formatoCientifico.format(potencia); // Convierte el número a notación científica.
        return resultadoCientifico; // Retorna el número formateado.
    }

    // Muestra un cuadro de diálogo con un mensaje de error.
    private void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Error", JOptionPane.ERROR_MESSAGE); // Muestra un mensaje emergente.
    }

    // Método para crear una línea horizontal de caracteres.
    private String crearLinea(int num) {
        return "═".repeat(num) + "\n"; // Retorna una línea formada por el carácter '═'.
    }

    // Método para verificar si un número es primo.
    private boolean esPrimo(int numero) {
        if (numero <= 1) return false; // Los números menores o iguales a 1 no son primos.
        if (numero <= 3) return true; // Los números 2 y 3 son primos.
        if (numero % 2 == 0 || numero % 3 == 0) return false; // Excluye múltiplos de 2 y 3.
        for (int i = 5; i * i <= numero; i += 6) { // Comprueba los posibles divisores.
            if (numero % i == 0 || numero % (i + 2) == 0) return false;
        }
        return true; // Retorna verdadero si el número es primo.
    }

    // Método que verifica si las esquinas de la matriz son números primos.
    private void verificarEsquinasPrimos() {
        if (matrizSuma == null || matrizSuma.length == 0) return;

        int n = matrizSuma.length;
        int[] esquinas = {
            matrizSuma[0][0], // Esquina superior izquierda.
            matrizSuma[0][n - 1], // Esquina superior derecha.
            matrizSuma[n - 1][0], // Esquina inferior izquierda.
            matrizSuma[n - 1][n - 1] // Esquina inferior derecha.
        };

        textAreaResultados.append(crearLinea(90));
        textAreaResultados.append("ESQUINAS DE LA MATRIZ NÚMEROS PRIMOS\n");
        textAreaResultados.append(crearLinea(90));
        for (int i = 0; i < esquinas.length; i++) {
            int esquina = esquinas[i];
            textAreaResultados.append(String.format("El número en la esquina %d %s primo: %d\n",
                    i + 1, esPrimo(esquina) ? "es" : "no es", esquina)); // Muestra si cada esquina es o no primo.
        }
    }

    // Método para calcular el promedio de la última fila de la matriz.
    private double calcularPromedioUltimaFila() {
        if (matrizSuma == null || matrizSuma.length == 0) return 0.0;

        int n = matrizSuma[0].length;
        double producto = 1.0;

        for (int j = 0; j < n; j++) {
            producto *= matrizSuma[matrizSuma.length - 1][j]; // Multiplica todos los elementos de la última fila.
        }

        return producto / n; // Retorna el promedio de la última fila.
    }

    // Método para generar una matriz aleatoria.
    private int[][] generarMatrizAleatoria(int n) {
        Random rand = new Random(); // Crea un generador de números aleatorios.
        int[][] matriz = new int[n][n]; // Crea una matriz de tamaño n x n.

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                matriz[i][j] = rand.nextInt((MAX_RANGO - MIN_RANGO) + 1) + MIN_RANGO; // Genera un número aleatorio en el rango.
            }
        }

        return matriz; // Retorna la matriz generada.
    }

    // Método para sumar dos matrices.
    private int[][] sumarMatrices(int[][] matriz1, int[][] matriz2) {
        int n = matriz1.length;
        int[][] suma = new int[n][n]; // Crea una nueva matriz para almacenar la suma.

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                suma[i][j] = matriz1[i][j] + matriz2[i][j]; // Suma los elementos correspondientes de ambas matrices.
            }
        }

        return suma; // Retorna la matriz suma.
    }

    // Método para obtener la diagonal secundaria de una matriz.
    private int[] obtenerDiagonalSecundaria(int[][] matriz) {
        int n = matriz.length;
        int[] diagonal = new int[n]; // Crea un arreglo para almacenar los elementos de la diagonal secundaria.

        for (int i = 0; i < n; i++) {
            diagonal[i] = matriz[i][n - 1 - i]; // Asigna los valores de la diagonal secundaria.
        }

        return diagonal; // Retorna la diagonal secundaria.
    }

    // Método para encontrar el valor máximo en un arreglo.
    private int encontrarMaximo(int[] arreglo) {
        int max = arreglo[0]; // Inicializa el valor máximo con el primer elemento.
        for (int valor : arreglo) {
            if (valor > max) max = valor; // Actualiza el valor máximo si se encuentra uno mayor.
        }
        return max; // Retorna el valor máximo.
    }

    // Método para encontrar el valor mínimo en un arreglo.
    private int encontrarMinimo(int[] arreglo) {
        int min = arreglo[0]; // Inicializa el valor mínimo con el primer elemento.
        for (int valor : arreglo) {
            if (valor < min) min = valor; // Actualiza el valor mínimo si se encuentra uno menor.
        }
        return min; // Retorna el valor mínimo.
    }

    // Método para filtrar y mostrar los números positivos de una columna específica.
    private void filtrarColumna() {
        try {
            String inputColumna = textField2.getText(); // Obtiene el número de columna ingresado por el usuario.
            if (inputColumna.isEmpty()) {
                textAreaResultados.append("Por favor, ingrese un número de columna para filtrar.\n");
                return;
            }

            int columna = Integer.parseInt(inputColumna); // Convierte el número de columna a entero.
            if (columna < 1 || columna > matrizSuma.length) {
                textAreaResultados.append("La columna ingresada no existe en la matriz.\n");
                return;
            }

            // Filtra y muestra los números positivos de la columna seleccionada.
            StringBuilder resultados = new StringBuilder();
            resultados.append("Números positivos en la columna ").append(columna).append(": ");
            boolean hayPositivos = false;
            for (int i = 0; i < matrizSuma.length; i++) {
                int valor = matrizSuma[i][columna - 1];
                if (valor > 0) {
                    resultados.append(valor).append(", ");
                    hayPositivos = true; // Indica que hay al menos un número positivo.
                }
            }
            if (!hayPositivos) {
                resultados.append("No hay números positivos en esta columna."); // Si no hay números positivos, lo indica.
            }
            textAreaResultados.append(resultados.toString());
            textAreaResultados.append("\n");

        } catch (NumberFormatException e) {
            textAreaResultados.append("Por favor, ingrese un número de columna válido.\n");
        }
    }

    // Método para mostrar una matriz en el área de texto de forma visualmente clara.
    private void mostrarMatrizBonita(int[][] matriz, JTextArea textArea) {
        int n = matriz.length;

        // Añade la parte superior de la tabla.
        textArea.append("+");
        for (int i = 0; i < n; i++) {
            textArea.append("-----------------+");
        }
        textArea.append("\n");

        // Añade las filas de la matriz.
        for (int[] fila : matriz) {
            textArea.append("|");
            for (int valor : fila) {
                textArea.append(String.format(" %5d\t|", valor)); // Formatea los valores para que se vean alineados.
            }
            textArea.append("\n");

            // Añade la separación entre filas.
            textArea.append("+");
            for (int i = 0; i < n; i++) {
                textArea.append("-----------------+");
            }
            textArea.append("\n");
        }
    }
}
