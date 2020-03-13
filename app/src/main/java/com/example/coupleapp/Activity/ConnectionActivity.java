package com.example.coupleapp.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.coupleapp.R;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class ConnectionActivity extends AppCompatActivity {

    private static String IP_ADDRESS = "13.125.232.78";
    private static String TAG = "phptest";


    private boolean saveLoginData;
    private boolean waitgrant;
    private boolean numok;
    private String email;
    private TextView tv_logout;
    private EditText et_mynum;
    private EditText et_oppnum;
    private String mynum;
    private String oppnum;
    private TextView tv_grant;
    private Button btn_sign;
    private LinearLayout linearLayout;

    private SharedPreferences sf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connection);
        Log.e("새로고침","새로고침확인");

        //저장된 값을 불러오기 위해 같은 네임파일을 찾음.
         sf = getSharedPreferences("LOGIN",MODE_PRIVATE);

         linearLayout = (LinearLayout)findViewById(R.id.linearlayout);
        tv_logout = (TextView)findViewById(R.id.tv_logout);
        et_mynum = (EditText) findViewById(R.id.et_mynum);
        et_oppnum = (EditText) findViewById(R.id.et_oppnum);
        btn_sign = (Button) findViewById(R.id.btn_connection);
        tv_grant = (TextView) findViewById(R.id.tv_grant);

        tv_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = sf.edit();
                editor.putBoolean("SAVE_LOGIN_DATA", false);
                editor.putString("et_email", "");
                editor.apply();
                Log.e(TAG,"로그아웃 ");

                Intent intent = new Intent(ConnectionActivity.this,StartpageActivity.class);
                startActivity(intent);
                finish();
            }
        });


        //text라는 key에 저장된 값이 있는지 확인. 아무값도 들어있지 않으면 ""를 반환
        email = sf.getString("et_email","");
        Log.e("login",email);
        saveLoginData = sf.getBoolean("SAVE_LOGIN_DATA",false);
        Log.e("login",saveLoginData+"");

        SharedPreferences num_ok = getSharedPreferences("NUM_OK",MODE_PRIVATE);
        numok = num_ok.getBoolean("NUM_OK"+email,false);
        Log.e("num_ok",numok+"");

        //기존에 번호를 입력한 사실이 있으면
        if(numok){
            //여기다가 다시 쿼리를 불러오는 구문을 넣고 리절트가 2이면 넘어가고 1이면 대기하는 화면 표시 하게 해준다.
            InsertData1 task = new InsertData1();
            task.execute("http://" + IP_ADDRESS + "/connection.php", email);

        }else {
            linearLayout.setVisibility(View.VISIBLE);

            //승인 대기중인가 쉐어드 프리펀스 확인
            SharedPreferences gt = getSharedPreferences("GRANT", MODE_PRIVATE);
            waitgrant = gt.getBoolean("WAIT_GRANT" + email, false);

            //승인 대기중이면
            if (waitgrant) {
                tv_grant.setVisibility(View.VISIBLE);
                et_mynum.setVisibility(View.GONE);
                et_oppnum.setVisibility(View.GONE);
                btn_sign.setVisibility(View.GONE);
            } else {
                btn_sign.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        mynum = et_mynum.getText().toString().trim();
                        oppnum = et_oppnum.getText().toString().trim();

                        InsertData task = new InsertData();
                        task.execute("http://" + IP_ADDRESS + "/phone_con.php", mynum, oppnum, email);

                    }
                });
            }
        }


    }
    class InsertData extends AsyncTask<String, Void, String> {
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = ProgressDialog.show(ConnectionActivity.this,
                    "Please Wait", null, true, true);
        }


        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            progressDialog.dismiss();
            Log.d(TAG, "POST result  - " + result);

            //둘다 연결이 성공하면
            if(result.equals("2")){

                //번호 입력 성공했다는 표시
                SharedPreferences num_ok = getSharedPreferences("NUM_OK",MODE_PRIVATE);
                SharedPreferences.Editor editor1 = num_ok.edit();
                editor1.putBoolean("NUM_OK"+email, true);
                editor1.apply();

                //프로필 작성 화면으로 넘어간다
                Intent intent = new Intent(ConnectionActivity.this,ProfileWriteActivity.class);
                startActivity(intent);
                finish();

            }else if(result.equals("1")){
                //쉐어드 프리펀스를 이용해서 승인대기를 기달리게해준다.
                SharedPreferences grant = getSharedPreferences("GRANT",MODE_PRIVATE);
                SharedPreferences.Editor editor = grant.edit();
                editor.putBoolean("WAIT_GRANT"+email, true);
                editor.apply();

                //번호 입력 성공했다는 표시
                SharedPreferences num_ok = getSharedPreferences("NUM_OK",MODE_PRIVATE);
                SharedPreferences.Editor editor1 = num_ok.edit();
                editor1.putBoolean("NUM_OK"+email, true);
                editor1.apply();

                Log.e(TAG,"입력성공시 ");

                Intent intent = getIntent();
                finish();
                startActivity(intent);

            }


        }


        @Override
        protected String doInBackground(String... params) {

            String mynum = (String)params[1];
            Log.d(TAG, "POST response  - " + mynum);
            String oppnum = (String)params[2];
            Log.d(TAG, "POST response  - " + oppnum);
            String email = (String)params[3];
            Log.d(TAG, "POST response  - " + email);

            // 1. PHP 파일을 실행시킬 수 있는 주소와 전송할 데이터를 준비합니다.

            // POST 방식으로 데이터 전달시에는 데이터가 주소에 직접 입력되지 않습니다.
            String serverURL = (String)params[0];
            Log.d(TAG, "POST response  - " + serverURL);



            // HTTP 메시지 본문에 포함되어 전송되기 때문에 따로 데이터를 준비해야 합니다.

            // 전송할 데이터는 “이름=값” 형식이며 여러 개를 보내야 할 경우에는 항목 사이에 &를 추가합니다.

            // 여기에 적어준 이름을 나중에 PHP에서 사용하여 값을 얻게 됩니다.

            String postParameters = "mynum=" + mynum + "&oppnum=" + oppnum+ "&email=" + email;
            Log.d(TAG, "postParameters  - " + postParameters);

            try {
                // 2. HttpURLConnection 클래스를 사용하여 POST 방식으로 데이터를 전송합니다.
                URL url = new URL(serverURL); // 주소가 저장된 변수를 이곳에 입력합니다.


                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

                httpURLConnection.setReadTimeout(5000); //5초안에 응답이 오지 않으면 예외가 발생합니다.

                httpURLConnection.setConnectTimeout(5000); //5초안에 연결이 안되면 예외가 발생합니다.

                httpURLConnection.setRequestMethod("POST"); //요청 방식을 POST로 합니다.
                httpURLConnection.setDoInput(true);
                Log.e(TAG, "GetData : 666 ");

                httpURLConnection.connect();


                OutputStream outputStream = httpURLConnection.getOutputStream();
                outputStream.write(postParameters.getBytes("UTF-8")); //전송할 데이터가 저장된 변수를 이곳에 입력합니다. 인코딩을 고려해줘야 합니다.

                outputStream.flush();
                outputStream.close();



                // 3. 응답을 읽습니다.

                int responseStatusCode = httpURLConnection.getResponseCode();
                Log.d(TAG, "POST response code - " + responseStatusCode);

                InputStream inputStream;
                if(responseStatusCode == HttpURLConnection.HTTP_OK) {

                    // 정상적인 응답 데이터
                    inputStream = httpURLConnection.getInputStream();//연결이 성공되면 inputstream에 httpURLConnection에 담긴 값을 넣는다.
                }
                else{

                    // 에러 발생

                    inputStream = httpURLConnection.getErrorStream();//연결이 안되면 inputstream에 에러값을 넣는다.
                }



                // 4. StringBuilder를 사용하여 수신되는 데이터를 저장합니다.
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                StringBuilder sb = new StringBuilder();
                String line = null;

                while((line = bufferedReader.readLine()) != null){
                    sb.append(line);
                }


                bufferedReader.close();



                // 5. 저장된 데이터를 스트링으로 변환하여 리턴합니다.

                Log.e(TAG,"sb"+sb.toString());


                return sb.toString();


            } catch (Exception e) {

                Log.d(TAG, "InsertData: Error ", e);

                return new String("Error: " + e.getMessage());
            }

        }
    }
    class InsertData1 extends AsyncTask<String, Void, String> {
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = ProgressDialog.show(ConnectionActivity.this,
                    "Please Wait", null, true, true);
        }


        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            progressDialog.dismiss();
            Log.d(TAG, "POST result  - " + result);

            //둘다 연결이 성공하면
            if(result.equals("2")){

                //프로필 작성 화면으로 넘어간다
                Intent intent = new Intent(ConnectionActivity.this,ProfileWriteActivity.class);
                startActivity(intent);
                finish();


            }else if(result.equals("1")){
                //쉐어드 프리펀스를 이용해서 승인대기를 기달리게해준다.
                SharedPreferences grant = getSharedPreferences("GRANT",MODE_PRIVATE);
                SharedPreferences.Editor editor = grant.edit();
                editor.putBoolean("WAIT_GRANT"+email, true);
                editor.apply();

                linearLayout.setVisibility(View.VISIBLE);
                tv_grant.setVisibility(View.VISIBLE);
                et_mynum.setVisibility(View.GONE);
                et_oppnum.setVisibility(View.GONE);
                btn_sign.setVisibility(View.GONE);


            }


        }


        @Override
        protected String doInBackground(String... params) {

            String email = (String)params[1];
            Log.d(TAG, "POST response  - " + email);

            // 1. PHP 파일을 실행시킬 수 있는 주소와 전송할 데이터를 준비합니다.

            // POST 방식으로 데이터 전달시에는 데이터가 주소에 직접 입력되지 않습니다.
            String serverURL = (String)params[0];
            Log.d(TAG, "POST response  - " + serverURL);



            // HTTP 메시지 본문에 포함되어 전송되기 때문에 따로 데이터를 준비해야 합니다.

            // 전송할 데이터는 “이름=값” 형식이며 여러 개를 보내야 할 경우에는 항목 사이에 &를 추가합니다.

            // 여기에 적어준 이름을 나중에 PHP에서 사용하여 값을 얻게 됩니다.

            String postParameters ="&email=" + email;
            Log.d(TAG, "postParameters  - " + postParameters);

            try {
                // 2. HttpURLConnection 클래스를 사용하여 POST 방식으로 데이터를 전송합니다.
                URL url = new URL(serverURL); // 주소가 저장된 변수를 이곳에 입력합니다.


                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

                httpURLConnection.setReadTimeout(5000); //5초안에 응답이 오지 않으면 예외가 발생합니다.

                httpURLConnection.setConnectTimeout(5000); //5초안에 연결이 안되면 예외가 발생합니다.

                httpURLConnection.setRequestMethod("POST"); //요청 방식을 POST로 합니다.
                httpURLConnection.setDoInput(true);
                Log.e(TAG, "GetData : 666 ");

                httpURLConnection.connect();


                OutputStream outputStream = httpURLConnection.getOutputStream();
                outputStream.write(postParameters.getBytes("UTF-8")); //전송할 데이터가 저장된 변수를 이곳에 입력합니다. 인코딩을 고려해줘야 합니다.

                outputStream.flush();
                outputStream.close();



                // 3. 응답을 읽습니다.

                int responseStatusCode = httpURLConnection.getResponseCode();
                Log.d(TAG, "POST response code - " + responseStatusCode);

                InputStream inputStream;
                if(responseStatusCode == HttpURLConnection.HTTP_OK) {

                    // 정상적인 응답 데이터
                    inputStream = httpURLConnection.getInputStream();//연결이 성공되면 inputstream에 httpURLConnection에 담긴 값을 넣는다.
                }
                else{

                    // 에러 발생

                    inputStream = httpURLConnection.getErrorStream();//연결이 안되면 inputstream에 에러값을 넣는다.
                }



                // 4. StringBuilder를 사용하여 수신되는 데이터를 저장합니다.
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                StringBuilder sb = new StringBuilder();
                String line = null;

                while((line = bufferedReader.readLine()) != null){
                    sb.append(line);
                }


                bufferedReader.close();



                // 5. 저장된 데이터를 스트링으로 변환하여 리턴합니다.

                Log.e(TAG,"sb"+sb.toString());


                return sb.toString();


            } catch (Exception e) {

                Log.d(TAG, "InsertData: Error ", e);

                return new String("Error: " + e.getMessage());
            }

        }
    }

}

