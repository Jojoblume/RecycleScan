package com.example.jojo.recyclescan;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Bildschirm, um die Spierlstatistiken des Erstellers des Produkts anzuzeigen.
 * (Titel, Benutzername, Punkte und Anzahl eingetragener Produkte)
 */
public class CreatorActivity extends AppCompatActivity {

    TextView textBenutzername;
    TextView textTitel;
    TextView textPunkte;
    TextView textAnzahl;

    ImageView img_titel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creator);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        textBenutzername = findViewById(R.id.textViewBenutzername);
        String benutzername = getIntent().getExtras().getString("BENUTZERNAME");
        textBenutzername.setText(benutzername);

        textTitel = findViewById(R.id.textViewTitel);
        String titel = getIntent().getExtras().getString("TITEL");
        textTitel.setText(titel);

        textPunkte = findViewById(R.id.textViewPunkte);
        long punkte = getIntent().getExtras().getLong("PUNKTE");
        textPunkte.setText(punkte + " Punkte");

        textAnzahl = findViewById(R.id.textViewAnzahlProdukte);
        int anzahl = getIntent().getExtras().getInt("ANZAHLPRODUKTE");
        textAnzahl.setText(anzahl + " eingetragene Produkte");

        getImage(titel);

    }

    private void getImage(String titel) {
        img_titel = findViewById(R.id.imageViewTitel);
        if (titel.equals("Müllmonster")){
            //Icon made by Freepik from www.flaticon.com
            img_titel.setImageResource(R.drawable.monster);
        }
        else if (titel.equals("Schrottsammler")){
            //Icon made by Freepik from www.flaticon.com
            img_titel.setImageResource(R.drawable.schrottsammler);
        }
        else if (titel.equals("Sprössling")){
            //Icon made by Icongeek26 from www.flaticon.com
            img_titel.setImageResource(R.drawable.sproessling);

        }
        else if (titel.equals("Recycler")){
            //Icon made by monkik from www.flaticon.com
            img_titel.setImageResource(R.drawable.recycler);

        }
        else if (titel.equals("Klimaheld")){
            //Icon made by Freepik from www.flaticon.com
            img_titel.setImageResource(R.drawable.klimaheld);

        }else {
            //Umweltaktivist
            //Icon made by Freepik from www.flaticon.com
            img_titel.setImageResource(R.drawable.umweltaktivist);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
