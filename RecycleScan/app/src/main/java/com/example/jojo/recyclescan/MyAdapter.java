package com.example.jojo.recyclescan;

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

import java.util.ArrayList;
import java.util.List;


public class MyAdapter extends ArrayAdapter<Bestandteil> {

    private Context mContext;
    private List<Bestandteil> besList = new ArrayList<>();

    public MyAdapter(@NonNull Context context, @SuppressLint("SupportAnnotationUsage") @LayoutRes ArrayList<Bestandteil> list) {
        super(context, 0 , list);
        mContext = context;
        besList = list;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItem = convertView;
        if(listItem == null)
            listItem = LayoutInflater.from(mContext).inflate(R.layout.list_item_bes,parent,false);

        Bestandteil bestandteil = besList.get(position);

        ImageView image = (ImageView)listItem.findViewById(R.id.imageTonne);
        image.setImageResource(bestandteil.getmImageDrawable());

        TextView name = (TextView) listItem.findViewById(R.id.textViewBestandteil);
        name.setText(bestandteil.getmName());

        return listItem;
    }
}