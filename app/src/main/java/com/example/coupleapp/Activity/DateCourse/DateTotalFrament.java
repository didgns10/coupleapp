package com.example.coupleapp.Activity.DateCourse;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.example.coupleapp.Activity.Map.Gloval;
import com.example.coupleapp.Adapter.DatePlaceAdapter;
import com.example.coupleapp.Model.DateTotalModel.ApiUtils;
import com.example.coupleapp.Model.DateTotalModel.Arrange;
import com.example.coupleapp.Model.DateTotalModel.Date1;
import com.example.coupleapp.Model.DateTotalModel.DateInterface;
import com.example.coupleapp.Model.DateTotalModel.DatePlaceData;
import com.example.coupleapp.R;

import java.util.ArrayList;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;


public class DateTotalFrament extends Fragment {
    private RecyclerView rv;
    private static String IP_ADDRESS="13.125.232.78";  // 퍼블릭 IPv4 주소
    private static String TAG = "스토리";          // 로그에 사용할 태그

    private ArrayList<DatePlaceData> mArrayList;             // 모델 클래스의 데이터를 받아 리사이클러뷰에 뿌리는 데 쓸 ArrayList
    private DatePlaceAdapter mAdapter;                       // 리사이클러뷰 어댑터
    private String mJsonString1;                         // JSON 값을 저장할 String 변수

    private SharedPreferences sf;
    private SharedPreferences sf_idx;

    private String email;
    private String couple_idx;

    RequestQueue queue ;
    private TextView tv_test;

    private double lat ;
    private double lon ;

    private DateInterface service;

    public DateTotalFrament() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //로그인 저장 정보
        sf = getActivity().getSharedPreferences("LOGIN",MODE_PRIVATE);
        email = sf.getString("et_email","");

        //커플 인덱스 번호가져오기
        sf_idx =getActivity().getSharedPreferences("COPLE",MODE_PRIVATE);
        couple_idx = sf_idx.getString("cople_idx","");

        // 프래그먼트에 리사이클러뷰, 텍스트뷰 등을 표시하려면 먼저 ViewGroup 클래스의 객체를 만들고, fragment의 xml 파일을 inflate해야 한다
        ViewGroup viewGroup = (ViewGroup) inflater.inflate(R.layout.fragment_date_total, container, false);

        if (Gloval.getState() != null) {
            lat = Gloval.getLatitude();
            lon = Gloval.getLongitude();
        } else {
            lat = 37.57;
            lon = 126.98;
        }


        // 리사이클러뷰 선언
        rv = (RecyclerView) viewGroup.findViewById(R.id.rv_tourist);


        // 프래그먼트기 때문에 context가 아니라 getActivity()를 쓴다!!!
        rv.setLayoutManager(new LinearLayoutManager(getActivity()));

        // 프래그먼트기 때문에 구분선을 줄 때 context 부분에 getActivity()를 넣어야 한다
        // 그냥 getActivity()만 넣으면 노란 박스 쳐져서 안 보이게 하려고 getActivity()를 다르게 표현함
        rv.addItemDecoration(new DividerItemDecoration(Objects.requireNonNull(getActivity()), DividerItemDecoration.VERTICAL));

        // 리사이클러뷰에 연결되서 데이터를 뿌릴 ArrayList 선언
        mArrayList = new ArrayList<>();

        // 프래그먼트에서 리사이클러뷰의 어댑터를 붙일 땐 context 쓰는 부분에 getActivity()를 쓴다
        mAdapter = new DatePlaceAdapter(mArrayList,getActivity());
        rv.setAdapter(mAdapter);

        // 버튼 위에 표시되는 텍스트뷰에 mArrayList의 내용들을 뿌리는데 이것들을 전부 지우고
        mArrayList.clear();

        // 어댑터에 데이터가 변경됐다는 걸 알린다
        mAdapter.notifyDataSetChanged();
        service = ApiUtils.getSOService();


        // getPlace();

        return viewGroup;
    }

    @Override
    public void onResume() {
        super.onResume();
        loadAnswers1();
    }

    public void loadAnswers1(){
        service.getAnswers(12,lon,lat, Arrange.getArrange()).enqueue(new Callback<Date1>() {
            @Override
            public void onResponse(Call<Date1> call, Response<Date1> response) {
                if(response.isSuccessful()) {
                    Log.e("리스포", "posts loaded from API");
                    Log.e("리스포",response+"");
                    Log.e("리스포", response.body().getResponse1().getBody().getItems().getItem().size()+"");


                    for(int i=0; i< response.body().getResponse1().getBody().getItems().getItem().size() ; i++ ){
                        DatePlaceData datePlaceData = new DatePlaceData();
                        datePlaceData.setTitle(response.body().getResponse1().getBody().getItems().getItem().get(i).getTitle());
                        datePlaceData.setAddr1(response.body().getResponse1().getBody().getItems().getItem().get(i).getAddr1());
                        datePlaceData.setFirstimage(response.body().getResponse1().getBody().getItems().getItem().get(i).getFirstimage());
                        datePlaceData.setTel(response.body().getResponse1().getBody().getItems().getItem().get(i).getTel());
                        datePlaceData.setFirstimage2(response.body().getResponse1().getBody().getItems().getItem().get(i).getFirstimage2());
                        datePlaceData.setReadcount(response.body().getResponse1().getBody().getItems().getItem().get(i).getReadcount()+"");
                        datePlaceData.setContentid(response.body().getResponse1().getBody().getItems().getItem().get(i).getContentid()+"");
                        datePlaceData.setContenttypeid(response.body().getResponse1().getBody().getItems().getItem().get(i).getContenttypeid()+"");

                        mArrayList.add(datePlaceData);
                        mAdapter.notifyDataSetChanged();
                    }


                }else {
                    int statusCode  = response.code();
                    // handle request errors depending on status code
                    Log.e("리스포",statusCode+"");
                }
            }

            @Override
            public void onFailure(Call<Date1> call, Throwable t) {
                Log.e("리스포", t+"");


            }
        });
    }


}