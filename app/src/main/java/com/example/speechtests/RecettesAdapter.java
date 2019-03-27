package com.example.speechtests;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.speechtests.DO.RecetteSimple;

import java.util.ArrayList;
import java.util.List;


public class RecettesAdapter extends ArrayAdapter<RecetteSimple> {
    private Context mContext;
    private List<RecetteSimple> recettesList = new ArrayList<>();
    private static class IngredientsHolder {
        public TextView nameIngr;
        public TextView match;
    }

    public RecettesAdapter(@NonNull Context context, ArrayList<RecetteSimple> recettes) {
        super(context, 0, recettes);
        mContext = context;
        recettesList = recettes;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItem = convertView;

        IngredientsHolder holder = new IngredientsHolder();

        if(listItem == null){
            listItem = LayoutInflater.from(mContext).inflate(R.layout.list_recettes_item,parent,false);

            TextView name = listItem.findViewById(R.id.nom_recette);
            TextView match = listItem.findViewById(R.id.match);

            holder.nameIngr = name;
            holder.match = match;

            listItem.setTag(holder);
        } else {
            holder = (IngredientsHolder) listItem.getTag();
        }


        RecetteSimple currentRecette = recettesList.get(position);

        holder.nameIngr.setText(currentRecette.getNom());
        holder.match.setText(currentRecette.getMatch()+"%");

        return listItem;
    }

}
