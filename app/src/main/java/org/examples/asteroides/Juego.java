package org.examples.asteroides;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class Juego extends Activity {
    private VistaJuego vistaJuego;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_juego);

        vistaJuego = (VistaJuego) findViewById(R.id.VistaJuego);
        vistaJuego.setPadre(this);
    }

    @Override
    protected void onResume(){
        super.onResume();
        vistaJuego.getThread().reanudar();
        vistaJuego.activarSensores(this);
    }

    @Override
    protected void onPause(){
        vistaJuego.getThread().pausar();
        vistaJuego.desactivarSensores(this);
        super.onPause();
    }

    @Override
    protected void onDestroy(){
        vistaJuego.getThread().detener();
        vistaJuego.desactivarSensores(this);
        super.onDestroy();
    }

    @Override
    protected void onRestart(){
        vistaJuego.activarSensores(this);
        super.onRestart();
    }

    @Override
    protected void onStop(){
        vistaJuego.desactivarSensores(this);
        super.onStop();
    }

    @Override
    protected void onStart(){
        vistaJuego.activarSensores(this);
        super.onStart();
    }
}
