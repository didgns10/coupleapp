package com.example.coupleapp.Activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.coupleapp.Activity.Calender.CalenderDiaryActivity;
import com.example.coupleapp.FirebaseMessagingService;
import com.example.coupleapp.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Calendar;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {


    private static String IP_ADDRESS = "13.125.232.78";
    private static String TAG = "phptest";
    private String mJsonString;

    private DrawerLayout mDrawerLayout;

    private TextView tv_logout,tv_count;

    private String email;
    private String name;
    private String imgurl;
    private String opp_imgurl;
    private String opp_name;

    private SharedPreferences sf;
    private SharedPreferences sf_idx;
    private SharedPreferences sf_name;

    private TextView tv_manname;
    private TextView tv_womanname;
    private TextView tv_date;
    private CircleImageView c_imgv_man;
    private CircleImageView c_imgv_woman;
    private int year,month,day;

    private ImageView img_main;
    private FloatingActionButton ftbt_chat;
    private FloatingActionButton ftbt_background;
    private TextView tv_1;
    private TextView tv_2;
    private ImageButton ibtn_video;

    private static final int IMG_REQUEST = 777;
    private Bitmap bitmap;
    private String main_img;
    private String couple_idx;

    private Boolean signok;

    private String message="2";

    private boolean test;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

         String token = FirebaseInstanceId.getInstance().getToken();

        Log.e("토큰1",token);



        SharedPreferences g = getSharedPreferences("LOGIN",MODE_PRIVATE);
        boolean saveLoginData = g.getBoolean("SAVE_LOGIN_DATA",false);
        Log.e("login",saveLoginData+"");

        if(saveLoginData==false){
            Intent intent = new Intent(MainActivity.this, StartpageActivity.class);
            startActivity(intent);
            finish();
        }

/*        //프로필 설정이 완료된경우
        SharedPreferences sign_ok = getSharedPreferences("SIGNOK",MODE_PRIVATE);
        signok = sign_ok.getBoolean("SIGNOK"+email,false);
        Log.e("signok",signok+"");

        if(signok==false){
            Intent intent = new Intent(MainActivity.this, ConnectionActivity.class);
            startActivity(intent);
            finish();
        }*/


        //로그인 저장 정보
        sf = getSharedPreferences("LOGIN",MODE_PRIVATE);
        email = sf.getString("et_email","");

        //커플 인덱스 번호가져오기
        sf_idx = getSharedPreferences("COPLE",MODE_PRIVATE);
        couple_idx = sf_idx.getString("cople_idx","");

        Log.e("login",email);
        //네비게이션 드로어 만드는 부분
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false); // 기존 title 지우기
        actionBar.setDisplayHomeAsUpEnabled(true); // 뒤로가기 버튼 만들기
        actionBar.setHomeAsUpIndicator(R.drawable.menu_image); //뒤로가기 버튼 이미지 지정

        //레이아웃 선언들
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        tv_logout = (TextView)findViewById(R.id.tv_logout);
        tv_date = (TextView)findViewById(R.id.tv_date);
        tv_manname = (TextView)findViewById(R.id.tv_manname);
        tv_womanname = (TextView)findViewById(R.id.tv_womanname);
        c_imgv_man = (CircleImageView)findViewById(R.id.c_imgv_man);
        c_imgv_woman = (CircleImageView)findViewById(R.id.c_imgv_woman);
        img_main = (ImageView)findViewById(R.id.img_main);
        ftbt_chat=(FloatingActionButton)findViewById(R.id.ftbt_chat);
        ftbt_background=(FloatingActionButton)findViewById(R.id.ftbt_background);
        tv_1=(TextView)findViewById(R.id.tv_1);
        tv_2 =(TextView)findViewById(R.id.tv_2);
        tv_count=findViewById(R.id.tv_count);
        ibtn_video=findViewById(R.id.ibtn_video);

        message="2";
        ibtn_video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (message.equals("1")) {
                    Intent intent = new Intent(MainActivity.this, VideoChatActivity.class);
                    intent.putExtra("start", "2");
                    startActivity(intent);
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

                    builder.setTitle(opp_name + "님과 영상통화를 하시겠습니까?").setMessage("");

                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {

                            GetData4 task4 = new GetData4();
                            task4.execute("http://" + IP_ADDRESS + "/video_call.php?email=" + email + "&couple_idx=" + couple_idx + "&name=" + name, "");

                            Intent intent = new Intent(MainActivity.this, VideoChatActivity.class);
                            intent.putExtra("start", "1");
                            startActivity(intent);

                        }

                    });

                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {

                        }
                    });


                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();

                }
            }
        });

        tv_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = sf.edit();
                editor.putBoolean("SAVE_LOGIN_DATA", false);
                editor.putString("et_email", "");
                editor.apply();

                Intent intent = new Intent(MainActivity.this,StartpageActivity.class);
                startActivity(intent);
                finish();
            }
        });

        ftbt_background.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                seletImage();
            }
        });



        //Json

        GetData task = new GetData();
        task.execute( "http://" + IP_ADDRESS + "/coupledata.php?email="+email, "");

        GetData1 task1 = new GetData1();
        task1.execute( "http://" + IP_ADDRESS + "/user_infomation.php?email="+email, "");

        GetData2 task2 = new GetData2();
        task2.execute( "http://" + IP_ADDRESS + "/opp_user_infomation.php?email="+email, "");



        ftbt_chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,ChatActivity.class);
                intent.putExtra("opp_imgurl",opp_imgurl);
                intent.putExtra("opp_name",opp_name);
                intent.putExtra("myname",name);
                intent.putExtra("myimgurl",imgurl);
                startActivity(intent);

            }
        });

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

                }
                else if(id == R.id.date_course){/*
                    Intent intent = new Intent(getApplicationContext(), SelectActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.addFlags (Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(intent);*/
                }
                else if(id == R.id.story_album){
                    Intent intent = new Intent(getApplicationContext(), StoryActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.addFlags (Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(intent);
                }
                else if(id == R.id.couple_calender) {
                    Intent intent = new Intent(getApplicationContext(), CalenderDiaryActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.addFlags (Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(intent);
                }else if(id == R.id.couple_dday){
                    Intent intent = new Intent(getApplicationContext(), AnniversaryActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.addFlags (Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(intent);
                }

                return true;
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();



        LocalBroadcastManager.getInstance(this).registerReceiver( mMessageReceiver, new IntentFilter("custom-event-name"));
        GetData3 task3 = new GetData3();
        task3.execute( "http://" + IP_ADDRESS + "/message_read.php?couple_idx="+couple_idx+"&email="+email, "");

        message ="2";
    }

    @Override
    protected void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver( mMessageReceiver);

        message ="2";
    }

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override public void onReceive(Context context, Intent intent) {
            // TODO Auto-generated method stub
            // Get extra data included in the Intent
            message = intent.getStringExtra("start");
            Log.d("receiver", "Got message: " + message);

        }
    };

    //프로필 버튼을 클릭하게 되면 앨범에서 선택하게 해주는 함수
    private void seletImage(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,IMG_REQUEST);
    }
    //앨범에서 가져오는 데이터값들
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==IMG_REQUEST && resultCode==RESULT_OK && data!=null){
            Uri path = data.getData();

            try {

                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),path);
                bitmap = Bitmap.createScaledBitmap(bitmap,700,900,true);
                img_main.setImageBitmap(bitmap);
                uploadImage();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    //이미지의 절대경로를 비트맥 형식을 스트링 값으로바꿔주는 함수
    private String imageToString(){
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,byteArrayOutputStream);
        byte[] imgByte = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(imgByte,Base64.DEFAULT);
    }
    // 레트로핏을 통한 프로필 업로드 과정
    private void uploadImage(){

        main_img = imageToString();

        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<MainImgClass> call = apiInterface.uploadImage(email,main_img,couple_idx);

        call.enqueue(new Callback<MainImgClass>() {
            @Override
            public void onResponse(Call<MainImgClass> call, Response<MainImgClass> response) {

                MainImgClass mainImgClass = response.body();

                if(mainImgClass.getResponse().equals("yes")){
                    Toast.makeText(MainActivity.this,"메인 화면을 설정했습니다.",Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<MainImgClass> call, Throwable t) {

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

    //json 데이터 가져오는  부분
    private class GetData extends AsyncTask<String, Void, String> {

        ProgressDialog progressDialog;
        String errorString = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

/*            progressDialog = ProgressDialog.show(Program.this,
                    "Please Wait", null, true, true);*/
        }


        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            // progressDialog.dismiss();

            Log.d(TAG, "response - " + result);

            if (result == null){

            }
            else {

                mJsonString = result;
                showResult();
            }
        }


        @Override
        protected String doInBackground(String... params) {

            String serverURL = params[0];
            String postParameters = params[1];


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


    private void showResult(){

        String TAG_JSON="couple";
        String TAG_MANNAME = "manname";
        String TAG_IDX = "idx";
        String TAG_WOMANNAME = "womanname";
        String TAG_DATE ="datef";
        String TAG_MANBRITHDAY = "manbirthday";
        String TAG_WOMANBRITHDAY = "womanbirthday";
        String TAG_MANIMG = "manimg";
        String TAG_WOMANIMG = "womanimg";
        String TAG_MAINIMG = "mainimg";


        try {
            JSONObject jsonObject = new JSONObject(mJsonString);
            JSONArray jsonArray = jsonObject.getJSONArray(TAG_JSON);

            for(int i=0;i<jsonArray.length();i++){

                JSONObject item = jsonArray.getJSONObject(i);

                String idx = item.getString(TAG_IDX);
                String manname = item.getString(TAG_MANNAME);
                String womanname = item.getString(TAG_WOMANNAME);
                String datef = item.getString(TAG_DATE);
                String manbirthday = item.getString(TAG_MANBRITHDAY);
                String womanbirthday = item.getString(TAG_WOMANBRITHDAY);
                String manimg =item.getString(TAG_MANIMG);
                String womanimg=item.getString(TAG_WOMANIMG);
                String mainimg=item.getString(TAG_MAINIMG);
                Log.e("로그",mainimg);

                //쉐어드 프리펀스를 이용해서 커플 인덱스를 저장한다.
                SharedPreferences appData = getSharedPreferences("COPLE",MODE_PRIVATE);
                SharedPreferences.Editor editor = appData.edit();
                editor.putString("cople_idx", idx);
                editor.apply();


                //날짜 형식이 yyyy-mm-dd로 되어있어서 - 기준으로 date라는 string 배열에 담는과정이다.
                String date[] = datef.split("-");

                Log.e("로그",date[0]+"-"+date[1]+"-"+date[2]);
                year = Integer.parseInt(date[0]);
                month = Integer.parseInt(date[1]);
                day  = Integer.parseInt(date[2]);
                Log.e("로그",year+"-"+month+"-"+day);

                //각종 값을 집어넣어서 ui 형성해주는 과정
                Glide.with(this).load(manimg)
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .skipMemoryCache(true).into(c_imgv_man);
                Glide.with(this).load(womanimg)
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .skipMemoryCache(true).into(c_imgv_woman);


                tv_date.setText(caldate(year,month-1,day+1)+"일");
                tv_manname.setText(manname);
                tv_womanname.setText(womanname);
                if(mainimg==""){
                    tv_1.setVisibility(View.VISIBLE);
                    tv_2.setVisibility(View.VISIBLE);
                }else {
                    tv_1.setVisibility(View.GONE);
                    tv_2.setVisibility(View.GONE);
                    //글라이드 라이브러리 자체 캐시가 적용되기때문에 캐시를 적용 안시킨다.
                    Glide.with(this).load(mainimg)
                                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                                            .skipMemoryCache(true)
                                            .into(img_main);
                    Log.e("로그",mainimg);
                }
                Log.e("확인","확인");



            }
            SharedPreferences appData = getSharedPreferences("DAY",MODE_PRIVATE);
            SharedPreferences.Editor editor = appData.edit();
            editor.putString("day",caldate(year,month-1,day+1)+"일");
            editor.apply();



        } catch (JSONException e) {

            Log.d(TAG, "showResult : ", e);
        }
    }
    //json 데이터 가져오는  부분
    private class GetData1 extends AsyncTask<String, Void, String> {

        ProgressDialog progressDialog;
        String errorString = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

/*            progressDialog = ProgressDialog.show(Program.this,
                    "Please Wait", null, true, true);*/
        }


        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            // progressDialog.dismiss();

            Log.d(TAG, "response - " + result);

            if (result == null){

            }
            else {

                mJsonString = result;
                showResult1();
            }
        }


        @Override
        protected String doInBackground(String... params) {

            String serverURL = params[0];
            String postParameters = params[1];


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


    private void showResult1() {

        String TAG_JSON1="users";                         // JSON 배열의 이름
        String TAG_MYNAME = "name";
        String TAG_IMGURL = "imgurl";

        try {

            // JSON 배열로 받아오기 위해 JSONObject, JSONArray 차례로 선언
            JSONObject jsonObject1 = new JSONObject(mJsonString);

            // JSONArray에는 root를 넣어서 root란 이름의 JSON 배열을 가져올 수 있도록 한다
            JSONArray jsonArray1 = jsonObject1.getJSONArray(TAG_JSON1);

            // for문으로 JSONArray의 길이만큼 반복해서 String 변수에 담는다
            for(int i = 0; i < jsonArray1.length(); i++){

                JSONObject item = jsonArray1.getJSONObject(i);

                imgurl = item.getString(TAG_IMGURL);
                name = item.getString(TAG_MYNAME);
                SharedPreferences appData = getSharedPreferences("CHAT",MODE_PRIVATE);
                SharedPreferences.Editor editor = appData.edit();
                editor.putString("name", name);
                editor.putString("imgurl",imgurl);
                editor.apply();
                Log.e("로그3",name);
                Log.e("로그3",imgurl);
            }

        } catch (JSONException e) {
            // 에러 뜨면 결과 출력
            Log.e(TAG, "showResult : ", e);
        }
    }

    //json 데이터 가져오는  부분
    private class GetData2 extends AsyncTask<String, Void, String> {

        ProgressDialog progressDialog;
        String errorString = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

/*            progressDialog = ProgressDialog.show(Program.this,
                    "Please Wait", null, true, true);*/
        }


        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            // progressDialog.dismiss();

            Log.d(TAG, "response - " + result);

            if (result == null){

            }
            else {

                mJsonString = result;
                showResult2();
            }
        }


        @Override
        protected String doInBackground(String... params) {

            String serverURL = params[0];
            String postParameters = params[1];


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


    private void showResult2() {

        String TAG_JSON1="opp_users";                         // JSON 배열의 이름
        String TAG_OPPNAME = "name";
        String TAG_IMGURL = "imgurl";

        try {

            // JSON 배열로 받아오기 위해 JSONObject, JSONArray 차례로 선언
            JSONObject jsonObject1 = new JSONObject(mJsonString);

            // JSONArray에는 root를 넣어서 root란 이름의 JSON 배열을 가져올 수 있도록 한다
            JSONArray jsonArray1 = jsonObject1.getJSONArray(TAG_JSON1);

            // for문으로 JSONArray의 길이만큼 반복해서 String 변수에 담는다
            for(int i = 0; i < jsonArray1.length(); i++){

                JSONObject item = jsonArray1.getJSONObject(i);

                opp_imgurl = item.getString(TAG_IMGURL);
                opp_name = item.getString(TAG_OPPNAME);
                SharedPreferences appData = getSharedPreferences("CHAT",MODE_PRIVATE);
                SharedPreferences.Editor editor = appData.edit();
                editor.putString("opp_name", opp_name);
                editor.putString("opp_imgurl",opp_imgurl);
                editor.apply();

                Log.e("로그3",opp_imgurl);
                Log.e("로그3",opp_name);
            }

        } catch (JSONException e) {
            // 에러 뜨면 결과 출력
            Log.e(TAG, "showResult : ", e);
        }
    }
    //json 데이터 가져오는  부분
    private class GetData3 extends AsyncTask<String, Void, String> {

        ProgressDialog progressDialog;
        String errorString = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

/*            progressDialog = ProgressDialog.show(Program.this,
                    "Please Wait", null, true, true);*/
        }


        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            // progressDialog.dismiss();

            Log.d(TAG, "response - " + result);

            if (result == null){

            }
            else {
                if(result.equals("0")){
                    tv_count.setVisibility(View.GONE);

                }else {
                    tv_count.setVisibility(View.VISIBLE);
                    tv_count.setText(result);
                }

            }
        }


        @Override
        protected String doInBackground(String... params) {

            String serverURL = params[0];
            String postParameters = params[1];


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
    //json 데이터 가져오는  부분
    private class GetData4 extends AsyncTask<String, Void, String> {

        ProgressDialog progressDialog;
        String errorString = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

/*            progressDialog = ProgressDialog.show(Program.this,
                    "Please Wait", null, true, true);*/
        }


        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            // progressDialog.dismiss();

            Log.d(TAG, "response - " + result);

            if (result == null){

            }
            else {

            }
        }


        @Override
        protected String doInBackground(String... params) {

            String serverURL = params[0];
            String postParameters = params[1];


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
