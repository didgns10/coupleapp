package com.example.coupleapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.coupleapp.Model.SelectImageData;
import com.example.coupleapp.R;

import java.util.ArrayList;

public class SelectImageAdapter extends RecyclerView.Adapter<SelectImageAdapter.ViewHolder>{

    private ArrayList<SelectImageData> mList;
    private Context context;

    public SelectImageAdapter(ArrayList<SelectImageData> mList, Context context) {
        this.mList = mList;
        this.context = context;
    }

    @NonNull
    @Override
    public SelectImageAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_selectimg,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SelectImageAdapter.ViewHolder holder, int position) {

        holder.imgv.setImageBitmap(mList.get(position).getBitmap());

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView imgv;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imgv = itemView.findViewById(R.id.imgv);
        }
    }
}
