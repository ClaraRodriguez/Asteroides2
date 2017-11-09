package org.examples.asteroides;

import java.util.Vector;

/**
 * Created by Vespertino on 08/11/2017.
 */

public interface AlmacenPuntuaciones {
    public void guardarPuntuacion(int puntos, String nombre, long fecha);
    public Vector<String> listaPuntuaciones(int cantidad);
}
