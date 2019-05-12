package com.example.jojo.recyclescan;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

public class ArticleActivity extends AppCompatActivity {

    TextView headline;
    TextView article;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        headline = findViewById(R.id.textViewHeadline);
        article = findViewById(R.id.textViewArticle);

        String cv = getIntent().getExtras().getString("CV");

        if (cv.equals("cv1")){
            headline.setText("Wie benutzt man Recycle Scan?");
            article.setText("Es gibt 3 Wege Recycle Scan zu benutzen:\n\n1. SCANNEN Button\nScanne einen Barcode von einer Lebensmittelverpackung. Falls sich die Verpackung bereits" +
                    "in unserer Datenbank befindet, bekommst du Informationen zu den Bestandteilen und in welcher Tonne diese entsorgt werden müssen." +
                    "Falls sich die Verpackung noch nicht in der Datenbank befindet, kannst du selber Angaben machen. Hierzu wird die Bezeichnung des Produkts benötigt(" +
                    "Z.B. \"Milch\" oder \"Erdbeerjoghurt\") sowie die Bestandteile. Wenn du dir unsicher bist, lass die Angaben weg. Ansonsten hast du die Möglichkeit entweder aus" +
                    "der Liste der Verpackungsbestandteile oder der Liste der Recyclingcodes auszuwählen. ");
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
