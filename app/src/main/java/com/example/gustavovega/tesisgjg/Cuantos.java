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
    String dato1,dato2,dato3,enviado;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cuantos);


        Bundle datos= this.getIntent().getExtras();

        TextView nombre= (TextView)findViewById(R.id.nombreproducto);
        TextView cantidad=(TextView)findViewById(R.id.stockproducto);
        TextView precio= (TextView)findViewById(R.id.precioproducto);


        //botones para agregar al carrito y para volver sin hacer el pedido
        Button volver= (Button)findViewById(R.id.botonVolver);
        Button carrito= (Button)findViewById(R.id.botonAgregar);

        nombre.setText(datos.getString("nombre"));
        cantidad.setText(datos.getString("stock"));
        precio.setText(datos.getString("precio"));

        oferta=Integer.parseInt(cantidad.getText().toString());

    }

    public void VolverOnClick(View v){
        this.finish();
    }

    public void agregarOnClick(View v){
        pedido=(EditText)findViewById(R.id.pedidoproducto);
        nombre=(TextView)findViewById(R.id.nombreproducto  );
        precio=(TextView)findViewById(R.id.precioproducto);

        demanda=Integer.parseInt(pedido.getText().toString());
        enviado=nombre.getText().toString();
        envioprecio=Integer.parseInt(precio.getText().toString());
        if( demanda <= oferta){
            Log.i("agergarOnclick", "entro oferta " + oferta + "demanda " + demanda);
            Intent resultIntent = new Intent();

            resultIntent.putExtra("pedido",Integer.toString(demanda));
            resultIntent.putExtra("nombre",enviado);
            resultIntent.putExtra("precio",Integer.toString(envioprecio));
            setResult(this.RESULT_OK, resultIntent);
            this.finish();
        }
        else{
            Toast.makeText(this,"no hay esa cantidad en stock",Toast.LENGTH_SHORT).show();
        }
    }
}