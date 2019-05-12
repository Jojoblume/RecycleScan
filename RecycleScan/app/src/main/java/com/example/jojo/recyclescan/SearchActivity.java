package com.example.jojo.recyclescan;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Activity SearchActivity
 * Hier Suche nach typischen Produkten.
 * Auswahl wurde anhand von Gruener Punkt und Mülltrennung-wirkt (Initiative aller dualen Systeme) getroffen.
 * Ausnahmsweise hier auch Restmüll dabei, damit Irrtümer direkt aufgedeckt werden können.
 */
public class SearchActivity extends AppCompatActivity {

    ListView lv_search;
    ArrayAdapter<String> adapter;
    SearchView searchView;

    //Liste
    ArrayList<String> arrayProdukte = new ArrayList<>();
    List<String> arrayProdukteGelb = new ArrayList<>();
    List<String> arrayProdukteGlas = new ArrayList<>();
    List<String> arrayProduktePappe = new ArrayList<>();
    List<String> arrayProdukteRest = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        lv_search = findViewById(R.id.ListViewSearch);

        //Liste der dualen Systeme einfach in Items gepackt.
        //Zugriff von jeder Activity aus... Man muss nicht extra von den Klassen Objekte generieren...
        //In Klasse muss nicht mit ArrayList jedes String Objekt einzeleln mit .add("") hinzugefügt werden.
        //Array ist einfach da :)
        //UUUUND: Bei Änderungen der Liste, ist es einfacher.
        arrayProdukteGelb = Arrays.asList(getResources().getStringArray(R.array.arrayProdukteGelb));
        arrayProdukteGlas = Arrays.asList(getResources().getStringArray(R.array.arrayProdukteGlas));
        arrayProduktePappe = Arrays.asList(getResources().getStringArray(R.array.arrayProduktePappe));
        arrayProdukteRest = Arrays.asList(getResources().getStringArray(R.array.arrayProdukteRest));

        arrayProdukte.addAll(arrayProdukteGelb);
        arrayProdukte.addAll(arrayProdukteGlas);
        arrayProdukte.addAll(arrayProduktePappe);
        arrayProdukte.addAll(arrayProdukteRest);

        //Alphabetisch sortiert
        Collections.sort(arrayProdukte);

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, arrayProdukte);
        lv_search.setAdapter(adapter);

        searchView = findViewById(R.id.searchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return false;
            }
        });

        lv_search.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String name = parent.getItemAtPosition(position).toString();
                Intent intent = new Intent(SearchActivity.this, InfoTonneActivity.class);
                if (arrayProdukteGelb.contains(name)){
                    intent.putExtra("TONNE", "gelb");
                }
                else if ( arrayProdukteGlas.contains(name)){
                    intent.putExtra("TONNE", "glas");
                }
                else if (arrayProduktePappe.contains(name)){
                    intent.putExtra("TONNE", "pap");
                }
                else if ( arrayProdukteRest.contains(name)){
                    intent.putExtra("TONNE", "rest");
                }
                startActivity(intent);

            }
        });

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
