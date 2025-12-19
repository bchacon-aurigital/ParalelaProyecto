package com.mycompany.proyectofinal.cliente;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class ClienteMonitoreo {

    private static final String SERVIDOR_HOST = "127.0.0.1";
    private static final int SERVIDOR_PUERTO = 45000;

    private Socket socket;
    private BufferedReader entrada;
    private PrintWriter salida;
    private Scanner scanner;

    public ClienteMonitoreo() {
        scanner = new Scanner(System.in);
    }

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

    public void iniciar() {
        System.out.println("\n>> Ingrese comandos (escriba 'AYUDA' para ver opciones):\n");

        try {
            while (true) {
                System.out.print(">>> ");
                String comando = scanner.nextLine().trim();

                if (comando.isEmpty()) {
                    continue;
                }

                salida.println(comando);

                if (comando.equalsIgnoreCase("SALIR")) {
                    String respuesta = entrada.readLine();
                    if (respuesta != null) {
                        System.out.println(respuesta);
                    }
                    break;
                }

                leerRespuestaServidor();
            }

        } catch (IOException e) {
            System.err.println("ERROR: Conexión perdida con el servidor.");
            System.err.println("Detalles: " + e.getMessage());
        } finally {
            desconectar();
        }
    }

    private void leerRespuestaServidor() throws IOException {
        String linea;
        boolean leyendoRespuesta = true;

        while (leyendoRespuesta && (linea = entrada.readLine()) != null) {
            System.out.println(linea);

            if (linea.isEmpty()) {
                leyendoRespuesta = false;
            }
        }
    }

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


    public static void main(String[] args) {
        ClienteMonitoreo cliente = new ClienteMonitoreo();

        if (cliente.conectar()) {
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
