package com.example.grootan.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.grootan.databinding.RowItemViewDataBinding;
import com.example.grootan.models.ScannedDataModel;

import java.util.ArrayList;

import io.grpc.Context;

public class ScannedDataAdapter extends RecyclerView.Adapter<ScannedDataAdapter.ScannedDataViewHolder> {
    private ArrayList<ScannedDataModel> scannedDataModelArrayList;
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
        ScannedDataModel scannedDataModel = scannedDataModelArrayList.get(position);
        holder.rowItemViewDataBinding.setScannedDataModel(scannedDataModel);
    }

    @Override
    public int getItemCount() {
        if (scannedDataModelArrayList != null) {
            return scannedDataModelArrayList.size();
        } else {
            return 0;
        }
    }

    public void getScannedData(ArrayList<ScannedDataModel> arrayList) {
        this.scannedDataModelArrayList = arrayList;
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
