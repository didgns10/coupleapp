package com.example.coupleapp.Adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.coupleapp.Model.MessageData;
import com.example.coupleapp.R;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.content.Context.MODE_PRIVATE;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {

    private ArrayList<MessageData> mList;
    private Context context;


    private SharedPreferences sf;
    private SharedPreferences sf_idx;

    private String email;
    private String couple_idx;

    public MessageAdapter(ArrayList<MessageData> mList, Context context) {
        this.mList = mList;
        this.context = context;
    }

    @NonNull
    @Override
    public MessageAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageAdapter.ViewHolder holder, int position) {
        //로그인 저장 정보
        sf = context.getSharedPreferences("LOGIN",MODE_PRIVATE);
        email = sf.getString("et_email","");

        //커플 인덱스 번호가져오기
        sf_idx =context.getSharedPreferences("COPLE",MODE_PRIVATE);
        couple_idx = sf_idx.getString("cople_idx","");

        if(mList.get(position).getServer().equals("send")){

            holder.tv_mymessage.setVisibility(View.VISIBLE);
            holder.tv_mydatetime.setVisibility(View.VISIBLE);
            holder.cimgv_profile.setVisibility(View.GONE);
            holder.tv_message.setVisibility(View.GONE);
            holder.tv_datetime.setVisibility(View.GONE);
            holder.tv_name.setVisibility(View.GONE);

            holder.tv_mymessage.setText(mList.get(position).getMessage());
            holder.tv_mydatetime.setText(mList.get(position).getDatetime());


        }else if(mList.get(position).getServer().equals("receive")){

            holder.tv_mymessage.setVisibility(View.GONE);
            holder.tv_mydatetime.setVisibility(View.GONE);
            holder.cimgv_profile.setVisibility(View.VISIBLE);
            holder.tv_message.setVisibility(View.VISIBLE);
            holder.tv_datetime.setVisibility(View.VISIBLE);
            holder.tv_name.setVisibility(View.VISIBLE);

            Glide.with(context).load(mList.get(position).getOpp_profile_img())
                    /*.diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)*/.into(holder.cimgv_profile);
            holder.tv_message.setText(mList.get(position).getMessage());
            holder.tv_datetime.setText(mList.get(position).getDatetime());
            holder.tv_name.setText(mList.get(position).getName());

        }else if(mList.get(position).getServer().equals("mysql")){
            if(mList.get(position).getEmail().equals(email)){

                holder.tv_mymessage.setVisibility(View.VISIBLE);
                holder.tv_mydatetime.setVisibility(View.VISIBLE);
                holder.cimgv_profile.setVisibility(View.GONE);
                holder.tv_message.setVisibility(View.GONE);
                holder.tv_datetime.setVisibility(View.GONE);
                holder.tv_name.setVisibility(View.GONE);

                holder.tv_mymessage.setText(mList.get(position).getMessage());
                holder.tv_mydatetime.setText(mList.get(position).getDatetime());

            }else{
                holder.tv_mymessage.setVisibility(View.GONE);
                holder.tv_mydatetime.setVisibility(View.GONE);
                holder.cimgv_profile.setVisibility(View.VISIBLE);
                holder.tv_message.setVisibility(View.VISIBLE);
                holder.tv_datetime.setVisibility(View.VISIBLE);
                holder.tv_name.setVisibility(View.VISIBLE);

                Glide.with(context).load(mList.get(position).getOpp_profile_img())
                        /*.diskCacheStrategy(DiskCacheStrategy.NONE)
                        .skipMemoryCache(true)*/.into(holder.cimgv_profile);
                holder.tv_message.setText(mList.get(position).getMessage());
                holder.tv_datetime.setText(mList.get(position).getDatetime());
                holder.tv_name.setText(mList.get(position).getName());
            }
        }

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public CircleImageView cimgv_profile;
        public TextView tv_name,tv_message,tv_datetime,tv_mymessage,tv_mydatetime;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            cimgv_profile = itemView.findViewById(R.id.cimgv_profile);
            tv_name = itemView.findViewById(R.id.tv_name);
            tv_message = itemView.findViewById(R.id.tv_message);
            tv_datetime = itemView.findViewById(R.id.tv_datetime);
            tv_mymessage = itemView.findViewById(R.id.tv_mymessage);
            tv_mydatetime = itemView.findViewById(R.id.tv_mydatetime);

        }
    }
}
