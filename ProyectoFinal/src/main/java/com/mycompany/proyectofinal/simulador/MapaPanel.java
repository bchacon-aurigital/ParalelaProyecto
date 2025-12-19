package com.mycompany.proyectofinal.simulador;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.net.URL;
import java.util.Collections;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

public class MapaPanel extends JPanel {

    private static final int ANCHO_MAPA = 1120;
    private static final int ALTO_MAPA = 584;

    private Ruta ruta;
    private List<Autobus> buses = Collections.emptyList();
    private final Image fondo;

    public MapaPanel() {
        setPreferredSize(new Dimension(ANCHO_MAPA, ALTO_MAPA));
        fondo = cargarImagen("/imagenes/Mapa.png");
        setBackground(Color.WHITE);
    }

    public void setRuta(Ruta ruta) {
        this.ruta = ruta;
    }

    public void setBuses(List<Autobus> buses) {
        this.buses = buses;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        if (fondo != null) {
            g2.drawImage(fondo, 0, 0, ANCHO_MAPA, ALTO_MAPA, null);
        }

        if (ruta == null) {
            return;
        }

        dibujarParadas(g2);
        dibujarBuses(g2);
    }

    private void dibujarParadas(Graphics2D g2) {
        g2.setColor(new Color(0, 0, 0, 180));
        for (Parada parada : ruta.getParadas()) {
            int px = ajustarX(parada.getX());
            int py = ajustarY(parada.getY());
            g2.fillOval(px - 5, py - 5, 10, 10);
            g2.drawString(String.valueOf(parada.getNumero()), px + 6, py - 6);
        }
    }

    private void dibujarBuses(Graphics2D g2) {
        for (Autobus bus : buses) {
            int px = ajustarX(bus.getX());
            int py = ajustarY(bus.getY());

            Image icono = bus.getIcono();
            if (icono != null) {
                g2.drawImage(icono, px - 20, py - 20, 40, 40, null);
            } else {
                g2.setColor(bus.getColor());
                g2.fillOval(px - 14, py - 14, 28, 28);
                g2.setColor(Color.BLACK);
                g2.drawOval(px - 14, py - 14, 28, 28);
            }
            g2.drawString(bus.getNombre(), px - 24, py - 24);
        }
    }

    private int ajustarX(int x) {
        if (x < 5) {
            return 5;
        }
        if (x > ANCHO_MAPA - 5) {
            return ANCHO_MAPA - 5;
        }
        return x;
    }

    private int ajustarY(int y) {
        if (y < 5) {
            return 5;
        }
        if (y > ALTO_MAPA - 5) {
            return ALTO_MAPA - 5;
        }
        return y;
    }

    private Image cargarImagen(String recurso) {
        URL url = getClass().getResource(recurso);
        if (url != null) {
            return new ImageIcon(url).getImage();
        }
        return null;
    }
}

