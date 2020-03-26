package com.example.coupleapp.Activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.coupleapp.Adapter.MessageAdapter;
import com.example.coupleapp.FirebaseMessagingService;
import com.example.coupleapp.Model.MessageData;
import com.example.coupleapp.R;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import retrofit2.Call;
import retrofit2.Callback;

public class ChatActivity extends AppCompatActivity {

    private EditText messageBox;
    private TextView send ;
    private WebSocket webSocket;
    private ImageButton img_button;


    private MessageAdapter mAdapter;
    private ArrayList<MessageData> mArrayList;
    private RecyclerView recyclerView;

    private String formatDate;

    private SharedPreferences sf;
    private SharedPreferences sf_idx;
    private SharedPreferences sfc;

    private String email;
    private String couple_idx;
    private String name;
    private String imgurl;
    private String oppname;
    private String opp_imgurl;
    private String message;
    private String images="none";
    private Call<MessageClass> call;


    private static String IP_ADDRESS="13.125.232.78";  // 퍼블릭 IPv4 주소
    private static String TAG = "스토리";          // 로그에 사용할 태그
    private String mJsonString;                         // JSON 값을 저장할 String 변수
    private int IMAGE_REQUEST_ID = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);


        //로그인 저장 정보
        sfc = getSharedPreferences("CHAT",MODE_PRIVATE);
        name = sfc.getString("name","");
        imgurl = sfc.getString("imgurl","");
        oppname = sfc.getString("opp_name","");
        opp_imgurl = sfc.getString("opp_imgurl","");


/*
        name = getIntent().getStringExtra("myname");
        imgurl = getIntent().getStringExtra("myimgurl");
        oppname =getIntent().getStringExtra("opp_name");
        opp_imgurl = getIntent().getStringExtra("opp_imgurl");*/

        Log.e("로그4",name);
        Log.e("로그4",imgurl);
        Log.e("로그4",oppname);
        Log.e("로그4",opp_imgurl);

        //네비게이션 드로어 만드는 부분
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true); // 뒤로가기 버튼 만들기
        actionBar.setHomeAsUpIndicator(R.drawable.ic_back); //뒤로가기 버튼 이미지 지정
        actionBar.setTitle(Html.fromHtml("<font color='#ffffff'>" + oppname+"님과 채팅방" + "</font>"));


        //로그인 저장 정보
        sf = getSharedPreferences("LOGIN",MODE_PRIVATE);
        email = sf.getString("et_email","");

        //커플 인덱스 번호가져오기
        sf_idx =getSharedPreferences("COPLE",MODE_PRIVATE);
        couple_idx = sf_idx.getString("cople_idx","");



        recyclerView = findViewById(R.id.messageList);
        recyclerView.setLayoutManager(new LinearLayoutManager(ChatActivity.this));


        messageBox = findViewById(R.id.messageBox);
        send = findViewById(R.id.send);
        img_button = findViewById(R.id.img_button);

        //현재 시간을 구하는 메소드
        // 현재시간을 msec 으로 구한다.
        long now = System.currentTimeMillis();
        // 현재시간을 date 변수에 저장한다.
        final Date date = new Date(now);
        // 시간을 나타냇 포맷을 정한다 ( yyyy/MM/dd 같은 형태로 변형 가능 )
        SimpleDateFormat sdfNow = new SimpleDateFormat("MM월dd일 HH:mm");
        // nowDate 변수에 값을 저장한다.
        formatDate = sdfNow.format(date);



        instantiateWebSocket();

        mArrayList = new ArrayList<>();
        mArrayList.clear();
        GetData task = new GetData();

        // execute() 사용 시 DB의 값을 JSON 형태로 가져오는 코드가 적힌 php 파일의 경로를 적어
        // AsyncTask로 값들을 JSON 형태로 가져올 수 있게 한다
        task.execute( "http://" + IP_ADDRESS + "/message.php?couple_idx="+couple_idx+"&email="+email+"&opp_name="+oppname, "");

        mAdapter = new MessageAdapter(mArrayList,ChatActivity.this);
        recyclerView.setAdapter(mAdapter);
        recyclerView.scrollToPosition(mAdapter.getItemCount() - 1);

        mAdapter.notifyDataSetChanged();

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                message = messageBox.getText().toString();
                String server = "send";

                if(!message.isEmpty()){
                    images = "none";
                    uploadMessage();
                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put("message",message);
                        jsonObject.put("couple_idx",couple_idx);
                        jsonObject.put("date",formatDate);
                        jsonObject.put("email",email);
                        jsonObject.put("name",name);
                        jsonObject.put("imgurl",imgurl);
                        jsonObject.put("image", "");
                        jsonObject.put("byServer",server);

                        Log.e("로그",message);
                        Log.e("로그",couple_idx);
                        Log.e("로그",formatDate);
                        Log.e("로그",email);
                        Log.e("로그",name);
                        Log.e("로그",imgurl);
                        Log.e("로그",jsonObject.toString());

                        MessageData messageData = new MessageData();

                        messageData.setMessage(message);
                        messageData.setDatetime(formatDate);
                        messageData.setServer(server);

                        mArrayList.add(messageData);
                        mAdapter.notifyDataSetChanged();
                        recyclerView.scrollToPosition(mAdapter.getItemCount() - 1);

                        webSocket.send(jsonObject.toString());
                        messageBox.setText("");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            }
        });

        img_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");

                startActivityForResult(Intent.createChooser(intent, "Pick image"),
                        IMAGE_REQUEST_ID);
            }
        });

    }

    //뒤로가기 버튼 눌렀을때
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // 그 다음 AsyncTask 객체를 만들어 execute()한다
        SharedPreferences appData = getSharedPreferences("CHAT_ON",MODE_PRIVATE);
        SharedPreferences.Editor editor = appData.edit();
        editor.putBoolean("chat_on", true);
        editor.apply();

    }

    @Override
    protected void onPause() {
        super.onPause();
        SharedPreferences appData = getSharedPreferences("CHAT_ON",MODE_PRIVATE);
        SharedPreferences.Editor editor = appData.edit();
        editor.putBoolean("chat_on", false);
        editor.apply();
        GetData task = new GetData();

        // execute() 사용 시 DB의 값을 JSON 형태로 가져오는 코드가 적힌 php 파일의 경로를 적어
        // AsyncTask로 값들을 JSON 형태로 가져올 수 있게 한다
        task.execute( "http://" + IP_ADDRESS + "/message.php?couple_idx="+couple_idx+"&email="+email+"&opp_name="+oppname, "");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == IMAGE_REQUEST_ID && resultCode == RESULT_OK) {

            try {
                InputStream is = getContentResolver().openInputStream(data.getData());
                Bitmap image = BitmapFactory.decodeStream(is);

                sendImage(image);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

        }

    }
    private void sendImage(Bitmap image) {

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 50, outputStream);

        String base64String = Base64.encodeToString(outputStream.toByteArray(),
                Base64.DEFAULT);

        message = "none";
        images = base64String;
        uploadMessage();

        JSONObject jsonObject = new JSONObject();
        String server = "send";
        String message = "";
        try {
            jsonObject.put("couple_idx",couple_idx);
            jsonObject.put("date",formatDate);
            jsonObject.put("email",email);
            jsonObject.put("name",name);
            jsonObject.put("message","");
            jsonObject.put("byServer",server);
            jsonObject.put("imgurl",imgurl);
            jsonObject.put("image", base64String);

            webSocket.send(jsonObject.toString());

            MessageData messageData = new MessageData();

            messageData.setMessage(message);
            messageData.setBit_image(image);
            messageData.setDatetime(formatDate);
            messageData.setServer(server);


            mArrayList.add(messageData);
            mAdapter.notifyDataSetChanged();
            recyclerView.scrollToPosition(mAdapter.getItemCount() - 1);

            recyclerView.smoothScrollToPosition(mAdapter.getItemCount() - 1);

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
    private Bitmap getBitmapFromString(String image) {

        byte[] bytes = Base64.decode(image, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }


    //메세지 디비 적용과정
    private void uploadMessage(){

        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        call = apiInterface.uploadMessage(couple_idx,images,name,message,formatDate,email);



        call.enqueue(new Callback<MessageClass>() {
            @Override
            public void onResponse(Call<MessageClass> call, retrofit2.Response<MessageClass> response) {

                MessageClass messageClass = response.body();

            }

            @Override
            public void onFailure(Call<MessageClass> call, Throwable t) {

            }
        });

    }

    //소켓 연결 과정
    private void instantiateWebSocket() {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url("http://13.125.232.78:8080").build();
        SocketListener socketListener = new SocketListener(this);
        webSocket = client.newWebSocket(request,socketListener);
    }

    public class SocketListener extends WebSocketListener {


        public  ChatActivity activity;
        public SocketListener(ChatActivity activity){
            this.activity = activity;
        }

        @Override
        public void onOpen(WebSocket webSocket, Response response) {
            super.onOpen(webSocket, response);

            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                }
            });
        }

        @Override
        public void onMessage(WebSocket webSocket, final String text) {
            super.onMessage(webSocket, text);

            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(text);

                        String couple_idx1 = (String)jsonObject.get("couple_idx");
                        String message =(String)jsonObject.get("message");
                        String name =(String)jsonObject.get("name");
                        String email=(String)jsonObject.get("email");
                        String date=(String)jsonObject.get("date");
                        String image=(String)jsonObject.get("image");
                        String server="receive";

                        if(couple_idx1.equals(couple_idx)){

                            if(image.equals("")) {
                                jsonObject.put("message", (String) jsonObject.get("message"));
                                jsonObject.put("couple_idx", (String) jsonObject.get("couple_idx"));
                                jsonObject.put("name", (String) jsonObject.get("name"));
                                jsonObject.put("email", (String) jsonObject.get("email"));
                                jsonObject.put("date", (String) jsonObject.get("date"));
                                jsonObject.put("imgurl",(String)jsonObject.get("imgurl"));
                                jsonObject.put("byServer", "receive");

                                Log.e("로그1", message);
                                Log.e("로그1", couple_idx1);
                                Log.e("로그1", date);
                                Log.e("로그1", email);
                                Log.e("로그1", name);
                                Log.e("로그1",imgurl);
                                Log.e("로그1", jsonObject.toString());

                                MessageData messageData = new MessageData();

                                messageData.setMessage(message);
                                messageData.setDatetime(date);
                                messageData.setName(name);
                                messageData.setServer(server);
                                messageData.setOpp_profile_img(opp_imgurl);

                                mArrayList.add(messageData);
                                mAdapter.notifyDataSetChanged();
                                recyclerView.scrollToPosition(mAdapter.getItemCount() - 1);
                            }else if(message.equals("")){
                                jsonObject.put("image", (String) jsonObject.get("image"));
                                jsonObject.put("couple_idx", (String) jsonObject.get("couple_idx"));
                                jsonObject.put("name", (String) jsonObject.get("name"));
                                jsonObject.put("email", (String) jsonObject.get("email"));
                                jsonObject.put("date", (String) jsonObject.get("date"));
                                jsonObject.put("imgurl",(String)jsonObject.get("imgurl"));
                                jsonObject.put("byServer", "receive");

                                Log.e("로그1", image);
                                Log.e("로그1", couple_idx1);
                                Log.e("로그1", date);
                                Log.e("로그1", email);
                                Log.e("로그1", name);
                                Log.e("로그1", jsonObject.toString());

                                Bitmap bitmap = getBitmapFromString(image);
                                MessageData messageData = new MessageData();

                                messageData.setMessage("");
                                messageData.setBit_image(bitmap);
                                messageData.setDatetime(date);
                                messageData.setName(name);
                                messageData.setServer(server);
                                messageData.setOpp_profile_img(opp_imgurl);

                                mArrayList.add(messageData);
                                mAdapter.notifyDataSetChanged();
                                recyclerView.scrollToPosition(mAdapter.getItemCount() - 1);
                            }
                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        }

        @Override
        public void onClosing(WebSocket webSocket, int code, String reason) {
            super.onClosing(webSocket, code, reason);
        }

        @Override
        public void onClosed(WebSocket webSocket, int code, String reason) {
            super.onClosed(webSocket, code, reason);
        }

        @Override
        public void onFailure(WebSocket webSocket, Throwable t, @Nullable Response response) {
            super.onFailure(webSocket, t, response);
        }
    }

    /* HTTPUrlConnection을 써서 POST 방식으로 phpmyadmin DB에서 값들을 가져오는 AsyncTask 클래스 정의 */
    private class GetData extends AsyncTask<String, Void, String> {

        ProgressDialog progressDialog;
        String errorString = null;

        /* AsyncTask 작업 시작 전에 UI 처리할 내용을 정의하는 함수 */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mArrayList.clear();

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

        String TAG_JSON="messages";                         // JSON 배열의 이름

        /* DB 컬럼명을 적는 String 변수 */
        String TAG_MSG_IDX = "message_idx";     // 밑으로 3개는
        String TAG_MSG = "message";
        String TAG_DATE= "datem";
        String TAG_IMG = "image";     // 밑으로 3개는
        String TAG_NAME = "name";
        String TAG_EMAIL = "email";
        String TAG_SERVER = "server";


        try {

            // JSON 배열로 받아오기 위해 JSONObject, JSONArray 차례로 선언
            JSONObject jsonObject = new JSONObject(mJsonString);

            // JSONArray에는 root를 넣어서 root란 이름의 JSON 배열을 가져올 수 있도록 한다
            JSONArray jsonArray = jsonObject.getJSONArray(TAG_JSON);

            // for문으로 JSONArray의 길이만큼 반복해서 String 변수에 담는다
            for(int i = 0; i < jsonArray.length(); i++){

                JSONObject item = jsonArray.getJSONObject(i);

                String message_idx = item.getString(TAG_MSG_IDX);
                String message = item.getString(TAG_MSG);
                String date = item.getString(TAG_DATE);
                String image = item.getString(TAG_IMG);
                String name = item.getString(TAG_NAME);
                String email = item.getString(TAG_EMAIL);
                String server = item.getString(TAG_SERVER);


                MessageData messageData = new MessageData();

                messageData.setMessage(message);
                messageData.setDatetime(date);
                messageData.setName(name);
                messageData.setEmail(email);
                messageData.setServer(server);
                messageData.setImage(image);
                messageData.setOpp_profile_img(opp_imgurl);


                mArrayList.add(messageData);
                mAdapter.notifyDataSetChanged();
                recyclerView.scrollToPosition(mAdapter.getItemCount() - 1);
            }

        } catch (JSONException e) {
            // 에러 뜨면 결과 출력
            Log.e(TAG, "showResult : ", e);
        }

    }   // showResult() end

}


