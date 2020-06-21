package com.example.coupleapp.Activity.DateCourse;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager.widget.ViewPager;

import com.example.coupleapp.Activity.AnniversaryActivity;
import com.example.coupleapp.Activity.Calender.CalenderDiaryActivity;
import com.example.coupleapp.Activity.MainActivity;
import com.example.coupleapp.Activity.Map.Gloval;
import com.example.coupleapp.Activity.Map.GoogleMapActivity;
import com.example.coupleapp.Activity.StartpageActivity;
import com.example.coupleapp.Activity.StoryActivity;
import com.example.coupleapp.Model.DateTotalModel.Arrange;
import com.example.coupleapp.R;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class DatecouseActivity extends AppCompatActivity {

    private DatecousePagerAdapter datecousePagerAdapter;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private DrawerLayout mDrawerLayout;

    private ImageButton imgbtn_location;
    private TextView tv_site;


    private SharedPreferences sf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_datecouse);
        // Tabbed Activity
        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.viewPager);
        imgbtn_location = findViewById(R.id.imgbtn_location);
        tv_site = findViewById(R.id.tv_site);
        datecousePagerAdapter = new DatecousePagerAdapter(getSupportFragmentManager());


        //로그인 저장 정보
        sf = getSharedPreferences("LOGIN",MODE_PRIVATE);

        String lat = getIntent().getStringExtra("lat");
        String longt = getIntent().getStringExtra("long");
        String site = getIntent().getStringExtra("site");


        if((getIntent().getStringExtra("lat")) != null ){
            Log.e("lat",lat);
            Log.e("lat",longt);
            Log.e("lat",site);

            tv_site.setText(site);
        }else{
            tv_site.setText(getCurrentAddress(new LatLng(Gloval.getLatitude(),Gloval.getLongitude())));
        }


        imgbtn_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DatecouseActivity.this, GoogleMapActivity.class);
                startActivity(intent);
            }
        });

        // viewPagerAdapter
      //  datecousePagerAdapter.addFragment(new DateTotalFrament(), "");
        datecousePagerAdapter.addFragment(new DateCouseFragment(), "");
        datecousePagerAdapter.addFragment(new DateTouristFragment(), "");
        datecousePagerAdapter.addFragment(new DateCultureFragment(), "");
        datecousePagerAdapter.addFragment(new DateRestarantFragment(), "");
        datecousePagerAdapter.addFragment(new DateFestivalFragment(), "");
        datecousePagerAdapter.addFragment(new DateLeportsFragment(), "");
        datecousePagerAdapter.addFragment(new DateShopingFragment(), "");
        viewPager.setAdapter(datecousePagerAdapter);
        tabLayout.setupWithViewPager(viewPager);

        // 탭들의 제목 설정
     //   tabLayout.getTabAt(0).setText("전체");
        tabLayout.getTabAt(0).setText("추천코스AI");
        tabLayout.getTabAt(1).setText("관광지");
        tabLayout.getTabAt(2).setText("문화시설");
        tabLayout.getTabAt(3).setText("음식점");
        tabLayout.getTabAt(4).setText("행사/공연/축제");
        tabLayout.getTabAt(5).setText("레포츠");
        tabLayout.getTabAt(6).setText("쇼핑");

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
                //메뉴바에 아이디랑 같은지 확인하고 그 메뉴를 클릭했을때 무슨 동작을 해줄건지에 대한 부분
                if(id == R.id.mainpage){
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.addFlags (Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(intent);
                }
                else if(id == R.id.date_course){

                }
                else if(id == R.id.story_album){
                    Intent intent = new Intent(getApplicationContext(), StoryActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.addFlags (Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(intent);
                }
                else if(id == R.id.couple_calender) {
                    Intent intent = new Intent(getApplicationContext(), CalenderDiaryActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.addFlags (Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(intent);
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

                    Intent intent = new Intent(DatecouseActivity.this, StartpageActivity.class);
                    startActivity(intent);
                    finish();
                }

                return true;
            }
        });
    }
    @Override

    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.toolbars, menu);
        return true;
    }


    //햄버거 버튼 눌렀을때 드로어가 시작하게 하는 동작 부분
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:{ // 왼쪽 상단 버튼 눌렀을 때
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
            }
            case R.id.toolbar_arrange:{
                final CharSequence[] oItems = {"거리순","조회순","제목순"};

                AlertDialog.Builder oDialog = new AlertDialog.Builder(this,
                        android.R.style.Theme_DeviceDefault_Light_Dialog_Alert);

                oDialog.setTitle("선택하세요")
                        .setItems(oItems, new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialog, int which)
                            {
                                if(which==0){
                                    Arrange.setArrange("S");
                                    Intent intent =  new Intent(DatecouseActivity.this,DatecouseActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                    startActivity(intent);
                                }else if(which==1){
                                    Arrange.setArrange("P");
                                    Intent intent =  new Intent(DatecouseActivity.this,DatecouseActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                    startActivity(intent);
                                }else if(which==2){
                                    Arrange.setArrange("O");
                                    Intent intent =  new Intent(DatecouseActivity.this,DatecouseActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                    startActivity(intent);
                                }
                            }
                        })
                        .setCancelable(true)
                        .show();
            }

        }
        return super.onOptionsItemSelected(item);
    }

    public String getCurrentAddress(LatLng latlng) {

        //지오코더... GPS를 주소로 변환
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());

        List<Address> addresses;

        try {

            addresses = geocoder.getFromLocation(
                    latlng.latitude,
                    latlng.longitude,
                    1);
        } catch (IOException ioException) {
            //네트워크 문제
            //  Toast.makeText(this, "지오코더 서비스 사용불가", Toast.LENGTH_LONG).show();
            return "lat="+latlng.latitude+" lon="+latlng.longitude;
        } catch (IllegalArgumentException illegalArgumentException) {
            Toast.makeText(this, "잘못된 GPS 좌표", Toast.LENGTH_LONG).show();
            return "잘못된 GPS 좌표";

        }


        if (addresses == null || addresses.size() == 0) {
            Toast.makeText(this, "주소 미발견", Toast.LENGTH_LONG).show();
            return "주소 미발견";

        } else {
            Address address = addresses.get(0);
            return address.getAddressLine(0).toString();
        }

    }

}