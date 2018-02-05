package org.examples.asteroides;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.PathShape;
import android.graphics.drawable.shapes.RectShape;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;

import java.util.List;
import java.util.Vector;

public class VistaJuego extends View implements SensorEventListener {
    //ASTEROIDES//
    private Vector<Grafico> asteroides;
    private int numAsteroides = 5;
    private int numFragmentos = 3;

    //NAVE//
    private Grafico nave;
    private int giroNave;
    private double aceleracionNave;
    private static final int MAX_VELOCIDAD_NAVE = 20;

    //Incremento estándar de giro y aceleración
    private static final int PASO_GIRO_NAVE = 5;
    private static final float PASO_ACELERACION_NAVE = 0.5f;

    private float mX = 0, mY = 0;
    private boolean disparo = false;

    private boolean hayValorInicial = false;
    private float valorInicial;

    /////MISIL/////
    //private Grafico misil;
    private Vector<Grafico> misiles;
    private static int PASO_VELOCIDAD_MISIL = 12;
    //private boolean misilActivo = false;
    //private int tiempoMisil;
    private Vector<Integer> tiempoMisiles;

    Drawable drawableMisil;
    private Drawable drawableAsteroide[] = new Drawable[3];

    //THREAD Y TIEMPO
    //Thread encargado de procesar el juego
    private ThreadJuego thread = new ThreadJuego();

    //Cada cuanto queremos procesar cambios (ms)
    private static int PERIODO_PROCESO = 50;

    //Cuándo se realozó el último proceso
    private long ultimoProceso = 0;


    //MÚSICA
    SensorManager mSensorManager;
    List<Sensor> listSensors;

    /////MULTIMEDIA/////
    SoundPool soundPool;
    int idDisparo, idExplosion;

    //ALMACENAMIENTO
    private int puntuacion = 0;
    private Activity padre;

    public VistaJuego(Context context, AttributeSet attrs){
        super(context, attrs);
        Drawable drawableNave;

        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getContext());
        if(pref.getString("graficos", "1").equals("0")){
            Path pathAsteroide = new Path();
            pathAsteroide.moveTo((float) 0.3, (float) 0.0);
            pathAsteroide.lineTo((float) 0.6, (float) 0.0);
            pathAsteroide.lineTo((float) 0.6, (float) 0.3);
            pathAsteroide.lineTo((float) 0.8, (float) 0.2);
            pathAsteroide.lineTo((float) 1.0, (float) 0.4);
            pathAsteroide.lineTo((float) 0.8, (float) 0.6);
            pathAsteroide.lineTo((float) 0.9, (float) 0.9);
            pathAsteroide.lineTo((float) 0.8, (float) 1.0);
            pathAsteroide.lineTo((float) 0.4, (float) 1.0);
            pathAsteroide.lineTo((float) 0.0, (float) 0.6);
            pathAsteroide.lineTo((float) 0.0, (float) 0.2);
            pathAsteroide.lineTo((float) 0.3, (float) 0.0);

            //drawableAsteroide = dAsteroide;
            for (int i = 0; i < 3; i++){
                ShapeDrawable dAsteroide = new ShapeDrawable(new PathShape(pathAsteroide, 1, 1));
                dAsteroide.getPaint().setColor(Color.WHITE);
                dAsteroide.getPaint().setStyle(Paint.Style.STROKE);
                dAsteroide.setIntrinsicWidth(50 - i * 14);
                dAsteroide.setIntrinsicHeight(50 - i * 14);
                drawableAsteroide[i] = dAsteroide;
            }
            setBackgroundColor(Color.BLACK);
            setLayerType(View.LAYER_TYPE_SOFTWARE, null);
            //nave
            Path pathNave = new Path();
            pathNave.moveTo((float) 0, (float) 0);
            pathNave.lineTo((float) 1.0, (float) 0.5);
            pathNave.lineTo((float) 0.0, (float) 1.0);
            pathNave.lineTo((float) 0.0, (float) 0.0);
            ShapeDrawable dNave = new ShapeDrawable(new PathShape(pathNave, 1, 1));
            dNave.getPaint().setColor(Color.WHITE);
            dNave.getPaint().setStyle(Paint.Style.STROKE);
            dNave.setIntrinsicHeight(15);
            dNave.setIntrinsicWidth(20);
            drawableNave = dNave;
            //misil
            ShapeDrawable dMisil = new ShapeDrawable(new RectShape());
            dMisil.getPaint().setColor(Color.WHITE);
            dMisil.getPaint().setStyle(Paint.Style.STROKE);
            dMisil.setIntrinsicWidth(15);
            dMisil.setIntrinsicHeight(3);
            drawableMisil = dMisil;
        } else{
            //drawableAsteroide = context.getResources().getDrawable(R.drawable.asteroide1);
            drawableAsteroide[0] = context.getResources().getDrawable(R.drawable.asteroide1);
            drawableAsteroide[1] = context.getResources().getDrawable(R.drawable.asteroide2);
            drawableAsteroide[2] = context.getResources().getDrawable(R.drawable.asteroide3);
            setLayerType(View.LAYER_TYPE_HARDWARE, null);
            //nave
            drawableNave = context.getResources().getDrawable(R.drawable.nave);
            //misil
            drawableMisil = context.getResources().getDrawable(R.drawable.misil1);
        }

        nave = new Grafico(this, drawableNave);
        asteroides = new Vector<Grafico>();
        for (int i = 0; i < numAsteroides; i++){
            Grafico asteroide = new Grafico(this, drawableAsteroide[i]); //sin [i]
            asteroide.setIncY(Math.random() * 4 - 2);
            asteroide.setIncX(Math.random() * 4 - 2);
            asteroide.setAngulo((int) (Math.random() * 360));
            asteroide.setRotacion((int) (Math.random() * 8 - 4));
            asteroides.add(asteroide);
        }

        //misil = new Grafico(this, drawableMisil);
        misiles = new Vector<Grafico>();
        tiempoMisiles = new Vector<Integer>();

        mSensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        listSensors = mSensorManager.getSensorList(Sensor.TYPE_ORIENTATION);

        /*if(pref.getBoolean("musica", true)){ // == true
            soundPool = new SoundPool(5, AudioManager.STREAM_MUSIC, 0);
        } else if (pref.getBoolean("musica", false)){

        }*/

        soundPool = new SoundPool(5, AudioManager.STREAM_MUSIC, 0);

        /*if(pref.getBoolean("sonido", true) ){
            idDisparo = soundPool.load(context, R.raw.disparo, 0);
            idExplosion = soundPool.load(context, R.raw.explosion, 0);
        } else if (pref.getBoolean("sonido", false)){

        }*/

        idDisparo = soundPool.load(context, R.raw.disparo, 0);
        idExplosion = soundPool.load(context, R.raw.explosion, 0);

    }


    @Override
    protected void onSizeChanged(int ancho, int alto, int ancho_anterior, int alto_anterior){
        super.onSizeChanged(ancho, alto, ancho_anterior, alto_anterior);
        nave.setCenX(this.getWidth()/2);
        nave.setCenY(this.getHeight()/2);
        //Una vez conocemos nuestro ancho y alto
        for(Grafico asteroide : asteroides){
            do{
                asteroide.setCenX((int) (Math.random()*ancho));
                asteroide.setCenY((int) (Math.random()*alto));
            }while(asteroide.distancia(nave) < (ancho+alto)/5); //entre asteroides tambien
        }
        ultimoProceso = System.currentTimeMillis();
        thread.start();
    }

    @Override
    protected void onDraw(Canvas canvas){
        super.onDraw(canvas);
        synchronized (asteroides) {
            for (Grafico asteroide : asteroides) {
                asteroide.dibujarGrafico(canvas);
            }

            for (int i = misiles.size()-1; i >= 0; i--){
                if (tiempoMisiles.get(i) > 0){
                    misiles.get(i).dibujarGrafico(canvas);
                }
            }
        }
        nave.dibujarGrafico(canvas);
        /*if (misilActivo){
            misil.dibujarGrafico(canvas);
        }*/
    }

    protected synchronized void actualizaFisica(){
        long ahora = System.currentTimeMillis();
        if (ultimoProceso + PERIODO_PROCESO > ahora){
            return;
        }
        //calcular el factor de movimiento
        double factorMov = (ahora - ultimoProceso) / PERIODO_PROCESO;
        ultimoProceso = ahora; //para la próxima vez
        //Actualizamos velocidad y dirección de la nave a partir de
        //giroNave y aceleracionNave (según la entrada del jugador)
        nave.setAngulo((int) (nave.getAngulo() + giroNave * factorMov));
        double nIncX = nave.getIncX() + aceleracionNave * Math.cos(Math.toRadians(nave.getAngulo())) * factorMov;
        double nIncY = nave.getIncY() + aceleracionNave * Math.sin(Math.toRadians(nave.getAngulo())) * factorMov;
        //Actualizamos si el módulo de la velocidad no extiende el máximo
        if (Math.hypot(nIncX,nIncY) <= MAX_VELOCIDAD_NAVE){
            nave.setIncX(nIncX);
            nave.setIncY(nIncY);
        }
        nave.incrementarPos(factorMov); //actualizamos posición
        for(Grafico asteroide : asteroides){
            asteroide.incrementarPos(factorMov);
        }

        //Actualizamos posición de misil
        for (int i = misiles.size()-1; i >= 0; i--){
            Grafico misil = misiles.get(i);
            misil.incrementarPos(factorMov);
            tiempoMisiles.set(i, tiempoMisiles.get(i) - (int)factorMov);
            if (tiempoMisiles.get(i) < 0){
                misiles.remove(i);
                tiempoMisiles.remove(i);
            }else{
                for (int j = 0; j < asteroides.size(); j++) {
                    if (misil.verificarColision(asteroides.elementAt(j))) {
                        destruyeAsteroide(j);
                        misiles.remove(i);
                        tiempoMisiles.remove(i);
                        break;
                    }
                }
            }
        }

        for (Grafico asteroide : asteroides){
            if(asteroide.verificarColision(nave)){
                salir();
            }
        }
    }

    @Override
    public boolean onKeyDown(int codigoTecla, KeyEvent evento){
        super.onKeyDown(codigoTecla, evento);
        boolean procesada = true;
        switch (codigoTecla) {
            case KeyEvent.KEYCODE_DPAD_UP:
                aceleracionNave = +PASO_ACELERACION_NAVE;
                break;
            case KeyEvent.KEYCODE_DPAD_LEFT:
                giroNave = -PASO_GIRO_NAVE;
                break;
            case KeyEvent.KEYCODE_DPAD_RIGHT:
                giroNave = +PASO_GIRO_NAVE;
                break;
            case KeyEvent.KEYCODE_DPAD_CENTER:
            case KeyEvent.KEYCODE_ENTER:
                activaMisil();
                break;
            default:
                procesada = false;
                break;
        }
        return procesada;
    }

    @Override
    public boolean onTouchEvent (MotionEvent event){
        super.onTouchEvent(event);
        float x = event.getX();
        float y = event.getY();
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                disparo = true;
                break;
            case MotionEvent.ACTION_MOVE:
                float dx = Math.abs(x - mX);
                float dy = Math.abs(y - mY);
                if (dy < 6 && dx > 6){
                    giroNave = Math.round((x - mX) / 2);
                    disparo = false;
            } else if (dx < 6 && dy > 6){
                    aceleracionNave = Math.round((mY - y) / 25);
                    disparo = false;
                }
                break;
            case MotionEvent.ACTION_UP:
                giroNave = 0;
                aceleracionNave = 0;
                if (disparo){
                    activaMisil();
                }
                break;
        }
        mX = x;
        mY = y;
        return true;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {}

    @Override
    public void onSensorChanged(SensorEvent event) {
        float valor = event.values[1];
        if(!hayValorInicial){
            valorInicial = valor;
            hayValorInicial = true;
        }
        giroNave = (int) (valor - valorInicial)/3;
    }

    private void destruyeAsteroide(int i){
        int tam;
        if(asteroides.get(i).getDrawable() != drawableAsteroide[2]){
            if(asteroides.get(i).getDrawable() == drawableAsteroide[1]){
                tam = 2;
            } else{
                tam = 1;
            }

            for (int n = 0; n < numFragmentos; n++){
                Grafico asteroide = new Grafico(this, drawableAsteroide[tam]);
                asteroide.setCenX(asteroides.get(i).getCenX());
                asteroide.setCenY(asteroides.get(i).getCenY());
                asteroide.setIncX(Math.random()*7-2-tam);
                asteroide.setIncY(Math.random()*7-2-tam);
                asteroide.setAngulo((int)(Math.random()*360));
                asteroide.setRotacion((int) Math.random()*8-4);
                asteroides.add(asteroide);
            }
        }
        synchronized (asteroides){
            asteroides.remove(i);
            //misilActivo = false;
        }
        this.postInvalidate();
        soundPool.play(idExplosion, 1, 1, 0, 0, 1);
        puntuacion += 1000;

        if (asteroides.isEmpty()){
            salir();
        }
    }

    private void activaMisil(){
        Grafico misil = new Grafico(this, drawableMisil);
        misil.setCenX(nave.getCenX());
        misil.setCenY(nave.getCenY());
        misil.setAngulo(nave.getAngulo());
        misil.setIncX(Math.cos(Math.toRadians(misil.getAngulo())) * PASO_VELOCIDAD_MISIL);
        misil.setIncY(Math.sin(Math.toRadians(misil.getAngulo())) * PASO_VELOCIDAD_MISIL);
        misiles.add(misil);
        tiempoMisiles.add((int) Math.min(this.getWidth() / Math.abs(misil.getIncX()), this.getHeight() / Math.abs(misil.getIncY()))-2);
        //tiempoMisil = (int) Math.min(this.getWidth() / Math.abs(misil.getIncX()), this.getHeight() / Math.abs(misil.getIncY())) - 2;
        //misilActivo = true;

        soundPool.play(idDisparo, 1, 1, 1, 0, 1);
    }

    public void activarSensores(Context context){
        if(!listSensors.isEmpty()){
            Sensor orientationSensor = listSensors.get(0);
            mSensorManager.registerListener(this, orientationSensor, SensorManager.SENSOR_DELAY_GAME);
        }

    }

    public void desactivarSensores(Context context){
        if(listSensors.isEmpty()){
            mSensorManager.unregisterListener(this);
        }
    }

    public void setPadre(Activity padre){
        this.padre = padre;
    }

    private void salir(){
        Bundle bundle = new Bundle();
        bundle.putInt("puntuacion", puntuacion);
        Intent intent = new Intent();
        intent.putExtras(bundle);
        padre.setResult(Activity.RESULT_OK, intent);
        padre.finish();
    }

    class ThreadJuego extends Thread{
        private boolean pausa, corriendo;

        public synchronized void pausar(){
            pausa = true;
        }

        public synchronized void reanudar(){
            pausa = false;
            notify();
        }

        public void detener(){
            corriendo = false;
            if (pausa) reanudar();
        }

        @Override
        public void run(){
            corriendo = true;
            while(corriendo){
                actualizaFisica();
                synchronized (this){
                    while(pausa){
                        try{
                            wait();
                        } catch(Exception e){
                        }
                    }
                }
            }
        }
    }

    public ThreadJuego getThread(){
        return thread;
    }
}


