package com.zielonka.lab.lab1;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.zielonka.lab.R;

import java.util.List;

public class OcenyModelAdapter extends RecyclerView.Adapter<OcenyViewHolder> {

    private List<ModelOceny> listaOcen;
    private LayoutInflater layoutInflater;

    public OcenyModelAdapter(Activity context, List<ModelOceny> listaOcen) {
        layoutInflater = context.getLayoutInflater();
        this.listaOcen = listaOcen;
    }

    @NonNull
    @Override
    public OcenyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View wiersz = layoutInflater.inflate(R.layout.ocena_wiersz, null);

        return new OcenyViewHolder(wiersz);
    }

    @Override
    public void onBindViewHolder(@NonNull OcenyViewHolder holder, int position) {
        holder.bind(listaOcen.get(position));
    }

    @Override
    public int getItemCount() {
        return listaOcen.size();
    }

}
