package com.example.jojo.helpers;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.jojo.recyclescan.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Eigener Adapter für die ListView in ErgebnisActivity.class
 * Die Liste soll ein Bild und die Bezeichnung angeben.
 * Die Klasse Bestandteil ist dazu notwendig.
 */
public class MyAdapter extends ArrayAdapter<Bestandteil>{

    private Context mContext;
    private List<Bestandteil> besList ;

    public MyAdapter(@NonNull Context context, @SuppressLint("SupportAnnotationUsage") @LayoutRes ArrayList<Bestandteil> list) {
        super(context, 0, list);
        mContext = context;
        besList = list;
    }

    public String getName(int position){
        return besList.get(position).getmName();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItem = convertView;
        if(listItem == null)
            listItem = LayoutInflater.from(mContext).inflate(R.layout.list_item_bes, parent, false);

        Bestandteil bestandteil = besList.get(position);

        ImageView image = (ImageView)listItem.findViewById(R.id.imageTonne);
        image.setImageResource(bestandteil.getmImageDrawable());

        TextView name = (TextView) listItem.findViewById(R.id.textViewBestandteil);
        name.setText(bestandteil.getmName());

        return listItem;
    }

}

