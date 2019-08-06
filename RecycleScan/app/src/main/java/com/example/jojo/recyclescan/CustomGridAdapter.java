package com.example.jojo.recyclescan;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class CustomGridAdapter extends BaseAdapter {

    private LayoutInflater inflater;
    private List<String> produkte;
    private Context context;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private String produktname = "Marshmallow";

    public CustomGridAdapter(Context context, List<String> produkte){
        this.context = context;
        this.produkte = produkte;
        inflater = LayoutInflater.from(context);
    }
    @Override
    public int getCount() {
        return produkte.size();
    }

    @Override
    public Object getItem(int position) {
        return produkte.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }



    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItem = convertView;
        if(listItem == null)
            listItem = inflater.inflate(R.layout.grid_item, parent, false);


        ImageView image = (ImageView)listItem.findViewById(R.id.imageViewProduct);
        int zahl = (int) (Math.random()*3+1);
        if ( zahl <2){
            image.setImageResource(R.mipmap.marshmallow);
        }
        else if ( zahl <3 ){
            image.setImageResource(R.mipmap.watermelon);

        }else{
            image.setImageResource(R.mipmap.pretzel);

        }
        //Macht einzelne Regalbretter
        //listItem.setBackgroundResource(R.mipmap.newshelf);


        final TextView name = (TextView) listItem.findViewById(R.id.textViewProduktname);
        String ean = getItem(position).toString();

        final DocumentReference docRef = db.collection("Produkte").document(ean);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if ( task.isSuccessful()){
                    DocumentSnapshot document = task.getResult();
                    produktname = (String) document.get("Bezeichnung");
                    name.setText(produktname);
                }
            }
        });
        return listItem;
    }
}
