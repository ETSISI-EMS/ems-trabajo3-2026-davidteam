package com.practica.ems.covid;

import java.util.Iterator;
import java.util.LinkedList;

import com.practica.excecption.EmsDuplicateLocationException;
import com.practica.excecption.EmsLocalizationNotFoundException;
import com.practica.genericas.FechaHora;
import com.practica.genericas.PosicionPersona;

public class Localizacion {
    LinkedList<PosicionPersona> lista;

    public Localizacion() {
        super();
        this.lista = new LinkedList<PosicionPersona>();
    }

    public LinkedList<PosicionPersona> getLista() {
        return lista;
    }

    public void setLista(LinkedList<PosicionPersona> lista) {
        this.lista = lista;
    }

    public void addLocalizacion(PosicionPersona p) throws EmsDuplicateLocationException {
        try {
            findLocalizacion(p.getDocumento(), formatearFecha(p), formatearHora(p));
            throw new EmsDuplicateLocationException();
        } catch (EmsLocalizationNotFoundException e) {
            lista.add(p);
        }
    }

    public int findLocalizacion(String documento, String fecha, String hora) throws EmsLocalizationNotFoundException {
        int cont = 0;
        Iterator<PosicionPersona> it = lista.iterator();
        while (it.hasNext()) {
            cont++;
            PosicionPersona pp = it.next();
            FechaHora fechaHora = parsearFecha(fecha, hora);
            if (pp.getDocumento().equals(documento) &&
                    pp.getFechaPosicion().equals(fechaHora)) {
                return cont;
            }
        }
        throw new EmsLocalizationNotFoundException();
    }

    public void delLocalizacion(String documento, String fecha, String hora) throws EmsLocalizationNotFoundException {
        int pos = -1;
        try {
            pos = findLocalizacion(documento, fecha, hora);
        } catch (EmsLocalizationNotFoundException e) {
            throw new EmsLocalizationNotFoundException();
        }
        this.lista.remove(pos);
    }

    void printLocalizacion() {
        for (int i = 0; i < this.lista.size(); i++) {
            imprimirLinea(i, lista.get(i));
        }
    }

    @Override
    public String toString() {
        StringBuilder cadena = new StringBuilder();
        for (int i = 0; i < this.lista.size(); i++) {
            cadena.append(formatearLinea(lista.get(i)));
        }
        return cadena.toString();
    }

    private void imprimirLinea(int index, PosicionPersona pp) {
        System.out.printf("%d;%s;", index, pp.getDocumento());
        System.out.printf("%s;", formatearFechaHora(pp));
        System.out.printf("%.4f;%.4f\n",
                pp.getCoordenada().getLatitud(),
                pp.getCoordenada().getLongitud());
    }

    private String formatearLinea(PosicionPersona pp) {
        return String.format("%s;%s;%.4f;%.4f\n",
                pp.getDocumento(),
                formatearFechaHora(pp),
                pp.getCoordenada().getLatitud(),
                pp.getCoordenada().getLongitud());
    }

    private String formatearFechaHora(PosicionPersona pp) {
        FechaHora fecha = pp.getFechaPosicion();
        return String.format("%02d/%02d/%04d;%02d:%02d",
                fecha.getFecha().getDia(),
                fecha.getFecha().getMes(),
                fecha.getFecha().getAnio(),
                fecha.getHora().getHora(),
                fecha.getHora().getMinuto());
    }

    private String formatearFecha(PosicionPersona pp) {
        FechaHora fecha = pp.getFechaPosicion();
        return String.format("%02d/%02d/%04d",
                fecha.getFecha().getDia(),
                fecha.getFecha().getMes(),
                fecha.getFecha().getAnio());
    }

    private String formatearHora(PosicionPersona pp) {
        FechaHora fecha = pp.getFechaPosicion();
        return String.format("%02d:%02d",
                fecha.getHora().getHora(),
                fecha.getHora().getMinuto());
    }

    @SuppressWarnings("unused")
    private FechaHora parsearFecha(String fecha) {
        int dia, mes, anio;
        String[] valores = fecha.split("\\/");
        dia = Integer.parseInt(valores[0]);
        mes = Integer.parseInt(valores[1]);
        anio = Integer.parseInt(valores[2]);
        FechaHora fechaHora = new FechaHora(dia, mes, anio, 0, 0);
        return fechaHora;
    }

    private FechaHora parsearFecha(String fecha, String hora) {
        int dia, mes, anio;
        String[] valores = fecha.split("\\/");
        dia = Integer.parseInt(valores[0]);
        mes = Integer.parseInt(valores[1]);
        anio = Integer.parseInt(valores[2]);
        int minuto, segundo;
        valores = hora.split("\\:");
        minuto = Integer.parseInt(valores[0]);
        segundo = Integer.parseInt(valores[1]);
        FechaHora fechaHora = new FechaHora(dia, mes, anio, minuto, segundo);
        return fechaHora;
    }
}