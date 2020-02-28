package com.example.coupleapp.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.coupleapp.R;

public class StartpageActivity extends AppCompatActivity {

    private TextView tv_login;
    private Button btn_sign;

    private boolean saveLoginData;
    private String email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_startpage);

        tv_login = (TextView)findViewById(R.id.tv_login);
        btn_sign = (Button)findViewById(R.id.btn_sign);

        //저장된 값을 불러오기 위해 같은 네임파일을 찾음.
        SharedPreferences sf = getSharedPreferences("LOGIN",MODE_PRIVATE);
        //text라는 key에 저장된 값이 있는지 확인. 아무값도 들어있지 않으면 ""를 반환
        email = sf.getString("et_email","");
        Log.e("login",email);
        saveLoginData = sf.getBoolean("SAVE_LOGIN_DATA",false);
        Log.e("login",saveLoginData+"");

        if(saveLoginData){
            Intent intent = new Intent(StartpageActivity.this,ConnectionActivity.class);
            startActivity(intent);
            finish();
        }


        btn_sign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StartpageActivity.this,SignActivity.class);
                startActivity(intent);
                finish();
            }
        });

        tv_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StartpageActivity.this,LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }
}
