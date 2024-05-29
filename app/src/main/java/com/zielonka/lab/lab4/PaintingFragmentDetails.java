package com.zielonka.lab.lab4;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.zielonka.lab.R;

public class PaintingFragmentDetails extends Fragment {

    public static PaintingFragmentDetails newInstance(int position){
        PaintingFragmentDetails fragmentDetails = new PaintingFragmentDetails();
        Bundle args = new Bundle();
        args.putInt("position", position);
        fragmentDetails.setArguments(args);
        return fragmentDetails;
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        menu.clear();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_item_details, container, false);
        int position = getArguments().getInt("position");
        PaintingContainer.PaintingItem item = PaintingContainer.getPaintings().get(position);
        Log.d("DETAILS", "POSITION: " + position);
        String imagePath = item.getFilepath();
        Log.d("DETAILS", "filepath: " + imagePath + "  filename: " + item.getFilename());
        ImageView imageView = view.findViewById(R.id.paintingImageView);
        TextView textView = view.findViewById(R.id.imgText);
        textView.setText(imagePath);


        Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
        imageView.setImageBitmap(bitmap);
        return view;
    }
}
