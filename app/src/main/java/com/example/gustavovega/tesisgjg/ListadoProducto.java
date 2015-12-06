package com.example.gustavovega.tesisgjg;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cz.msebera.android.httpclient.Header;


public class ListadoProducto extends AppCompatActivity {
    TextView server;
    ListView listado;
    TextView totalmuestra;
    Button confirmar;
    Date horaPedido = new Date();

    List<ItemLista> item = new ArrayList<>();//lista de objects ItemLista, para albergar los datos necesarios.
    List<ItemLista> item2 = new ArrayList<>();
    List<ItemLista> headers = new ArrayList<>();
    List<String[]> separadosalmacen = new ArrayList<>();
    ItemLista itemTemp = new ItemLista(); //Objeto que contendra los datos que requerimos
    ItemLista itemTemp2 = new ItemLista(); //Objeto que contendra los datos que requerimos
    Bundle datosLogin, enMovDatos;
    String[] separados, separadoseleccion;
   JSONObject objetojson;
    ListView carrito,titulocarro,titulolistado;
    String Rut;
    int mRequestCode = 100, seleccionado, totalalmacenado = 0;
    Intent enMovimiento;


    AdaptadorListado adapter2, adaptaCarro;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_listado_producto);
        Log.i("ONCREATE", "LISTADO PRODUCTO");
        enMovDatos = new Bundle();
        enMovimiento = new Intent(this, Cuantos.class);
        itemTemp.setId("ID");
        itemTemp.setNombreprod("Nombre");//comenzamos a agregar datos por defecto de nuestro proyecto
        itemTemp.setStockprod("Stock");
        itemTemp.setPrecioprod("Precio");
        headers.add(itemTemp);  //Agregamos a la lista de objetos, nuestro objeto cargado con los datos


        server = (TextView) findViewById(R.id.ListadoServidor);
        listado = (ListView) findViewById(R.id.listadoPrincipal);
        confirmar = (Button) findViewById(R.id.botonConfirma);
        datosLogin = this.getIntent().getExtras(); //se almacenan datos desde login
        totalmuestra = (TextView) findViewById(R.id.total);
        adaptaCarro = new AdaptadorListado(this, R.layout.single_item, headers);//Se envia el contexto, se referencia el xml


        carrito = (ListView) findViewById(R.id.carritolista);//carrito donde se iran agregando los resultados
        titulolistado=(ListView) findViewById(R.id.cabeceralistado);
        titulocarro=(ListView) findViewById(R.id.cabecera);
        titulolistado.setAdapter(adaptaCarro);
        titulocarro.setAdapter(adaptaCarro);


        Rut = datosLogin.getString("rut");
        if (datosLogin != null) {
            server.setText("Bienvenido estimado/a  " + datosLogin.getString("nombre"));
        }
        obtenerDatos(datosLogin.getString("servidor"));//se realiza la conexion para obtener datos

        listado.setOnItemClickListener(new AdapterView.OnItemClickListener()//se agrega un listener para los items del listado
        {
            @SuppressLint("NewApi")
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                seleccionado = position; //Se alamacena posicion del objeto seleccionado para luego actualizarla
                separadoseleccion = separadosalmacen.get(seleccionado);//funcion que separa
                Log.i("succes listado producto", "sepadado" + separadoseleccion.length);
                enMovDatos.putString("id_producto", separadoseleccion[0]);
                Log.i("onclicklistener", "put extra" + separadoseleccion[0]);
                enMovDatos.putString("nombre", separadoseleccion[1]);
                Log.i("succes listado producto", "put extra" + separadoseleccion[1]);
                enMovDatos.putString("stock", separadoseleccion[2]);
                Log.i("succes listado producto", "put extra" + separadoseleccion[2]);
                enMovDatos.putString("precio", separadoseleccion[3]);
                Log.i("succes listado producto", "put extra" + separadoseleccion[3]);
                enMovimiento.putExtras(enMovDatos);
                startActivityForResult(enMovimiento, mRequestCode);
            }
        });

        confirmar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fecha = new SimpleDateFormat("dd-MM-yyyy").format(horaPedido);

                hacerPedido(item2, datosLogin.getString("rut"), Integer.toString(totalalmacenado), datosLogin.getString("servidor"), fecha);


            }
        });
    }


    public void hacerPedido(final List<ItemLista> lista, String rut, String total, final String serv, final String fecha) {
        AsyncHttpClient cliente = new AsyncHttpClient();

        String URL = "http://" + serv + "/tesis/Android/consultaId.php";


        final JSONArray enviarJson = new JSONArray(); //Enviar listado con detalles del pedido
        final RequestParams datosPedido = new RequestParams("json", enviarJson);

        datosPedido.add("rut", rut);
        datosPedido.add("fecha", fecha);
        datosPedido.add("total", total);

        objetojson = new JSONObject();
        for (int i = 0; i < lista.size(); i++) {
            try {
                objetojson=new JSONObject();
                objetojson.put("id",lista.get(i).getId());
                objetojson.put("nombre", lista.get(i).getNombreprod());
                objetojson.put("cantidad",lista.get(i).getStockprod());
                objetojson.put("precio", lista.get(i).getPrecioprod());

                enviarJson.put(objetojson);


            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        cliente.post(URL, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                int resultado =1;
                if (statusCode == 200 && responseBody.length != 0) {
                    resultado =Integer.parseInt(new String(responseBody));
                    insertarPedido(datosPedido, resultado, serv);
                    resultado = resultado + 1;
                    Log.i("onsuccess hacer pedido", " "+enviarJson.toString());
                    insertarDetalle(enviarJson, resultado, serv);
                } else {

                    insertarPedido(datosPedido,resultado, serv);
                    insertarDetalle(enviarJson, resultado, serv);

                }
                carrito.setAdapter(null);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.i("onfail de envia datos", "fallo");
            }


        });
    }

    public void insertarDetalle(JSONArray datosPedido, final int resultado, String serv) {

        AsyncHttpClient cliente = new AsyncHttpClient();
        Log.i("insertar detalle", "servidor " + serv);
        String URL = "http://" + serv + "/tesis/Android/envioDetalles.php";
        RequestParams json = new RequestParams();
        json.put("id", resultado);
        json.put("jsonarray", datosPedido);
        Log.i("insertar detalle", " " + resultado);
        cliente.post(URL, json, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                String prueba=new String(responseBody);
                Toast.makeText(ListadoProducto.this, "Pedido Ingresado con id " + prueba, Toast.LENGTH_LONG).show();

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Toast.makeText(ListadoProducto.this, "ON fail de insertarDetalle", Toast.LENGTH_LONG).show();

            }
        });
    }
    public void insertarPedido(RequestParams datosPedido, final int resultado, String serv){

        AsyncHttpClient cliente = new AsyncHttpClient();
        Log.i("hacerPedido", "servidor " + serv);
        String URL = "http://" + serv + "/tesis/Android/envioPedido.php";

        cliente.post(URL, datosPedido, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {




            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.i("onfail  insertar pedido", " " + resultado);
            }
        });
    }
    public void  obtenerDatos(final String serv) { //Aqui se consulta a la base de datos via  metodos asincronos
        AsyncHttpClient cliente = new AsyncHttpClient();
        Log.i("obetenerdatos Listado", "servidor "+serv);
        String URL = "http://" + serv + "/tesis/Android/getProducto.php";
        Log.i("obetenerdatos Listado", "antes del new asynchttphandler");

        cliente.post(URL, new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                if (statusCode == 200) {

                    actualizarList(obtenerDatosJson(new String(responseBody)));
                    cargaLista(separadosalmacen);//remplazo obtiene datos desde json (query en php)

                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.i("Onfail", "fallo");
            }


        });

    }
    public  ArrayList<String> obtenerDatosJson(String response){
        ArrayList<String> listado= new ArrayList<>();

        try{
            Log.i("OBETENER DATOS JSON"," "+response+" ");
            JSONArray jsonarray= new JSONArray(response);
            String texto=" No entro for";

            for (int i=0;i<jsonarray.length();i++){

                texto= jsonarray.getJSONObject(i).getString("id_productos")+" "+ jsonarray.getJSONObject(i).getString("nombre")+" "+ jsonarray.getJSONObject(i).getString("stock")+" "+ jsonarray.getJSONObject(i).getString("precio");
                listado.add(texto);


            }
            Log.i("OBETENER DATOS JSON"," "+texto+" ");
        }catch (Exception ex){
            ex.printStackTrace();
        }

        return listado;

    } //
    public void actualizarList(ArrayList entradaDatos){
        int j=0;
        String[] sinespacio;
        while(j<entradaDatos.size()){
            sinespacio=separar2(entradaDatos.get(j).toString());
            separadosalmacen.add(sinespacio);
            j++;
        }
    }

    public void cargaLista(List<String[]> datos){// recibe remplazo inicial y con stock actualizado.   // se vacia almacen para no repetir valores
        item.clear();
        int i=0;
            while (i<datos.size()){

                separados=separadosalmacen.get(i);
                itemTemp=new ItemLista();
                itemTemp.setId(separados[0]);
                Log.w("cargaLista", "separado " + separados[0]);
                itemTemp.setNombreprod(separados[1]);
                Log.w("cargaLista", "separado " + separados[1]);
                itemTemp.setStockprod(separados[2]);
                Log.w("cargaLista", "separado " + separados[2]);
                itemTemp.setPrecioprod(separados[3]);
                Log.w("cargaLista", "separado " + separados[3]);
                item.add(itemTemp);
            i++;
        }
        adapter2= new AdaptadorListado(this,R.layout.single_listado,item);
        listado.setAdapter(adapter2);

    }//se carga la lista de productos

    public String[] separar2(String valor){ //se separan los valores por los espacios encontrados, cada uno en una posición diferente
        String[] valores;
        valores=valor.split(" ");
        return valores;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent pedidoCuantos) {
        super.onActivityResult(requestCode, resultCode, pedidoCuantos);
        /*try{*/
            int subtotal=0;
            String catidadcalc,preciocalc;


            if(requestCode == mRequestCode && resultCode == RESULT_OK){
                //se repite la cantidad seleccionada
                Toast.makeText(this,"la compra fue de "+ pedidoCuantos.getStringExtra("demanda").toString() ,Toast.LENGTH_SHORT).show();
                enMovDatos=pedidoCuantos.getExtras();
                //agregamos valores enviados de vuelta para rellenar carrito
                itemTemp2=new ItemLista();
                itemTemp2.setId(enMovDatos.getString("id"));
                itemTemp2.setNombreprod(enMovDatos.getString("nombre"));
                itemTemp2.setStockprod(enMovDatos.getString("demanda"));
                itemTemp2.setPrecioprod(enMovDatos.getString("precio"));

                //se crea un string para actualizar el valor en la lista general
                String actualizada= itemTemp2.getId()+itemTemp2.getNombreprod()+itemTemp2.getStockprod()+enMovDatos.getString("stockactual");

                Log.w("Activity Result",actualizada);
                catidadcalc=enMovDatos.getString("demanda");
                preciocalc=enMovDatos.getString("precio");

                int num1 = Integer.parseInt(catidadcalc);
                int num2 = Integer.parseInt(preciocalc);
                Log.w("num1"," " +num1);
                Log.w("num2"," "+num2);

                subtotal=num1*num2;
                totalalmacenado=totalalmacenado+subtotal;// suma de totales

                totalmuestra.setText(String.valueOf(totalalmacenado));
                item2.add(itemTemp2);

                separadoseleccion=separadosalmacen.get(seleccionado);// se cambia la linea seleccionada con nueva cantidad
                separadoseleccion[2]=enMovDatos.getString("stockactual");
                separadosalmacen.set(seleccionado,separadoseleccion);
                cargaLista(separadosalmacen);



                adaptaCarro = new AdaptadorListado(ListadoProducto.this,R.layout.single_listado, item2);

                try {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            carrito.setAdapter(adaptaCarro);
                            adaptaCarro.notifyDataSetChanged();

                        }
                    });
                }catch(Exception e){
                    Toast.makeText(this,"catch ui run"+e.getMessage(),Toast.LENGTH_SHORT);
                }

            }else{
                Toast.makeText(this,"volvio mal =(",Toast.LENGTH_SHORT).show();
            }
        /*}catch (Exception ex){
            Log.w("onActivityResult","catch fuera " + ex.getMessage());
        }*/
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //Cuando se selecciona alguno de los items de menu se dispara
        int id = item.getItemId();

        //Aqui se verifica cual item se selecciono.
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }









}