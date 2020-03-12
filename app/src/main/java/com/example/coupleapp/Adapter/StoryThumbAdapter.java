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
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.coupleapp.Model.StoryThumData;
import com.example.coupleapp.R;

import java.util.ArrayList;

public class StoryThumbAdapter extends RecyclerView.Adapter<StoryThumbAdapter.ViewHolder> {

    private ArrayList<StoryThumData> mList;
    private Context context;

    public StoryThumbAdapter(ArrayList<StoryThumData> mList, Context context) {
        this.mList = mList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_story,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.tv_day.setText(mList.get(position).getStoryday());
        holder.tv_title.setText(mList.get(position).getStorytitle());

        Glide.with(context).load(mList.get(position).getStoryThumbimg())
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true).into(holder.img_thumbnail);

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                Intent intent = new Intent(context, TimerMain.class);
//                intent.putExtra("program","program");
//                intent.putExtra("num",holder.num.getText());
//                context.startActivity(intent);

            }
        });

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView img_thumbnail;
        private TextView tv_day,tv_title;
        private CardView cardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            img_thumbnail=(ImageView) itemView.findViewById(R.id.img_thumbnail);
            tv_day = (TextView) itemView.findViewById(R.id.tv_day);
            cardView = (CardView)itemView.findViewById(R.id.card_view);
            tv_title=(TextView)itemView.findViewById(R.id.tv_title);

        }
    }
}
