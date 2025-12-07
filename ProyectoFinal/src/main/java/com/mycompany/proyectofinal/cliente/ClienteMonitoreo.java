package com.mycompany.proyectofinal.cliente;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

/**
 * Cliente TCP para monitorear el sistema de buses de forma remota.
 *
 * Funcionalidad:
 * - Se conecta al servidor de monitoreo via TCP
 * - Permite al usuario enviar comandos al servidor
 * - Muestra las respuestas del servidor en tiempo real
 * - Interfaz de línea de comandos interactiva
 *
 * Uso:
 * 1. Ejecutar esta clase como aplicación independiente
 * 2. Ingresar comandos según el menú
 * 3. Recibir información en tiempo real del sistema
 *
 * Comandos soportados:
 * - ESTADO: Ver todos los buses
 * - BUS <id>: Ver información de un bus específico
 * - AYUDA: Mostrar ayuda
 * - SALIR: Desconectar del servidor
 *
 * Autor: Proyecto Final - Programación Paralela y Distribuida
 * Fecha: Diciembre 2025
 */
public class ClienteMonitoreo {

    private static final String SERVIDOR_HOST = "127.0.0.1";  // Localhost
    private static final int SERVIDOR_PUERTO = 45000;

    private Socket socket;
    private BufferedReader entrada;
    private PrintWriter salida;
    private Scanner scanner;

    /**
     * Constructor del cliente de monitoreo.
     */
    public ClienteMonitoreo() {
        scanner = new Scanner(System.in);
    }

    /**
     * Conecta al servidor de monitoreo.
     *
     * @return true si la conexión fue exitosa, false en caso contrario
     */
    public boolean conectar() {
        try {
            System.out.println("===========================================");
            System.out.println("  CLIENTE DE MONITOREO DE BUSES");
            System.out.println("===========================================");
            System.out.println("Conectando al servidor " + SERVIDOR_HOST + ":" + SERVIDOR_PUERTO + "...");

            socket = new Socket(SERVIDOR_HOST, SERVIDOR_PUERTO);
            entrada = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            salida = new PrintWriter(socket.getOutputStream(), true);

            System.out.println("¡Conexión establecida exitosamente!");
            System.out.println();

            // Leer mensaje de bienvenida del servidor
            String linea;
            while ((linea = entrada.readLine()) != null && !linea.isEmpty()) {
                System.out.println(linea);
            }

            return true;

        } catch (IOException e) {
            System.err.println("ERROR: No se pudo conectar al servidor.");
            System.err.println("Asegúrese de que el servidor esté ejecutándose en " + SERVIDOR_HOST + ":" + SERVIDOR_PUERTO);
            System.err.println("Detalles: " + e.getMessage());
            return false;
        }
    }

    /**
     * Inicia el bucle principal de interacción con el usuario.
     */
    public void iniciar() {
        System.out.println("\n>> Ingrese comandos (escriba 'AYUDA' para ver opciones):\n");

        try {
            while (true) {
                // Leer comando del usuario
                System.out.print(">>> ");
                String comando = scanner.nextLine().trim();

                if (comando.isEmpty()) {
                    continue;
                }

                // Enviar comando al servidor
                salida.println(comando);

                // Si el usuario quiere salir, cerrar
                if (comando.equalsIgnoreCase("SALIR")) {
                    // Leer respuesta de despedida
                    String respuesta = entrada.readLine();
                    if (respuesta != null) {
                        System.out.println(respuesta);
                    }
                    break;
                }

                // Leer y mostrar la respuesta del servidor
                leerRespuestaServidor();
            }

        } catch (IOException e) {
            System.err.println("ERROR: Conexión perdida con el servidor.");
            System.err.println("Detalles: " + e.getMessage());
        } finally {
            desconectar();
        }
    }

    /**
     * Lee y muestra la respuesta del servidor.
     *
     * @throws IOException si hay error al leer
     */
    private void leerRespuestaServidor() throws IOException {
        String linea;
        boolean leyendoRespuesta = true;

        while (leyendoRespuesta && (linea = entrada.readLine()) != null) {
            System.out.println(linea);

            // Si encontramos una línea vacía, terminamos de leer la respuesta
            if (linea.isEmpty()) {
                leyendoRespuesta = false;
            }
        }
    }

    /**
     * Cierra la conexión con el servidor.
     */
    public void desconectar() {
        try {
            if (entrada != null) {
                entrada.close();
            }
            if (salida != null) {
                salida.close();
            }
            if (socket != null && !socket.isClosed()) {
                socket.close();
            }
            if (scanner != null) {
                scanner.close();
            }
            System.out.println("\nConexión cerrada correctamente.");

        } catch (IOException e) {
            System.err.println("Error al cerrar conexión: " + e.getMessage());
        }
    }

    /**
     * Método main para ejecutar el cliente de forma independiente.
     *
     * Instrucciones de uso:
     * 1. Asegurarse de que el servidor esté ejecutándose
     * 2. Ejecutar esta clase
     * 3. Seguir las instrucciones en pantalla
     *
     * @param args Argumentos de línea de comandos (no utilizados)
     */
    public static void main(String[] args) {
        ClienteMonitoreo cliente = new ClienteMonitoreo();

        // Intentar conectar al servidor
        if (cliente.conectar()) {
            // Si la conexión fue exitosa, iniciar interacción
            cliente.iniciar();
        } else {
            System.out.println("\nNo se pudo establecer conexión con el servidor.");
            System.out.println("Verifique que:");
            System.out.println("  1. El simulador esté ejecutándose");
            System.out.println("  2. La simulación esté iniciada");
            System.out.println("  3. El servidor TCP esté activo en el puerto " + SERVIDOR_PUERTO);
        }
    }
}
