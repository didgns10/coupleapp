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
import com.example.coupleapp.Activity.DateCourse.CourseRecommendData;
import com.example.coupleapp.Activity.DateCourse.DateCourseDetailActivity;
import com.example.coupleapp.R;

import java.util.ArrayList;

public class DateCourseRecommendAdapter extends RecyclerView.Adapter<DateCourseRecommendAdapter.ViewHolder> {

    private ArrayList<CourseRecommendData> mList;
    private Context context;

    public DateCourseRecommendAdapter(ArrayList<CourseRecommendData> mList, Context context) {
        this.mList = mList;
        this.context = context;
    }

    @NonNull
    @Override
    public DateCourseRecommendAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_course_recommend,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DateCourseRecommendAdapter.ViewHolder holder, final int position) {

        holder.tv_title.setText(mList.get(position).getTitle());

        Glide.with(context).load(mList.get(position).getImg()).into(holder.imgv);

        holder.constraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DateCourseDetailActivity.class);
                intent.putExtra("contentid",mList.get(position).getContentid()+"");
                intent.putExtra("title",mList.get(position).getTitle());
                intent.putExtra("contenttypeid","25");
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
