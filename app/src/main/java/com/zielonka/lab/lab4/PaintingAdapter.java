package com.zielonka.lab.lab4;

import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.zielonka.lab.databinding.FragmentItemBinding;

import java.util.List;

public class PaintingAdapter extends RecyclerView.Adapter<PaintingAdapter.PaintingAdapterViewHolder> {

    Activity activity;
    List<PaintingContainer.PaintingItem> mValues;
    private ItemClickListener itemClickListener;
    public PaintingAdapter(List<PaintingContainer.PaintingItem> items, Activity context) {
        this.mValues = items;
        this.activity = context;
    }

    @Override
    public PaintingAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        FragmentItemBinding binding = FragmentItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new PaintingAdapterViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(final PaintingAdapterViewHolder holder, int position) {
        PaintingContainer.PaintingItem paintingItem = mValues.get(position);
        holder.setItemFilename(paintingItem.getFilename());
        Bitmap bitmap = BitmapFactory.decodeFile(paintingItem.filepath);
        holder.setImage(bitmap);
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
        public final TextView itemFilename;
        public final Button paintingDetailsButton;
        private final ImageView imageView;

        public PaintingAdapterViewHolder(FragmentItemBinding binding) {
            super(binding.getRoot());
            itemFilename = binding.itemFilename;
            paintingDetailsButton = binding.paintingDetailsButton;
            imageView = binding.itemImage;
            binding.paintingDetailsButton.setOnClickListener(this);
        }
        public void setItemFilename(String filename){
            itemFilename.setText(filename);
        }
        public void setImage(Bitmap bm) {
            imageView.setImageBitmap(bm);
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
