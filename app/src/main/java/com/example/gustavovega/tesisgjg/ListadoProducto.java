package com.example.gustavovega.tesisgjg;

import android.content.Intent;
import android.preference.PreferenceActivity;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;


public class ListadoProducto extends ActionBarActivity {
    TextView server;
    ListView listado;
    TextView totalmuestra;

    List<ItemLista> item= new ArrayList<ItemLista>();//lista de objects ItemLista, para albergar los datos necesarios.
    ItemLista itemTemp= new ItemLista(); //Objeto que contendra los datos que requerimos
    Bundle main;
    ListView carrito;
    int mRequestCode=100;

    ClassArrayAdapter adaptadorsecundario;
    public String[] valores=new String[4];
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listado_producto);
        Log.i("ONCREATE", "LISTADO PRODUCTO");
        itemTemp.setNombreProd("DetalleNombre");//comenzamos a agregar datos por defecto de nuestro proyecto
        itemTemp.setStockProd("DetalleStock");
        itemTemp.setPrecioProd("DetallePrecio");
        item.add(itemTemp);  //Agregamos a la lista de objetos, nuestro objeto cargado con los datos


        server=(TextView)findViewById(R.id.ListadoServidor);
        listado= (ListView)findViewById(R.id.listadoPrincipal);
        main= this.getIntent().getExtras();
        totalmuestra=(TextView)findViewById(R.id.total);
        adaptadorsecundario= new ClassArrayAdapter(this,R.layout.single_list,item);//Se envia el conexto, se referencia el xml
        // creado por nosotros y la lista de objectos personalizada

        carrito=(ListView)findViewById(R.id.carritolista);
        carrito.setAdapter(adaptadorsecundario);
        adaptadorsecundario.clear();
        listado.setOnItemClickListener(new AdapterView.OnItemClickListener() {//se agrega un listener para los items del listado

            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(view.getContext(), Cuantos.class);
                valores = separar(parent, position);
                i.putExtra("id_productos", valores[0]);
                Log.i("onclicklistener", "put extra" + valores[0]);
                i.putExtra("nombre", valores[1]);
                Log.i("succes listado producto", "put extra" + valores[1]);
                i.putExtra("stock", valores[2]);
                Log.i("succes listado producto", "put extra" + valores[2]);
                i.putExtra("precio", valores[3]);
                Log.i("succes listado producto", "put extra" + valores[3]);

                startActivityForResult(i, mRequestCode);
            }
        });
        if(main!=null){
            server.setText("Bienvenido estimado/a  "+main.getString("nombre"));

        }
        obtenerDatos(main.getString("servidor"));
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try{
            int subtotal=0;
            String pasadero,pasadero2,totalstring;

            if(requestCode == mRequestCode && resultCode == RESULT_OK){
                Toast.makeText(this,"la compra fue de "+ data.getStringExtra("pedido").toString() ,Toast.LENGTH_SHORT).show();

                itemTemp=new ItemLista();
                itemTemp.setNombreProd(data.getStringExtra("nombre").toString());
                itemTemp.setStockProd(data.getStringExtra("pedido").toString());
                itemTemp.setPrecioProd(data.getStringExtra("precio").toString());

                pasadero=data.getStringExtra("pedido").toString();
                pasadero2=data.getStringExtra("precio").toString();

                //if (!pasadero.equals("") && !pasadero2.equals("")) {
                int num1 = Integer.parseInt(pasadero);
                int num2 = Integer.parseInt(pasadero2);
                Log.w("num1"," " +num1);
                Log.w("num2"," "+num2);
                subtotal=num1*num2;

                //}

                Log.w("subtotal", " " + subtotal);
                totalmuestra.setText(String.valueOf(subtotal));
                item.add(itemTemp);
                adaptadorsecundario = new ClassArrayAdapter(ListadoProducto.this, R.layout.single_list, item);
                // total=intTemporal+subtotal;
                try {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            carrito.setAdapter(adaptadorsecundario);
                            adaptadorsecundario.notifyDataSetChanged();

                        }
                    });
                }catch(Exception e){
                    Toast.makeText(this,"catch ui run"+e.getMessage(),Toast.LENGTH_SHORT);
                }

            }else{
                Toast.makeText(this,"volvio mal =(",Toast.LENGTH_SHORT).show();
            }
        }catch (Exception ex){
            Log.w("onActivityResult","catch fuera " + ex.getMessage());
        }
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



    public void  obtenerDatos(final String serv) {
        AsyncHttpClient cliente = new AsyncHttpClient();
        ArrayList<String> listado = new ArrayList<String>();

        RequestParams parametros = new RequestParams();
        parametros.put("minimo", 18);
        Log.i("obetenerdatos Listado", "servidor "+serv);
        String URL = "http://" + serv + "/tesis/Android/getProducto.php";
        Log.i("obetenerdatos Listado", "antes del new asynchttphandler");
        cliente.post(URL, new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                if (statusCode == 200) {

                    Log.i("succes obtener listado", "trdponseBody "+responseBody);
                    cargaLista(obtenerDatosJson(new String(responseBody)));

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

        try{Log.i("OBETENER DATOS JSON"," "+response+" ");
            JSONArray jsonarray= new JSONArray(response);
            String texto=" No entro for";

            for (int i=0;i<jsonarray.length();i++){
                Log.i("OBETENER DATOS JSON"," "+texto+" ");

                 texto= jsonarray.getJSONObject(i).getString("id_productos")+" "+ jsonarray.getJSONObject(i).getString("nombre")+" "+ jsonarray.getJSONObject(i).getString("stock")+" "+ jsonarray.getJSONObject(i).getString("precio");
                listado.add(texto);


            }
            Log.i("OBETENER DATOS JSON"," "+texto+" ");
        }catch (Exception ex){
            ex.printStackTrace();
        }

        return listado;

    } //

    public void cargaLista(ArrayList<String> datos){
        ArrayAdapter<String> adapter= new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,datos);
        listado.setAdapter(adapter);

    }//se carga la lista de productos

    public String[] separar(AdapterView  vista,int lugar){ //se separan los valores por los espacios encontrados, cada uno en una posición diferente
        String[] valores;
        String valor;
        valor = (String)vista.getItemAtPosition(lugar);
        valores=valor.split(" ");
        return valores;
    }
    public  ArrayList<String> obtenerDatosJson2(String response){// se toma la codificacion JSON y se agrega a un arraylist
        ArrayList<String> listado= new ArrayList<String>();

        try{
            JSONArray jsonarray= new JSONArray(response);
            String texto,texto2,texto3;
            for (int i=0;i<jsonarray.length();i++){
                texto= jsonarray.getJSONObject(i).getString("rut");
                texto2= jsonarray.getJSONObject(i).getString("pass");
                texto3= jsonarray.getJSONObject(i).getString("nombre");
                listado.add(texto);
                listado.add(texto2);
                listado.add(texto3);

            }
        }catch (Exception ex){
            ex.printStackTrace();
        }

        return listado;

    } // metodo para ordenar lista


}