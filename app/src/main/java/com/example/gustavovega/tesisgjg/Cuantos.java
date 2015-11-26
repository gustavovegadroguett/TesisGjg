package com.example.gustavovega.tesisgjg;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class Cuantos extends ActionBarActivity {
    int oferta,demanda,envioprecio;
    EditText pedido;
    TextView nombre;
    TextView precio;
    String producto;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cuantos);


        Bundle datos= this.getIntent().getExtras();// se recibe el paquete desde el intent enviado

        TextView nombre= (TextView)findViewById(R.id.nombreproducto);//controles asociados a objetos
        TextView cantidad=(TextView)findViewById(R.id.stockproducto);
        TextView precio= (TextView)findViewById(R.id.precioproducto);


        //botones para agregar al carrito y para volver sin hacer el pedido
        Button volver= (Button)findViewById(R.id.botonVolver);
        Button carrito= (Button)findViewById(R.id.botonAgregar);

        //mostrar los datos que vienen desde el formulario listado.
        nombre.setText(datos.getString("nombre"));
        cantidad.setText(datos.getString("stock"));
        precio.setText(datos.getString("precio"));

        //establece numero de productos disponibles, para no vender los productos con que no se cuenta.
        oferta=Integer.parseInt(cantidad.getText().toString());

    }

    public void VolverOnClick(View v){
        this.finish();
    }

    public void agregarOnClick(View v){
        int actual;
        pedido=(EditText)findViewById(R.id.pedidoproducto);
        nombre=(TextView)findViewById(R.id.nombreproducto  );
        precio=(TextView)findViewById(R.id.precioproducto);

        demanda=Integer.parseInt(pedido.getText().toString());
        producto=nombre.getText().toString();

        envioprecio=Integer.parseInt(precio.getText().toString());

        if( demanda <= oferta && pedido!=null && demanda!=0 ){
            actual=oferta-demanda;
            Log.i("agergarOnclick", "entro oferta " + oferta + "demanda " + demanda);
            Intent datosDePedido = new Intent();

            datosDePedido.putExtra("pedido", Integer.toString(demanda));
            datosDePedido.putExtra("nombre", producto);
            datosDePedido.putExtra("precio", Integer.toString(envioprecio));
            datosDePedido.putExtra("actual", Integer.toString(actual));
            Log.i("agergarOnclick", "como quedo stock " + actual);

            setResult(this.RESULT_OK, datosDePedido);
            this.finish();
        }
        else{
            Toast.makeText(this,"no hay esa cantidad en stock",Toast.LENGTH_SHORT).show();
        }
    }
}