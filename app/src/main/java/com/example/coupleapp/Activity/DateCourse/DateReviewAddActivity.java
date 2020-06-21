package com.example.coupleapp.Activity.DateCourse;

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
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.coupleapp.Adapter.SelectImageAdapter;
import com.example.coupleapp.Model.DateReviewModel.ApiClientReview;
import com.example.coupleapp.Model.DateReviewModel.ApiInterfaceReview;
import com.example.coupleapp.Model.DateReviewModel.ReviewClass;
import com.example.coupleapp.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DateReviewAddActivity extends AppCompatActivity {

    private SharedPreferences sf;
    private SharedPreferences sf_idx;
    private SharedPreferences sfc;
    private String email;
    private String couple_idx,imgurl;

    private EditText et_story_title,et_date;
    private ImageButton imageButton_back;
    private FloatingActionButton ftbt_image;
    private Button bt_upload;
    private TextView tv_imagecnt;
    private ImageView imgv_review;
    private RatingBar ratingBar2;

    private static final int IMG_REQUEST = 777;
    private final int REQUEST_CODE_READ_STORAGE = 2;

    private Bitmap bitmap;

    private ArrayList<Bitmap> arrayList;

    private ReviewClass reviewClass;

    private boolean size=false;

    private RecyclerView recyclerView;
    private ArrayList<SelectImageData> selectlist;
    private SelectImageAdapter selectImageAdapter;

    private String content,date,name,rate,review_img;
    private float rates;

    private String contentid,contenttypeid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_date_review_add);

        //인텐트 받는거
        contentid = getIntent().getStringExtra("contentid");
        contenttypeid = getIntent().getStringExtra("contenttypeid");

        //로그인 저장 정보
        sfc = getSharedPreferences("CHAT",MODE_PRIVATE);
        name = sfc.getString("name","");
        imgurl = sfc.getString("imgurl","");

        //로그인 저장 정보
        sf = getSharedPreferences("LOGIN",MODE_PRIVATE);
        email = sf.getString("et_email","");

        //커플 인덱스 번호가져오기
        sf_idx =getSharedPreferences("COPLE",MODE_PRIVATE);
        couple_idx = sf_idx.getString("cople_idx","");

        long now = System.currentTimeMillis();
        Date mDate = new Date(now);
        SimpleDateFormat simpleDate = new SimpleDateFormat("yyyy-MM-dd");
        date = simpleDate.format(mDate);

        //변수이름은 타이틀인데 실제로는 내용이다
        et_story_title = findViewById(R.id.et_stroy_title);
        imageButton_back = findViewById(R.id.imageButton_back);
        ftbt_image = findViewById(R.id.ftbt_image);
        bt_upload = findViewById(R.id.bt_upload);
        ratingBar2 = findViewById(R.id.ratingBar2);
        imgv_review = findViewById(R.id.imgv_review);
        imageButton_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //이미지 추가 버튼을 누르게 되면
        ftbt_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
           //     arrayList.clear();
            //    selectlist.clear();
                seletImage();
            }
        });

        bt_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadImage();
            }
        });

        ratingBar2.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                rates = rating;
            }
        });
/*        //비트맵의 어레이 리스트를 생성한다.
        arrayList = new ArrayList<>();
        selectlist = new ArrayList<>();

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        recyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));

        selectImageAdapter = new SelectImageAdapter(selectlist,DateReviewAddActivity.this);
        recyclerView.setAdapter(selectImageAdapter);

        selectlist.clear();
        selectImageAdapter.notifyDataSetChanged();*/
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

        //이름,프로필사진,시간,평점,리뷰내용,리뷰이미지
        content = et_story_title.getText().toString();
        rate = rates+"";

        if(bitmap ==null){
            review_img="1";
        }else {
            review_img = imageToString();
        }

        Log.e("리뷰",review_img);

        ApiInterfaceReview apiInterfaceReview = ApiClientReview.getApiClient().create(ApiInterfaceReview.class);
        Call<ReviewClass> call = apiInterfaceReview.uploadReview(name,imgurl,date,rate,content,review_img,contentid,contenttypeid);

        call.enqueue(new Callback<ReviewClass>() {
            @Override
            public void onResponse(Call<ReviewClass> call, Response<ReviewClass> response) {

                ReviewClass reviewClass = response.body();
                Log.e("리뷰",response.body()+"");
                Log.e("리뷰",response.body().getResponse()+"");

                if(reviewClass.getResponse().equals("no")){
                    Toast.makeText(DateReviewAddActivity.this,"빈칸없이 채워주세요",Toast.LENGTH_SHORT).show();
                }else if(reviewClass.getResponse().equals("yes")){

                    Toast.makeText(DateReviewAddActivity.this,"리뷰작성을 완료했습니다.",Toast.LENGTH_SHORT).show();
                    finish();

                }else{
                    Toast.makeText(DateReviewAddActivity.this,"리뷰작성에 실패하셨습니다.",Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<ReviewClass> call, Throwable t) {

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
                bitmap = Bitmap.createScaledBitmap(bitmap,700,900,true);
                imgv_review.setVisibility(View.VISIBLE);
                imgv_review.setImageBitmap(bitmap);
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

}
