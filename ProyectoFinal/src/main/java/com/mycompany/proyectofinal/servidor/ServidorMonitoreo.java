package com.mycompany.proyectofinal.servidor;

import com.mycompany.proyectofinal.simulador.SimuladorControl;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Servidor TCP que permite a clientes remotos consultar el estado del sistema de buses.
 *
 * Características:
 * - Utiliza TCP para comunicación confiable
 * - Acepta múltiples clientes concurrentemente
 * - Cada cliente es atendido en un hilo separado
 * - Puerto: 45000 (configurable)
 *
 * Autor: Proyecto Final - Programación Paralela y Distribuida
 * Fecha: Diciembre 2025
 */
public class ServidorMonitoreo extends Thread {

    private final int puerto;
    private final SimuladorControl control;
    private ServerSocket serverSocket;
    private volatile boolean activo = true;

    /**
     * Constructor del servidor de monitoreo.
     *
     * @param puerto Puerto en el que escuchará el servidor
     * @param control Controlador del simulador para obtener información
     */
    public ServidorMonitoreo(int puerto, SimuladorControl control) {
        this.puerto = puerto;
        this.control = control;
        setName("ServidorMonitoreo-" + puerto);
    }

    /**
     * Método principal del hilo del servidor.
     * Escucha conexiones entrantes y crea un hilo para cada cliente.
     */
    @Override
    public void run() {
        try {
            serverSocket = new ServerSocket(puerto);
            System.out.println("[SERVIDOR] Servidor de monitoreo iniciado en puerto " + puerto);
            control.registrarEvento("Servidor TCP iniciado en puerto " + puerto);

            while (activo) {
                try {
                    // Esperar conexión de un cliente
                    Socket clienteSocket = serverSocket.accept();

                    System.out.println("[SERVIDOR] Cliente conectado desde: "
                            + clienteSocket.getInetAddress().getHostAddress());

                    // Crear un hilo para atender a este cliente
                    HiloCliente hiloCliente = new HiloCliente(clienteSocket, control);
                    hiloCliente.start();

                } catch (IOException e) {
                    if (activo) {
                        System.err.println("[SERVIDOR] Error aceptando cliente: " + e.getMessage());
                    }
                }
            }

        } catch (IOException e) {
            System.err.println("[SERVIDOR] Error al iniciar servidor: " + e.getMessage());
            control.registrarEvento("Error al iniciar servidor TCP: " + e.getMessage());
        } finally {
            cerrarServidor();
        }
    }

    /**
     * Detiene el servidor y cierra todas las conexiones.
     */
    public void detenerServidor() {
        activo = false;
        cerrarServidor();
    }

    /**
     * Cierra el socket del servidor.
     */
    private void cerrarServidor() {
        try {
            if (serverSocket != null && !serverSocket.isClosed()) {
                serverSocket.close();
                System.out.println("[SERVIDOR] Servidor detenido correctamente");
                control.registrarEvento("Servidor TCP detenido");
            }
        } catch (IOException e) {
            System.err.println("[SERVIDOR] Error al cerrar servidor: " + e.getMessage());
        }
    }
}
