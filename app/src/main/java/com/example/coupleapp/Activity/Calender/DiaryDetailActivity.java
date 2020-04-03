package com.example.coupleapp.Activity.Calender;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.coupleapp.R;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class DiaryDetailActivity extends AppCompatActivity {

    private SharedPreferences sf;
    private SharedPreferences sf_idx;
    private SharedPreferences sfc;
    private String email;
    private String couple_idx;
    private String title;
    private String content;
    private String idx;
    private String name;
    private String focus;

    private EditText et_add_comment;
    private ImageButton imageButton_back;
    private TextView tv_title,tv_date,tv_time,tv_name,tv_content,tv_post;
    private ScrollView scrollView;

    private RecyclerView rv;
    private ArrayList<DiaryCommentData> mArrayList;             // 모델 클래스의 데이터를 받아 리사이클러뷰에 뿌리는 데 쓸 ArrayList
    private DiaryCommentAdapter mAdapter;                       // 리사이클러뷰 어댑터

    private String day;
    private String date;
    private String time;

    private static String IP_ADDRESS="13.125.232.78";  // 퍼블릭 IPv4 주소
    private static String TAG = "스토리";          // 로그에 사용할 태그
    private String mJsonString;                         // JSON 값을 저장할 String 변수
    private String mJsonString2;                         // JSON 값을 저장할 String 변수

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diary_detail);

        //로그인 저장 정보
        sf = getSharedPreferences("LOGIN",MODE_PRIVATE);
        email = sf.getString("et_email","");

        //커플 인덱스 번호가져오기
        sf_idx =getSharedPreferences("COPLE",MODE_PRIVATE);
        couple_idx = sf_idx.getString("cople_idx","");

        //인텐트로 값 받아오기
        title = getIntent().getStringExtra("title");
        idx = getIntent().getStringExtra("idx");
        content = getIntent().getStringExtra("content");
        date = getIntent().getStringExtra("date");
        time = getIntent().getStringExtra("time");
        name = getIntent().getStringExtra("name");
        focus = getIntent().getStringExtra("focus");

        Log.e("focus",focus+"");

        tv_title = findViewById(R.id.tv_title);
        tv_date = findViewById(R.id.tv_date);
        tv_time = findViewById(R.id.tv_time);
        tv_name = findViewById(R.id.tv_name);
        tv_content = findViewById(R.id.tv_content);
        tv_post = findViewById(R.id.tv_post);
        scrollView = findViewById(R.id.scrollview);


        imageButton_back = findViewById(R.id.imageButton_back);

        et_add_comment = findViewById(R.id.et_add_comment);

        tv_title.setText(title);
        tv_date.setText(date);
        tv_time.setText(time);
        tv_name.setText(name);
        tv_content.setText(content);


        imageButton_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // 리사이클러뷰 선언
        rv = (RecyclerView) findViewById(R.id.recyclerview_content);
        // 프래그먼트기 때문에 context가 아니라 getActivity()를 쓴다!!!
        rv.setLayoutManager(new LinearLayoutManager(this));

        // 리사이클러뷰에 연결되서 데이터를 뿌릴 ArrayList 선언
        mArrayList = new ArrayList<>();

        // 프래그먼트에서 리사이클러뷰의 어댑터를 붙일 땐 context 쓰는 부분에 getActivity()를 쓴다
        mAdapter = new DiaryCommentAdapter(mArrayList,DiaryDetailActivity.this);
        rv.setAdapter(mAdapter);

        // 버튼 위에 표시되는 텍스트뷰에 mArrayList의 내용들을 뿌리는데 이것들을 전부 지우고
        mArrayList.clear();

        // 어댑터에 데이터가 변경됐다는 걸 알린다
        mAdapter.notifyDataSetChanged();

        tv_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String comment = et_add_comment.getText().toString();

                if(comment.equals("")){
                    Toast.makeText(DiaryDetailActivity.this,"내용을 입력해주세요.",Toast.LENGTH_SHORT).show();
                }else{
                    DiaryCommentData diaryCommentData = new DiaryCommentData();

                    //로그인 저장 정보
                    sfc = getSharedPreferences("CHAT",MODE_PRIVATE);
                    name = sfc.getString("name","");

                    long now = System.currentTimeMillis();
                    Date mDate = new Date(now);
                    SimpleDateFormat simpleDate = new SimpleDateFormat("MM-dd HH:mm");
                    String getTime = simpleDate.format(mDate);

/*                    diaryCommentData.setName(name);
                    diaryCommentData.setDate(getTime);
                    diaryCommentData.setComment(comment);

                    mArrayList.add(diaryCommentData);
                    mAdapter.notifyDataSetChanged();*/
                    GetData1 task1 = new GetData1();

                    // execute() 사용 시 DB의 값을 JSON 형태로 가져오는 코드가 적힌 php 파일의 경로를 적어
                    // AsyncTask로 값들을 JSON 형태로 가져올 수 있게 한다
                    task1.execute( "http://" + IP_ADDRESS + "/diary_comment_add.php?couple_idx="+couple_idx+"&diary_idx="+idx+"&name="+name+"&date="+getTime+"&comment="+comment, "");

                    GetData task = new GetData();

                    // execute() 사용 시 DB의 값을 JSON 형태로 가져오는 코드가 적힌 php 파일의 경로를 적어
                    // AsyncTask로 값들을 JSON 형태로 가져올 수 있게 한다
                    task.execute( "http://" + IP_ADDRESS + "/diary_comment_view.php?couple_idx="+couple_idx+"&diary_idx="+idx, "");

                    et_add_comment.setText("");
                    rv.scrollToPosition(mAdapter.getItemCount());
                    scrollView.fullScroll(ScrollView.FOCUS_DOWN);

                }
            }
        });
    }
    @Override
    protected void onResume() {
        super.onResume();
        // 그 다음 AsyncTask 객체를 만들어 execute()한다
        mArrayList.clear();

        GetData task = new GetData();

        // execute() 사용 시 DB의 값을 JSON 형태로 가져오는 코드가 적힌 php 파일의 경로를 적어
        // AsyncTask로 값들을 JSON 형태로 가져올 수 있게 한다
        task.execute( "http://" + IP_ADDRESS + "/diary_comment_view.php?couple_idx="+couple_idx+"&diary_idx="+idx, "");


    }
    public void scrollToEnd(){
        scrollView.post(new Runnable() {
            @Override
            public void run() {
                scrollView.fullScroll(View.FOCUS_DOWN);
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

        String TAG_JSON="diary_comment";                         // JSON 배열의 이름

        /* DB 컬럼명을 적는 String 변수 */
        String TAG_IDX = "idx";     // 밑으로 3개는
        String TAG_DATE = "datef";
        String TAG_COMMENT = "comment";
        String TAG_NAME = "name";


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
                String comment_idx = item.getString(TAG_IDX);   // 운동 이름
                String date1 = item.getString(TAG_DATE);
                String comment = item.getString(TAG_COMMENT);
                String name1 = item.getString(TAG_NAME);

                DiaryCommentData diaryCommentData = new DiaryCommentData();

                diaryCommentData.setIdx(comment_idx);
                diaryCommentData.setDate(date1);
                diaryCommentData.setComment(comment);
                diaryCommentData.setDiary_idx(idx);
                diaryCommentData.setName(name1);

                diaryCommentData.setDiary_content(content);
                diaryCommentData.setDiary_date(date);
                diaryCommentData.setDiary_time(time);
                diaryCommentData.setDiary_name(name);
                diaryCommentData.setDiary_focus(focus);
                diaryCommentData.setDiary_title(title);

                mArrayList.add(diaryCommentData);

                mAdapter.notifyDataSetChanged();



            }

        } catch (JSONException e) {
            // 에러 뜨면 결과 출력
            Log.e(TAG, "showResult : ", e);
        }

        mAdapter.notifyDataSetChanged();
        if(focus.equals("1")){
            scrollToEnd();
        }

    }   // showResult() end


    /* HTTPUrlConnection을 써서 POST 방식으로 phpmyadmin DB에서 값들을 가져오는 AsyncTask 클래스 정의 */
    private class GetData1 extends AsyncTask<String, Void, String> {

        ProgressDialog progressDialog;
        String errorString1 = null;

        /* AsyncTask 작업 시작 전에 UI 처리할 내용을 정의하는 함수 */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        /* AsyncTask 작업 종료 후 UI 처리할 내용을 정의하는 함수 */
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            // 프로그레스 다이얼로그를 죽이고

            // doInBackground()의 리턴값이 담긴 result를 버튼 밑 텍스트뷰에 setText()해서 JSON 형태로 받아온 값들을 출력
//            mTextViewResult.setText(result);
            Log.e(TAG, "response - " + result);

            // 결과가 없으면 에러 때문에 못 받아온 거니까 에러 문구를 버튼 밑 텍스트뷰에 출력
            if (result == null) {
//                mTextViewResult.setText(errorString);
            } else {
                // 결과가 있다면 버튼 위 텍스트뷰에 JSON 데이터들을 텍스트뷰 형태에 맞게 출력한다
                mJsonString2 = result;

                if(result.equals("2")){
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
                errorString1 = e.toString();

                return null;
            }

        }
    }
}
