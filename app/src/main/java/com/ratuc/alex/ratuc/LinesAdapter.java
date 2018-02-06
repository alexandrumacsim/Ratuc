package com.ratuc.alex.ratuc;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


public class LinesAdapter extends RecyclerView.Adapter<LinesAdapter.LineHolder> implements Filterable {   //clasa care ne permite sa afisam lista , implementeaza filterable pt filtrare
    private List<Line> lines;                       // lista de linii
    private List<Line> filteredLines;               // lista filtrata de linii
    private OnLineClickListener listener;           // listener ca sa trimitem numele pdf-ului pe care am dat click si lista cu linii
    private boolean onBind;                         // la main activity

    LinesAdapter() {
        this.lines = new ArrayList<>();
        this.filteredLines = new ArrayList<>();
    }

    @Override
    public LinesAdapter.LineHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.line_list_item, parent,          // in v facem inflate la layoutu line_list_item care reprezinta o linie din lista
                false);
        return new LineHolder(v);
    }

    @Override
    public void onBindViewHolder(LinesAdapter.LineHolder holder, int position) {
        Line line = filteredLines.get(position);                        // in onbindviewholder se seteaza datele la fiecare linie din lista

        holder.nameTV.setText(line.getName());                          //setam numele liniei pe textview (nameTV)

        onBind = true;
        holder.favoriteCB.setChecked(line.isFavorite());                // setam checkboxul cu valoarea campului is favorite
        onBind = false;
    }

    void setLines(List<Line> lineSet) {                             // setam listele de linii
        this.lines.clear();
        this.lines.addAll(lineSet);
        this.filteredLines.clear();
        this.filteredLines.addAll(lines);

        sortByFavoriteLines(filteredLines);        //filtram lista dupa liniile favorite, favoritele sunt sus

        notifyDataSetChanged();
    }

    void setListener(OnLineClickListener listener) {
        this.listener = listener;
    }

    @Override
    public int getItemCount() {
        return filteredLines != null ? filteredLines.size() : 0; //returnam marimea listei de linii ca sa stie onbindviewholder cate elemente are
    }

    private void sortByFavoriteLines(List<Line> listToSort) {     //metoda pt sortarea listei de linii dupa liniile favorite
        Collections.sort(listToSort, new Comparator<Line>() {
            @Override
            public int compare(Line l1, Line l2) {
                return Boolean.compare(l2.isFavorite(), l1.isFavorite());
            }
        });
    }

    @Override
    public Filter getFilter() {                                         //metoda pt filtrarea dupa textul introdus la search
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                filteredLines.clear();
                FilterResults results = new FilterResults();

                if (charSequence.length() == 0) {                   //daca nu avem nimic introdus la search, returnam lista initiala
                    filteredLines.addAll(lines);
                } else {
                    String filterPattern = charSequence.toString().toLowerCase().trim();

                    for (Line line : lines) {       //parcurgem lista de linii sa vedem daca gasim in numele liniei vreun caracter introdus in search
                        if (line.getName().toLowerCase().contains(filterPattern)) {     //daca gasim, adaugam linia in lista
                            filteredLines.add(line);
                        }
                    }
                }
                results.values = filteredLines;
                results.count = filteredLines.size();
                return results;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults results) {
                filteredLines = new ArrayList<>();                    //setam rezultatele cautarii la lista de linii filtrate
                filteredLines.addAll((List<Line>) results.values);
                sortByFavoriteLines(filteredLines);                     //sortam lista dupa favorite iara
                notifyDataSetChanged();                       //notificam adapteru ca s-o schimbat lista de date si o reincarcam
            }
        };
    }

    class LineHolder extends RecyclerView.ViewHolder {        //holderul tine un element din lista
        TextView nameTV;                                      // textview = view folosit pt scris text in el, folosit pt numele liniei
        CheckBox favoriteCB;                                  //checkbox poate fi bifat sau debifat, folosit pt favorite
        RelativeLayout lineItemLL;                            // layoutu parinte la alea 2 de mai sus

        LineHolder(View itemView) {
            super(itemView);
            nameTV = itemView.findViewById(R.id.nameTV);                          //luam fiecare view dupa id
            favoriteCB = itemView.findViewById(R.id.favoriteCB);
            lineItemLL = itemView.findViewById(R.id.line_itemLL);

            lineItemLL.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {                              //setam click listener pe un rand din lista
                    listener.onLineClick(filteredLines.get(getAdapterPosition()).getAssetName());   //cand dam click pe un rand, apelam onLineClick din listener si trimite numele pdf-ului la mainactivity
                }
            });

            favoriteCB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {           //setam listener pt cand se schimba valoarea checkboxului
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if (!onBind) {
                        Line line = filteredLines.get(getAdapterPosition());
                        line.setFavorite(favoriteCB.isChecked());                  //setam obiectului linie valoarea checkboxului

                        if (favoriteCB.isChecked()) {         //daca checkboxu ii activat, mutam linia respectiva pe prima pozitie din lista
                            filteredLines.remove(line);
                            filteredLines.add(0, line);
                            notifyDataSetChanged();
                        }
                        //notificam ca am schimbat ceva la lista si reincarcam ( pt ca am mutat linia pe prima pozitie)
                        listener.onFavoriteSelected(filteredLines);                                         //apelam metoda de la listener pt linia selectata
                    }
                }
            });
        }
    }

    interface OnLineClickListener {
        void onLineClick(String assetName);

        void onFavoriteSelected(List<Line> filteredLines);
    }
}
