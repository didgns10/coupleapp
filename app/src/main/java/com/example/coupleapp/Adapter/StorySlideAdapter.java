package com.example.coupleapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.example.coupleapp.Model.StoryVIewData;
import com.example.coupleapp.R;

import java.util.ArrayList;

public class StorySlideAdapter extends PagerAdapter {

    private LayoutInflater inflater;
    private ArrayList<StoryVIewData> mList;
    private Context context;

    private ImageView imgv_detail;
    private ImageButton imgbt_delete;
    private TextView tv_cnt;

    // 해당 context가 자신의 context 객체와 똑같이 되도록 생성자를 만듬

    public StorySlideAdapter(ArrayList<StoryVIewData> mList, Context context) {
        this.mList = mList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        // object를 LinearLayout 형태로 형변환했을 때 view와 같은지 여부를 반환
//        return view == ((LinearLayout)object); 으로 했을때 오류 나서 View 로 바꿈..
        return view == ((View)object);
    }

    // 각각의 item을 인스턴스 화
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        //초기화
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.activity_story_detail_slide, container, false);

        imgv_detail = v.findViewById(R.id.imgv_detail);
        imgbt_delete = v.findViewById(R.id.imgbt_delete);
        tv_cnt = v.findViewById(R.id.tv_cnt);

        String cnt = (position+1)+"/"+mList.size();
        tv_cnt.setText(cnt);

        Glide.with(context).load(mList.get(position).getImg()).into(imgv_detail);
        container.addView(v);
        return v;
    }

    //할당을 해제
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.invalidate();
//        super.destroyItem(container, position, object);
    }


}
