package com.mycompany.proyectofinal.servidor;

import com.mycompany.proyectofinal.simulador.Autobus;
import com.mycompany.proyectofinal.simulador.SimuladorControl;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;

public class HiloCliente extends Thread {

    private final Socket clienteSocket;
    private final SimuladorControl control;
    private BufferedReader entrada;
    private PrintWriter salida;

    public HiloCliente(Socket clienteSocket, SimuladorControl control) {
        this.clienteSocket = clienteSocket;
        this.control = control;
        setName("HiloCliente-" + clienteSocket.getInetAddress().getHostAddress());
    }

    @Override
    public void run() {
        try {
            entrada = new BufferedReader(new InputStreamReader(clienteSocket.getInputStream()));
            salida = new PrintWriter(clienteSocket.getOutputStream(), true);

            salida.println("================================================================");
            salida.println("    SISTEMA DE MONITOREO DE BUSES - SAN JOSE/CARTAGO");
            salida.println("================================================================");
            salida.println();
            salida.println("PROTOCOLO: TCP/IP");
            salida.println("Puerto del servidor: 45000");
            salida.println("IP del servidor: " + clienteSocket.getLocalAddress().getHostAddress());
            salida.println("IP del cliente: " + clienteSocket.getInetAddress().getHostAddress());
            salida.println("Estado de conexion: CONECTADO");
            salida.println();
            salida.println("Escriba 'AYUDA' para ver comandos disponibles");
            salida.println("================================================================");
            salida.println();

            String comando;
            while ((comando = entrada.readLine()) != null) {
                comando = comando.trim().toUpperCase();

                System.out.println("[HILO-CLIENTE] Comando recibido: " + comando);

                procesarComando(comando);

                if (comando.equals("SALIR")) {
                    break;
                }
            }

        } catch (IOException e) {
            System.err.println("[HILO-CLIENTE] Error de comunicación: " + e.getMessage());
        } finally {
            cerrarConexion();
        }
    }

    private void procesarComando(String comando) {
        if (comando.equals("ESTADO")) {
            enviarEstadoGeneral();
        } else if (comando.startsWith("BUS ")) {
            String[] partes = comando.split(" ");
            if (partes.length == 2) {
                try {
                    int idBus = Integer.parseInt(partes[1]);
                    enviarEstadoBus(idBus);
                } catch (NumberFormatException e) {
                    salida.println("ERROR: ID de bus inválido");
                }
            } else {
                salida.println("ERROR: Formato incorrecto. Use: BUS <id>");
            }
        } else if (comando.equals("AYUDA")) {
            enviarAyuda();
        } else if (comando.equals("SALIR")) {
            salida.println("Cerrando conexión. ¡Hasta pronto!");
        } else {
            salida.println("ERROR: Comando no reconocido. Escriba 'AYUDA' para ver comandos disponibles.");
        }
    }

    private void enviarEstadoGeneral() {
        List<Autobus> buses = control.obtenerBuses();

        salida.println();
        salida.println("================================================================");
        salida.println("       ESTADO GENERAL DEL SISTEMA - Protocolo TCP");
        salida.println("       Puerto: 45000");
        salida.println("================================================================");
        salida.println("Total de buses en servicio: " + buses.size());
        salida.println();

        for (Autobus bus : buses) {
            salida.println("---------------------------------------------------------------");
            salida.println("Bus #" + bus.getIdAutobus() + ": " + bus.getNombre());
            salida.println("  Estado: " + bus.getEstadoBus());
            salida.println("  PARADA ACTUAL: " + bus.getParadaActualNombre());
            salida.println("  Direccion: " + bus.getDireccionActual());
            salida.println("  Posicion: X=" + bus.getX() + ", Y=" + bus.getY());
            salida.println("---------------------------------------------------------------");
            salida.println();
        }
        salida.println("Comunicacion via TCP/IP - Conexion activa");
        salida.println();
    }

    private void enviarEstadoBus(int idBus) {
        List<Autobus> buses = control.obtenerBuses();

        Autobus busEncontrado = null;
        for (Autobus bus : buses) {
            if (bus.getIdAutobus() == idBus) {
                busEncontrado = bus;
                break;
            }
        }

        if (busEncontrado != null) {
            salida.println();
            salida.println("================================================================");
            salida.println("  INFORMACION DETALLADA DEL BUS #" + idBus + " - Protocolo TCP");
            salida.println("================================================================");
            salida.println();
            salida.println("ID: " + busEncontrado.getIdAutobus());
            salida.println("Nombre: " + busEncontrado.getNombre());
            salida.println();
            salida.println("PARADA ACTUAL: " + busEncontrado.getParadaActualNombre());
            salida.println("Estado del bus: " + busEncontrado.getEstadoBus());
            salida.println("Direccion de viaje: " + busEncontrado.getDireccionActual());
            salida.println();
            salida.println("Coordenada X: " + busEncontrado.getX() + " px");
            salida.println("Coordenada Y: " + busEncontrado.getY() + " px");
            salida.println();
            salida.println("Datos obtenidos via TCP desde servidor en tiempo real");
            salida.println();
        } else {
            salida.println();
            salida.println("ERROR: No se encontro un bus con ID #" + idBus);
            salida.println("IDs validos: 1 a " + buses.size());
            salida.println();
        }
    }

    private void enviarAyuda() {
        salida.println();
        salida.println("================================================================");
        salida.println("         COMANDOS DISPONIBLES - PROTOCOLO TCP");
        salida.println("================================================================");
        salida.println();
        salida.println("COMANDOS:");
        salida.println();
        salida.println("  ESTADO");
        salida.println("    - Muestra el estado de TODOS los buses");
        salida.println("    - Incluye: PARADA ACTUAL, posicion, estado, direccion");
        salida.println();
        salida.println("  BUS <id>");
        salida.println("    - Muestra informacion detallada de un bus especifico");
        salida.println("    - Ejemplo: BUS 1");
        salida.println("    - IDs validos: 1 a 10");
        salida.println();
        salida.println("  AYUDA");
        salida.println("    - Muestra esta ayuda");
        salida.println();
        salida.println("  SALIR");
        salida.println("    - Cierra la conexion TCP con el servidor");
        salida.println();
        salida.println("================================================================");
        salida.println();
    }

    private void cerrarConexion() {
        try {
            if (entrada != null) {
                entrada.close();
            }
            if (salida != null) {
                salida.close();
            }
            if (clienteSocket != null && !clienteSocket.isClosed()) {
                clienteSocket.close();
            }
            System.out.println("[HILO-CLIENTE] Conexión cerrada con cliente: "
                    + clienteSocket.getInetAddress().getHostAddress());
        } catch (IOException e) {
            System.err.println("[HILO-CLIENTE] Error al cerrar conexión: " + e.getMessage());
        }
    }
}
