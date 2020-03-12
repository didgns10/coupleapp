package com.example.coupleapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.coupleapp.Activity.ApiClient;
import com.example.coupleapp.Activity.ApiInterface;
import com.example.coupleapp.Activity.ImageClass;
import com.example.coupleapp.Activity.MainActivity;
import com.example.coupleapp.Activity.StoryActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StoryUploadActivity extends AppCompatActivity {

    private SharedPreferences sf;
    private SharedPreferences sf_idx;
    private String email;
    private String couple_idx;

    private EditText et_story_title,et_date;
    private ImageButton imageButton_back;
    private FloatingActionButton ftbt_image;
    private Button bt_upload;
    private TextView tv_imagecnt;

    private static final int IMG_REQUEST = 777;
    private final int REQUEST_CODE_READ_STORAGE = 2;

    private Bitmap bitmap;

    private ArrayList<Bitmap> arrayList;

    private ImageClass imageClass1;

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story_upload);

        //로그인 저장 정보
        sf = getSharedPreferences("LOGIN",MODE_PRIVATE);
        email = sf.getString("et_email","");

        //커플 인덱스 번호가져오기
        sf_idx =getSharedPreferences("COPLE",MODE_PRIVATE);
        couple_idx = sf_idx.getString("cople_idx","");

        et_story_title = findViewById(R.id.et_stroy_title);
        et_date = findViewById(R.id.et_date);
        imageButton_back = findViewById(R.id.imageButton_back);
        ftbt_image = findViewById(R.id.ftbt_image);
        bt_upload = findViewById(R.id.bt_upload);

        //이미지 추가 버튼을 누르게 되면
        ftbt_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showChooser();
            }
        });

        bt_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadImage();
            }
        });

        //비트맵의 어레이 리스트를 생성한다.
        arrayList = new ArrayList<>();
    }

    //이미지를 선택하는 함수구현
    private void showChooser() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(intent, REQUEST_CODE_READ_STORAGE);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent resultData) {
        super.onActivityResult(requestCode, resultCode, resultData);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CODE_READ_STORAGE) {
                if (resultData != null) {
                    if (resultData.getClipData() != null) { //사진 여러장을 선택을 했을경우
                        int count = resultData.getClipData().getItemCount(); // 그 사진의 번호를 매긴다.
                        int currentItem = 0;
                        while (currentItem < count) {   //0부터 사진의 갯수 만큼의 와일문을 돌려서
                            Uri imageUri = resultData.getClipData().getItemAt(currentItem).getUri(); //imaguri의 변수에 담는다.
                            currentItem = currentItem + 1;

                            Log.d("Uri Selected", imageUri.toString());

                            try {
                                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),imageUri); //그다음 해당 uri의 비트맵에 저장을하고
                                arrayList.add(bitmap); //비트맵 배열에 담는다.

                            } catch (Exception e) {
                            }
                        }
                    } else if (resultData.getData() != null) {  //이미지가 하나만 선택된경우에는

                        final Uri uri = resultData.getData();

                        try {
                            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),uri);
                            arrayList.add(bitmap);

                        } catch (Exception e) {
                        }
                    }
                }
            }
        }
    }


    private void uploadImage(){

        if(arrayList != null){ //배열이 담겨져있으면

            String Image = imageToString(arrayList.get(0)); //배열의 순서에 있는 배열값을 (이미지를) String으로 변환시켜준다.
            String Title = et_story_title.getText().toString();  //타이틀 이고
            String thumb = "thumb";
            String album = "스토리";
            String day = et_date.getText().toString();

            Log.e("어레이",Image);
            Log.e("어레이",Title);
            Log.e("어레이",arrayList.get(0)+":0");
            ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class); //포스트로 넘길것을 해당 클래스와 인터페이스에 정의되어있는것을 호출해준다.
            Call<ImageClass> call = apiInterface.uploadImage(couple_idx,Title,Image,thumb,day,album); //imageclass 에 있는것을 업로드 해주는부분

            call.enqueue(new Callback<ImageClass>() {
                @Override
                public void onResponse(Call<ImageClass> call, Response<ImageClass> response) {  // 콜해준다음에 반응

                    ImageClass imageClass = response.body();
                    Toast.makeText(StoryUploadActivity.this,"Server Response: "+imageClass.getResponse(),Toast.LENGTH_SHORT).show();


                }

                @Override
                public void onFailure(Call<ImageClass> call, Throwable t) {

                }
            });


            for(int i=1; i< arrayList.size(); i++){ //배열의 사이즈에 맞게 포문을 돌려주고
                String Image1 = imageToString(arrayList.get(i)); //배열의 순서에 있는 배열값을 (이미지를) String으로 변환시켜준다.
                String Title1 = et_story_title.getText().toString();  //타이틀 이고
                String thumb1 = ""+i;
                String album1 = "스토리";
                String day1 = et_date.getText().toString();

                Log.e("어레이",arrayList.get(i)+":"+i);
                ApiInterface apiInterface1 = ApiClient.getApiClient().create(ApiInterface.class); //포스트로 넘길것을 해당 클래스와 인터페이스에 정의되어있는것을 호출해준다.
                Call<ImageClass> call1 = apiInterface1.uploadImage(couple_idx,Title1,Image1,thumb1,day1,album1); //imageclass 에 있는것을 업로드 해주는부분

                call1.enqueue(new Callback<ImageClass>() {
                    @Override
                    public void onResponse(Call<ImageClass> call, Response<ImageClass> response) {  // 콜해준다음에 반응

                        imageClass1 = response.body();
                        Toast.makeText(StoryUploadActivity.this,"Server Response: "+imageClass1.getResponse(),Toast.LENGTH_SHORT).show();


                    }

                    @Override
                    public void onFailure(Call<ImageClass> call, Throwable t) {

                    }
                });
            }
            if(imageClass1.getResponse().equals("yes"))
        }


    }

    //비트맵을 스트링으로 변환 시켜주는 함수
    private String imageToString(Bitmap bit){
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bit.compress(Bitmap.CompressFormat.JPEG,100,byteArrayOutputStream);
        byte[] imgByte = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(imgByte,Base64.DEFAULT);
    }
    private void updateLabel() {
        String myFormat = "yyyy-MM-dd";    // 출력형식   2018/11/28
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.KOREA);

        et_date.setText(sdf.format(myCalendar.getTime()));
    }
}
