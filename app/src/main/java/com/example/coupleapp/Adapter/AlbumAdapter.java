package com.example.coupleapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.coupleapp.Model.AlbumData;
import com.example.coupleapp.Model.StoryThumData;
import com.example.coupleapp.R;

import java.util.ArrayList;

public class AlbumAdapter extends RecyclerView.Adapter<AlbumAdapter.ViewHolder> {

    private ArrayList<AlbumData> mList;
    private Context context;

    public AlbumAdapter(ArrayList<AlbumData> mList, Context context) {
        this.mList = mList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_album,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Glide.with(context).load(mList.get(position).getAlbumtitleimg())
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true).into(holder.imgv_album);
        holder.tv_album_title.setText(mList.get(position).getAlbumtitle());

/*        holder.constraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });*/

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView imgv_album;
        public TextView tv_album_title;
        public ConstraintLayout constraintLayout;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgv_album = (ImageView)itemView.findViewById(R.id.imgv_album);
            tv_album_title = (TextView)itemView.findViewById(R.id.tv_album_title);
            constraintLayout = (ConstraintLayout) itemView.findViewById(R.id.constraintlayout);


        }
    }
}
