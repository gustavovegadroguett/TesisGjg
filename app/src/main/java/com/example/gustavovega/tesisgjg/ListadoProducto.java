package com.example.gustavovega.tesisgjg;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
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


public class ListadoProducto extends AppCompatActivity {
    TextView server;
    ListView listado;
    TextView totalmuestra;
    ArrayList<String> remplazo;
    int totalalmacenado=0;
    List<ItemLista> item= new ArrayList<>();//lista de objects ItemLista, para albergar los datos necesarios.
    List<ItemLista> item2= new ArrayList<>();
    ItemLista itemTemp= new ItemLista(); //Objeto que contendra los datos que requerimos
    Bundle listadoFijo,enMovDatos;
    String[] separados, separadoseleccion;
    ListView carrito;
    int mRequestCode=100, seleccionado;
    Intent enMovimiento;
    AdaptadorCarrito adaptaCarro;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_listado_producto);
        Log.i("ONCREATE", "LISTADO PRODUCTO");
        enMovDatos= new Bundle();
        enMovimiento= new Intent(this, Cuantos.class);

        itemTemp.setNombreProd("DetalleNombre");//comenzamos a agregar datos por defecto de nuestro proyecto
        itemTemp.setStockProd("DetalleStock");
        itemTemp.setPrecioProd("DetallePrecio");
        item2.add(itemTemp);  //Agregamos a la lista de objetos, nuestro objeto cargado con los datos


        server=(TextView)findViewById(R.id.ListadoServidor);
        listado= (ListView)findViewById(R.id.listadoPrincipal);
        listadoFijo= this.getIntent().getExtras();
        totalmuestra=(TextView)findViewById(R.id.total);
        adaptaCarro = new AdaptadorCarrito(this,R.layout.single_item,item2);//Se envia el contexto, se referencia el xml
        // creado por nosotros y la lista de objectos personalizada

        carrito=(ListView)findViewById(R.id.carritolista);//carrito donde se iran agregando los resultados
        carrito.setAdapter(adaptaCarro);


        listado.setOnItemClickListener(new AdapterView.OnItemClickListener() {//se agrega un listener para los items del listado

            @SuppressLint("NewApi")
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {



                seleccionado=position;
                enMovDatos.putString("id_producto", separadoseleccion[0]);
                Log.i("onclicklistener", "put extra" + separadoseleccion[0]);
                enMovDatos.putString("nombre", separadoseleccion[1]);
                Log.i("succes listado producto", "put extra" +separadoseleccion[1]);
                enMovDatos.putString("stock", separadoseleccion[2]);
                Log.i("succes listado producto", "put extra" + separadoseleccion[2]);
                enMovDatos.putString("precio", separadoseleccion[3]);
                Log.i("succes listado producto", "put extra" + separadoseleccion[3]);
                enMovimiento.putExtras(enMovDatos);
                startActivityForResult(enMovimiento, mRequestCode);
            }
        });
        if(listadoFijo!=null){
            server.setText("Bienvenido estimado/a  " + listadoFijo.getString("nombre"));

        }
        obtenerDatos(listadoFijo.getString("servidor"));
    }
    public String[] separar(AdapterView  vista,int lugar){ //se separan los valores por los espacios encontrados, cada uno en una posición diferente
        String[] valores;
        String valor;
        valor = vista.getItemAtPosition(lugar).toString();
        valores=valor.split(" ");
        return valores;
    }
    public String[] separar2(String valor){ //se separan los valores por los espacios encontrados, cada uno en una posición diferente
        String[] valores;
        valores=valor.split(" ");
        return valores;
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

                    remplazo = obtenerDatosJson(new String(responseBody));//remplazo obtiene datos desde json (query en php)
                    cargaLista(remplazo);

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
    public void cargaLista(ArrayList<String> datos){

       // ArrayAdapter<String> adapter= new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,datos);
        // listado.setAdapter(adapter);

        AdaptadorListado  adapter2;

        int i=0;
        while (i<datos.size()){
            separados=separar2(datos.get(i));
            if(i==0){
                separadoseleccion=separados;
            }
            itemTemp=new ItemLista();
            itemTemp.setId(separados[0]);
            itemTemp.setNombreProd(separados[1]);
            itemTemp.setStockProd(separados[2]);
            itemTemp.setPrecioProd(separados[3]);

            item.add(itemTemp);
            i++;
        }
        adapter2= new AdaptadorListado(this,R.layout.single_listado,item);
        listado.setAdapter(adapter2);

    }//se carga la lista de productos



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent pedidoCuantos) {
        super.onActivityResult(requestCode, resultCode, pedidoCuantos);
        try{
            int subtotal=0;
            String pasadero,pasadero2;

                enMovDatos=pedidoCuantos.getExtras();
            if(requestCode == mRequestCode && resultCode == RESULT_OK){
                Toast.makeText(this,"la compra fue de "+ pedidoCuantos.getStringExtra("pedido").toString() ,Toast.LENGTH_SHORT).show();


                itemTemp=new ItemLista();
                itemTemp.setId(enMovDatos.getString("id"));
                itemTemp.setNombreProd(enMovDatos.getString("nombre"));
                itemTemp.setStockProd(enMovDatos.getString("pedido"));
                itemTemp.setPrecioProd(enMovDatos.getString("precio"));

                String actualizada= enMovDatos.getString("id")+"  "+enMovDatos.getString("nombre")+"  "+enMovDatos.getString("actual")+"  "+enMovDatos.getString("precio");

                pasadero=enMovDatos.getString("pedido");
                pasadero2=enMovDatos.getString("precio");

                int num1 = Integer.parseInt(pasadero);
                int num2 = Integer.parseInt(pasadero2);
                Log.w("num1"," " +num1);
                Log.w("num2"," "+num2);

                subtotal=num1*num2;
                totalalmacenado=totalalmacenado+subtotal;// suma de totales

                totalmuestra.setText(String.valueOf(totalalmacenado));
                item2.add(itemTemp);

                remplazo.set(seleccionado, actualizada);//
                cargaLista(remplazo);

                adaptaCarro = new AdaptadorCarrito(ListadoProducto.this,R.layout.single_item, item2);

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







}