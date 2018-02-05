package org.examples.asteroides;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private Button btnJugar;
    private Button btnAcercaDe;
    private Button btnSalir;
    private Button btnConfig;

    private TextView titulo;
    Animation animacion;

    MediaPlayer mp;

    static final int ACTIV_JUEGO = 0;

    public static AlmacenPuntuaciones almacen = new AlmacenPuntuacionesArray();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toast.makeText(this, "onCreate", Toast.LENGTH_SHORT).show();

        btnJugar = (Button) findViewById(R.id.btnjugar);
        btnAcercaDe = (Button) findViewById(R.id.btnacerca);
        btnSalir = (Button) findViewById(R.id.btnsalir);
        btnConfig = (Button) findViewById(R.id.btnconfig);

        titulo = (TextView) findViewById(R.id.txtTitulo);

        animacion = AnimationUtils.loadAnimation(this, R.anim.giro_con_zoom);
        titulo.startAnimation(animacion);
        Animation animacionJugar = AnimationUtils.loadAnimation(this, R.anim.aparecer);
        btnJugar.startAnimation(animacionJugar);
        Animation animacionConfig = AnimationUtils.loadAnimation(this, R.anim.desplazamiento_derecha);
        btnConfig.startAnimation(animacionConfig);
        Animation animacionAdercaDe = AnimationUtils.loadAnimation(this, R.anim.zoom_desplazamiento);
        btnAcercaDe.startAnimation(animacionAdercaDe);
        Animation animacionPuntuaciones = AnimationUtils.loadAnimation(this, R.anim.giro_aparecer);
        btnSalir.startAnimation(animacionPuntuaciones);

        mp = MediaPlayer.create(this, R.raw.audio);
        //mp.start();

        //almacen = new AlmacenPuntuacionesPreferencias(this);

        btnJugar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //mostrarPreferencias(view);
                lanzarJuego(view);
            }
        });

        btnAcercaDe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnAcercaDe.startAnimation(animacion);
                lanzarAcercaDe(null);
            }
        });

        btnSalir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lanzarPuntuaciones(null);
            }
        });

        btnConfig.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lanzarConfig(null);
            }
        });
    }

    public void lanzarConfig(View view){
        Intent i = new Intent(this, PreferenciasActivity.class);
        startActivityForResult(i, 0);
    }

    /*@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        checkPref();
    }*/

    /*private boolean checkPref(){
        SharedPreferences myPref = PreferenceManager.getDefaultSharedPreferences(this);
        return myPref.getBoolean("musica", false);
    }*/

    public void lanzarAcercaDe(View view){
        Intent i = new Intent(this, AcercaDeActivity.class);
        startActivity(i);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();
        if(id == R.id.acercaDe){
            lanzarAcercaDe(null);
            return true;
        }
        if(id == R.id.action_settings){
            lanzarConfig(null);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void mostrarPreferencias(View view){
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
        String s = "música: " + pref.getBoolean("musica",true)
                + ", gráficos: " + pref.getString("graficos","?")
                + ", fragmentos: " + pref.getString("fragmentos","?")
                + ", multijugador: " + pref.getBoolean("multi",true)
                + ", jugadores: " + pref.getString("jugadores","?")
                + ", conexión: " + pref.getString("conexion","?");
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }

    public void lanzarPuntuaciones(View view){
        Intent i = new Intent(this, Puntuaciones.class);
        startActivity(i);
    }

    public void lanzarJuego(View view){
        Intent i = new Intent(this, Juego.class);
        startActivityForResult(i, ACTIV_JUEGO);
    }

    @Override
    protected void onActivityResult (int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ACTIV_JUEGO && resultCode == RESULT_OK && data != null){
            int puntuacion = data.getExtras().getInt("puntuacion");
            String nombre = "Yo";
            // Mejor leer nombre desde un AlertDialog.Builder o preferencias
            almacen.guardarPuntuacion(puntuacion, nombre, System.currentTimeMillis());
            lanzarPuntuaciones(null);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle estadoGuardado){
        super.onSaveInstanceState(estadoGuardado);
        if (mp != null){
            int pos = mp.getCurrentPosition();
            estadoGuardado.putInt("posicion", pos);
        }
    }

    @Override
    protected void onRestoreInstanceState(Bundle estadoGuardado){
        super.onRestoreInstanceState(estadoGuardado);
        if (estadoGuardado != null && mp != null){
            int pos = estadoGuardado.getInt("posicion");
            mp.seekTo(pos);
        }
    }

    @Override
    protected void onStart(){
        super.onStart();
        /*if(checkPref()) {

        }*/
        mp.start();
        Toast.makeText(this, "onStart", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onResume(){
        super.onResume();
        /*if(checkPref()) {

        }*/
        mp.start();
        Toast.makeText(this, "onResume", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onPause(){
        Toast.makeText(this, "onPause", Toast.LENGTH_SHORT).show();
        mp.pause();
        //mp.start();
        super.onPause();
    }

    @Override
    protected void onStop(){
        Toast.makeText(this, "onStop", Toast.LENGTH_SHORT).show();
        mp.pause();
        super.onStop();
    }

    @Override
    protected void onRestart(){
        super.onRestart();
        /*if(checkPref()) {

        }*/
        mp.start();
        Toast.makeText(this, "onRestart", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy(){
        Toast.makeText(this, "onDestroy", Toast.LENGTH_SHORT).show();
        mp.pause();
        super.onDestroy();
    }
}
