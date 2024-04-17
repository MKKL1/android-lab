package com.zielonka.lab.lab2;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.zielonka.lab.R;

import java.util.List;

public class PhoneListAdapter extends RecyclerView.Adapter<PhoneListAdapter.PhoneViewHolder> {

    private List<Phone> phoneList;
    private final LayoutInflater layoutInflater;

    public PhoneListAdapter(Context context) {
        layoutInflater = LayoutInflater.from(context);
    }

    public void setPhoneList(List<Phone> phoneList) {
        this.phoneList = phoneList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public PhoneViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View wiersz = layoutInflater.inflate(R.layout.phone_row, null);
        return new PhoneViewHolder(wiersz);
    }

    @Override
    public void onBindViewHolder(@NonNull PhoneViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        if(phoneList != null)
            return phoneList.size();
        return 0;
    }

    public class PhoneViewHolder extends RecyclerView.ViewHolder{

        public PhoneViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        public void bind(int position) {
            Phone phone = phoneList.get(position);
            ((TextView)itemView.findViewById(R.id.producerText)).setText(phone.getProducer());
            ((TextView)itemView.findViewById(R.id.modelText)).setText(phone.getModel());
        }
    }
}
