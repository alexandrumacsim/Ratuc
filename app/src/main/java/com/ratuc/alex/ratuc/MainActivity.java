package com.ratuc.alex.ratuc;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.ratuc.alex.ratuc.PdfViewActivity.ASSET_TO_OPEN_NAME;

public class MainActivity extends AppCompatActivity implements LinesAdapter.OnLineClickListener {
    private RecyclerView linesRv;                              //recyclerview ii folosit pt a afisa liste
    private LinesAdapter adapter;
    private EditText searchET;                                 //edittext folosit pt a scrie text in el, folosit pt search

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        linesRv = findViewById(R.id.linesRV);
        searchET = findViewById(R.id.line_searchET);

        linesRv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        adapter = new LinesAdapter();                            //cream instanta de adapter
        adapter.setListener(this);                               //setam adapteru la recyclerview
        linesRv.setAdapter(adapter);

        createLineList();
        setUpSearch();
    }

    private void setUpSearch() {
        searchET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                adapter.getFilter().filter(charSequence);          //facem search
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
    }

    private void createLineList() {
        List<Line> lines = new ArrayList<>();

        try {
            for (String schedule : getAssets().list("lines")) {    //luam lista de pdf din folderu assets din proiect
                lines.add(new Line(schedule, getNameFromAsset(schedule), schedule));     // (deasupra la java in stanga)
            }                                        //si cream o lista de linii pt afisare o lista de linii pt afisare
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (DbHelper.getLines() != null && DbHelper.getLines().size() > 0) {   //daca avem linii in baza de date le setam la adapter
            adapter.setLines(DbHelper.getLines());
        } else {                                   //daca nu avem ,salvam liniile din lista creata mai sus si setam la adapter
            DbHelper.saveLines(lines);
            adapter.setLines(lines);
        }
    }

    private String getNameFromAsset(String assetName) {
        return assetName.substring(0, assetName.indexOf(".")).replace("_", " ");
    }                 // luam numele liniei din numele pdf-ului (exemplu : pdf name=Linie_1.pdf, numele liniei = Linia 1)

    @Override
    public void onLineClick(String assetName) {
        Intent intent = new Intent(this, PdfViewActivity.class);   //mergem la activitatea care ne afiseaza
        intent.putExtra(ASSET_TO_OPEN_NAME, assetName);      //pdf-ul, ii dam extra numele pdf-ului ca sa stie sa il ia din assets
        startActivity(intent);
    }

    @Override
    public void onFavoriteSelected(List<Line> filteredLines) {
        DbHelper.saveLines(filteredLines);    //cand adaugam o linie la favorite, salvam lista cu obiectul cu flagul isFavorite updatat
    }
}
