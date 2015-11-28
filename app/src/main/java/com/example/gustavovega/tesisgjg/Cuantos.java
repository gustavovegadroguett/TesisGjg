package com.example.gustavovega.tesisgjg;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;


public class Cuantos extends AppCompatActivity {
    int oferta,demanda,envioprecio;
    EditText pedido;
    TextView id;
    TextView nombre;
    TextView precio;
    TextView cantidad;
    String producto;
    Button botonAgregar,botonCancelar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cuantos);


        Bundle datos= this.getIntent().getExtras();// se recibe el paquete desde el intent enviado
        id=(TextView)findViewById(R.id.idproducto);
        nombre= (TextView)findViewById(R.id.nombreproducto);//controles asociados a objetos
        cantidad=(TextView)findViewById(R.id.stockproducto);
        precio= (TextView)findViewById(R.id.precioproducto);
        pedido=(EditText)findViewById(R.id.pedidoproducto);
        botonAgregar=(Button)findViewById(R.id.botonAgregar);

        //Se agregan los datos desde el bundle que fue enviado de la actividad anterior a las variables de actividad
        id.setText(datos.getString("id_producto"));
        nombre.setText(datos.getString("nombre"));
        cantidad.setText(datos.getString("stock"));
        precio.setText(datos.getString("precio"));

        //establece numero de productos disponibles, para no vender los productos con que no se cuenta.
        if(pedido.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
            pedido.selectAll();
        }
        pedido.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (pedido.getText().toString().trim().length() == 0 || pedido.getText().toString().equals("0")) {
                    botonAgregar.setEnabled(false);
                }else {
                    botonAgregar.setEnabled(true);
                }

            }
        });
    }


    public void VolverOnClick(View v){
        this.finish();
    }

    public void agregarOnClick(View v){
        int actual;
    try {
        if (pedido.getText().toString() !=null) {
            oferta = Integer.parseInt(cantidad.getText().toString());
            demanda = Integer.parseInt(pedido.getText().toString());

             if (demanda <= oferta && demanda != 0) {
                 actual = oferta - demanda;
                 Log.i("agergarOnclick", "entro oferta " + oferta + "demanda " + demanda);
                 Intent datosDePedido = new Intent();
                 datosDePedido.putExtra("id", id.getText().toString());
                 datosDePedido.putExtra("nombre", nombre.getText().toString());
                 datosDePedido.putExtra("demanda", Integer.toString(demanda));
                 datosDePedido.putExtra("precio", precio.getText().toString());
                 datosDePedido.putExtra("stockactual", Integer.toString(actual));
                 Log.i("agergarOnclick", "como quedo stock " + actual);

                 setResult(this.RESULT_OK, datosDePedido);
                 this.finish();
             }
          else {
            Toast.makeText(this, "no hay esa cantidad en stock", Toast.LENGTH_SHORT).show();
          }
        }else{
            Toast.makeText(this, " No ingno hay esa cantidad en stock", Toast.LENGTH_SHORT).show();
        }
    }catch (Exception ex){
        Toast.makeText(this,"Exception en agregarOnclick"+ ex.getMessage(),Toast.LENGTH_LONG).show();

        }
    }
}