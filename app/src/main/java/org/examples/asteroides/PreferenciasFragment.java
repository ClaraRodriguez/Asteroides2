package org.examples.asteroides;

import android.preference.CheckBoxPreference;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

public class PreferenciasFragment extends PreferenceFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferencias);

        final EditTextPreference fragmentos = (EditTextPreference)findPreference("fragmentos");
        final CheckBoxPreference musica = (CheckBoxPreference) findPreference("musica");
        final CheckBoxPreference sonido = (CheckBoxPreference) findPreference("sonido");

        fragmentos.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener(){
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue){
                int valor;
                try{
                    valor = Integer.parseInt((String)newValue);
                } catch(Exception e){
                    Toast.makeText(getActivity(), "Ha de ser un número", Toast.LENGTH_SHORT).show();
                    return false;
                }
                if (valor >=0 && valor <= 9){
                    fragmentos.setSummary("En cuantos trozos se divide un asteroide (" + valor + ")");
                    return true;
                } else{
                    Toast.makeText(getActivity(), "Máximo de fragmentos 9", Toast.LENGTH_SHORT).show();
                    return false;
                }
            }
        });

        /*musica.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener(){
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue){
                boolean valor;
                try{
                    valor = (boolean) newValue;
                } catch(Exception e){
                    Toast.makeText(getActivity(), "Error con la música", Toast.LENGTH_SHORT).show();
                    return false;
                }
                if(valor){
                    musica.setSummary("La música está activada.");
                    return true;
                } else{
                    return false;
                }
            }
        });

        sonido.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener(){
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue){
                boolean valor;
                try{
                    valor = (boolean) newValue;
                } catch(Exception e){
                    Toast.makeText(getActivity(), "Error con el sonido", Toast.LENGTH_SHORT).show();
                    return false;
                }
                if(valor){
                    sonido.setSummary("El sonido está activado.");
                    return true;
                } else{
                    return false;
                }
            }
        });*/
    }
}
