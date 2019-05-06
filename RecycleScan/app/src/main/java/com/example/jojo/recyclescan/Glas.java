package com.example.jojo.recyclescan;

import java.util.ArrayList;

public class Glas {

    String bes1 = "Farbloses Glas";
    String bes2 = "Grünes Glas";
    String bes3 = "Braunes Glas";
    String bes4 = "Andersfarbiges Glas";

    String code1 = "70 GL";//Farblos
    String code2 = "71 GL";//Grün
    String code3 = "72 GL";//Braun



    public ArrayList<String> listeGlas = new ArrayList<>();
    public ArrayList<String > listeCodeGlas = new ArrayList<>();

    public Glas(){
        listeGlas.add(bes1);
        listeGlas.add(bes2);
        listeGlas.add(bes3);
        listeGlas.add(bes4);

        listeCodeGlas.add(code1);
        listeCodeGlas.add(code2);
        listeCodeGlas.add(code3);


    }

    public int getTonne(String bes){
        if (bes.equals(bes1) || bes.equals(code1)){
           return R.drawable.glass_white; //farblos
        } else if (bes.equals(bes2) || bes.equals(code2) || bes.equals(bes4)) {
            return R.drawable.glass_green; //grün
        }
        else if ( bes.equals(bes3) || bes.equals(code3)){
            return R.drawable.glass_brown; //braun
        }
        return -1; //Fragezeichen
    }

}
