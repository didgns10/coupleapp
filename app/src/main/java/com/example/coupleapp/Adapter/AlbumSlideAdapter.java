package com.example.coupleapp.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.coupleapp.Model.AlbumViewData;
import com.example.coupleapp.Model.StoryVIewData;
import com.example.coupleapp.R;

import java.util.ArrayList;

public class AlbumSlideAdapter extends PagerAdapter {

    private LayoutInflater inflater;
    private ArrayList<AlbumViewData> mList;
    private Context context;

    private ImageView imgv_detail;
    private TextView tv_cnt;

    private String img_idx;
    private String title;
    private String img_day;

    public AlbumSlideAdapter(ArrayList<AlbumViewData> mList, Context context) {
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
    public Object instantiateItem(ViewGroup container, final int position) {
        //초기화
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.activity_album_detail_slide, container, false);

        imgv_detail = v.findViewById(R.id.imgv_detail);
        tv_cnt = v.findViewById(R.id.tv_cnt);

        String cnt = (position+1)+"/"+mList.size();
        tv_cnt.setText(cnt);

        img_idx = mList.get(position).getImg_idx();

        Log.e("스토리2",img_idx);
        Log.e("스토리2",mList.get(position).toString());

        Glide.with(context).load(mList.get(position).getImg())
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(imgv_detail);

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
