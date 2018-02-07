package org.examples.asteroides;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.util.Vector;

public class AlmacenPuntuacionesFicheroExterno implements AlmacenPuntuaciones {
    private static String FICHERO = Environment.getExternalStorageDirectory() + "/puntuaciones.txt";
    private Context context;

    public AlmacenPuntuacionesFicheroExterno(Context context){
        this.context = context;
    }

    @Override
    public void guardarPuntuacion(int puntos, String nombre, long fecha) {
        try{
            FileOutputStream f = new FileOutputStream(FICHERO, true);
            String texto = puntos + " " + nombre + "\n";
            f.write(texto.getBytes());
            f.close();
        } catch (Exception e){
            Log.e("Asteroides", e.getMessage(), e);
        }
    }

    @Override
    public Vector<String> listaPuntuaciones(int cantidad) {
        Vector<String> result = new Vector<String>();
        try{
            FileInputStream f =  new FileInputStream(FICHERO);
            BufferedReader entrada = new BufferedReader(new InputStreamReader(f));
            String linea;

            while((linea = entrada.readLine()) != null){
                result.add(linea);
            }

            f.close();
        } catch (Exception e){
            Log.e("Asteroides", e.getMessage(), e);
        }
        return result;
    }
}
