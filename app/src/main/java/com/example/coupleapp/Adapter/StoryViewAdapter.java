package com.example.coupleapp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.coupleapp.Activity.StoryDetailActivity;
import com.example.coupleapp.Model.StoryVIewData;
import com.example.coupleapp.R;

import java.util.ArrayList;

public class StoryViewAdapter extends RecyclerView.Adapter<StoryViewAdapter.ViewHolder> {

    private ArrayList<StoryVIewData> mList;
    private Context context;

    public StoryViewAdapter(ArrayList<StoryVIewData> mList, Context context) {
        this.mList = mList;
        this.context = context;
    }

    @NonNull
    @Override
    public StoryViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_storyview,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final StoryViewAdapter.ViewHolder holder, final int position) {

        Glide.with(context).load(mList.get(position).getImg())
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(holder.imgv_storyview);
        holder.tv_title.setText(mList.get(position).getTitle());
        holder.tv_day.setText(mList.get(position).getImg_day());
        holder.tv_img_idx.setText(mList.get(position).getImg_idx());
        holder.tv_img.setText(mList.get(position).getImg());

        holder.imgv_storyview.setOnClickListener(new View.OnClickListener() {
           @Override
            public void onClick(View v) {
               Intent intent = new Intent(context, StoryDetailActivity.class);
               intent.putExtra("position",position+"");
               intent.putExtra("img_day",holder.tv_day.getText());
               intent.putExtra("title",holder.tv_title.getText());
               intent.putExtra("img_idx",holder.tv_img_idx.getText());
               intent.putExtra("img",holder.tv_img.getText());
               context.startActivity(intent);

           }
       });

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tv_title,tv_day,tv_img_idx,tv_img;
        public ImageView imgv_storyview;
        public CardView cardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imgv_storyview = (ImageView)itemView.findViewById(R.id.imgv_storyview);
            tv_title = (TextView)itemView.findViewById(R.id.tv_title);
            tv_day = (TextView)itemView.findViewById(R.id.tv_day);
            tv_img_idx = (TextView)itemView.findViewById(R.id.tv_img_idx);
            tv_img = (TextView)itemView.findViewById(R.id.tv_img);
            cardView =(CardView) itemView.findViewById(R.id.card_view);
        }
    }
}
