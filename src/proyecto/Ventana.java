package proyecto;

import java.awt.*;
import java.awt.event.*;
import java.math.BigDecimal;
import java.math.MathContext;
import java.text.DecimalFormat;

import javax.swing.*;
import java.util.Random;

public class Ventana extends JFrame {
    private static final int MAX_TAMANO = 9;
    private static final int MIN_TAMANO = 1;
    private static final int MAX_RANGO = 1000;
    private static final int MIN_RANGO = -1000;
    private static final Font FUENTE = new Font("Arial", Font.PLAIN, 14);

    private JLabel label1, label2;
    private JButton buttonGenerar;
    private JTextField textField1, textField2;
    private JTextArea textAreaMatrices, textAreaResultados;
    private JPanel formPanel;
    private int[][] matrizSuma;

    public Ventana() {
        configurarVentana();
        inicializarComponentes();
        agregarAcciones();
    }

    private void configurarVentana() {
        this.setTitle("Interfaz Gráfica");
        this.setSize(1080, 720);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.setLocationRelativeTo(null);
        getContentPane().setLayout(new BorderLayout());
    }

    private void inicializarComponentes() {
        formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        // Configurar etiquetas y campos de texto
        label1 = crearEtiqueta("Ingrese el tamaño de las Matrices (n x n): ", gbc, 0, 0);
        textField1 = crearCampoTexto(gbc, 1, 0);
        buttonGenerar = crearBoton("Generar Matrices", gbc, 2, 0);
        label2 = crearEtiqueta("Ingrese columna para filtrar positivos: ", gbc, 3, 0);
        textField2 = crearCampoTexto(gbc, 4, 0);

        getContentPane().add(formPanel, BorderLayout.NORTH);
        textAreaMatrices = crearTextoArea(15, 50);
        textAreaResultados = crearTextoArea(10, 50);

        JScrollPane scrollPaneMatrices = new JScrollPane(textAreaMatrices);
        JScrollPane scrollPaneResultados = new JScrollPane(textAreaResultados);
        getContentPane().add(scrollPaneMatrices, BorderLayout.CENTER);
        getContentPane().add(scrollPaneResultados, BorderLayout.SOUTH);
    }

    private JLabel crearEtiqueta(String texto, GridBagConstraints gbc, int x, int y) {
        JLabel etiqueta = new JLabel(texto);
        etiqueta.setFont(FUENTE);
        gbc.gridx = x;
        gbc.gridy = y;
        formPanel.add(etiqueta, gbc);
        return etiqueta;
    }

    private JTextField crearCampoTexto(GridBagConstraints gbc, int x, int y) {
        JTextField campoTexto = new JTextField(9);
        campoTexto.setFont(FUENTE);
        gbc.gridx = x;
        gbc.gridy = y;
        formPanel.add(campoTexto, gbc);
        return campoTexto;
    }

    private JButton crearBoton(String texto, GridBagConstraints gbc, int x, int y) {
        JButton boton = new JButton(texto);
        boton.setFont(FUENTE);
        boton.setEnabled(false);
        gbc.gridx = x;
        gbc.gridy = y;
        formPanel.add(boton, gbc);
        return boton;
    }

    private JTextArea crearTextoArea(int filas, int columnas) {
        JTextArea textoArea = new JTextArea(filas, columnas);
        textoArea.setFont(FUENTE);
        return textoArea;
    }

    private void agregarAcciones() {
        buttonGenerar.addActionListener(e -> generarMatrices());
        textField1.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                validarEntrada();
            }
        });
    }

    private void validarEntrada() {
        try {
            int n = Integer.parseInt(textField1.getText());
            buttonGenerar.setEnabled(n >= MIN_TAMANO && n <= MAX_TAMANO);
        } catch (NumberFormatException e) {
            buttonGenerar.setEnabled(false);
        }
    }

    private void generarMatrices() {
        try {
            int n = Integer.parseInt(textField1.getText());
            if (n < MIN_TAMANO || n > MAX_TAMANO) {
                mostrarError("El tamaño de la matriz debe ser entre 1 y 9");
                return;
            }

            int[][] matriz1 = generarMatrizAleatoria(n);
            int[][] matriz2 = generarMatrizAleatoria(n);
            matrizSuma = sumarMatrices(matriz1, matriz2);

            mostrarMatrices(matriz1, matriz2);
            procesarResultados();

        } catch (NumberFormatException ex) {
            mostrarError("Por favor, ingrese un número válido");
        }
    }

    private void mostrarMatrices(int[][] matriz1, int[][] matriz2) {
        textAreaMatrices.setText("");
        textAreaMatrices.append("Matriz 1:\n");
        mostrarMatrizBonita(matriz1, textAreaMatrices);

        textAreaMatrices.append("\nMatriz 2:\n");
        mostrarMatrizBonita(matriz2, textAreaMatrices);

        textAreaMatrices.append("\nMatriz Suma (Matriz 1 + Matriz 2):\n");
        mostrarMatrizBonita(matrizSuma, textAreaMatrices);
    }

    private void procesarResultados() {
        int[] diagonalSecundaria = obtenerDiagonalSecundaria(matrizSuma);
        int max = encontrarMaximo(diagonalSecundaria);
        int min = encontrarMinimo(diagonalSecundaria);

        BigDecimal potencia = calcularPotencia(new BigDecimal(max), min);
        System.out.println(potencia);
        String notacionCientifica = notacionCientifica(potencia);
        double promedioUltimaFila = calcularPromedioUltimaFila();

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
        filtrarColumna();
        textAreaResultados.append(crearLinea(90));
        textAreaResultados.append("PROMEDIO DE LA ÚLTIMA FILA\n");
        textAreaResultados.append(crearLinea(90));
        textAreaResultados.append(String.format("Promedio de la última fila (multiplicación): %.2f\n", promedioUltimaFila));
        verificarEsquinasPrimos();
        textAreaResultados.append(crearLinea(90));
    }
     private  BigDecimal calcularPotencia(BigDecimal base, int exponente) {
        if (exponente == 0) {
            return BigDecimal.ONE; // Cualquier número elevado a la 0 es 1
        }

        if (exponente < 0) {
            base = BigDecimal.ONE.divide(base, MathContext.DECIMAL128); // Inverso para exponentes negativos
            exponente = -exponente; // Hacer el exponente positivo
        }

        BigDecimal resultado = BigDecimal.ONE;
        for (int i = 0; i < exponente; i++) {
            resultado = resultado.multiply(base);
        }

        return resultado;
    }
    private String notacionCientifica(BigDecimal potencia){
        DecimalFormat formatoCientifico = new DecimalFormat("0.###E0");
        String resultadoCientifico = formatoCientifico.format(potencia);
        return resultadoCientifico;
    }

    private void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
    }

    private String crearLinea(int num) {
        return "═".repeat(num) + "\n";
    }

    private boolean esPrimo(int numero) {
        if (numero <= 1) return false;
        if (numero <= 3) return true;
        if (numero % 2 == 0 || numero % 3 == 0) return false;
        for (int i = 5; i * i <= numero; i += 6) {
            if (numero % i == 0 || numero % (i + 2) == 0) return false;
        }
        return true;
    }

    private void verificarEsquinasPrimos() {
        if (matrizSuma == null || matrizSuma.length == 0) return;

        int n = matrizSuma.length;
        int[] esquinas = {
            matrizSuma[0][0],
            matrizSuma[0][n - 1],
            matrizSuma[n - 1][0],
            matrizSuma[n - 1][n - 1]
        };
        textAreaResultados.append(crearLinea(90));
        textAreaResultados.append("ESQUINAS DE LA MATRIZ NÚMEROS PRIMOS\n");
        textAreaResultados.append(crearLinea(90));
        for (int i = 0; i < esquinas.length; i++) {
            int esquina = esquinas[i];
            textAreaResultados.append(String.format("El número en la esquina %d %s primo: %d\n",
                    i + 1, esPrimo(esquina) ? "es" : "no es", esquina));
        }
    }

    private double calcularPromedioUltimaFila() {
        if (matrizSuma == null || matrizSuma.length == 0) return 0.0;

        int n = matrizSuma[0].length;
        double producto = 1.0;

        for (int j = 0; j < n; j++) {
            producto *= matrizSuma[matrizSuma.length - 1][j];
        }

        return producto / n;
    }

    private int[][] generarMatrizAleatoria(int n) {
        Random rand = new Random();
        int[][] matriz = new int[n][n];

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                matriz[i][j] = rand.nextInt((MAX_RANGO - MIN_RANGO) + 1) + MIN_RANGO;
            }
        }

        return matriz;
    }

    private int[][] sumarMatrices(int[][] matriz1, int[][] matriz2) {
        int n = matriz1.length;
        int[][] suma = new int[n][n];

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                suma[i][j] = matriz1[i][j] + matriz2[i][j];
            }
        }

        return suma;
    }

    private int[] obtenerDiagonalSecundaria(int[][] matriz) {
        int n = matriz.length;
        int[] diagonal = new int[n];

        for (int i = 0; i < n; i++) {
            diagonal[i] = matriz[i][n - 1 - i];
        }

        return diagonal;
    }

    private int encontrarMaximo(int[] arreglo) {
        int max = arreglo[0];
        for (int valor : arreglo) {
            if (valor > max) max = valor;
        }
        return max;
    }

    private int encontrarMinimo(int[] arreglo) {
        int min = arreglo[0];
        for (int valor : arreglo) {
            if (valor < min) min = valor;
        }
        return min;
    }

    private void filtrarColumna() {
        try {
            String inputColumna = textField2.getText();
            if (inputColumna.isEmpty()) {
                textAreaResultados.append("Por favor, ingrese un número de columna para filtrar.\n");
                return;
            }

            int columna = Integer.parseInt(inputColumna);
            if (columna < 1 || columna > matrizSuma.length) {
                textAreaResultados.append("La columna ingresada no existe en la matriz.\n");
                return;
            }

            StringBuilder resultados = new StringBuilder();
            resultados.append("Números positivos en la columna ").append(columna).append(": ");
            boolean hayPositivos = false;
            for (int i = 0; i < matrizSuma.length; i++) {
                int valor = matrizSuma[i][columna - 1];
                if (valor > 0) {
                    resultados.append(valor).append(", ");
                    hayPositivos = true;
                }
            }
            if (!hayPositivos) {
                resultados.append("No hay números positivos en esta columna.");
            }
            textAreaResultados.append(resultados.toString());
            textAreaResultados.append("\n");

        } catch (NumberFormatException e) {
            textAreaResultados.append("Por favor, ingrese un número de columna válido.\n");
        }
    }

    private void mostrarMatrizBonita(int[][] matriz, JTextArea textArea) {
        int n = matriz.length;

        textArea.append("+");
        for (int i = 0; i < n; i++) {
            textArea.append("-----------------+");
        }
        textArea.append("\n");

        for (int[] fila : matriz) {
            textArea.append("|");
            for (int valor : fila) {
                textArea.append(String.format(" %5d\t|", valor));
            }
            textArea.append("\n");

            textArea.append("+");
            for (int i = 0; i < n; i++) {
                textArea.append("-----------------+");
            }
            textArea.append("\n");
        }
    }
}
