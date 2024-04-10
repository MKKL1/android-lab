package com.zielonka.lab.lab1;

import android.view.View;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.zielonka.lab.R;

import java.util.HashMap;
import java.util.Map;

public class OcenyViewHolder extends RecyclerView.ViewHolder implements RadioGroup.OnCheckedChangeListener {
    private static final Map<Integer, Integer> radioIdToValue = new HashMap<>();
    static {
        radioIdToValue.put(R.id.grade_2, 2);
        radioIdToValue.put(R.id.grade_3, 3);
        radioIdToValue.put(R.id.grade_4, 4);
        radioIdToValue.put(R.id.grade_5, 5);
    }
    private ModelOceny modelOceny;

    public OcenyViewHolder(@NonNull View itemView) {
        super(itemView);
    }

    public void bind(ModelOceny modelOceny) {
        this.modelOceny = modelOceny;
        TextView nazwa = itemView.findViewById(R.id.name);
        nazwa.setText(modelOceny.getNazwa());

        RadioGroup ocena = itemView.findViewById(R.id.buttons);
        ocena.setOnCheckedChangeListener(this);
    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
        modelOceny.setOcena(radioIdToValue.get(checkedId));
    }

}
