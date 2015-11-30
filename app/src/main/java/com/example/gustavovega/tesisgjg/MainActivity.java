package com.example.gustavovega.tesisgjg;

import android.content.Intent;
import android.os.Looper;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.loopj.android.http.*;


import org.json.JSONArray;


import java.util.ArrayList;
/*import java.util.logging.Handler;
import org.apache.http.Header;
import java.lang.reflect.Array;
import java.net.UnknownHostException;
*/

import cz.msebera.android.httpclient.Header;


public class MainActivity extends AppCompatActivity {
    EditText rut;
    EditText usuario;
    EditText contra;
    EditText servidor;
    Button aceptar;
    Button salir;
    ArrayList<String> listado= new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Asignamos los controles de nuestro layout xml a variables accesibles desde java
        rut=(EditText)findViewById(R.id.id);
        usuario= (EditText)findViewById(R.id.user);
        contra= (EditText)findViewById(R.id.pass);
        servidor= (EditText)findViewById(R.id.server);
        aceptar= (Button)findViewById(R.id.ButtonIngreso);
        salir= (Button)findViewById(R.id.ButtonSalida);


    }
    public void ingresoOnClick(View v){
        String id=rut.getText().toString();
        String user= usuario.getText().toString();
        String password=contra.getText().toString();
        String server=servidor.getText().toString();
        obtenerDatos(id,user, password, server);
    }
    public void salidaOnClick(View v){
        this.finish();
    }

    public void   obtenerDatos(final String id,final String user,final String pass,final String serv){
        try {
            AsyncHttpClient cliente = new AsyncHttpClient();
            Log.i("obetener datos", "check de datos " + user + " " + pass + " " + serv);
            RequestParams parametros = new RequestParams();
            parametros.add("rut", user);
            parametros.add("pass", pass);
            String serverPhp = "http://" + serv + "/tesis/Android/logindatos.php";
            Log.i("obetener datos", "antes del new asynchttphandler");

            cliente.post(serverPhp, parametros, new AsyncHttpResponseHandler() {


                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                    if (statusCode == 200) {
                        Log.i("en onsuccess", "Antes de verificar " + new String(responseBody));
                           if (responseBody.length!= 0) {
                            envioListado(obtenerDatosJson(new String(responseBody)),id, user, pass, serv);
                        }else{
                            Log.i("en onsuccess", "verificando contenido de responsebody " +responseBody.length);
                               Toast.makeText(MainActivity.this, "Usuario o Password incorrecto ", Toast.LENGTH_LONG).show();

                           }
                    }

                }


                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, final Throwable error) {
                    Log.w("onFailure", "Detalles dentro de on failure: " + error.getMessage());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            Toast.makeText(MainActivity.this, " Debe ingresar un host valido", Toast.LENGTH_LONG).show();

                        }
                    });
                }
            });

        }catch (Exception ex){
            Log.w("Catch de obtenerDatos","Detalles: "+ex.getLocalizedMessage());
        }
    }
    public void envioListado(ArrayList<String> datos,String id, String user,String password, String server){
        Log.i("Verificar dentro", "check antes del envío"+ user +" "+ password +" "+server+" "+datos.get(2).toString());
        try{
            Intent i = new Intent(this, ListadoProducto.class);
            i.putExtra("id",id);
            i.putExtra("usuario", user);
            i.putExtra("password", password);
            i.putExtra("servidor", server);
            i.putExtra("nombre",datos.get(2).toString());
            Log.i("SALIDA MAIN ACTIVITY","ENTRADA LISTADO PROD");
        startActivity(i);
        }catch(Exception ex){
            Log.w("catch de envioListado","Detalles: "+ex.getMessage());
        }

    }
    public  ArrayList<String> obtenerDatosJson(String response){
        ArrayList<String> listado= new ArrayList<>();

        try{
            JSONArray jsonarray= new JSONArray(response);
            Log.w("Ex obtenerdatosjson","jsonarray tamano "+jsonarray.length());
            String texto,texto2,texto3;
            for (int i=0;i<jsonarray.length();i++){
                texto= jsonarray.getJSONObject(i).getString("RUT");
                texto2= jsonarray.getJSONObject(i).getString("Clave");
                texto3= jsonarray.getJSONObject(i).getString("Nombre");
                listado.add(texto);
                listado.add(texto2);
                listado.add(texto3);

            }
        }catch (Exception ex){


            Log.w("Catch obtenerdatosjson","jsonarray"+ex.getMessage());
        }

        return listado;

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }




}
