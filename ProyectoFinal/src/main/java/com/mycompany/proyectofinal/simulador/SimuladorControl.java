package com.mycompany.proyectofinal.simulador;

import com.mycompany.proyectofinal.servidor.ServidorMonitoreo;
import java.awt.Color;
import java.awt.Image;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.swing.ImageIcon;

/**
 * Coordina los hilos, la interfaz y el servidor TCP de monitoreo.
 *
 * Responsabilidades:
 * - Gestionar los buses y sus hilos
 * - Coordinar la sincronización entre componentes
 * - Administrar el servidor TCP de monitoreo
 * - Actualizar la interfaz gráfica
 *
 * Autor: Proyecto Final - Programación Paralela y Distribuida
 * Fecha: Diciembre 2025
 */
public class SimuladorControl {

    private final Ruta ruta;
    private final MapaPanel mapaPanel;
    private final InfoPanel infoPanel;

    private final List<Autobus> buses = new ArrayList<>();
    private boolean enMarcha = false;

    // Servidor TCP para monitoreo remoto
    private ServidorMonitoreo servidorMonitoreo;
    private static final int PUERTO_SERVIDOR = 45000;

    public SimuladorControl(MapaPanel mapaPanel, InfoPanel infoPanel) {
        this.ruta = Ruta.crearRutaCartago();
        this.mapaPanel = mapaPanel;
        this.infoPanel = infoPanel;
        this.mapaPanel.setRuta(ruta);
        prepararBuses();
    }

    private void prepararBuses() {
        ruta.reiniciarParadas();
        buses.clear();

        Color[] colores = new Color[]{
            Color.YELLOW, Color.BLUE, Color.CYAN, Color.GRAY, new Color(139, 69, 19),
            new Color(128, 0, 128), Color.ORANGE, Color.RED, Color.PINK, Color.GREEN
        };

        String[] nombres = new String[]{
            "Bus Amarillo", "Bus Azul", "Bus Cian", "Bus Gris", "Bus Marrón",
            "Bus Morado", "Bus Naranja", "Bus Rojo", "Bus Rosa", "Bus Verde"
        };

        String[] iconos = new String[]{
            "bus_amarillo.png", "bus_azul.png", "bus_cian.png", "bus_gris.png", "bus_marron.png",
            "bus_morado.png", "bus_naranja.png", "bus_rojo.png", "bus_rosa.png", "bus_verde.png"
        };

        int[] posiciones = new int[]{0, 2, 4, 6, 8, 10, 12, 14, 16, 18};

        for (int i = 0; i < nombres.length; i++) {
            Autobus bus = new Autobus(i + 1, nombres[i], colores[i], ruta, posiciones[i], this);
            bus.setIcono(cargarIcono(iconos[i]));
            bus.prepararInicio();
            buses.add(bus);
        }

        mapaPanel.setBuses(buses);
        actualizarPaneles();
    }

    /**
     * Inicia la simulación de buses y el servidor TCP de monitoreo.
     * Utiliza sincronización para evitar inicios múltiples.
     */
    public synchronized void iniciarSimulacion() {
        if (enMarcha) {
            return;
        }
        enMarcha = true;
        infoPanel.agregarEvento("Simulación iniciada");
        infoPanel.actualizarEstados(buses);

        // Iniciar servidor TCP de monitoreo
        servidorMonitoreo = new ServidorMonitoreo(PUERTO_SERVIDOR, this);
        servidorMonitoreo.start();

        // Iniciar todos los hilos de buses
        for (Autobus bus : buses) {
            bus.start();
        }
    }

    /**
     * Detiene la simulación de buses y el servidor TCP.
     * Espera a que todos los hilos terminen correctamente usando join().
     */
    public synchronized void detenerSimulacion() {
        if (!enMarcha) {
            prepararBuses();
            return;
        }

        // Detener todos los buses
        for (Autobus bus : buses) {
            bus.detenerBus();
        }

        // Esperar a que los buses terminen (join con timeout)
        for (Autobus bus : buses) {
            try {
                bus.join(500);
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
            }
        }

        // Detener el servidor TCP
        if (servidorMonitoreo != null) {
            servidorMonitoreo.detenerServidor();
            try {
                servidorMonitoreo.join(1000);
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
            }
        }

        enMarcha = false;
        infoPanel.agregarEvento("Simulación detenida");
        prepararBuses();
    }

    public void registrarEvento(String texto) {
        infoPanel.agregarEvento(texto);
    }

    public void actualizarPaneles() {
        mapaPanel.repaint();
        infoPanel.actualizarEstados(buses);
    }

    /**
     * Retorna una lista inmutable de los buses actualmente en el sistema.
     * Este método es utilizado por el servidor TCP para consultar el estado.
     *
     * @return Lista inmutable de buses
     */
    public synchronized List<Autobus> obtenerBuses() {
        return Collections.unmodifiableList(new ArrayList<>(buses));
    }

    /**
     * Carga un icono desde los recursos del proyecto.
     *
     * @param nombreArchivo Nombre del archivo de imagen
     * @return Imagen cargada, o null si no se encuentra
     */
    private Image cargarIcono(String nombreArchivo) {
        URL url = getClass().getResource("/imagenes/" + nombreArchivo);
        if (url != null) {
            return new ImageIcon(url).getImage();
        }
        return null;
    }
}



