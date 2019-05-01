package com.example.jojo.recyclescan;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.google.firebase.firestore.FirebaseFirestore;

public class Ergebnis extends AppCompatActivity {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    TextView textEAN;
    TextView textBez;
    TextView textTeil;

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

        textTeil = findViewById(R.id.textViewTeil);
        String bestandteile = getIntent().getExtras().getString("TEIL");
        textTeil.setText(bestandteile);


    }
}
