package com.example.jojo.recyclescan;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.widget.ListView;
import android.widget.TextView;
import java.util.ArrayList;


public class Ergebnis extends AppCompatActivity {

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
                drawable = getTonne(array.get(i));
                newArray.add(new Bestandteil(drawable, array.get(i)));
            }
        }

        mAdapter = new MyAdapter(this,newArray);
        lv.setAdapter(mAdapter);
    }

    //Gibt int zurück, da die Images R.drawable.. den Datentyp Integer haben.
    public int getTonne(String bes){
        //TODO: Zuweisungslogik!

        //TODO: Ggf. Nicht einzeln gelb, glas, papier, sondern in Klasse Bestandteil integrieren.
        GelberSack gelb = new GelberSack();
        Glas glas = new Glas();
        Altpapier pap = new Altpapier();
        if ( gelb.listeGelb.contains(bes) || gelb.listeCodeGelb.contains(bes)){
            return gelb.getTonne();
        }
        if(glas.listeGlas.contains(bes) || glas.listeCodeGlas.contains(bes)){
            return glas.getTonne(bes); //weiß, grün oder braun...
        }
        if(pap.listePap.contains(bes) || pap.listeCodePap.contains(bes)){
            return pap.getTonne();
        }
        return -1;
    }


}
