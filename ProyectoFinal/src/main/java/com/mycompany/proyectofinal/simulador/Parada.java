package com.mycompany.proyectofinal.simulador;

/**
 * Representa una parada fija dentro de la ruta.
 */
public class Parada {

    private final int numero;
    private final String nombre;
    private final String localidad;
    private final String sentido;
    private final int x;
    private final int y;

    private boolean ocupada;
    private Autobus autobusActual;

    public Parada(int numero, String nombre, String localidad, String sentido, int x, int y) {
        this.numero = numero;
        this.nombre = nombre;
        this.localidad = localidad;
        this.sentido = sentido;
        this.x = x;
        this.y = y;
    }

    public int getNumero() {
        return numero;
    }

    public String getNombre() {
        return nombre;
    }

    public String getLocalidad() {
        return localidad;
    }

    public String getSentido() {
        return sentido;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public synchronized boolean intentarOcupar(Autobus autobus) {
        if (!ocupada) {
            ocupada = true;
            autobusActual = autobus;
            return true;
        }
        return false;
    }

    public synchronized void ocuparInicial(Autobus autobus) {
        ocupada = true;
        autobusActual = autobus;
    }

    public synchronized void liberar(Autobus autobus) {
        if (autobusActual == autobus) {
            ocupada = false;
            autobusActual = null;
        }
    }

    public synchronized boolean estaOcupada() {
        return ocupada;
    }

    public synchronized void reiniciar() {
        ocupada = false;
        autobusActual = null;
    }

    @Override
    public String toString() {
        return numero + ". " + nombre + " (" + localidad + ")";
    }
}



