package com.example.coupleapp.Activity.DateCourse;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewpager.widget.ViewPager;


import com.example.coupleapp.Adapter.DateDetailViewPagerAdapter;
import com.example.coupleapp.Model.DateDetailModel.DateDetail;
import com.example.coupleapp.Model.DateDetailModel.DateDetailInterface;
import com.example.coupleapp.Model.DateDetailModel.DetailApiUtils;
import com.example.coupleapp.Model.DateImgModel.DateImage;
import com.example.coupleapp.Model.DateImgModel.DateImgInterface;
import com.example.coupleapp.Model.DateImgModel.ImgApiUtils;
import com.example.coupleapp.R;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DatePlaceDetailActivity extends AppCompatActivity {

    //이미지 뷰페이저 선언
    private ArrayList<String> imageList;
    private ViewPager viewPager;

    //각 아이템별 선언
    private TextView tv_title,tv_detail,tv_rate,tv_review_count,tv_adr,tv_tel;
    private ConstraintLayout constraintlayout;

    private String contentid,tel,contenttypeid,title,rate="0.0",review_count="0";

    private DateDetailInterface service;
    private DateImgInterface img_service;

    private static String IP_ADDRESS="13.125.232.78";  // 퍼블릭 IPv4 주소
    private static String TAG = "스토리";          // 로그에 사용할 태그
    private String mJsonString;                         // JSON 값을 저장할 String 변수

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_date_place_detail);

        //인텐트 받는거
        contentid = getIntent().getStringExtra("contentid");
        tel = getIntent().getStringExtra("tel");
        contenttypeid = getIntent().getStringExtra("contenttypeid");
        Log.e("콘텐트",contentid);
        Log.e("콘텐트",contenttypeid);

        //아이템 선언
        tv_title = findViewById(R.id.tv_title);
        tv_detail = findViewById(R.id.tv_detail);
        tv_rate = findViewById(R.id.tv_rate);
        tv_review_count = findViewById(R.id.tv_review_count);
        tv_adr = findViewById(R.id.tv_adr);
        tv_tel = findViewById(R.id.tv_tel);
        constraintlayout = findViewById(R.id.constraintlayout);

        imageList = new ArrayList<>();

        //뷰페이저 선언
        viewPager = findViewById(R.id.viewPager);
        viewPager.setClipToPadding(false);

        service = DetailApiUtils.getSOService();
        img_service = ImgApiUtils.getSOService();

        loadImg();
        loadPlace();


        constraintlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DatePlaceDetailActivity.this,DatePlaceReviewActivity.class);
                intent.putExtra("contentid",contentid);
                intent.putExtra("contenttypeid",contenttypeid);
                intent.putExtra("title",title);
                intent.putExtra("rate",rate);
                intent.putExtra("review_count",review_count);
                startActivity(intent);
            }
        });

    }
    @Override
    protected void onResume() {
        super.onResume();
        GetData task = new GetData();

        // execute() 사용 시 DB의 값을 JSON 형태로 가져오는 코드가 적힌 php 파일의 경로를 적어
        // AsyncTask로 값들을 JSON 형태로 가져올 수 있게 한다
        task.execute( "http://" + IP_ADDRESS + "/review_count.php?contentid="+contentid, "");
    }

    public void loadImg(){
        //음식점 사진일때만 다르기때문에 이렇게 줘야된다.
        if(contenttypeid.equals("39")) {
            img_service.getAnswers(contentid,"N").enqueue(new Callback<DateImage>() {
                @Override
                public void onResponse(Call<DateImage> call, Response<DateImage> response) {
                    if (response.isSuccessful()) {
                        Log.e("리스포1", "posts loaded from API");
                        Log.e("리스포1", response + "");
                        Log.e("리스포1", response.body().getResponse().getBody().getTotalCount() + "");


                        imageList.clear();
                        for (int i = 0; i < response.body().getResponse().getBody().getItems().getItem().size(); i++) {
                            imageList.add(response.body().getResponse().getBody().getItems().getItem().get(i).getOriginimgurl());

                        }

                        viewPager.setAdapter(new DateDetailViewPagerAdapter(DatePlaceDetailActivity.this, imageList));


                        Log.e("콘텐츠",imageList.size()+"");
                    } else {
                        int statusCode = response.code();
                        // handle request errors depending on status code
                        Log.e("리스포1", statusCode + "");

                    }
                }

                @Override
                public void onFailure(Call<DateImage> call, Throwable t) {

                    imageList.add("https://encrypted-tbn0.gstatic.com/images?q=tbn%3AANd9GcQKRPBzo-of-qNwvQpKgJEmAAYSQwwSAiksVY4sd6x-X2jI6-Zp&usqp=CAU");
                    viewPager.setAdapter(new DateDetailViewPagerAdapter(DatePlaceDetailActivity.this, imageList));
                }
            });
        }else{
            img_service.getAnswers(contentid,"Y").enqueue(new Callback<DateImage>() {
                @Override
                public void onResponse(Call<DateImage> call, Response<DateImage> response) {
                    if (response.isSuccessful()) {
                        Log.e("리스포", "posts loaded from API");
                        Log.e("리스포", response + "");


                        imageList.clear();
                        for (int i = 0; i < response.body().getResponse().getBody().getItems().getItem().size(); i++) {
                            imageList.add(response.body().getResponse().getBody().getItems().getItem().get(i).getOriginimgurl());

                        }

                        viewPager.setAdapter(new DateDetailViewPagerAdapter(DatePlaceDetailActivity.this, imageList));
                    } else {
                        int statusCode = response.code();
                        // handle request errors depending on status code
                        Log.e("리스포", statusCode + "");
                    }
                }

                @Override
                public void onFailure(Call<DateImage> call, Throwable t) {

                    imageList.add("https://encrypted-tbn0.gstatic.com/images?q=tbn%3AANd9GcQKRPBzo-of-qNwvQpKgJEmAAYSQwwSAiksVY4sd6x-X2jI6-Zp&usqp=CAU");
                    viewPager.setAdapter(new DateDetailViewPagerAdapter(DatePlaceDetailActivity.this, imageList));
                }
            });
        }
    }


    public void loadPlace(){
        service.getAnswers(contentid).enqueue(new Callback<DateDetail>() {
            @Override
            public void onResponse(Call<DateDetail> call, Response<DateDetail> response) {
                if(response.isSuccessful()) {
                    Log.e("리스포", "posts loaded from API");
                    Log.e("리스포",response+"");

                    title = response.body().getResponse().getBody().getItems().getItem().getTitle();
                    tv_title.setText(response.body().getResponse().getBody().getItems().getItem().getTitle());
                    tv_adr.setText(response.body().getResponse().getBody().getItems().getItem().getAddr1());
                    String detail = response.body().getResponse().getBody().getItems().getItem().getOverview();
                    String detail1 = detail.replaceAll("<br>","");
                    String detail2 =detail1.replaceAll("<br />","");
                    tv_detail.setText(detail2);
                    if(tel.equals("null")){
                        tv_tel.setText("등록된 번호가 없습니다.");
                    }else {
                        tv_tel.setText(tel);
                    }

                }else {
                    int statusCode  = response.code();
                    // handle request errors depending on status code
                    Log.e("리스포",statusCode+"");
                }
            }

            @Override
            public void onFailure(Call<DateDetail> call, Throwable t) {

            }
        });
    }
    /* HTTPUrlConnection을 써서 POST 방식으로 phpmyadmin DB에서 값들을 가져오는 AsyncTask 클래스 정의 */
    private class GetData extends AsyncTask<String, Void, String> {

        ProgressDialog progressDialog;
        String errorString = null;

        /* AsyncTask 작업 시작 전에 UI 처리할 내용을 정의하는 함수 */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        /* AsyncTask 작업 종료 후 UI 처리할 내용을 정의하는 함수 */
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

     /*       // 프로그레스 다이얼로그를 죽이고
            progressDialog.dismiss();*/

            // doInBackground()의 리턴값이 담긴 result를 버튼 밑 텍스트뷰에 setText()해서 JSON 형태로 받아온 값들을 출력
//            mTextViewResult.setText(result);
            Log.e(TAG, "response - " + result);

            // 결과가 없으면 에러 때문에 못 받아온 거니까 에러 문구를 버튼 밑 텍스트뷰에 출력
            if (result == null) {
//                mTextViewResult.setText(errorString);
            } else {
                // 결과가 있다면 버튼 위 텍스트뷰에 JSON 데이터들을 텍스트뷰 형태에 맞게 출력한다
                mJsonString = result;

                if(result.equals("1")){

                }else {
                    String array[] = result.split(":");

                    rate = array[0];
                    review_count = array[1];
                    float flo ;
                    flo = Float.parseFloat(array[0]);

                    String rate = String.format("%.2f",flo);

                    tv_rate.setText(rate);
                    tv_review_count.setText(array[1]);
                }

            }
        }

        /* AsyncTask가 수행할 작업 내용을 정의하는 함수 */
        @Override
        protected String doInBackground(String... params) {

            String serverURL = params[0];       // IP_ADDRESS에 적은 퍼블릭 IPv4 주소를 저장할 변수
//            Log.e("params[0] : ", params[0].toString());
            String postParameters = params[1];  // HttpUrlConnection 결과로 얻은 Request body에 담긴 내용들을 저장할 변수
//            Log.e("params[1] : ", params[1].toString());


            try {

                URL url = new URL(serverURL);
                Log.e("확인",serverURL);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();


                httpURLConnection.setReadTimeout(5000);
                httpURLConnection.setConnectTimeout(5000);
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoInput(true);
                httpURLConnection.connect();


                OutputStream outputStream = httpURLConnection.getOutputStream();
                outputStream.write(postParameters.getBytes("UTF-8"));
                outputStream.flush();
                outputStream.close();


                int responseStatusCode = httpURLConnection.getResponseCode();
                Log.d(TAG, "response code - " + responseStatusCode);

                InputStream inputStream;
                if(responseStatusCode == HttpURLConnection.HTTP_OK) {
                    inputStream = httpURLConnection.getInputStream();
                }
                else{
                    inputStream = httpURLConnection.getErrorStream();
                }


                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                StringBuilder sb = new StringBuilder();
                String line;

                while((line = bufferedReader.readLine()) != null){
                    sb.append(line);
                }

                bufferedReader.close();

                return sb.toString().trim();


            } catch (Exception e) {

                Log.d(TAG, "GetData : Error ", e);
                errorString = e.toString();

                return null;
            }

        }
    }

}
