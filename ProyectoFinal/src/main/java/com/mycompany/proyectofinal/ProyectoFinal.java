package com.mycompany.proyectofinal;

import com.mycompany.proyectofinal.simulador.SimuladorFrame;
import javax.swing.SwingUtilities;

public class ProyectoFinal {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            SimuladorFrame frame = new SimuladorFrame();
            frame.setVisible(true);
        });
    }
}
