package com.example.coupleapp.Activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.coupleapp.Adapter.StoryThumbAdapter;
import com.example.coupleapp.Model.StoryThumData;
import com.example.coupleapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Objects;

import static android.content.Context.MODE_PRIVATE;


public class StoryFragment extends Fragment {

    private RecyclerView rv;
    private static String IP_ADDRESS="13.125.232.78";  // 퍼블릭 IPv4 주소
    private static String TAG = "스토리";          // 로그에 사용할 태그

    private ArrayList<StoryThumData> mArrayList;             // 모델 클래스의 데이터를 받아 리사이클러뷰에 뿌리는 데 쓸 ArrayList
    private StoryThumbAdapter mAdapter;                       // 리사이클러뷰 어댑터
    private String mJsonString;                         // JSON 값을 저장할 String 변수

    private SharedPreferences sf;
    private SharedPreferences sf_idx;

    private String email;
    private String couple_idx;

    public StoryFragment()
    {
        // Required empty public constructor (디폴트 생성자)
    }

    /* 프래그먼트의 화면을 그리는 메서드, onCreate()라고 생각하면 됨 */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {

        //로그인 저장 정보
        sf = getActivity().getSharedPreferences("LOGIN",MODE_PRIVATE);
        email = sf.getString("et_email","");

        //커플 인덱스 번호가져오기
        sf_idx =getActivity().getSharedPreferences("COPLE",MODE_PRIVATE);
        couple_idx = sf_idx.getString("cople_idx","");

        Log.e("로그",couple_idx);

        // 프래그먼트에 리사이클러뷰, 텍스트뷰 등을 표시하려면 먼저 ViewGroup 클래스의 객체를 만들고, fragment의 xml 파일을 inflate해야 한다
        ViewGroup viewGroup = (ViewGroup) inflater.inflate(R.layout.fragment_story, container, false);
//        mTextViewResult = (TextView) viewGroup.findViewById(R.id.textView_main_result);

        // setMovementMethod(new ScrollingMovementMethod()) : 텍스트뷰를 스크롤 가능하게 만들어주는 함수
        // 텍스트뷰 안에 너무 긴 텍스트가 들어갔을 때 이걸 쓰면 리사이클러뷰마냥 상하 스크롤해서 볼 수 있다
//        mTextViewResult.setMovementMethod(new ScrollingMovementMethod());

        // 리사이클러뷰 선언
        rv = (RecyclerView) viewGroup.findViewById(R.id.recyclerview_story);

        // 프래그먼트기 때문에 context가 아니라 getActivity()를 쓴다!!!
        rv.setLayoutManager(new LinearLayoutManager(getActivity()));

        // 프래그먼트기 때문에 구분선을 줄 때 context 부분에 getActivity()를 넣어야 한다
        // 그냥 getActivity()만 넣으면 노란 박스 쳐져서 안 보이게 하려고 getActivity()를 다르게 표현함
        rv.addItemDecoration(new DividerItemDecoration(Objects.requireNonNull(getActivity()), DividerItemDecoration.VERTICAL));

        // 리사이클러뷰에 연결되서 데이터를 뿌릴 ArrayList 선언
        mArrayList = new ArrayList<>();

        // 프래그먼트에서 리사이클러뷰의 어댑터를 붙일 땐 context 쓰는 부분에 getActivity()를 쓴다
        mAdapter = new StoryThumbAdapter(mArrayList,getActivity());
        rv.setAdapter(mAdapter);

        // 버튼 위에 표시되는 텍스트뷰에 mArrayList의 내용들을 뿌리는데 이것들을 전부 지우고
        mArrayList.clear();

        // 어댑터에 데이터가 변경됐다는 걸 알린다
        mAdapter.notifyDataSetChanged();

/*
        // 그 다음 AsyncTask 객체를 만들어 execute()한다
        GetData task = new GetData();

        // execute() 사용 시 DB의 값을 JSON 형태로 가져오는 코드가 적힌 php 파일의 경로를 적어
        // AsyncTask로 값들을 JSON 형태로 가져올 수 있게 한다
        task.execute( "http://" + IP_ADDRESS + "/story_thumb.php?couple_idx="+couple_idx, "");
*/

        // inflate() 대신 위에서 선언한 ViewGroup의 객체를 리턴시켜 fragment.xml의 뷰들이 보이게 한다
        return viewGroup;
    }

    @Override
    public void onResume() {
        super.onResume();
        // 그 다음 AsyncTask 객체를 만들어 execute()한다
        mArrayList.clear();
        // 그 다음 AsyncTask 객체를 만들어 execute()한다
        GetData task = new GetData();

        // execute() 사용 시 DB의 값을 JSON 형태로 가져오는 코드가 적힌 php 파일의 경로를 적어
        // AsyncTask로 값들을 JSON 형태로 가져올 수 있게 한다
        task.execute( "http://" + IP_ADDRESS + "/story_thumb.php?couple_idx="+couple_idx, "");
    }

    /* HTTPUrlConnection을 써서 POST 방식으로 phpmyadmin DB에서 값들을 가져오는 AsyncTask 클래스 정의 */
    private class GetData extends AsyncTask<String, Void, String> {

        ProgressDialog progressDialog;
        String errorString = null;

        /* AsyncTask 작업 시작 전에 UI 처리할 내용을 정의하는 함수 */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

/*            // 프래그먼트에 프로그레스 다이얼로그를 띄우고, 값이 가져와지는 동안 기다리라는 메시지를 띄운다
            // 마찬가지로 프래그먼트를 쓰기 때문에 context 대신 getActivity() 사용
            progressDialog = ProgressDialog.show(getActivity(),
                    "Please Wait",
                    null,
                    true,
                    true);*/
        }

        /* AsyncTask 작업 종료 후 UI 처리할 내용을 정의하는 함수 */
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            // 프로그레스 다이얼로그를 죽이고
           // progressDialog.dismiss();

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

        String TAG_JSON="story_thumb";                         // JSON 배열의 이름

        /* DB 컬럼명을 적는 String 변수 */
        String TAG_TITLE = "title";                      // 정적 이미지(나중에 DB에서 이미지 뽑아올 것)
        String TAG_IMG = "img";     // 밑으로 3개는 DB 컬럼명임 (운동명)
        String TAG_IMGDAY = "img_day";                // 상중하 중의 난이도

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
                String img = item.getString(TAG_IMG);   // 운동 이름
                String img_day = item.getString(TAG_IMGDAY);         // 상중하 중 하나의 난이도

                // 데이터 모델 클래스 객체 선언 후 settter()로 컬럼에서 값 추출
                StoryThumData storyThumData = new StoryThumData();

                storyThumData.setStorytitle(titie);
                storyThumData.setStoryday(img_day);
                storyThumData.setStoryThumbimg(img);

                // 데이터 모델 클래스 객체를 리사이클러뷰에 연결된 ArrayList에 삽입
                mArrayList.add(storyThumData);

                // ArrayList에 변동이 생겼으니 어댑터에 알림
                mAdapter.notifyDataSetChanged();
            }

        } catch (JSONException e) {
            // 에러 뜨면 결과 출력
            Log.e(TAG, "showResult : ", e);
        }

    }   // showResult() end

}