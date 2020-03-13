package com.example.coupleapp.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.coupleapp.Adapter.AlbumAdapter;
import com.example.coupleapp.Adapter.SelectImageAdapter;
import com.example.coupleapp.Model.AlbumData;
import com.example.coupleapp.Model.SelectImageData;
import com.example.coupleapp.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PhotoaddActivity extends AppCompatActivity {

    private Spinner spinner;
    private ArrayList<String> arrayLists;
    private ArrayAdapter<String> arrayAdapter;


    private static String IP_ADDRESS="13.125.232.78";  // 퍼블릭 IPv4 주소
    private static String TAG = "스토리";          // 로그에 사용할 태그

    private ArrayList<AlbumData> mArrayList;             // 모델 클래스의 데이터를 받아 리사이클러뷰에 뿌리는 데 쓸 ArrayList
    private String mJsonString;                         // JSON 값을 저장할 String 변수

    private String album;

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

    private PhotoClass imageClass1;

    private boolean size=false;

    private RecyclerView recyclerView;
    private ArrayList<SelectImageData> selectlist;
    private SelectImageAdapter selectImageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photoadd);

        //로그인 저장 정보
        sf = getSharedPreferences("LOGIN",MODE_PRIVATE);
        email = sf.getString("et_email","");

        //커플 인덱스 번호가져오기
        sf_idx =getSharedPreferences("COPLE",MODE_PRIVATE);
        couple_idx = sf_idx.getString("cople_idx","");

        imageButton_back = findViewById(R.id.imageButton_back);
        ftbt_image = findViewById(R.id.ftbt_image);
        bt_upload = findViewById(R.id.bt_upload);
        tv_imagecnt =findViewById(R.id.tv_imagecnt);

        //이미지 추가 버튼을 누르게 되면
        ftbt_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                arrayList.clear();
                selectlist.clear();
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

        //스피너의 어레이 리스트
        arrayLists = new ArrayList<>();
        // 그 다음 AsyncTask 객체를 만들어 execute()한다

        GetData task = new GetData();

        // execute() 사용 시 DB의 값을 JSON 형태로 가져오는 코드가 적힌 php 파일의 경로를 적어
        // AsyncTask로 값들을 JSON 형태로 가져올 수 있게 한다
        task.execute( "http://" + IP_ADDRESS + "/album.php?couple_idx="+couple_idx, "");

        arrayAdapter = new ArrayAdapter<>(getApplicationContext(),
                android.R.layout.simple_spinner_dropdown_item,
                arrayLists);

        arrayAdapter.notifyDataSetChanged();

        spinner = (Spinner)findViewById(R.id.spinner);
        spinner.setAdapter(arrayAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                album = arrayLists.get(i);
                Log.e("스피너",album);
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        selectlist = new ArrayList<>();

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        recyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));

        selectImageAdapter = new SelectImageAdapter(selectlist,PhotoaddActivity.this);
        recyclerView.setAdapter(selectImageAdapter);

        selectlist.clear();
        selectImageAdapter.notifyDataSetChanged();


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
                                bitmap = Bitmap.createScaledBitmap(bitmap,700,900,true);
                                arrayList.add(bitmap); //비트맵 배열에 담는다.
                                SelectImageData selectImageData = new SelectImageData();

                                selectImageData.setBitmap(bitmap);
                                selectlist.add(selectImageData);
                                selectImageAdapter.notifyDataSetChanged();

                                size=true;

                            } catch (Exception e) {
                            }
                        }
                        tv_imagecnt.setText("사진 총 "+arrayList.size()+"개");
                    } else if (resultData.getData() != null) {  //이미지가 하나만 선택된경우에는

                        final Uri uri = resultData.getData();

                        try {
                            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),uri);
                            bitmap = Bitmap.createScaledBitmap(bitmap,700,900,true);
                            arrayList.add(bitmap);
                            SelectImageData selectImageData = new SelectImageData();

                            selectImageData.setBitmap(bitmap);
                            selectlist.add(selectImageData);
                            selectImageAdapter.notifyDataSetChanged();

                            tv_imagecnt.setText("사진 총 "+arrayList.size()+"개");
                            size=false;

                        } catch (Exception e) {
                        }
                    }
                }
            }
        }
    }


    private void uploadImage(){

        if(arrayList != null) { //배열이 담겨져있으면

            if (size == false) {
                String Image = imageToString(arrayList.get(0)); //배열의 순서에 있는 배열값을 (이미지를) String으로 변환시켜준다.
                Log.e("로그찡", "" + arrayList.size());
                Log.e("어레이", Image);
                Log.e("어레이", arrayList.get(0) + ":0");
                ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class); //포스트로 넘길것을 해당 클래스와 인터페이스에 정의되어있는것을 호출해준다.
                Call<PhotoClass> call = apiInterface.uploadImage2(couple_idx,Image,album); //imageclass 에 있는것을 업로드 해주는부분

                call.enqueue(new Callback<PhotoClass>() {
                    @Override
                    public void onResponse(Call<PhotoClass> call, Response<PhotoClass> response) {  // 콜해준다음에 반응

                        PhotoClass imageClass = response.body();
                        //   Toast.makeText(StoryUploadActivity.this,"Server Response: "+imageClass.getResponse(),Toast.LENGTH_SHORT).show();


                    }

                    @Override
                    public void onFailure(Call<PhotoClass> call, Throwable t) {

                    }
                });
            } else {

                String Image = imageToString(arrayList.get(0)); //배열의 순서에 있는 배열값을 (이미지를) String으로 변환시켜준다.
                Log.e("로그찡", "" + arrayList.size());

                Log.e("어레이", Image);
                Log.e("어레이", arrayList.get(0) + ":0");
                ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class); //포스트로 넘길것을 해당 클래스와 인터페이스에 정의되어있는것을 호출해준다.
                Call<PhotoClass> call = apiInterface.uploadImage2(couple_idx, Image, album); //imageclass 에 있는것을 업로드 해주는부분

                call.enqueue(new Callback<PhotoClass>() {
                    @Override
                    public void onResponse(Call<PhotoClass> call, Response<PhotoClass> response) {  // 콜해준다음에 반응

                        PhotoClass imageClass = response.body();
                        //   Toast.makeText(StoryUploadActivity.this,"Server Response: "+imageClass.getResponse(),Toast.LENGTH_SHORT).show();


                    }

                    @Override
                    public void onFailure(Call<PhotoClass> call, Throwable t) {

                    }
                });

                for (int i =1; i < arrayList.size()-1; i++) { //배열의 사이즈에 맞게 포문을 돌려주고
                    String Image1 = imageToString(arrayList.get(i)); //배열의 순서에 있는 배열값을 (이미지를) String으로 변환시켜준다.

                    Log.e("어레이", arrayList.get(i) + ":" + i);
                    ApiInterface apiInterface1 = ApiClient.getApiClient().create(ApiInterface.class); //포스트로 넘길것을 해당 클래스와 인터페이스에 정의되어있는것을 호출해준다.
                    Call<PhotoClass> call1 = apiInterface1.uploadImage2(couple_idx, Image1, album); //imageclass 에 있는것을 업로드 해주는부분

                    call1.enqueue(new Callback<PhotoClass>() {
                        @Override
                        public void onResponse(Call<PhotoClass> call, Response<PhotoClass> response) {  // 콜해준다음에 반응

                            imageClass1 = response.body();
                            //  Toast.makeText(StoryUploadActivity.this,"Server Response: "+imageClass1.getResponse(),Toast.LENGTH_SHORT).show();


                        }

                        @Override
                        public void onFailure(Call<PhotoClass> call, Throwable t) {

                        }
                    });
                }
                String Image1 = imageToString(arrayList.get(arrayList.size() - 1)); //배열의 순서에 있는 배열값을 (이미지를) String으로 변환시켜준다.

                Log.e("어레이", arrayList.get(arrayList.size() - 1) + ":" + (arrayList.size() - 1));
                ApiInterface apiInterface1 = ApiClient.getApiClient().create(ApiInterface.class); //포스트로 넘길것을 해당 클래스와 인터페이스에 정의되어있는것을 호출해준다.
                Call<PhotoClass> call1 = apiInterface1.uploadImage2(couple_idx, Image1, album); //imageclass 에 있는것을 업로드 해주는부분

                call1.enqueue(new Callback<PhotoClass>() {
                    @Override
                    public void onResponse(Call<PhotoClass> call, Response<PhotoClass> response) {  // 콜해준다음에 반응

                        imageClass1 = response.body();
                        //  Toast.makeText(StoryUploadActivity.this,"Server Response: "+imageClass1.getResponse(),Toast.LENGTH_SHORT).show();
                        if (imageClass1.getResponse().equals("yes")) {
                            //  Toast.makeText(StoryUploadActivity.this,"Server Response: "+imageClass1.getResponse(),Toast.LENGTH_SHORT).show();
                            Toast.makeText(PhotoaddActivity.this,"사진이 업로드 되었습니다.",Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(PhotoaddActivity.this, StoryActivity.class);
                            startActivity(intent);
                            finish();
                        }

                    }

                    @Override
                    public void onFailure(Call<PhotoClass> call, Throwable t) {

                    }
                });

            }
        }


    }

    //비트맵을 스트링으로 변환 시켜주는 함수
    private String imageToString(Bitmap bit){
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bit.compress(Bitmap.CompressFormat.JPEG,100,byteArrayOutputStream);
        byte[] imgByte = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(imgByte,Base64.DEFAULT);
    }

    /* HTTPUrlConnection을 써서 POST 방식으로 phpmyadmin DB에서 값들을 가져오는 AsyncTask 클래스 정의 */
    private class GetData extends AsyncTask<String, Void, String> {

        ProgressDialog progressDialog;
        String errorString = null;

        /* AsyncTask 작업 시작 전에 UI 처리할 내용을 정의하는 함수 */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        /* AsyncTask 작업 종료 후 UI 처리할 내용을 정의하는 함수 */
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);


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

        String TAG_JSON="album";                         // JSON 배열의 이름

        /* DB 컬럼명을 적는 String 변수 */
        String TAG_TITLE = "albumtitle";                      // 정적 이미지(나중에 DB에서 이미지 뽑아올 것)

        try {

            // JSON 배열로 받아오기 위해 JSONObject, JSONArray 차례로 선언
            JSONObject jsonObject = new JSONObject(mJsonString);

            // JSONArray에는 root를 넣어서 root란 이름의 JSON 배열을 가져올 수 있도록 한다
            JSONArray jsonArray = jsonObject.getJSONArray(TAG_JSON);

            // for문으로 JSONArray의 길이만큼 반복해서 String 변수에 담는다
            for(int i = 0; i < jsonArray.length(); i++){

                JSONObject item = jsonArray.getJSONObject(i);

                // 컬럼의 값들을 getString()으로 받아와서 String 변수에 저장
                // 정적 이미지는 나중에 DB에서 받아와야 한다. 일단 기본 이미지 채워넣음
                String titie = item.getString(TAG_TITLE);

                // 데이터 모델 클래스 객체 선언 후 settter()로 컬럼에서 값 추출
                arrayLists.add(titie);


                // ArrayList에 변동이 생겼으니 어댑터에 알림
                arrayAdapter.notifyDataSetChanged();
            }

        } catch (JSONException e) {
            // 에러 뜨면 결과 출력
            Log.e(TAG, "showResult : ", e);
        }

    }   // showResult() end

}
