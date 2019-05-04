package com.example.jojo.recyclescan;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class NewProduct extends AppCompatActivity {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    TextView textEAN;
    FloatingActionButton btn_float;
    EditText textBez;
    EditText text1;
    EditText text2;
    EditText text3;
    Button btn_bes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_product);

        textEAN = findViewById(R.id.textViewEAN2);
        final String ean = getIntent().getExtras().getString("EAN");
        textEAN.setText(ean);

        textBez = findViewById(R.id.editTextBez);
        text1 = findViewById(R.id.editText1);
        text2 = findViewById(R.id.editText2);
        text3 = findViewById(R.id.editText3);

        btn_bes = findViewById(R.id.button2);
        btn_bes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            Intent nextIntent = new Intent(getApplicationContext(), BestandList.class);
            startActivity(nextIntent);
            }
        });

        btn_float = findViewById(R.id.floatButton);
        btn_float.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String bez = textBez.getText().toString();
                ArrayList<String> array = getIntent().getStringArrayListExtra("LISTBES");

                Map<String, Object> product = new HashMap<>();
                product.put("Bezeichnung", bez);
                product.put("Bestandteil 1", array.get(0));
                product.put("Bestandteil 2", array.get(1));
                product.put("Bestandteil 3", array.get(2));
                product.put("Bestandteil 4", array.get(3));
                product.put("Bestandteil 5", array.get(4));

                db.collection("Produkte").document(ean)
                        .set(product)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d(MainActivity.TAG, "DocumentSnapshot successfully written!");
                                Intent homeIntent = new Intent(getApplicationContext(), MainActivity.class);
                                startActivity(homeIntent);
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w(MainActivity.TAG, "Error writing document", e);
                            }
                        });


            }
        });

    }
}
