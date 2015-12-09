package com.example.gustavovega.tesisgjg;

import android.content.Intent;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by Gustavo Vega on 09-12-2015.
 */
public class MainEnvio {
    public Intent cargaIntent(ArrayList<String> datos,String rut,String password, String server){
        Intent i = new Intent();
        try{


            i.putExtra("rut",datos.get(0).toString());
            i.putExtra("nombre",datos.get(1).toString());
            i.putExtra("telefono",datos.get(2).toString());
            i.putExtra("email",datos.get(3).toString());
            i.putExtra("clave", datos.get(4));
            i.putExtra("servidor", server);
            Log.i("SALIDA MAIN ACTIVITY", "ENTRADA LISTADO PROD");
            return i;
        }catch(Exception ex){
            Log.w("catch de envioListado","Detalles: "+ex.getMessage());
        }
        return i;
    }
}
