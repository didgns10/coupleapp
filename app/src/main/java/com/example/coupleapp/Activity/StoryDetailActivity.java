package com.example.coupleapp.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.coupleapp.Adapter.StorySlideAdapter;
import com.example.coupleapp.Model.StoryVIewData;
import com.example.coupleapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class StoryDetailActivity extends AppCompatActivity {

    private StorySlideAdapter adapter;
    private ViewPager viewPager;
    private ArrayList<StoryVIewData> mList;

    private static String IP_ADDRESS="13.125.232.78";  // 퍼블릭 IPv4 주소
    private static String TAG = "스토리";          // 로그에 사용할 태그
    private String mJsonString;                         // JSON 값을 저장할 String 변수

    private SharedPreferences sf;
    private SharedPreferences sf_idx;

    private String email;
    private String couple_idx;
    private String img;
    private String img_idx;
    private String position;
    private String title;
    private String img_day;
    private int posi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story_detail);

        //로그인 저장 정보
        sf = getSharedPreferences("LOGIN",MODE_PRIVATE);
        email = sf.getString("et_email","");

        //커플 인덱스 번호가져오기
        sf_idx =getSharedPreferences("COPLE",MODE_PRIVATE);
        couple_idx = sf_idx.getString("cople_idx","");

        //인텐트로 값 받아오기
        img = getIntent().getStringExtra("img");
        //프로그램 번호 가져오는 부분
        img_idx = getIntent().getStringExtra("img_idx");
        img_day = getIntent().getStringExtra("img_day");
        title = getIntent().getStringExtra("title");
        position = getIntent().getStringExtra("position");


        Log.e("스토리1",img);
        Log.e("스토리1",img_idx);
        Log.e("스토리1",img_day);
        Log.e("스토리1",title);
        Log.e("스토리1",couple_idx);
        Log.e("스토리1",position);


         posi = Integer.parseInt(position);


// 아까 만든 view
        viewPager = (ViewPager)findViewById(R.id.view);
        //adapter 초기화


        mList = new ArrayList<>();

        adapter = new StorySlideAdapter(mList,this);

        mList.clear();

        // 그 다음 AsyncTask 객체를 만들어 execute()한다
        GetData task = new GetData();

        // execute() 사용 시 DB의 값을 JSON 형태로 가져오는 코드가 적힌 php 파일의 경로를 적어
        // AsyncTask로 값들을 JSON 형태로 가져올 수 있게 한다
        task.execute( "http://" + IP_ADDRESS + "/StoryView.php?couple_idx="+couple_idx+"&title="+title+"&img_day="+img_day, "");

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


            // doInBackground()의 리턴값이 담긴 result를 버튼 밑 텍스트뷰에 setText()해서 JSON 형태로 받아온 값들을 출력
//            mTextViewResult.setText(result);
            Log.e(TAG, "response - " + result);

            // 결과가 없으면 에러 때문에 못 받아온 거니까 에러 문구를 버튼 밑 텍스트뷰에 출력
            if (result == null) {
//                mTextViewResult.setText(errorString);
            } else {
                // 결과가 있다면 버튼 위 텍스트뷰에 JSON 데이터들을 텍스트뷰 형태에 맞게 출력한다
                mJsonString = result;
                showResult();
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

    /* DB 테이블 컬럼의 값들을 JSON 형태로 받아와서 리사이클러뷰에 연결된 ArrayList에 박는 함수 */
    private void showResult() {

        String TAG_JSON="storyview";                         // JSON 배열의 이름

        /* DB 컬럼명을 적는 String 변수 */
        String TAG_TITLE = "title";                      // 정적 이미지(나중에 DB에서 이미지 뽑아올 것)
        String TAG_IMG = "img";     // 밑으로 3개는
        String TAG_IMG_IDX = "img_idx";
        String TAG_IMG_DAY = "img_day";

        try {

            // JSON 배열로 받아오기 위해 JSONObject, JSONArray 차례로 선언
            JSONObject jsonObject = new JSONObject(mJsonString);

            // JSONArray에는 root를 넣어서 root란 이름의 JSON 배열을 가져올 수 있도록 한다
            JSONArray jsonArray = jsonObject.getJSONArray(TAG_JSON);

            // for문으로 JSONArray의 길이만큼 반복해서 String 변수에 담는다
            for(int i = 0; i < jsonArray.length(); i++){

                JSONObject item = jsonArray.getJSONObject(i);

                // 컬럼의 값들을 getString()으로 받아와서 String 변수에 저장
                // 정적 이미지는 나중에 DB에서 받아와야 한다. 일단 기본 이미지 채워넣음
                String titie = item.getString(TAG_TITLE);
                String img = item.getString(TAG_IMG);   // 운동 이름
                String img_idx = item.getString(TAG_IMG_IDX);
                String img_day = item.getString(TAG_IMG_DAY);

                // 데이터 모델 클래스 객체 선언 후 settter()로 컬럼에서 값 추출
                StoryVIewData storyVIewData = new StoryVIewData();

                storyVIewData.setTitle(titie);
                storyVIewData.setImg(img);
                storyVIewData.setImg_day(img_day);
                storyVIewData.setImg_idx(img_idx);

                // 데이터 모델 클래스 객체를 리사이클러뷰에 연결된 ArrayList에 삽입
                mList.add(storyVIewData);

                // ArrayList에 변동이 생겼으니 어댑터에 알림
                adapter.notifyDataSetChanged();


            }
            viewPager.setAdapter(adapter);
            viewPager.setCurrentItem(posi);

        } catch (JSONException e) {
            // 에러 뜨면 결과 출력
            Log.e(TAG, "showResult : ", e);
        }

    }   // showResult() end
}

