package com.example.coupleapp.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import com.example.coupleapp.R;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class VideoTokenActivity extends AppCompatActivity {

    private static String IP_ADDRESS = "13.125.232.78";
    private static String TAG = "phptest";
    private String email;

    private SharedPreferences sf;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_token);

        //로그인 저장 정보
        sf = getSharedPreferences("LOGIN",MODE_PRIVATE);
        email = sf.getString("et_email","");
        Log.e("비디오토큰",email);

        GetData task = new GetData();
        task.execute( "http://" + IP_ADDRESS + "/video/sample/token.php?email="+email, "");
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
                Log.e("비디오토큰",result);
                SharedPreferences grant = getSharedPreferences("VIDEO", MODE_PRIVATE);
                SharedPreferences.Editor editor = grant.edit();
                editor.putString("video_token" + email, result);
                editor.apply();


                Intent intent = new Intent(VideoTokenActivity.this, MainActivity.class);
                startActivity(intent);
                finish();

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
}
