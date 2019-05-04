package com.example.jojo.recyclescan;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BestandList extends AppCompatActivity {

    ListView lv;
    FloatingActionButton btn_finish;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bestand_list);

        lv = findViewById(R.id.listBestandteile);
        btn_finish = findViewById(R.id.floatButton2);

        //Daten
        GelberSack gelb = new GelberSack();
        //hier dann glas und Altpapier Liste..
        GelberSack gelb2 = new GelberSack();
        ArrayList<String> combined = new ArrayList<>();
        combined.addAll(gelb.listeGelb);
        //hier dann glas und Altpapier Liste..
        combined.addAll(gelb2.listeGelb);

        ArrayAdapter<String> list = new ArrayAdapter<String>(BestandList.this, android.R.layout.simple_list_item_multiple_choice, combined);
        lv.setAdapter(list);

        final ArrayList<String> übergabe = new ArrayList<>();

        lv.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Verdreht?? isItemChecked...
                if ( lv.isItemChecked(position)){
                    lv.setItemChecked(position, true);
                    String item = parent.getItemAtPosition(position).toString();
                    Toast.makeText(getApplicationContext(),  item+ " ausgewählt", Toast.LENGTH_SHORT).show();
                    übergabe.add(item);
                }
                else
                {
                    lv.setItemChecked(position, false);
                    String item = parent.getItemAtPosition(position).toString();
                    Toast.makeText(getApplicationContext(),  item+ " entfernt", Toast.LENGTH_SHORT).show();
                    übergabe.remove(item);
                }

            }
        });

        btn_finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(übergabe.size() > 5){
                    Toast.makeText(getApplicationContext(),  "Max 5 Bestandteile auswählen!", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    //TODO: Backward Intent or Shared Preference or OnActivity Result...
                    Intent back = new Intent(getApplicationContext(), NewProduct.class);
                    back.putExtra("LISTBES", übergabe);
                    startActivity(back);
                }
            }
        });


    }
}
