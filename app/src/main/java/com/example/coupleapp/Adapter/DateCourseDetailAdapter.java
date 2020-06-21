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
import com.example.coupleapp.Model.DateTotalModel.DateCourseData;
import com.example.coupleapp.R;

import java.util.ArrayList;

public class DateCourseDetailAdapter extends RecyclerView.Adapter<DateCourseDetailAdapter.ViewHolder> {

    private ArrayList<DateCourseData> mList;
    private Context context;

    public DateCourseDetailAdapter(ArrayList<DateCourseData> mList, Context context) {
        this.mList = mList;
        this.context = context;
    }

    @NonNull
    @Override
    public DateCourseDetailAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_course_detail,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DateCourseDetailAdapter.ViewHolder holder, final int position) {

        holder.tv_title.setText(mList.get(position).getSubdetailalt());
        holder.tv_overview.setText(mList.get(position).getSubdetailoverview());

        Glide.with(context).load(mList.get(position).getSubdetailimg()).into(holder.imgv);

        holder.constraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DatePlaceDetailActivity.class);
                intent.putExtra("title",mList.get(position).getSubdetailalt());
                intent.putExtra("contentid",mList.get(position).getSubcontentid()+"");
                intent.putExtra("tel","null");
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
        public TextView tv_title,tv_overview;
        public ImageView imgv;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            constraintLayout = itemView.findViewById(R.id.constraintlayout);
            tv_title = itemView.findViewById(R.id.tv_title);
            tv_overview = itemView.findViewById(R.id.tv_overview);
            imgv = itemView.findViewById(R.id.imgv);
        }
    }
}
