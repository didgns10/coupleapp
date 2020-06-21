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
import com.example.coupleapp.Activity.DateCourse.DateCourseDetailActivity;
import com.example.coupleapp.Model.DateTotalModel.DatePlaceData;
import com.example.coupleapp.R;

import java.util.ArrayList;

public class DateCourseAdapter extends RecyclerView.Adapter<DateCourseAdapter.ViewHolder> {

    private ArrayList<DatePlaceData> mList;
    private Context context;

    public DateCourseAdapter(ArrayList<DatePlaceData> mList, Context context) {
        this.mList = mList;
        this.context = context;
    }

    @NonNull
    @Override
    public DateCourseAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_course,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DateCourseAdapter.ViewHolder holder, final int position) {

        holder.tv_title.setText(mList.get(position).getTitle());

        Glide.with(context).load(mList.get(position).getFirstimage()).into(holder.imgv);

        holder.constraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, DateCourseDetailActivity.class);
                intent.putExtra("title",mList.get(position).getTitle()+"");
                intent.putExtra("contentid",mList.get(position).getContentid()+"");
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
        public TextView tv_title;
        public ImageView imgv;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            constraintLayout = itemView.findViewById(R.id.constraintlayout);
            tv_title = itemView.findViewById(R.id.tv_title);
            imgv = itemView.findViewById(R.id.imgv);
        }
    }
}
