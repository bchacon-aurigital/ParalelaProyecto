package com.mycompany.proyectofinal.simulador;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Ruta {

    private final List<Parada> paradas;

    public Ruta(List<Parada> paradas) {
        this.paradas = new ArrayList<>(paradas);
    }

    public List<Parada> getParadas() {
        return Collections.unmodifiableList(paradas);
    }

    public Parada getParada(int indice) {
        return paradas.get(indice);
    }

    public int size() {
        return paradas.size();
    }

    public void reiniciarParadas() {
        for (Parada parada : paradas) {
            parada.reiniciar();
        }
    }

    public List<int[]> getPuntosIntermedios(int paradaActual) {
        List<int[]> puntos = new ArrayList<>();

        switch (paradaActual) {
            case 0:
                puntos.add(new int[]{33, 130});
                break;

            case 5:
                puntos.add(new int[]{413, 229});
                break;

            case 8:
                puntos.add(new int[]{576, 290});
                puntos.add(new int[]{659, 277});
                puntos.add(new int[]{732, 262});
                puntos.add(new int[]{821, 258});
                break;

            case 9:
                puntos.add(new int[]{896, 296});
                puntos.add(new int[]{947, 357});
                puntos.add(new int[]{983, 395});
                puntos.add(new int[]{941, 428});
                puntos.add(new int[]{914, 515});
                break;

            case 11:
                puntos.add(new int[]{1067, 543});
                break;

            case 12:
                puntos.add(new int[]{1004, 502});
                break;

            case 13:
                puntos.add(new int[]{974, 408});
                puntos.add(new int[]{983, 385});
                puntos.add(new int[]{947, 358});
                puntos.add(new int[]{922, 325});
                break;

            case 14:
                puntos.add(new int[]{922, 325});
                puntos.add(new int[]{896, 296});
                puntos.add(new int[]{821, 258});
                puntos.add(new int[]{732, 262});
                puntos.add(new int[]{560, 300});
                puntos.add(new int[]{505, 288});
                puntos.add(new int[]{471, 263});
                break;

            case 15:
                puntos.add(new int[]{471, 263});
                break;

            case 16:
                puntos.add(new int[]{353, 214});
                puntos.add(new int[]{333, 190});
                break;

            case 18:
                puntos.add(new int[]{33, 130});
                break;

            default:
                break;
        }

        return puntos;
    }

    public static Ruta crearRutaCartago() {
        List<Parada> lista = new ArrayList<>();
        lista.add(p(1, "Terminal Lumaca San José", "San José", "Ida", 38, 98));
        lista.add(p(2, "Frente a Parque Montealegre", "San José", "Ida", 124, 135));
        lista.add(p(3, "Agencia Purdy Motor Zapote", "San José", "Ida", 167, 162));
        lista.add(p(4, "Plaza Registro", "Curridabat", "Ida", 230, 167));
        lista.add(p(5, "Bar El Prado", "Curridabat", "Ida", 320, 194));
        lista.add(p(6, "Parque Las Embajadas", "Curridabat", "Ida", 371, 212));
        lista.add(p(7, "Puente Villas de Ayarco", "La Unión", "Ida", 468, 260));
        lista.add(p(8, "Costado Palí San Diego", "La Unión", "Ida", 533, 300));
        lista.add(p(9, "Puente Peatonal Terramall", "La Unión", "Ida", 570, 290));
        lista.add(p(10, "Servicentro Cristo Rey", "Cartago", "Ida", 859, 290));
        lista.add(p(11, "Paseo Metrópoli", "Cartago", "Ida", 930, 525));
        lista.add(p(12, "Cementerio General de Cartago", "Cartago", "Ida", 992, 537));
        lista.add(p(13, "Terminal Lumaca Cartago", "Cartago", "Ida/Vuelta", 1070, 523));
        lista.add(p(14, "Colegio San Nicolás, Taras", "Cartago", "Vuelta", 1000, 470));
        lista.add(p(15, "Recope Plantel Ochomogo", "Cartago", "Vuelta", 940, 320));
        lista.add(p(16, "Puente Tres Ríos", "La Unión", "Vuelta", 476, 258));
        lista.add(p(17, "Centro Comercial Pinares Place", "Curridabat", "Vuelta", 415, 225));
        lista.add(p(18, "Registro Nacional", "Curridabat", "Vuelta", 230, 167));
        lista.add(p(19, "Parque Montealegre", "San José", "Vuelta", 124, 135));
        lista.add(p(20, "Terminal Lumaca San José", "San José", "Vuelta", 38, 98));
        return new Ruta(lista);
    }

    private static Parada p(int numero, String nombre, String localidad, String sentido, int x, int y) {
        return new Parada(numero, nombre, localidad, sentido, x, y);
    }
}

