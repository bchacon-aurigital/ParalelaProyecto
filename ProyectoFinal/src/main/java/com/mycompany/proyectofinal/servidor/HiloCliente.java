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

            salida.println("╔════════════════════════════════════════════════════════════════╗");
            salida.println("║     SISTEMA DE MONITOREO DE BUSES - SAN JOSÉ/CARTAGO         ║");
            salida.println("╚════════════════════════════════════════════════════════════════╝");
            salida.println();
            salida.println("  🔌 PROTOCOLO DE COMUNICACIÓN:");
            salida.println("     • Tipo: TCP/IP (Transmission Control Protocol)");
            salida.println("     • Puerto del servidor: 45000");
            salida.println("     • IP del servidor: " + clienteSocket.getLocalAddress().getHostAddress());
            salida.println("     • Su IP: " + clienteSocket.getInetAddress().getHostAddress());
            salida.println("     • Conexión: ESTABLECIDA ✓");
            salida.println();
            salida.println("  📡 CARACTERÍSTICAS TCP:");
            salida.println("     • Comunicación orientada a conexión");
            salida.println("     • Garantía de entrega de datos");
            salida.println("     • Orden de paquetes preservado");
            salida.println("     • Control de flujo y congestión");
            salida.println();
            salida.println("╔════════════════════════════════════════════════════════════════╗");
            salida.println("║ Escriba 'AYUDA' para ver comandos disponibles                ║");
            salida.println("╚════════════════════════════════════════════════════════════════╝");
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

        salida.println("\n╔════════════════════════════════════════════════════════════════╗");
        salida.println("║        ESTADO GENERAL DEL SISTEMA DE BUSES - TCP              ║");
        salida.println("║        Protocolo: TCP | Puerto: 45000                         ║");
        salida.println("╚════════════════════════════════════════════════════════════════╝");
        salida.println("Total de buses en servicio: " + buses.size());
        salida.println();

        for (Autobus bus : buses) {
            salida.println("┌─────────────────────────────────────────────────────────┐");
            salida.println("│ Bus #" + bus.getIdAutobus() + ": " + bus.getNombre());
            salida.println("├─────────────────────────────────────────────────────────┤");
            salida.println("│ ✓ Estado actual: " + bus.getEstadoBus());
            salida.println("│ ✓ PARADA ACTUAL: " + bus.getParadaActualNombre());
            salida.println("│ ✓ Dirección: " + bus.getDireccionActual());
            salida.println("│ ✓ Posición exacta: X=" + bus.getX() + ", Y=" + bus.getY());
            salida.println("│ ✓ Color: RGB(" + bus.getColor().getRed() + ","
                    + bus.getColor().getGreen() + "," + bus.getColor().getBlue() + ")");
            salida.println("└─────────────────────────────────────────────────────────┘");
            salida.println();
        }
        salida.println("╔════════════════════════════════════════════════════════════════╗");
        salida.println("║ Comunicación establecida via TCP/IP - Conexión activa        ║");
        salida.println("╚════════════════════════════════════════════════════════════════╝\n");
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
            salida.println("\n╔════════════════════════════════════════════════════════════════╗");
            salida.println("║     INFORMACIÓN DETALLADA DEL BUS #" + idBus + " - Protocolo TCP    ║");
            salida.println("╚════════════════════════════════════════════════════════════════╝");
            salida.println();
            salida.println("  📍 Identificación:");
            salida.println("     • ID: " + busEncontrado.getIdAutobus());
            salida.println("     • Nombre: " + busEncontrado.getNombre());
            salida.println();
            salida.println("  🚏 UBICACIÓN EN PARADA:");
            salida.println("     • PARADA ACTUAL: " + busEncontrado.getParadaActualNombre());
            salida.println("     • Estado del bus: " + busEncontrado.getEstadoBus());
            salida.println("     • Dirección de viaje: " + busEncontrado.getDireccionActual());
            salida.println();
            salida.println("  📊 Posición exacta en el mapa:");
            salida.println("     • Coordenada X: " + busEncontrado.getX() + " px");
            salida.println("     • Coordenada Y: " + busEncontrado.getY() + " px");
            salida.println();
            salida.println("  🎨 Identificación visual:");
            salida.println("     • Color RGB: (" + busEncontrado.getColor().getRed() + ", "
                    + busEncontrado.getColor().getGreen() + ", "
                    + busEncontrado.getColor().getBlue() + ")");
            salida.println();
            salida.println("╔════════════════════════════════════════════════════════════════╗");
            salida.println("║ Datos obtenidos via TCP desde servidor en tiempo real        ║");
            salida.println("╚════════════════════════════════════════════════════════════════╝\n");
        } else {
            salida.println("\n❌ ERROR: No se encontró un bus con ID #" + idBus);
            salida.println("IDs válidos: 1 a " + buses.size() + "\n");
        }
    }

    private void enviarAyuda() {
        salida.println("\n╔════════════════════════════════════════════════════════════════╗");
        salida.println("║              COMANDOS DISPONIBLES - PROTOCOLO TCP             ║");
        salida.println("╚════════════════════════════════════════════════════════════════╝");
        salida.println();
        salida.println("  📋 COMANDOS:");
        salida.println();
        salida.println("  1️⃣  ESTADO");
        salida.println("      └─ Muestra el estado de TODOS los buses");
        salida.println("      └─ Incluye: PARADA ACTUAL, posición, estado, dirección");
        salida.println();
        salida.println("  2️⃣  BUS <id>");
        salida.println("      └─ Muestra información DETALLADA de un bus específico");
        salida.println("      └─ Ejemplo: BUS 1");
        salida.println("      └─ IDs válidos: 1 a 10");
        salida.println();
        salida.println("  3️⃣  AYUDA");
        salida.println("      └─ Muestra esta ayuda");
        salida.println();
        salida.println("  4️⃣  SALIR");
        salida.println("      └─ Cierra la conexión TCP con el servidor");
        salida.println();
        salida.println("╔════════════════════════════════════════════════════════════════╗");
        salida.println("║ 🔍 INFORMACIÓN MOSTRADA:                                      ║");
        salida.println("║   • Nombre del bus                                            ║");
        salida.println("║   • PARADA ACTUAL donde se encuentra                          ║");
        salida.println("║   • Estado (EN_PARADA, EN_TRANSITO, ESPERANDO)                ║");
        salida.println("║   • Dirección (IDA: SJ→Cartago, VUELTA: Cartago→SJ)          ║");
        salida.println("║   • Posición exacta en coordenadas X,Y                        ║");
        salida.println("╚════════════════════════════════════════════════════════════════╝\n");
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
