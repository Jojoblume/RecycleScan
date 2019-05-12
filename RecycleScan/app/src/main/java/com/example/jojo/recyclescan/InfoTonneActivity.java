package com.example.jojo.recyclescan;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Objects;

/**
 * Activity InfoTonneActivity.
 * Gibt weitere Informationen zur Mülltrennung an.
 * Aufrufbar durch Klick auf Listenelement von ErgebnisActivity.class oder SearchActivity.class
 */
public class InfoTonneActivity extends AppCompatActivity {
    TextView headline;
    TextView was;
    TextView zusatz1;
    TextView zusatz2;
    TextView zusatz3;
    TextView zusatz4;
    ImageView img_tonne;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        headline = findViewById(R.id.headline);
        was = findViewById(R.id.was);
        zusatz1 = findViewById(R.id.zusatz1);
        zusatz2 = findViewById(R.id.zusatz2);
        zusatz3 = findViewById(R.id.zusatz3);
        zusatz4 = findViewById(R.id.zusatz4);

        img_tonne = findViewById(R.id.imageTonne);

        String tonne = getIntent().getExtras().getString("TONNE");
        if (tonne.equals("gelb")){
            headline.setText("Gelber Sack / Gelbe Tonne");
            was.setText(getString(R.string.gelbWas));
            zusatz1.setText(getString(R.string.zusatz1Gelb));
            zusatz2.setText(getString(R.string.zusatz2Gelb));
            zusatz3.setText(getString(R.string.zusatz3Gelb));
            zusatz4.setText(getString(R.string.zusatz4Gelb));

            img_tonne.setImageResource(R.drawable.trash);
        }
        else if (tonne.equals("glas")){
            headline.setText("Glas Container");
            was.setText(getString(R.string.glasWas));
            zusatz1.setText(getString(R.string.zusatz1Glas));
            zusatz2.setText(getString(R.string.zusatz2Glas));
            zusatz3.setText(getString(R.string.zusatz3Glas));
            zusatz4.setVisibility(View.INVISIBLE);

            img_tonne.setImageResource(R.drawable.glass_white);

        }else if (tonne.equals("pap")){
            headline.setText("Altpapier Tonne / Container");
            was.setText(getString(R.string.pappeWas));
            zusatz1.setText(getString(R.string.zusatz1Pap));
            zusatz2.setText(getString(R.string.zusatz2Pap));
            zusatz3.setVisibility(View.INVISIBLE);
            zusatz4.setVisibility(View.INVISIBLE);

            img_tonne.setImageResource(R.drawable.paper);
        }
        else if ( tonne.equals("rest")){
            headline.setText("Restmüll");
            was.setText(getString(R.string.restWas));
            zusatz1.setVisibility(View.INVISIBLE);
            zusatz2.setVisibility(View.INVISIBLE);
            zusatz3.setVisibility(View.INVISIBLE);
            zusatz4.setVisibility(View.INVISIBLE);

            img_tonne.setImageResource(R.drawable.rest);
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


