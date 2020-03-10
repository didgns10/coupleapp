package com.example.coupleapp.Activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.coupleapp.R;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileWriteActivity extends AppCompatActivity {

    private EditText et_name;
    private Button btn_start;
    private ImageView imgv_profile;
    private EditText et_birthday;
    private EditText et_date;
    private RadioGroup radioGroup;
    private RadioButton rbtn_man,rbtn_woman;
    private TextView tv_result;

    private String sex;
    private String birthday;
    private String name;
    private String email;
    private String date;
    private String profileimg;
    private Boolean signok;
    private Bitmap bitmap;

    private static final int IMG_REQUEST = 777;
    private static String IP_ADDRESS = "13.125.232.78";
    private static String TAG = "phptest";
    private SharedPreferences sf;

    Calendar myCalendar = Calendar.getInstance();

    DatePickerDialog.OnDateSetListener myDatePicker = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, month);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel();
        }
    };

    Calendar myCalendar1 = Calendar.getInstance();

    DatePickerDialog.OnDateSetListener myDatePicker1 = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            myCalendar1.set(Calendar.YEAR, year);
            myCalendar1.set(Calendar.MONTH, month);
            myCalendar1.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel1();
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_write);


        rbtn_man = (RadioButton) findViewById(R.id.rbtn_man);
        rbtn_woman = (RadioButton) findViewById(R.id.rbtn_woman);
        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        et_birthday = (EditText) findViewById(R.id.et_birthday);
        et_name = (EditText) findViewById(R.id.et_name);
        et_date = (EditText)findViewById(R.id.et_date);
        btn_start = (Button)findViewById(R.id.btn_start);
        tv_result = (TextView)findViewById(R.id.tv_result);
        imgv_profile = (ImageView)findViewById(R.id.imgv_profile);

        //저장된 값을 불러오기 위해 같은 네임파일을 찾음.
        sf = getSharedPreferences("LOGIN",MODE_PRIVATE);
        email = sf.getString("et_email","");
        Log.e("login",email);

        //프로필 설정이 완료된경우
        SharedPreferences sign_ok = getSharedPreferences("SIGNOK",MODE_PRIVATE);
        signok = sign_ok.getBoolean("SIGNOK"+email,false);
        Log.e("signok",signok+"");

        if(signok){
            Intent intent = new Intent(ProfileWriteActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }

        rbtn_man.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sex = "남성";
            }
        });
        rbtn_woman.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sex = "여성";
            }
        });

        et_birthday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(ProfileWriteActivity.this, myDatePicker, myCalendar.get(Calendar.YEAR),
                        myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        et_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(ProfileWriteActivity.this, myDatePicker1, myCalendar1.get(Calendar.YEAR),
                        myCalendar1.get(Calendar.MONTH), myCalendar1.get(Calendar.DAY_OF_MONTH)).show();
            }
        });


        //이미지프로필을 클릭하면
        imgv_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                seletImage();
            }
        });

        btn_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                uploadImage();


            }
        });

    }
    //프로필 버튼을 클릭하게 되면 앨범에서 선택하게 해주는 함수
    private void seletImage(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,IMG_REQUEST);
    }

    // 레트로핏을 통한 프로필 업로드 과정
    private void uploadImage(){


        name = et_name.getText().toString().trim();
        birthday = et_birthday.getText().toString().trim();
        date = et_date.getText().toString().trim();
        profileimg = imageToString();

        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<ProfileClass> call = apiInterface.uploadImage1(email,name,birthday,sex,profileimg,date);

        call.enqueue(new Callback<ProfileClass>() {
            @Override
            public void onResponse(Call<ProfileClass> call, Response<ProfileClass> response) {

                ProfileClass profileClass = response.body();

                if(profileClass.getResponse().equals("no")){
                    Toast.makeText(ProfileWriteActivity.this,"빈칸없이 채워주세요",Toast.LENGTH_SHORT).show();
                }else if(profileClass.getResponse().equals("yes")){
                    //성공했을경우 회원가입 성공이라는 쉐어드에 담아 메인으로 넘어가게끔 한다.
                    SharedPreferences grant = getSharedPreferences("SIGNOK", MODE_PRIVATE);
                    SharedPreferences.Editor editor = grant.edit();
                    editor.putBoolean("SIGNOK" + email, true);
                    editor.apply();
                    Toast.makeText(ProfileWriteActivity.this,"회원가입을 완료했습니다.",Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(ProfileWriteActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }else{
                    Toast.makeText(ProfileWriteActivity.this,"회원가입에 실패하셨습니다.",Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<ProfileClass> call, Throwable t) {

            }
        });

    }

    //앨범에서 가져오는 데이터값들
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==IMG_REQUEST && resultCode==RESULT_OK && data!=null){
            Uri path = data.getData();

            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),path);
                imgv_profile.setImageBitmap(bitmap);
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

    private void updateLabel() {
        String myFormat = "yyyyMMdd";    // 출력형식   2018/11/28
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.KOREA);

        et_birthday.setText(sdf.format(myCalendar.getTime()));
    }

    private void updateLabel1() {
        String myFormat = "yyyyMMdd";    // 출력형식   2018/11/28
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.KOREA);

        et_date.setText(sdf.format(myCalendar1.getTime()));
    }

}
