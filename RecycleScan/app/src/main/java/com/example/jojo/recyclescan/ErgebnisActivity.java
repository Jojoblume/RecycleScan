package com.example.jojo.recyclescan;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * Activity ErgebnisActivity.
 * Zeigt nach dem Scannen die EAN, die Bezeichnung des Produkts, sowie dessen Bestandteile mit Zuordnung der Tonne an.
 */
public class ErgebnisActivity extends AppCompatActivity {

    TextView textEAN;
    TextView textBez;
    ListView lv;

    CardView cardView;
    TextView hinweis;
    TextView hinweisText;

    private MyAdapter mAdapter;

    List<String> gelb = new ArrayList<>();
    List<String> gelbCode = new ArrayList<>();
    List<String> glas = new ArrayList<>();
    List<String> glasCode = new ArrayList<>();
    List<String> pap = new ArrayList<>();
    List<String> papCode = new ArrayList<>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ergebnis);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        textEAN = findViewById(R.id.textViewEAN2);
        final String ean = getIntent().getExtras().getString("EAN");
        textEAN.setText(ean);

        textBez = findViewById(R.id.textViewBez);
        String bezeichnung = getIntent().getExtras().getString("BEZ");
        textBez.setText(bezeichnung);


        gelb = Arrays.asList(getResources().getStringArray(R.array.arrayBestandteileGelb));
        gelbCode = Arrays.asList(getResources().getStringArray(R.array.arrayCodesGelb));
        glas = Arrays.asList(getResources().getStringArray(R.array.arrayBestandteileGlas));
        glasCode = Arrays.asList(getResources().getStringArray(R.array.arrayCodesGlas));
        pap = Arrays.asList(getResources().getStringArray(R.array.arrayBestandteilePap));
        papCode = Arrays.asList(getResources().getStringArray(R.array.arrayCodesPap));

        ArrayList<String> array = getIntent().getStringArrayListExtra("TEILE");
        lv = (ListView) findViewById(R.id.lvBes);

        // Sichtbarkeit der Hinweistexte. Jeweils nur 1 Hinweis. Verpackungen mit mehr als 1 Hinweis unwahrscheinlich.
        // Falls doch, wird der Joghurt Hinweis angezeigt.
        cardView = findViewById(R.id.cardView);
        hinweis = findViewById(R.id.textViewHinweis);
        hinweisText = findViewById(R.id.textViewHinweisText);
        cardView.setVisibility(View.INVISIBLE);
        if (bezeichnung.contains("joghurt") || bezeichnung.contains("Joghurt") || bezeichnung.equals("Joghurt")){
            hinweis.setText("Hinweis: Joghurtbecher");
            hinweisText.setText("Den Joghurtbecher bitte NICHT platzsparend stapeln. Außerdem reicht es, wenn er \"löffelrein\" ist. Er muss nicht extra ausgespült werden.");
            cardView.setVisibility(View.VISIBLE);

        }
        else {
            for (int i = 0; i<array.size(); i++){
                if (array.get(i).equals("Tetra Pak")){
                    hinweis.setText("Hinweis: Tetra Pak");
                    hinweisText.setText("Verschlusskappe wieder anbringen und Trinkhalme zurück in die Packung drücken.  ");
                    cardView.setVisibility(View.VISIBLE);
                }
                else if (array.get(i).equals("Karton mit Plastik-Sichtfenster")){
                    hinweis.setText("Hinweis: Sichtfenster");
                    hinweisText.setText("Kartons / Briefe mit Plastik Sichtfenster gehören ins Altpapier. Der Kunststoffanteil ist verkraftbar und für die Weiterverarbeitung als Recyclingpapier unerheblich.");
                    cardView.setVisibility(View.VISIBLE);
                }
            }
        }

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

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(ErgebnisActivity.this, InfoTonneActivity.class);
                String name = mAdapter.getName(position);
                if ( gelb.contains(name) || gelbCode.contains(name)){
                    intent.putExtra("TONNE", "gelb");
                }
                else if ( glas.contains(name) || glasCode.contains(name)){
                    intent.putExtra("TONNE", "glas");
                }
                else if ( pap.contains(name) || papCode.contains(name)){
                    intent.putExtra("TONNE", "pap");
                }
                startActivity(intent);
            }
        });

    }


    //Gibt int zurück, da die Images R.drawable.. den Datentyp Integer haben.
    public int getTonne(String bes){

        if ( gelb.contains(bes) || gelbCode.contains(bes)){
            return R.drawable.trash;
        }
        else if(glas.contains(bes) || glasCode.contains(bes)){
            if (bes.equals(getString(R.string.farblos)) || bes.equals(getString(R.string.farblosCode))){
                return R.drawable.glass_white; //farblos
            } else if (bes.equals(getString(R.string.gruen)) || bes.equals(getString(R.string.anders)) || bes.equals(getString(R.string.gruenCode))) {
                return R.drawable.glass_green; //grün
            }
            else if ( bes.equals(getString(R.string.braun)) || bes.equals(getString(R.string.braunCode))){
                return R.drawable.glass_brown; //braun
            }
        }
        else if(pap.contains(bes) || papCode.contains(bes)){
            return R.drawable.paper;
        }
        return -1;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(this,MainActivity.class);
                startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
    }


}
