package com.example.coupleapp.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.coupleapp.R;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class SignActivity extends AppCompatActivity {

    private static String IP_ADDRESS = "13.125.232.78";
    private static String TAG = "phptest";

    private EditText et_email;
    private EditText et_password;
    private TextView mTextViewResult;
    private String email;
    private String password;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign);



        et_email = (EditText)findViewById(R.id.et_email);
        et_password = (EditText)findViewById(R.id.et_password);
        mTextViewResult = (TextView)findViewById(R.id.mTextViewResult);

        mTextViewResult.setMovementMethod(new ScrollingMovementMethod());


        Button btn_sign = (Button)findViewById(R.id.btn_sign);
        btn_sign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                 email = et_email.getText().toString().trim();
                 password = et_password.getText().toString().trim();

                InsertData task = new InsertData();
                task.execute("http://" + IP_ADDRESS + "/insert.php", email,password);




            }
        });

    }



    class InsertData extends AsyncTask<String, Void, String> {
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = ProgressDialog.show(SignActivity.this,
                    "Please Wait", null, true, true);
        }


        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            progressDialog.dismiss();
            Log.d(TAG, "POST result  - " + result);

            //정보를 입력하는데 성공을 하게되면
            if(result.equals( "1")){
                //쉐어드 프리펀스를 이용해서 로그인 유지를 하기위해 정보를 저장한다.
                SharedPreferences appData = getSharedPreferences("LOGIN",MODE_PRIVATE);
                SharedPreferences.Editor editor = appData.edit();
                editor.putBoolean("SAVE_LOGIN_DATA", true);
                editor.putString("et_email", email);
                editor.apply();
                Log.e(TAG,"입력성공시 ");

                Intent intent = new Intent(SignActivity.this,ConnectionActivity.class);
                startActivity(intent);
            }else if(result.equals("email또는 password를 확인하세요.")){
                mTextViewResult.setText(result);
                et_email.setText("");
                et_password.setText("");
                Log.e(TAG,"입력성공시 ");
            }else{
                mTextViewResult.setText(result);
            }
        }


        @Override
        protected String doInBackground(String... params) {

            String email = (String)params[1];
            Log.d(TAG, "POST response  - " + email);
            String password = (String)params[2];
            Log.d(TAG, "POST response  - " + password);

            // 1. PHP 파일을 실행시킬 수 있는 주소와 전송할 데이터를 준비합니다.

            // POST 방식으로 데이터 전달시에는 데이터가 주소에 직접 입력되지 않습니다.
            String serverURL = (String)params[0];
            Log.d(TAG, "POST response  - " + serverURL);



            // HTTP 메시지 본문에 포함되어 전송되기 때문에 따로 데이터를 준비해야 합니다.

            // 전송할 데이터는 “이름=값” 형식이며 여러 개를 보내야 할 경우에는 항목 사이에 &를 추가합니다.

            // 여기에 적어준 이름을 나중에 PHP에서 사용하여 값을 얻게 됩니다.

            String postParameters = "email=" + email + "&password=" + password;
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
