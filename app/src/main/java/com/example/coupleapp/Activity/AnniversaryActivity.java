package com.example.coupleapp.Activity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.coupleapp.Adapter.AnniversaryAdapter;
import com.example.coupleapp.Model.AnniversaryData;
import com.example.coupleapp.R;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;

public class AnniversaryActivity extends AppCompatActivity {


    private DrawerLayout mDrawerLayout;

    private RecyclerView rv;
    private ArrayList<AnniversaryData> mArrayList;             // 모델 클래스의 데이터를 받아 리사이클러뷰에 뿌리는 데 쓸 ArrayList
    private AnniversaryAdapter mAdapter;                       // 리사이클러뷰 어댑터

    private SharedPreferences sf;
    private SharedPreferences sf_idx;
    private SharedPreferences sf_day;

    private static String IP_ADDRESS="13.125.232.78";  // 퍼블릭 IPv4 주소
    private static String TAG = "스토리";          // 로그에 사용할 태그
    private String mJsonString;                         // JSON 값을 저장할 String 변수
    private String couple_idx;
    private String email;
    private String date;

    private int year,month,day;

    private ImageButton btn_plus;
    private TextView tv_day;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anniversary);


        //로그인 저장 정보
        sf = getSharedPreferences("LOGIN",MODE_PRIVATE);
        email = sf.getString("et_email","");

        //커플 인덱스 번호가져오기
        sf_idx = getSharedPreferences("COPLE",MODE_PRIVATE);
        couple_idx = sf_idx.getString("cople_idx","");

        //커플 인덱스 번호가져오기
        sf_day = getSharedPreferences("DAY",MODE_PRIVATE);
        date = sf_day.getString("day","");

        btn_plus = findViewById(R.id.ibtn_add);
        tv_day = findViewById(R.id.tv_day);

        tv_day.setText(date);


        // 리사이클러뷰 선언
        rv = (RecyclerView) findViewById(R.id.recyclerview);
        // 프래그먼트기 때문에 context가 아니라 getActivity()를 쓴다!!!
        rv.setLayoutManager(new LinearLayoutManager(this));

        // 리사이클러뷰에 연결되서 데이터를 뿌릴 ArrayList 선언
        mArrayList = new ArrayList<>();

        // 프래그먼트에서 리사이클러뷰의 어댑터를 붙일 땐 context 쓰는 부분에 getActivity()를 쓴다
        mAdapter = new AnniversaryAdapter(mArrayList,AnniversaryActivity.this);
        rv.setAdapter(mAdapter);

        // 버튼 위에 표시되는 텍스트뷰에 mArrayList의 내용들을 뿌리는데 이것들을 전부 지우고
        mArrayList.clear();

        // 어댑터에 데이터가 변경됐다는 걸 알린다
        mAdapter.notifyDataSetChanged();

        btn_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AnniversaryActivity.this,AnniversaryAddActivity.class);
                startActivity(intent);
            }
        });

        //네비게이션 드로어 만드는 부분
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false); // 기존 title 지우기
        actionBar.setDisplayHomeAsUpEnabled(true); // 뒤로가기 버튼 만들기
        actionBar.setHomeAsUpIndicator(R.drawable.menu_image); //뒤로가기 버튼 이미지 지정


        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        //네이게이션 버튼 눌렀을때 처리해주는 ㄱ부분
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                menuItem.setChecked(true);
                mDrawerLayout.closeDrawers();

                int id = menuItem.getItemId();
                String title = menuItem.getTitle().toString();

                //메뉴바에 아이디랑 같은지 확인하고 그 메뉴를 클릭했을때 무슨 동작을 해줄건지에 대한 부분
                if(id == R.id.mainpage){
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.addFlags (Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(intent);
                }
                else if(id == R.id.date_course){/*
                    Intent intent = new Intent(getApplicationContext(), SelectActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.addFlags (Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(intent);*/
                }
                else if(id == R.id.story_album){
                }
                else if(id == R.id.couple_diary){
                }
                else if(id == R.id.couple_calender){
                }

                return true;
            }
        });
    }

    //햄버거 버튼 눌렀을때 드로어가 시작하게 하는 동작 부분
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:{ // 왼쪽 상단 버튼 눌렀을 때
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // 그 다음 AsyncTask 객체를 만들어 execute()한다
        mArrayList.clear();

        GetData task = new GetData();

        // execute() 사용 시 DB의 값을 JSON 형태로 가져오는 코드가 적힌 php 파일의 경로를 적어
        // AsyncTask로 값들을 JSON 형태로 가져올 수 있게 한다
        task.execute( "http://" + IP_ADDRESS + "/anniversary_view.php?couple_idx="+couple_idx, "");


    }
    /* HTTPUrlConnection을 써서 POST 방식으로 phpmyadmin DB에서 값들을 가져오는 AsyncTask 클래스 정의 */
    private class GetData extends AsyncTask<String, Void, String> {

        ProgressDialog progressDialog;
        String errorString = null;

        /* AsyncTask 작업 시작 전에 UI 처리할 내용을 정의하는 함수 */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

/*            // 프래그먼트에 프로그레스 다이얼로그를 띄우고, 값이 가져와지는 동안 기다리라는 메시지를 띄운다
            // 마찬가지로 프래그먼트를 쓰기 때문에 context 대신 getActivity() 사용
            progressDialog = ProgressDialog.show(StoryVIewActivity.this,
                    "Please Wait",
                    null,
                    true,
                    true);*/
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

        String TAG_JSON="anniversary";                         // JSON 배열의 이름

        /* DB 컬럼명을 적는 String 변수 */
        String TAG_IDX = "idx";     // 밑으로 3개는
        String TAG_DATE = "datef";
        String TAG_ANNIVERSARY = "anniversaryf";


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
                String idx = item.getString(TAG_IDX);   // 운동 이름
                String date = item.getString(TAG_DATE);
                String anniversary = item.getString(TAG_ANNIVERSARY);


                String date1[] = date.split("-");

                Log.e("로그",date1[0]+"-"+date1[1]+"-"+date1[2]);
                year = Integer.parseInt(date1[0]);
                month = Integer.parseInt(date1[1]);
                day  = Integer.parseInt(date1[2]);
                // 데이터 모델 클래스 객체 선언 후 settter()로 컬럼에서 값 추출

                long now = System.currentTimeMillis();
                Date mDate = new Date(now);
                SimpleDateFormat simpleDate = new SimpleDateFormat("yyyy-MM-dd");
                String getTime = simpleDate.format(mDate);

                Date today = null;
                Date day2 = null;

                try{
                    today = simpleDate.parse(getTime);
                    day2 = simpleDate.parse(date);
                }catch (ParseException e){
                    e.printStackTrace();
                }
                int compare = today.compareTo(day2);
                if(compare<0){
                    AnniversaryData anniversaryData = new AnniversaryData();

                    anniversaryData.setIdx(idx);
                    anniversaryData.setDate(date);
                    anniversaryData.setAnniversary(anniversary);
                    anniversaryData.setDday("D"+caldate(year,month-1,day+1)+"일");


                    // 데이터 모델 클래스 객체를 리사이클러뷰에 연결된 ArrayList에 삽입
                    mArrayList.add(anniversaryData);

                    // ArrayList에 변동이 생겼으니 어댑터에 알림
                    mAdapter.notifyDataSetChanged();
                }else if(compare>0){

                }else{
                    AnniversaryData anniversaryData = new AnniversaryData();

                    anniversaryData.setIdx(idx);
                    anniversaryData.setDate(date);
                    anniversaryData.setAnniversary(anniversary);
                    anniversaryData.setDday("D-day");


                    // 데이터 모델 클래스 객체를 리사이클러뷰에 연결된 ArrayList에 삽입
                    mArrayList.add(anniversaryData);

                    // ArrayList에 변동이 생겼으니 어댑터에 알림
                    mAdapter.notifyDataSetChanged();
                }
            }

        } catch (JSONException e) {
            // 에러 뜨면 결과 출력
            Log.e(TAG, "showResult : ", e);
        }

        Collections.sort(mArrayList);
        mAdapter.notifyDataSetChanged();

    }   // showResult() end

    //날짜를 계산해주는 함수식
    public int caldate(int myear, int mmonth, int mday) {
        try {
            Calendar today = Calendar.getInstance(); //현재 오늘 날짜
            Calendar dday = Calendar.getInstance();


            dday.set(myear,mmonth,mday);// D-day의 날짜를 입력합니다.
            long day = dday.getTimeInMillis()/86400000;
            // 각각 날의 시간 값을 얻어온 다음
            // ( 1일의 값(86400000 = 24시간 * 60분 * 60초 * 1000(1초값) ) )

            long tday = today.getTimeInMillis()/86400000;
            String str = today.getTime().toString();
            String strs = dday.getTime().toString();

            Log.e("로그",str);
            Log.e("로그",strs);
            long count = tday - day; // 오늘 날짜에서 dday 날짜를 빼주게 됩니다.

            return (int) count+1; // 날짜는 하루 + 시켜줘야합니다.
        }
        catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }
}
