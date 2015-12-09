package com.example.gustavovega.tesisgjg;

import android.util.Log;

import org.json.JSONArray;

import java.util.ArrayList;

/**
 * Created by Gustavo Vega on 09-12-2015.
 */
public class MainJson {
    public ArrayList<String> obtenerDatosJson(String response){
        ArrayList<String> listado= new ArrayList<>();

        try{
            JSONArray jsonarray= new JSONArray(response);
            Log.w("Ex obtenerdatosjson", "jsonarray tamano " + jsonarray.length());
            String rut,nombre,telefono,email,clave;
            for (int i=0;i<jsonarray.length();i++){
                rut= jsonarray.getJSONObject(i).getString("RUT");
                nombre= jsonarray.getJSONObject(i).getString("Nombre");
                telefono=jsonarray.getJSONObject(i).getString("Telefono");
                email=jsonarray.getJSONObject(i).getString("Email");
                clave= jsonarray.getJSONObject(i).getString("Clave");

                listado.add(rut);
                Log.w("obtenerDatosJson", "RUT " + rut);
                listado.add(nombre);
                Log.w("obtenerDatosJson", "Nombre " + nombre);
                listado.add(telefono);
                Log.w("obtenerDatosJson", "Telefono" + telefono);
                listado.add(email);
                Log.w("obtenerDatosJson", "email"+email);
                listado.add(clave);
                Log.w("obtenerDatosJson", "clave"+clave);
            }
        }catch (Exception ex){


            Log.w("Catch obtenerdatosjson","jsonarray"+ex.getMessage());
        }

        return listado;

    }
}
