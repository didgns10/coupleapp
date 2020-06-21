package com.example.coupleapp.Activity.Calender;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

import com.example.coupleapp.Activity.AnniversaryActivity;
import com.example.coupleapp.Activity.DateCourse.DatecouseActivity;
import com.example.coupleapp.Activity.MainActivity;
import com.example.coupleapp.Activity.StartpageActivity;
import com.example.coupleapp.Activity.StoryActivity;
import com.example.coupleapp.R;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.CalendarMode;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executors;

public class CalenderDiaryActivity extends AppCompatActivity {

    private CalenderViewPagerAdapter calenderViewPagerAdapter;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private DrawerLayout mDrawerLayout;
    private ImageButton ibtn_add;

    String time,kcal,menu;
    private final OneDayDecorator oneDayDecorator = new OneDayDecorator();
    Cursor cursor;
    MaterialCalendarView materialCalendarView;


    private static String IP_ADDRESS="13.125.232.78";  // 퍼블릭 IPv4 주소
    private static String TAG = "스토리";          // 로그에 사용할 태그
    private String mJsonString;                         // JSON 값을 저장할 String 변수


    private String mJsonString1;                         // JSON 값을 저장할 String 변수

    private ArrayList<String> result1;
    private ArrayList<String> result2;

    private RecyclerView rv;
    private ArrayList<CalenderData> mArrayList;             // 모델 클래스의 데이터를 받아 리사이클러뷰에 뿌리는 데 쓸 ArrayList
    private CalenderAdapter mAdapter;                       // 리사이클러뷰 어댑터

    private int year,month,day;

    private FloatingActionButton fbtn_add;

    private String Month2 ,Day2 ;
    private String shot_Day;

    private SharedPreferences sf;
    private SharedPreferences sf_idx;
    private SharedPreferences sfc;

    private String email;
    private String couple_idx;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calender_diary);

        //로그인 저장 정보
        sf = getSharedPreferences("LOGIN",MODE_PRIVATE);
        email = sf.getString("et_email","");

        //커플 인덱스 번호가져오기
        sf_idx =getSharedPreferences("COPLE",MODE_PRIVATE);
        couple_idx = sf_idx.getString("cople_idx","");

        // Tabbed Activity
        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.viewPager);
        ibtn_add = findViewById(R.id.ibtn_add);
        calenderViewPagerAdapter = new CalenderViewPagerAdapter(getSupportFragmentManager());

        // viewPagerAdapter
        calenderViewPagerAdapter.addFragment(new CalenderFragment(), "");
        calenderViewPagerAdapter.addFragment(new DiaryFragment(), "");
        viewPager.setAdapter(calenderViewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);

        Log.e("순서","메인oncrete");
        // 탭들의 제목 설정
        tabLayout.getTabAt(0).setText("일정");
        tabLayout.getTabAt(1).setText("다이어리");

        long now = System.currentTimeMillis();
        Date mDate = new Date(now);
        SimpleDateFormat simpleDate = new SimpleDateFormat("yyyy-MM-dd");
        shot_Day = simpleDate.format(mDate);

        if(getIntent().getStringExtra("start") != null) {
            shot_Day = getIntent().getStringExtra("start");
        }



        Date day2 = null;
        try {
             day2 = simpleDate.parse(shot_Day);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        result1 =new ArrayList<>();

        result2 =new ArrayList<>();

        //일정 데이터를 가져오기 위한 처리
        GetData task = new GetData();

        // execute() 사용 시 DB의 값을 JSON 형태로 가져오는 코드가 적힌 php 파일의 경로를 적어
        // AsyncTask로 값들을 JSON 형태로 가져올 수 있게 한다
        task.execute( "http://" + IP_ADDRESS + "/alarm_view.php?couple_idx="+couple_idx, "");

        //다이어리 데이터를 가져오기 위한 처리
        GetData2 task2 = new GetData2();

        // execute() 사용 시 DB의 값을 JSON 형태로 가져오는 코드가 적힌 php 파일의 경로를 적어
        // AsyncTask로 값들을 JSON 형태로 가져올 수 있게 한다
        task2.execute( "http://" + IP_ADDRESS + "/diary_view.php?couple_idx="+couple_idx, "");


        materialCalendarView = (MaterialCalendarView)findViewById(R.id.calendarView);

        materialCalendarView.state().edit()
                .setFirstDayOfWeek(Calendar.SUNDAY)
                .setMinimumDate(CalendarDay.from(2010, 0, 1)) // 달력의 시작
                .setMaximumDate(CalendarDay.from(2030, 11, 31)) // 달력의 끝
                .setCalendarDisplayMode(CalendarMode.MONTHS)
                .commit();

        materialCalendarView.addDecorators(
                new SundayDecorator(),
                new SaturdayDecorator(),
                new OneDayDecorator());

        materialCalendarView.setDateSelected(day2,true);


        materialCalendarView.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                int Year = date.getYear();
                int Month = date.getMonth() + 1;
                int Day = date.getDay();

              //  mArrayList.clear();

                Log.i("Year test", Year + "");
                Log.i("Month test", Month + "");
                Log.i("Day test", Day + "");

                Month2 = Integer.toString(Month);
                Day2 = Integer.toString(Day);

                if(Month<10){
                    String Month1 = Integer.toString(Month);
                    Month2 = "0"+Month1;
                }
                if(Day<10){
                    String Day1 = Integer.toString(Day);
                    Day2 = "0"+Day1;
                }

                 shot_Day = Year + "-" + Month2 + "-" + Day2;

                Log.i("shot_Day test", shot_Day + "");
              //  materialCalendarView.clearSelection();

                Intent intent = new Intent("broad");
                intent.putExtra("shot_day", shot_Day);
                LocalBroadcastManager.getInstance(CalenderDiaryActivity.this).sendBroadcast(intent);


            }
        });

        final CharSequence[] items = {"일정추가하기", "다이어리추가하기", "취소"};


        ibtn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 여기서 부터는 알림창의 속성 설정

                AlertDialog.Builder builder = new AlertDialog.Builder(CalenderDiaryActivity.this);     // 여기서 this는 Activity의 this
                builder.setTitle("옵션을 선택하세요")        // 제목 설정

                        .setItems(items, new DialogInterface.OnClickListener(){    // 목록 클릭시 설정

                            public void onClick(DialogInterface dialog, int index){
                                if(items[index].equals("일정추가하기")){
                                    Intent intent = new Intent(CalenderDiaryActivity.this,CalenderAddActivity.class);
                                    intent.putExtra("day",shot_Day);
                                    startActivity(intent);
                                }else if(items[index].equals("다이어리추가하기")){
                                    Intent intent = new Intent(CalenderDiaryActivity.this,DiaryAddActivity.class);
                                    intent.putExtra("day",shot_Day);
                                    startActivity(intent);
                                }else{
                                    dialog.dismiss();
                                }

                            }

                        });
                AlertDialog dialog = builder.create();    // 알림창 객체 생성
                dialog.show();    // 알림창 띄우기

            }
        });
        //네비게이션 드로어 만드는 부분
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false); // 기존 title 지우기
        actionBar.setDisplayHomeAsUpEnabled(true); // 뒤로가기 버튼 만들기
        actionBar.setHomeAsUpIndicator(R.drawable.menu_image); //뒤로가기 버튼 이미지 지정


        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

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
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.addFlags (Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(intent);
                }
                else if(id == R.id.date_course){
                    Intent intent = new Intent(getApplicationContext(), DatecouseActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.addFlags (Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(intent);
                }
                else if(id == R.id.story_album){
                    Intent intent = new Intent(getApplicationContext(), StoryActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.addFlags (Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(intent);
                }
                else if(id == R.id.couple_calender) {
                }else if(id == R.id.couple_dday){

                    Intent intent = new Intent(getApplicationContext(), AnniversaryActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.addFlags (Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(intent);
                }else if(id == R.id.logout){
                    SharedPreferences.Editor editor = sf.edit();
                    editor.putBoolean("SAVE_LOGIN_DATA", false);
                    editor.putString("et_email", "");
                    editor.apply();

                    Intent intent = new Intent(CalenderDiaryActivity.this, StartpageActivity.class);
                    startActivity(intent);
                    finish();
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
    //프래그먼트에 값을 전달하기위한 함수
    public Object getData(){
        return shot_Day;
    }

    @Override
    protected void onResume() {
        super.onResume();
        // 그 다음 AsyncTask 객체를 만들어 execute()한다

        Log.e("순서","메인 onResume");
        Intent intent = new Intent("broad");
        intent.putExtra("shot_day", shot_Day);
        LocalBroadcastManager.getInstance(CalenderDiaryActivity.this).sendBroadcast(intent);
    }

    private class ApiSimulator extends AsyncTask<Void, Void, List<CalendarDay>> {

        ArrayList<String> Time_Result;

        ApiSimulator(ArrayList<String> Time_Result){
            this.Time_Result = Time_Result;
        }

        @Override
        protected List<CalendarDay> doInBackground(@NonNull Void... voids) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            Calendar calendar = Calendar.getInstance();

            Log.e("로그",calendar+"");
            ArrayList<CalendarDay> dates = new ArrayList<>();


            /*특정날짜 달력에 점표시해주는곳*/
            /*월은 0이 1월 년,일은 그대로*/
            //string 문자열인 Time_Result 을 받아와서 ,를 기준으로짜르고 string을 int 로 변환

            for(int i = 0 ; i < Time_Result.size() ; i ++){
                Log.e("로그",Time_Result.get(i)+"i");


                String[] time = Time_Result.get(i).split("-");
                int year = Integer.parseInt(time[0]);
                int month = Integer.parseInt(time[1]);
                int dayy = Integer.parseInt(time[2]);

                calendar.set(year,month-1,dayy);

                CalendarDay day = CalendarDay.from(calendar);
                dates.add(day);
            }


            Log.e("로그1",dates+"");
            return dates;
        }

        @Override
        protected void onPostExecute(@NonNull List<CalendarDay> calendarDays) {
            super.onPostExecute(calendarDays);

            if (isFinishing()) {
                return;
            }

            materialCalendarView.addDecorator(new EventDecorator(Color.RED, calendarDays, CalenderDiaryActivity.this));
        }
    }
    private class ApiSimulator1 extends AsyncTask<Void, Void, List<CalendarDay>> {

        ArrayList<String> Time_Result;

        ApiSimulator1(ArrayList<String> Time_Result){
            this.Time_Result = Time_Result;
        }

        @Override
        protected List<CalendarDay> doInBackground(@NonNull Void... voids) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            Calendar calendar = Calendar.getInstance();

            Log.e("로그",calendar+"");
            ArrayList<CalendarDay> dates = new ArrayList<>();


            /*특정날짜 달력에 점표시해주는곳*/
            /*월은 0이 1월 년,일은 그대로*/
            //string 문자열인 Time_Result 을 받아와서 ,를 기준으로짜르고 string을 int 로 변환

            for(int i = 0 ; i < Time_Result.size() ; i ++){
                Log.e("로그",Time_Result.get(i)+"i");


                String[] time = Time_Result.get(i).split("-");
                int year = Integer.parseInt(time[0]);
                int month = Integer.parseInt(time[1]);
                int dayy = Integer.parseInt(time[2]);

                calendar.set(year,month-1,dayy);

                CalendarDay day = CalendarDay.from(calendar);
                dates.add(day);
            }


            Log.e("로그1",dates+"");
            return dates;
        }

        @Override
        protected void onPostExecute(@NonNull List<CalendarDay> calendarDays) {
            super.onPostExecute(calendarDays);

            if (isFinishing()) {
                return;
            }

            materialCalendarView.addDecorator(new EventDecorator1(Color.BLUE, calendarDays, CalenderDiaryActivity.this));
        }
    }
    /* HTTPUrlConnection을 써서 POST 방식으로 phpmyadmin DB에서 값들을 가져오는 AsyncTask 클래스 정의 */
    private class GetData extends AsyncTask<String, Void, String> {

        ProgressDialog progressDialog;
        String errorString = null;

        /* AsyncTask 작업 시작 전에 UI 처리할 내용을 정의하는 함수 */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            result1.clear();
        }

        /* AsyncTask 작업 종료 후 UI 처리할 내용을 정의하는 함수 */
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

     /*       // 프로그레스 다이얼로그를 죽이고
            progressDialog.dismiss();*/

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
                Log.e("로그",result1+"");

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

        String TAG_JSON="alarm";                         // JSON 배열의 이름

        /* DB 컬럼명을 적는 String 변수 */
        String TAG_IDX = "idx";     // 밑으로 3개는
        String TAG_DATE = "datef";
        String TAG_TITLE = "title";


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
                String idx = item.getString(TAG_IDX);   // 운동 이름
                String date = item.getString(TAG_DATE);
                String title = item.getString(TAG_TITLE);

                result1.add(date);


            }

            new ApiSimulator(result1).executeOnExecutor(Executors.newSingleThreadExecutor());


        } catch (JSONException e) {
            // 에러 뜨면 결과 출력
            Log.e(TAG, "showResult : ", e);
        }


    }   // showResult() end
    /* HTTPUrlConnection을 써서 POST 방식으로 phpmyadmin DB에서 값들을 가져오는 AsyncTask 클래스 정의 */
    private class GetData2 extends AsyncTask<String, Void, String> {

        ProgressDialog progressDialog;
        String errorString = null;

        /* AsyncTask 작업 시작 전에 UI 처리할 내용을 정의하는 함수 */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            result1.clear();
        }

        /* AsyncTask 작업 종료 후 UI 처리할 내용을 정의하는 함수 */
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

     /*       // 프로그레스 다이얼로그를 죽이고
            progressDialog.dismiss();*/

            // doInBackground()의 리턴값이 담긴 result를 버튼 밑 텍스트뷰에 setText()해서 JSON 형태로 받아온 값들을 출력
//            mTextViewResult.setText(result);
            Log.e(TAG, "response - " + result);

            // 결과가 없으면 에러 때문에 못 받아온 거니까 에러 문구를 버튼 밑 텍스트뷰에 출력
            if (result == null) {
//                mTextViewResult.setText(errorString);
            } else {
                // 결과가 있다면 버튼 위 텍스트뷰에 JSON 데이터들을 텍스트뷰 형태에 맞게 출력한다
                mJsonString = result;
                showResult2();
                Log.e("로그",result1+"");

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
    private void showResult2() {

        String TAG_JSON="diary";                         // JSON 배열의 이름

        /* DB 컬럼명을 적는 String 변수 */
        String TAG_IDX = "idx";     // 밑으로 3개는
        String TAG_DATE = "datef";
        String TAG_TITLE = "title";
        String TAG_NAME = "name";


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
                String idx = item.getString(TAG_IDX);   // 운동 이름
                String date = item.getString(TAG_DATE);
                String title = item.getString(TAG_TITLE);
                String name = item.getString(TAG_NAME);

                result2.add(date);


            }

            new ApiSimulator1(result2).executeOnExecutor(Executors.newSingleThreadExecutor());


        } catch (JSONException e) {
            // 에러 뜨면 결과 출력
            Log.e(TAG, "showResult : ", e);
        }


    }   // showResult() end
}

