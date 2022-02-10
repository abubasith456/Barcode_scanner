package com.example.grootan.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.grootan.DashboardActivity;
import com.example.grootan.R;
import com.example.grootan.databinding.ItemDetailsShowBinding;
import com.example.grootan.databinding.RowItemViewDataBinding;
import com.example.grootan.models.ScannedDataModel;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;

import io.grpc.Context;

public class ScannedDataAdapter extends RecyclerView.Adapter<ScannedDataAdapter.ScannedDataViewHolder> {
    private ArrayList<ScannedDataModel> scannedDataModelArrayList;
    private Context context;
    private DashboardActivity activity;
    private ItemDetailsShowBinding itemDetailsShowBinding;

    public ScannedDataAdapter() {
    }

    public ScannedDataAdapter(DashboardActivity dashboardActivity) {
        this.activity = dashboardActivity;
    }

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
