package com.example.jojo.recyclescan;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class ArticleActivity extends AppCompatActivity {

    TextView headline;
    TextView article;
    ImageView img;
    TextView article2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        headline = findViewById(R.id.textViewHeadline);
        article = findViewById(R.id.textViewArticle);
        img = findViewById(R.id.imageViewArticle);
        article2 = findViewById(R.id.textViewArticle2);

        String cv = getIntent().getExtras().getString("CV");

        if (cv.equals("cv1")){
            headline.setText("Wie benutzt man Recycle Scan?");
            img.setVisibility(View.VISIBLE);
            article.setText("Es gibt 3 Wege Recycle Scan zu benutzen:\n\n1. SCANNEN Button\n\nScanne eine Lebensmittelverpackung und erfahre, welche Bestandteile" +
                    "in welche Tonne gehören. Falls die Verpackung noch nicht in der Datenbank ist, kannst du Angaben zur Bezeichnung und den Bestandteilen machen. \n" +
                    "Entweder du wählst aus der Liste der Verpackungsbestandteile und/oder der Liste der Recyclingcodes. Dabei bitte keine doppelten Angaben machen.");
            article2.setText("2. SUCHE BARCODE\n\n" +
                    "Wenn du keinen Zugriff zur Kamera erlauben möchtest, hast du die Möglichkeit die Nummer vom Barcode einzutippen." +
                    "Hierbei hast du nicht die Möglichkeit die Datenbank zu vervollständigen, da schnell Tippfehler passieren können.\n\n" +
                    "3. SUCHE TYPISCHE PRODUKTE\n\n" +
                    "Die kleine Lupe oben rechts auf dem Startbildschirm ermöglicht dir nach typischen Produkten zu suchen, " +
                    "welche zum Teil auch keinen Barcode besitzen. ");

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
