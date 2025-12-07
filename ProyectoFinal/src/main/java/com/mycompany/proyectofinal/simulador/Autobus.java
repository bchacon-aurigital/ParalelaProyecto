package com.mycompany.proyectofinal.simulador;

import java.awt.Color;
import java.awt.Image;
import java.util.Random;

/**
 * Hilo que representa a un autobús moviéndose por la ruta.
 */
public class Autobus extends Thread {

    private final int id;
    private final String nombre;
    private final Color color;
    private final Ruta ruta;
    private final SimuladorControl control;
    private final Random random = new Random();

    private int posicionActual;
    private double posX;
    private double posY;
    private DireccionRuta direccionActual = DireccionRuta.IDA;
    private volatile boolean activo = true;
    private EstadoBus estadoBus = EstadoBus.EN_PARADA;
    private Image icono;

    public Autobus(int id, String nombre, Color color, Ruta ruta, int posicionInicial, SimuladorControl control) {
        this.id = id;
        this.nombre = nombre;
        this.color = color;
        this.ruta = ruta;
        this.posicionActual = posicionInicial;
        this.control = control;
    }

    public void prepararInicio() {
        ruta.getParada(posicionActual).ocuparInicial(this);
        estadoBus = EstadoBus.EN_PARADA;
        direccionActual = DireccionRuta.IDA;
        Parada origen = ruta.getParada(posicionActual);
        posX = origen.getX();
        posY = origen.getY();
    }

    public void detenerBus() {
        activo = false;
        interrupt();
    }

    @Override
    public void run() {
        control.registrarEvento("Bus " + nombre + " en servicio.");
        while (activo) {
            esperarEnParada();
            if (!activo) {
                break;
            }

            int siguienteIndice = obtenerSiguienteIndice();
            Parada destino = ruta.getParada(siguienteIndice);
            estadoBus = EstadoBus.ESPERANDO;
            control.actualizarPaneles();

            while (activo && !destino.intentarOcupar(this)) {
                estadoBus = EstadoBus.ESPERANDO;
                control.registrarEvento("Bus " + nombre + " esperando en " + destino.getNombre());
                pausa(1000 + random.nextInt(2000));
            }

            if (!activo) {
                destino.liberar(this);
                break;
            }

            estadoBus = EstadoBus.EN_TRANSITO;
            control.actualizarPaneles();
            moverHacia(destino.getX(), destino.getY());

            Parada actual = ruta.getParada(posicionActual);
            actual.liberar(this);
            posicionActual = siguienteIndice;
            estadoBus = EstadoBus.EN_PARADA;
            actualizarDireccionSegunParada();
            control.registrarEvento("Bus " + nombre + " llegó a " + destino.getNombre());
        }
        ruta.getParada(posicionActual).liberar(this);
        estadoBus = EstadoBus.ESPERANDO;
        control.registrarEvento("Bus " + nombre + " fuera de servicio.");
        control.actualizarPaneles();
    }

    private void moverHacia(int destinoX, int destinoY) {
        double pasos = 20;
        double dx = (destinoX - posX) / pasos;
        double dy = (destinoY - posY) / pasos;

        for (int i = 0; i < pasos && activo; i++) {
            posX += dx;
            posY += dy;
            control.actualizarPaneles();
            pausa(80 + random.nextInt(60));
        }

        posX = destinoX;
        posY = destinoY;
        control.actualizarPaneles();
    }

    private void esperarEnParada() {
        estadoBus = EstadoBus.EN_PARADA;
        control.actualizarPaneles();
        pausa(1200 + random.nextInt(2500));
    }

    private void pausa(int tiempo) {
        try {
            Thread.sleep(tiempo);
        } catch (InterruptedException ex) {
            // solo continuar si el hilo sigue activo
        }
    }

    private int obtenerSiguienteIndice() {
        return (posicionActual + 1) % ruta.size();
    }

    private void actualizarDireccionSegunParada() {
        Parada parada = ruta.getParada(posicionActual);
        if (parada.getNumero() == 13) {
            direccionActual = DireccionRuta.VUELTA;
        } else if (parada.getNumero() == 20) {
            direccionActual = DireccionRuta.IDA;
        }
    }

    public int getIdAutobus() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public Color getColor() {
        return color;
    }

    public int getX() {
        return (int) Math.round(posX);
    }

    public int getY() {
        return (int) Math.round(posY);
    }

    public EstadoBus getEstadoBus() {
        return estadoBus;
    }

    public DireccionRuta getDireccionActual() {
        return direccionActual;
    }

    public String getParadaActualNombre() {
        return ruta.getParada(posicionActual).getNombre();
    }

    public Image getIcono() {
        return icono;
    }

    public void setIcono(Image icono) {
        this.icono = icono;
    }
}

