package com.example.jojo.recyclescan;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.zxing.integration.android.IntentIntegrator;

import java.util.ArrayList;


public class Ergebnis extends AppCompatActivity {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    TextView textEAN;
    TextView textBez;
    ListView lv;
    private MyAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ergebnis);

        textEAN = findViewById(R.id.textViewEAN2);
        String ean = getIntent().getExtras().getString("EAN");
        textEAN.setText(ean);

        textBez = findViewById(R.id.textViewBez);
        String bezeichnung = getIntent().getExtras().getString("BEZ");
        textBez.setText(bezeichnung);

        ArrayList<String> array = getIntent().getStringArrayListExtra("TEILE");
        lv = (ListView) findViewById(R.id.lvBes);

        ArrayList<Bestandteil> newArray = new ArrayList<>();
        int drawable;

        for ( int i = 0; i < array.size(); i++){
            if ( ! array.get(i).equals("")){
                //TODO: Zuweisungslogik!
                drawable = getTonne(array.get(i));
                newArray.add(new Bestandteil(drawable, array.get(i)));
            }
        }

        mAdapter = new MyAdapter(this,newArray);
        lv.setAdapter(mAdapter);
    }

    //Gibt int zurück, da die Images R.drawable.. den Datentyp Integer haben.
    public int getTonne(String bes){

        GelberSack gelb = new GelberSack();
        if ( gelb.listeGelb.contains(bes)){
            return gelb.getTonne();
        }
        return -1;
    }


}
