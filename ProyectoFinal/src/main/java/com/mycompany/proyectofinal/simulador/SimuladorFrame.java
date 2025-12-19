package com.mycompany.proyectofinal.simulador;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class SimuladorFrame extends JFrame {

    private final SimuladorControl control;

    public SimuladorFrame() {
        super("Simulador de Buses San José - Cartago");
        setLayout(new BorderLayout(10, 10));

        MapaPanel mapaPanel = new MapaPanel();
        InfoPanel infoPanel = new InfoPanel();
        control = new SimuladorControl(mapaPanel, infoPanel);

        add(crearEncabezado(), BorderLayout.NORTH);
        add(mapaPanel, BorderLayout.CENTER);
        add(infoPanel, BorderLayout.EAST);
        add(crearPanelBotones(), BorderLayout.SOUTH);

        pack();
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private JPanel crearPanelBotones() {
        JPanel panel = new JPanel();
        panel.setBackground(new Color(236, 239, 241));

        JButton iniciar = new JButton("Iniciar");
        JButton detener = new JButton("Detener");

        estilizarBoton(iniciar, new Color(67, 160, 71));
        estilizarBoton(detener, new Color(239, 83, 80));

        iniciar.addActionListener(e -> control.iniciarSimulacion());
        detener.addActionListener(e -> control.detenerSimulacion());

        panel.add(iniciar);
        panel.add(detener);
        return panel;
    }

    private JPanel crearEncabezado() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(33, 33, 33));
        header.setBorder(javax.swing.BorderFactory.createEmptyBorder(12, 16, 12, 16));

        JLabel titulo = new JLabel("Simulador San José – Cartago");
        titulo.setForeground(Color.WHITE);
        titulo.setFont(new Font("SansSerif", Font.BOLD, 20));

        JLabel subtitulo = new JLabel("Paralela • Movimiento de 10 buses en 20 paradas");
        subtitulo.setForeground(new Color(189, 189, 189));
        subtitulo.setFont(new Font("SansSerif", Font.PLAIN, 12));

        header.add(titulo, BorderLayout.CENTER);
        header.add(subtitulo, BorderLayout.SOUTH);
        return header;
    }

    private void estilizarBoton(JButton boton, Color colorFondo) {
        boton.setFocusPainted(false);
        boton.setBackground(colorFondo);
        boton.setForeground(Color.WHITE);
        boton.setFont(new Font("SansSerif", Font.BOLD, 14));
        boton.setOpaque(true);
        boton.setBorder(javax.swing.BorderFactory.createEmptyBorder(8, 18, 8, 18));
    }
}

