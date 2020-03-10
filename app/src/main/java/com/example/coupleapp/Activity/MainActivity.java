package com.example.coupleapp.Activity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

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

public class MainActivity extends AppCompatActivity {


    private static String IP_ADDRESS = "13.125.232.78";
    private static String TAG = "phptest";
    private String mJsonString;

    private DrawerLayout mDrawerLayout;

    private TextView tv_logout;

    private String email;

    private SharedPreferences sf;

    private TextView tv_manname;
    private TextView tv_womanname;
    private TextView tv_date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        sf = getSharedPreferences("LOGIN",MODE_PRIVATE);
        email = sf.getString("et_email","");
        Log.e("login",email);
        //네비게이션 드로어 만드는 부분
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false); // 기존 title 지우기
        actionBar.setDisplayHomeAsUpEnabled(true); // 뒤로가기 버튼 만들기
        actionBar.setHomeAsUpIndicator(R.drawable.menu_image); //뒤로가기 버튼 이미지 지정

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        tv_logout = (TextView)findViewById(R.id.tv_logout);
        tv_date = (TextView)findViewById(R.id.tv_date);
        tv_manname = (TextView)findViewById(R.id.tv_manname);
        tv_womanname = (TextView)findViewById(R.id.tv_womanname);

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

        //Json

        GetData task = new GetData();
        task.execute( "http://" + IP_ADDRESS + "/coupledata.php?email="+email, "");

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
                else if(id == R.id.my_exercise){/*
                    Intent intent = new Intent(getApplicationContext(), SelectActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.addFlags (Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(intent);*/
                }
                else if(id == R.id.exercise_dictionary){
                }
                else if(id == R.id.my_record){
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
        String TAG_WOMANNAME = "womanname";
        String TAG_DATE ="datef";
        String TAG_MANBRITHDAY = "manbirthday";
        String TAG_WOMANBRITHDAY = "womanbirthday";


        try {
            JSONObject jsonObject = new JSONObject(mJsonString);
            JSONArray jsonArray = jsonObject.getJSONArray(TAG_JSON);

            for(int i=0;i<jsonArray.length();i++){

                JSONObject item = jsonArray.getJSONObject(i);

                String manname = item.getString(TAG_MANNAME);
                String womanname = item.getString(TAG_WOMANNAME);
                String datef = item.getString(TAG_DATE);
                String manbirthday = item.getString(TAG_MANBRITHDAY);
                String womanbirthday = item.getString(TAG_WOMANBRITHDAY);


                tv_date.setText(datef);
                tv_manname.setText(manname);
                tv_womanname.setText(womanname);
                Log.e("확인","확인");



            }



        } catch (JSONException e) {

            Log.d(TAG, "showResult : ", e);
        }

    }
}
