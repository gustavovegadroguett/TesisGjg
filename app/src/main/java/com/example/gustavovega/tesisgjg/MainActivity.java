package com.example.gustavovega.tesisgjg;

import android.content.Context;
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
import android.widget.TextView;
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
    EditText rutUser;
    EditText usuario;
    EditText contra;
    EditText servidor;
    Button aceptar;
    Button salir;
    ArrayList<String> listado= new ArrayList<String>();
    MainJson modelojson;
    MainEnvio modeloenvio;
    Intent intentEnvio;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Asignamos los controles de nuestro layout xml a variables accesibles desde java
        rutUser=(EditText)findViewById(R.id.rut);
        contra= (EditText)findViewById(R.id.pass);
        servidor= (EditText)findViewById(R.id.server);
        aceptar= (Button)findViewById(R.id.ButtonIngreso);
        salir= (Button)findViewById(R.id.ButtonSalida);


    }
    public void ingresoOnClick(View v){
        String rut= rutUser.getText().toString();

        String password=contra.getText().toString();
        String server=servidor.getText().toString();



       obtenerDatos(rut,password, server);
    }
    public void salidaOnClick(View v){
        this.finish();
    }

    public void   obtenerDatos(final String rut,final String pass,final String serv){
        try {
            AsyncHttpClient cliente = new AsyncHttpClient();
            Log.i("obetener datos", "check de datos " + rut + " " + pass + " " + serv);
            RequestParams parametros = new RequestParams();
            parametros.add("rut", rut);
            parametros.add("pass", pass);
            String serverPhp = "http://" + serv + "/tesis/Android/logindatos.php";
            Log.i("obetener datos", "antes del new asynchttphandler");

            cliente.post(serverPhp, parametros, new AsyncHttpResponseHandler() {


                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                    modelojson= new MainJson();
                    modeloenvio=new MainEnvio();
                    if (statusCode == 200) {
                        Log.i("en onsuccess", "Antes de verificar " + new String(responseBody));
                           if (responseBody.length!= 0) {
                            intentEnvio=(modeloenvio.cargaIntent(modelojson.obtenerDatosJson(new String(responseBody)), rut, pass, serv));
                            intentEnvio.setClass(MainActivity.this,ListadoProducto.class);
                            startActivity(intentEnvio);
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


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }



}
