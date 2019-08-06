package com.example.jojo.helpers;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.jojo.recyclescan.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

/**
 * Adapter für GridView.
 * Bild wird zufällig ausgewählt und Produktname wird für jedes einzelne Item von Firebase abgefragt.
 */
public class CustomGridAdapter extends BaseAdapter {

    private LayoutInflater inflater;
    private List<String> produkte;
    private Context context;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private String produktname = "";

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


        ImageView image = listItem.findViewById(R.id.imageViewProduct);
        int zahl = (int) (Math.random()*3+1);
        if ( zahl <2){
            image.setImageResource(R.mipmap.marshmallow);
        }
        else if ( zahl <3 ){
            image.setImageResource(R.mipmap.watermelon);

        }else{
            image.setImageResource(R.mipmap.pretzel);

        }

        //Statt Klasse "MyGridView", könnte auch folgender Code benutzt werden.
        //Dann werden jedoch die Regalbretter einzeln für jedes Item angezeigt und nicht über die ganze Länge.
        //listItem.setBackgroundResource(R.mipmap.newshelf);


        final TextView name = listItem.findViewById(R.id.textViewProduktname);
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
