package com.example.coupleapp.Activity.DateCourse;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.example.coupleapp.Adapter.DateCourseDetailAdapter;
import com.example.coupleapp.Adapter.DateCourseRecommendAdapter;
import com.example.coupleapp.Model.DateCourtseDetailModel.CourseDetailApiUtils;
import com.example.coupleapp.Model.DateCourtseDetailModel.DateCourseDetail;
import com.example.coupleapp.Model.DateCourtseDetailModel.DateCourseDetailInterface;
import com.example.coupleapp.Model.DateTotalModel.DateCourseData;
import com.example.coupleapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DateCourseDetailActivity extends AppCompatActivity {

    private RecyclerView rv_course;
    private RecyclerView rv_recommend_course;
    private static String IP_ADDRESS="13.125.232.78";  // 퍼블릭 IPv4 주소
    private static String TAG = "스토리";          // 로그에 사용할 태그

    private ArrayList<DateCourseData> mArrayList;             // 모델 클래스의 데이터를 받아 리사이클러뷰에 뿌리는 데 쓸 ArrayList
    private DateCourseDetailAdapter mAdapter;                       // 리사이클러뷰 어댑터

    private ArrayList<CourseRecommendData> mArrayListRecommend;             // 모델 클래스의 데이터를 받아 리사이클러뷰에 뿌리는 데 쓸 ArrayList
    private DateCourseRecommendAdapter mAdapterRecommend;                       // 리사이클러뷰 어댑터

    private String mJsonString;                         // JSON 값을 저장할 String 변수

    private SharedPreferences sf;
    private SharedPreferences sf_idx;

    private String email;
    private String couple_idx;

    RequestQueue queue ;
    private TextView tv_title;

    private String contentid,title,contenttypeid;

    private DateCourseDetailInterface service;

    // 소켓통신에 필요한것
    private String html = "";
    private Handler mHandler;

    SocketChannel socketChannel;

    private static final String HOST = "13.125.232.78";
    private static final int PORT = 8088;

    private String data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_date_course_detail);

        //인텐트 받는거
        title = getIntent().getStringExtra("title");
        contentid = getIntent().getStringExtra("contentid");
        contenttypeid = getIntent().getStringExtra("contenttypeid");


        tv_title= findViewById(R.id.tv_title);
        tv_title.setText(title);

        rv_course = findViewById(R.id.rv_course);
        rv_recommend_course = findViewById(R.id.rv_recommend_course);

        rv_course.setLayoutManager(new LinearLayoutManager(DateCourseDetailActivity.this, LinearLayoutManager.HORIZONTAL,false));
        rv_recommend_course.setLayoutManager(new LinearLayoutManager(DateCourseDetailActivity.this, LinearLayoutManager.HORIZONTAL,false));

        // 리사이클러뷰에 연결되서 데이터를 뿌릴 ArrayList 선언
        mArrayList = new ArrayList<>();
        mArrayListRecommend = new ArrayList<>();

        // 프래그먼트에서 리사이클러뷰의 어댑터를 붙일 땐 context 쓰는 부분에 getActivity()를 쓴다
        mAdapter = new DateCourseDetailAdapter(mArrayList,DateCourseDetailActivity.this);
        rv_course.setAdapter(mAdapter);

        mAdapterRecommend = new DateCourseRecommendAdapter(mArrayListRecommend,DateCourseDetailActivity.this);
        rv_recommend_course.setAdapter(mAdapterRecommend);

        // 버튼 위에 표시되는 텍스트뷰에 mArrayList의 내용들을 뿌리는데 이것들을 전부 지우고
        mArrayList.clear();
        mArrayListRecommend.clear();
        // 어댑터에 데이터가 변경됐다는 걸 알린다
        mAdapter.notifyDataSetChanged();
        mAdapterRecommend.notifyDataSetChanged();

        service = CourseDetailApiUtils.getSOService();
    }
    @Override
    public void onResume() {
        super.onResume();

        recommend();
        loadAnswers1();
    }

    @Override
    protected void onPause() {
        super.onPause();

        try {
            socketChannel.close();
            Log.e("통신", "통신종료");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void recommend(){
        mHandler = new Handler();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    socketChannel = SocketChannel.open();
                    socketChannel.configureBlocking(true);
                    socketChannel.connect(new InetSocketAddress(HOST, PORT));
                    Log.e("통신", "통신완료");
                    Log.e("통신", contentid);

                } catch (Exception ioe) {
                    Log.d("asd", ioe.getMessage() + "a");
                    ioe.printStackTrace();

                }
                if(check.getState() == Thread.State.NEW) {
                    check.start();
                    new SendmsgTask().execute(contentid);
                }
            }
        }).start();
    }

    public void loadAnswers1(){
        service.getAnswers(contentid).enqueue(new Callback<DateCourseDetail>() {
            @Override
            public void onResponse(Call<DateCourseDetail> call, Response<DateCourseDetail> response) {
                if(response.isSuccessful()) {
                    Log.e("리스포", "posts loaded from API");
                    Log.e("리스포",response+"");
                    Log.e("리스포", response.body().getResponse().getBody().getItems().getItem().size()+"");


                    for(int i=0; i< response.body().getResponse().getBody().getItems().getItem().size() ; i++ ){

                        DateCourseData dateCourseData = new DateCourseData();
                        dateCourseData.setSubcontentid(response.body().getResponse().getBody().getItems().getItem().get(i).getSubcontentid()+"");
                        dateCourseData.setSubdetailalt(response.body().getResponse().getBody().getItems().getItem().get(i).getSubdetailalt());
                        dateCourseData.setSubdetailimg(response.body().getResponse().getBody().getItems().getItem().get(i).getSubdetailimg());
                        dateCourseData.setSubdetailoverview(response.body().getResponse().getBody().getItems().getItem().get(i).getSubdetailoverview());
                        dateCourseData.setContentid(response.body().getResponse().getBody().getItems().getItem().get(i).getContentid()+"");
                        dateCourseData.setContenttypeid(response.body().getResponse().getBody().getItems().getItem().get(i).getContenttypeid()+"");

                        mArrayList.add(dateCourseData);
                        mAdapter.notifyDataSetChanged();
                    }


                }else {
                    int statusCode  = response.code();
                    // handle request errors depending on status code
                    Log.e("리스포",statusCode+"");
                }
            }

            @Override
            public void onFailure(Call<DateCourseDetail> call, Throwable t) {
                Log.e("리스포", t+"");


            }
        });
    }

    private class SendmsgTask extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... strings) {
            try {
                socketChannel
                        .socket()
                        .getOutputStream()
                        .write(strings[0].getBytes("EUC-KR")); // 서버로


            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    //   tv.setText("");

                }
            });
        }
    }

    private Thread check = new Thread() {

        public void run() {
            try {
                String line;
                receive();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    void receive() {
        while (true) {
            try {
                ByteBuffer byteBuffer = ByteBuffer.allocate(256);
                //서버가 비정상적으로 종료했을 경우 IOException 발생
                int readByteCount = socketChannel.read(byteBuffer); //데이터받기
                Log.d("readByteCount", readByteCount + "");

                Log.e("통신", "readByteCount"+readByteCount);
                //서버가 정상적으로 Socket의 close()를 호출했을 경우
                if (readByteCount == -1) {
                    throw new IOException();
                }

                Log.e("통신", "readByteCount1"+byteBuffer);
                byteBuffer.flip(); // 문자열로 변환

                Log.e("통신", "readByteCount1"+byteBuffer);
                CharBuffer charBuffer = StandardCharsets.UTF_8.decode(byteBuffer);
                String text = charBuffer.toString();

                Charset charset = Charset.forName("EUC-KR");
                data = charBuffer.toString();


                GetData task = new GetData();

                // execute() 사용 시 DB의 값을 JSON 형태로 가져오는 코드가 적힌 php 파일의 경로를 적어
                // AsyncTask로 값들을 JSON 형태로 가져올 수 있게 한다
                task.execute( "http://" + IP_ADDRESS + "/course_recommend_view.php?contentid="+data, "");

                Log.e("통신", "통신 받기"+data);
                Log.d("receive", "msg :" + data);
                mHandler.post(showUpdate);
            } catch (IOException e) {
                Log.d("getMsg", e.getMessage() + "");
                try {
                    socketChannel.close();
                    break;
                } catch (IOException ee) {
                    ee.printStackTrace();
                }
            }
        }
    }

    private Runnable showUpdate = new Runnable() {

        public void run() {
            String receive = data;

            JSONObject jsonObject = null;

/*            try {
              //  jsonObject = new JSONObject(data);


            } catch (JSONException e) {
                e.printStackTrace();
            }*/

        }

    };

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

        String TAG_JSON="course_recommend";                         // JSON 배열의 이름

        /* DB 컬럼명을 적는 String 변수 */
        String TAG_CONTENTID = "contentid";     // 밑으로 3개는
        String TAG_TITLE = "title";
        String TAG_IMG = "firstimage";


        try {

            // JSON 배열로 받아오기 위해 JSONObject, JSONArray 차례로 선언
            JSONObject jsonObject = new JSONObject(mJsonString);

            // JSONArray에는 root를 넣어서 root란 이름의 JSON 배열을 가져올 수 있도록 한다
            JSONArray jsonArray = jsonObject.getJSONArray(TAG_JSON);

            // for문으로 JSONArray의 길이만큼 반복해서 String 변수에 담는다
            for(int i = 0; i < jsonArray.length(); i++){

                JSONObject item = jsonArray.getJSONObject(i);

                String contentid = item.getString(TAG_CONTENTID);
                String title = item.getString(TAG_TITLE);
                String img = item.getString(TAG_IMG);

                CourseRecommendData courseRecommendData = new CourseRecommendData();

                courseRecommendData.setContentid(contentid);
                courseRecommendData.setTitle(title);
                courseRecommendData.setImg(img);


                // 데이터 모델 클래스 객체를 리사이클러뷰에 연결된 ArrayList에 삽입
                mArrayListRecommend.add(courseRecommendData);

                // ArrayList에 변동이 생겼으니 어댑터에 알림
                mAdapterRecommend.notifyDataSetChanged();

            }

        } catch (JSONException e) {
            // 에러 뜨면 결과 출력
            Log.e(TAG, "showResult : ", e);
        }


    }   // showResult() end

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            socketChannel.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
