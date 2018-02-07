package org.examples.asteroides;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.util.Vector;

public class AlmacenPuntuacionesFicheroInterno implements AlmacenPuntuaciones {
    private static String FICHERO = "puntuaciones.txt";
    private Context context;

    public AlmacenPuntuacionesFicheroInterno(Context context){
        this.context = context;
    }

    @Override
    public void guardarPuntuacion(int puntos, String nombre, long fecha) {
        try{
            FileOutputStream f = context.openFileOutput(FICHERO, Context.MODE_APPEND);
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
            FileInputStream f =  context.openFileInput(FICHERO);
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
