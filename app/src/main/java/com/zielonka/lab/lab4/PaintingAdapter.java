package com.zielonka.lab.lab4;

import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.zielonka.lab.databinding.FragmentItemBinding;

import java.util.List;

public class PaintingAdapter extends RecyclerView.Adapter<PaintingAdapter.PaintingAdapterViewHolder> {

    Activity activity;
    List<PaintingContent.PaintingItem> mValues;
    private ItemClickListener itemClickListener;
    public PaintingAdapter(List<PaintingContent.PaintingItem> items, Activity context) {
        this.mValues = items;
        this.activity = context;
    }

    @Override
    public PaintingAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        FragmentItemBinding binding = FragmentItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new PaintingAdapterViewHolder(binding);  // Pass the adapter's listener here
    }

    @Override
    public void onBindViewHolder(final PaintingAdapterViewHolder holder, int position) {
        PaintingContent.PaintingItem paintingItem = mValues.get(position);
        holder.setItemFilename(paintingItem.getFilename());
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public interface ItemClickListener {
        void onButtonClick(int position);
    }
    public void setItemClickListener(ItemClickListener itemClickListener){
        this.itemClickListener = itemClickListener;
    }

    public class PaintingAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public final TextView mItemFilename;
        public final Button paintingDetailsButton;

        public PaintingAdapterViewHolder(FragmentItemBinding binding) {
            super(binding.getRoot());
            mItemFilename = binding.itemFilename;
            paintingDetailsButton = binding.paintingDetailsButton;
            binding.paintingDetailsButton.setOnClickListener(this);
        }
        public void setItemFilename(String filename){
            mItemFilename.setText(filename);
        }

        @Override
        public void onClick(View v) {

            int position = getAdapterPosition();
            Log.d("ERROR", "position: " + position);
            if (position != RecyclerView.NO_POSITION) {
                itemClickListener.onButtonClick(position);
            }
        }
    }
}
