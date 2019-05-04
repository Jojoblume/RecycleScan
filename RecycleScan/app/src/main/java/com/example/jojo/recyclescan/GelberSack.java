package com.example.jojo.recyclescan;

import java.util.ArrayList;

public class GelberSack {
    String bes1 = "Verpackungsplastik";
    String bes2 = "Plastik";
    String bes3 = "05 PP";
    public ArrayList<String> listeGelb = new ArrayList<>();

    public GelberSack(){
        listeGelb.add(bes1);
        listeGelb.add(bes2);
        listeGelb.add(bes3);

    }

    public String getSubCat(String bes){
        if ( listeGelb.contains(bes)){
            if ( bes.equals("Verschluss")){
                return "(Kunststoff, Alu, Clips...)";
            }
        }
        return "";
    }

    public int getTonne(){
        return R.drawable.trash;
    }
    public ArrayList<String> getArray(){
        return listeGelb;
    }
}
