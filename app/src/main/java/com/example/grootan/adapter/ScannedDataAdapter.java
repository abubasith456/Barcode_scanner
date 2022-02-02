package com.example.grootan.adapter;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.grootan.R;
import com.example.grootan.databinding.RowItemViewDataBinding;
import com.example.grootan.models.ScannedData;

import java.util.ArrayList;
import java.util.Objects;

import io.grpc.Context;

public class ScannedDataAdapter extends RecyclerView.Adapter<ScannedDataAdapter.ScannedDataViewHolder> {
    private ArrayList<ScannedData> scannedDataArrayList;
    private Context context;

//    public ScannedDataAdapter(ArrayList<ScannedData> scannedDataArrayList) {
//        this.scannedDataArrayList = scannedDataArrayList;
//        this.context = context;
//    }

    @NonNull
    @Override
    public ScannedDataViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        RowItemViewDataBinding rowItemViewDataBinding = RowItemViewDataBinding.inflate(layoutInflater, parent, false);
        return new ScannedDataViewHolder(rowItemViewDataBinding);

    }

    @Override
    public void onBindViewHolder(@NonNull ScannedDataViewHolder holder, int position) {
        ScannedData scannedData = scannedDataArrayList.get(position);
        holder.rowItemViewDataBinding.setScannedData(scannedData);
    }

    @Override
    public int getItemCount() {
        if (scannedDataArrayList != null) {
            return scannedDataArrayList.size();
        } else {
            return 0;
        }
    }

    public void getScannedData(ArrayList<ScannedData> arrayList) {
        this.scannedDataArrayList = arrayList;
        notifyDataSetChanged();

    }

    public static class ScannedDataViewHolder extends RecyclerView.ViewHolder {

        RowItemViewDataBinding rowItemViewDataBinding;

        public ScannedDataViewHolder(@NonNull RowItemViewDataBinding rowItemViewDataBinding) {
            super(rowItemViewDataBinding.getRoot());
            this.rowItemViewDataBinding = rowItemViewDataBinding;

        }
    }


}
