package com.ratuc.alex.ratuc;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;



public class Line extends RealmObject {     //clasa model linie , extinde RealmObject ca sa putem salva in realm
    @PrimaryKey
    private String id;
    private String name;                    //numele liniei
    private String assetName;               // numele pdf -ului corespunzator pt linie
    private boolean isFavorite;             // booleana sa stim daca ii salvata linia la preferinte sau nu

    public Line() {
    }

    Line(String id, String name, String assetName) {
        this.name = name;
        this.assetName = assetName;
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    String getAssetName() {
        return assetName;
    }

    public void setAssetName(String assetName) {
        this.assetName = assetName;
    }

    boolean isFavorite() {
        return isFavorite;
    }

    void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }
}
