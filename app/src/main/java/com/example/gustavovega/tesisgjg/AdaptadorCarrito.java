package com.example.gustavovega.tesisgjg;

import android.content.ClipData;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by nisho_000 on 21/06/2015.
 */
public class AdaptadorCarrito extends ArrayAdapter<ItemLista> {

    public AdaptadorCarrito(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
    }

    public AdaptadorCarrito(Context context, int resource, List items) {
        super(context, resource, items);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;

        if (v == null ){
            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            v = vi.inflate(R.layout.single_item, null);// se crea una vista, en base a single_item la cual fue creada anteriormentes.
        }
        ItemLista p = getItem(position);

        if (p != null) {

            TextView tt1 = (TextView) v.findViewById(R.id.nombre);
            TextView tt2 = (TextView) v.findViewById(R.id.cantidad);
            TextView tt3 = (TextView) v.findViewById(R.id.precio);

            if (tt1 != null) {
                tt1.setText(p.getNombreProd());
            }
            if (tt2 != null) {
                tt2.setText(p.getStockProd());
            }
            if (tt3 != null) {
                tt3.setText(p.getPrecioProd());
            }


        }
        return v;
    }

}

