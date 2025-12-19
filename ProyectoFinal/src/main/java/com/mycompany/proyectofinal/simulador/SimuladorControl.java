package com.mycompany.proyectofinal.simulador;

import com.mycompany.proyectofinal.servidor.ServidorMonitoreo;
import java.awt.Color;
import java.awt.Image;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.swing.ImageIcon;

public class SimuladorControl {

    private final Ruta ruta;
    private final MapaPanel mapaPanel;
    private final InfoPanel infoPanel;

    private final List<Autobus> buses = new ArrayList<>();
    private boolean enMarcha = false;

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

    public synchronized void iniciarSimulacion() {
        if (enMarcha) {
            return;
        }
        enMarcha = true;
        infoPanel.agregarEvento("Simulación iniciada");
        infoPanel.actualizarEstados(buses);

        servidorMonitoreo = new ServidorMonitoreo(PUERTO_SERVIDOR, this);
        servidorMonitoreo.start();

        for (Autobus bus : buses) {
            bus.start();
        }
    }

    public synchronized void detenerSimulacion() {
        if (!enMarcha) {
            prepararBuses();
            return;
        }

        for (Autobus bus : buses) {
            bus.detenerBus();
        }

        for (Autobus bus : buses) {
            try {
                bus.join(500);
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
            }
        }

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

    public synchronized List<Autobus> obtenerBuses() {
        return Collections.unmodifiableList(new ArrayList<>(buses));
    }

    private Image cargarIcono(String nombreArchivo) {
        URL url = getClass().getResource("/imagenes/" + nombreArchivo);
        if (url != null) {
            return new ImageIcon(url).getImage();
        }
        return null;
    }
}




