package org.examples.asteroides;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;

import java.util.Vector;

public class VistaJuego extends View {
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

    public VistaJuego(Context context, AttributeSet attrs){
        super(context, attrs);
        Drawable drawableNave, drawableAsteroide, drawableMisil;
        drawableAsteroide = context.getResources().getDrawable(R.drawable.asteroide1);
        drawableNave = context.getResources().getDrawable(R.drawable.nave);
        nave = new Grafico(this, drawableNave);
        asteroides = new Vector<Grafico>();
        for (int i = 0; i < numAsteroides; i++){
            Grafico asteroide = new Grafico(this, drawableAsteroide);
            asteroide.setIncY(Math.random() * 4 - 2);
            asteroide.setIncX(Math.random() * 4 - 2);
            asteroide.setAngulo((int) (Math.random() * 360));
            asteroide.setRotacion((int) (Math.random() * 8 - 4));
            asteroides.add(asteroide);
        }
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
    }

    @Override
    protected void onDraw(Canvas canvas){
        super.onDraw(canvas);
        for(Grafico asteroide : asteroides){
            asteroide.dibujarGrafico(canvas);
        }
        nave.dibujarGrafico(canvas);
    }
}
