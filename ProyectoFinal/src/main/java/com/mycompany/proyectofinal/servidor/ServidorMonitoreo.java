package com.mycompany.proyectofinal.servidor;

import com.mycompany.proyectofinal.simulador.SimuladorControl;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServidorMonitoreo extends Thread {

    private final int puerto;
    private final SimuladorControl control;
    private ServerSocket serverSocket;
    private volatile boolean activo = true;

    public ServidorMonitoreo(int puerto, SimuladorControl control) {
        this.puerto = puerto;
        this.control = control;
        setName("ServidorMonitoreo-" + puerto);
    }

    @Override
    public void run() {
        try {
            serverSocket = new ServerSocket(puerto);
            System.out.println("[SERVIDOR] Servidor de monitoreo iniciado en puerto " + puerto);
            control.registrarEvento("Servidor TCP iniciado en puerto " + puerto);

            while (activo) {
                try {
                    Socket clienteSocket = serverSocket.accept();

                    System.out.println("[SERVIDOR] Cliente conectado desde: "
                            + clienteSocket.getInetAddress().getHostAddress());

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

    public void detenerServidor() {
        activo = false;
        cerrarServidor();
    }

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
