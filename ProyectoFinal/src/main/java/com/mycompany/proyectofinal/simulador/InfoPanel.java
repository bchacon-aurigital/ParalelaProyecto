package com.mycompany.proyectofinal.simulador;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.BorderFactory;

public class InfoPanel extends JPanel {

    private final JTextArea estadoArea = new JTextArea();
    private final JTextArea eventosArea = new JTextArea();
    private final List<String> eventos = new ArrayList<>();
    private static final Color PANEL_BG = new Color(245, 245, 245);
    private static final Color TEXT_BG = Color.WHITE;
    private static final Color TEXT_FG = new Color(33, 33, 33);

    public InfoPanel() {
        setLayout(new BorderLayout(10, 10));
        setPreferredSize(new Dimension(320, 584));
        setBackground(PANEL_BG);

        estadoArea.setEditable(false);
        estadoArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        estadoArea.setBackground(TEXT_BG);
        estadoArea.setForeground(TEXT_FG);
        estadoArea.setMargin(new java.awt.Insets(6, 6, 6, 6));
        estadoArea.setLineWrap(true);
        estadoArea.setWrapStyleWord(true);

        eventosArea.setEditable(false);
        eventosArea.setFont(new Font("SansSerif", Font.PLAIN, 12));
        eventosArea.setBackground(TEXT_BG);
        eventosArea.setForeground(TEXT_FG);
        eventosArea.setMargin(new java.awt.Insets(6, 6, 6, 6));
        eventosArea.setLineWrap(true);
        eventosArea.setWrapStyleWord(true);

        add(crearSeccion("Estado de los buses", prepararScroll(estadoArea)), BorderLayout.CENTER);
        add(crearSeccion("Eventos recientes", prepararScroll(eventosArea)), BorderLayout.SOUTH);
    }

    public void actualizarEstados(List<Autobus> buses) {
        StringBuilder sb = new StringBuilder();
        for (Autobus bus : buses) {
            sb.append(bus.getNombre())
                .append(" - ")
                .append(bus.getEstadoBus())
                .append(" - ")
                .append(bus.getParadaActualNombre())
                .append(" (")
                .append(bus.getDireccionActual())
                .append(")")
                .append("\n");
        }
        estadoArea.setText(sb.toString());
    }

    public void agregarEvento(String texto) {
        eventos.add(0, texto);
        if (eventos.size() > 12) {
            eventos.remove(eventos.size() - 1);
        }
        StringBuilder sb = new StringBuilder();
        for (String evento : eventos) {
            sb.append(evento).append("\n");
        }
        eventosArea.setText(sb.toString());
    }

    private JScrollPane prepararScroll(JTextArea area) {
        JScrollPane scroll = new JScrollPane(area);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        return scroll;
    }

    private JPanel crearSeccion(String titulo, JScrollPane contenido) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);
        panel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                titulo));
        panel.add(contenido, BorderLayout.CENTER);
        return panel;
    }
}

