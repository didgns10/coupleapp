package com.example.coupleapp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.coupleapp.Activity.DateCourse.DatePlaceDetailActivity;
import com.example.coupleapp.Model.DateTotalModel.DatePlaceData;
import com.example.coupleapp.R;

import java.util.ArrayList;

public class DatePlaceAdapter extends RecyclerView.Adapter<DatePlaceAdapter.ViewHolder> {

    private ArrayList<DatePlaceData> mList;
    private Context context;

    public DatePlaceAdapter(ArrayList<DatePlaceData> mList, Context context) {
        this.mList = mList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_dateplace,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {

        holder.tv_title.setText(mList.get(position).getTitle());
        holder.tv_site.setText(mList.get(position).getAddr1());
        holder.tv_tel.setText(mList.get(position).getTel());

        Glide.with(context).load(mList.get(position).getFirstimage()).into(holder.imgv);

        holder.constraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DatePlaceDetailActivity.class);
                intent.putExtra("contentid",mList.get(position).getContentid()+"");
                intent.putExtra("tel",mList.get(position).getTel()+"");
                intent.putExtra("contenttypeid",mList.get(position).getContenttypeid()+"");
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ConstraintLayout constraintLayout;
        public TextView tv_title,tv_tel,tv_site;
        public ImageView imgv;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            constraintLayout = itemView.findViewById(R.id.constraintlayout);
            tv_title = itemView.findViewById(R.id.tv_title);
            tv_tel = itemView.findViewById(R.id.tv_tel);
            tv_site = itemView.findViewById(R.id.tv_site);
            imgv = itemView.findViewById(R.id.imgv);
        }
    }
}
