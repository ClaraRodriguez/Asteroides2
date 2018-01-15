package org.examples.asteroides;

import android.content.Intent;
import android.content.SharedPreferences;
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
        startActivity(i);
    }

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
        startActivity(i);
    }

    @Override
    protected void onStart(){
        super.onStart();
        Toast.makeText(this, "onStart", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onResume(){
        super.onResume();
        Toast.makeText(this, "onResume", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onPause(){
        Toast.makeText(this, "onPause", Toast.LENGTH_SHORT).show();
        super.onPause();
    }

    @Override
    protected void onStop(){
        Toast.makeText(this, "onStop", Toast.LENGTH_SHORT).show();
        super.onStop();
    }

    @Override
    protected void onRestart(){
        super.onRestart();
        Toast.makeText(this, "onRestart", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy(){
        Toast.makeText(this, "onDestroy", Toast.LENGTH_SHORT).show();
        super.onDestroy();
    }
}
