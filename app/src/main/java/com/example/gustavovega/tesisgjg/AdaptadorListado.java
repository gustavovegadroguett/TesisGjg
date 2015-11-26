package com.example.gustavovega.tesisgjg;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Gustavo Vega on 26-11-2015.
 */
public class AdaptadorListado extends ArrayAdapter<ItemLista> {
    /**
     * Created by nisho_000 on 21/06/2015.
     */


        public AdaptadorListado(Context context, int textViewResourceId) {
            super(context, textViewResourceId);
        }

        public AdaptadorListado(Context context, int resource, List items) {
            super(context, resource, items);
        }


        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            View v = convertView;

            ItemLista p = getItem(position);
            if (v == null ){
                LayoutInflater vi;
                vi = LayoutInflater.from(getContext());
                v = vi.inflate(R.layout.single_listado, null);// se crea una vista con los parametros de single list.
            }


            if (p != null) {

                TextView tt1 = (TextView) v.findViewById(R.id.id);
                TextView tt2 = (TextView) v.findViewById(R.id.nombre);
                TextView tt3 = (TextView) v.findViewById(R.id.cantidad);
                TextView tt4 = (TextView) v.findViewById(R.id.precio);

                if (tt1 != null) {
                    tt1.setText(p.getNombreProd());
                }
                if (tt2 != null) {
                    tt2.setText(p.getNombreProd());
                }
                if (tt3 != null) {
                    tt3.setText(p.getStockProd());
                }
                if (tt4 != null) {
                    tt4.setText(p.getPrecioProd());
                }


            }

            return v;

        }

    }

