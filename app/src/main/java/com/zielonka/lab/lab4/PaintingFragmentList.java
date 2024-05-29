package com.zielonka.lab.lab4;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;

import com.zielonka.lab.R;

import java.util.List;

public class PaintingFragmentList extends Fragment {

    public PaintingFragmentList() {}
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_item_list, container, false);
        List<PaintingContainer.PaintingItem> paintingItemList = PaintingContainer.getPaintings();
        PaintingAdapter paintingAdapter = new PaintingAdapter(paintingItemList, getActivity());
        RecyclerView recyclerView = view.findViewById(R.id.paintingList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(paintingAdapter);

        paintingAdapter.setItemClickListener(position -> {
            PaintingFragmentDetails fragmentList = PaintingFragmentDetails.newInstance(position);
            requireActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.main, fragmentList)
                    .addToBackStack(null)
                    .commit();
        });

        return view;
    }
}
