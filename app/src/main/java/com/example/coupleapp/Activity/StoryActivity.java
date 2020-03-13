package com.example.coupleapp.Activity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager.widget.ViewPager;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.coupleapp.R;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;

public class StoryActivity extends AppCompatActivity {

    private StoryViewPagerAdapter storyViewPagerAdapter;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private DrawerLayout mDrawerLayout;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story);
        // Tabbed Activity
        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.viewPager);
        storyViewPagerAdapter = new StoryViewPagerAdapter(getSupportFragmentManager());

        // viewPagerAdapter
        storyViewPagerAdapter.addFragment(new StoryFragment(), "");
        storyViewPagerAdapter.addFragment(new AlbumFragment(), "");
        viewPager.setAdapter(storyViewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);

        // 탭들의 제목 설정
        tabLayout.getTabAt(0).setText("스토리");
        tabLayout.getTabAt(1).setText("앨범");
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

                }
                else if(id == R.id.date_course){/*
                    Intent intent = new Intent(getApplicationContext(), SelectActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.addFlags (Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(intent);*/
                }
                else if(id == R.id.story_album){
                }
                else if(id == R.id.couple_diary){
                }
                else if(id == R.id.couple_calender){
                }

                return true;
            }
        });
    }
    @Override

    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.toolbar, menu);
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
            case R.id.toolbar_plus:{
                final CharSequence[] oItems = {"스토리추가","사진추가"};

                AlertDialog.Builder oDialog = new AlertDialog.Builder(this,
                        android.R.style.Theme_DeviceDefault_Light_Dialog_Alert);

                oDialog.setTitle("선택하세요")
                        .setItems(oItems, new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialog, int which)
                            {
                                if(which==0){
                                    Intent intent = new Intent(StoryActivity.this, StoryUploadActivity.class);
                                    startActivity(intent);
                                }else if(which==1){

                                    Toast.makeText(getApplicationContext(),
                                            oItems[1], Toast.LENGTH_SHORT).show();
                                }
                            }
                        })
                        .setCancelable(true)
                        .show();
            }

        }
        return super.onOptionsItemSelected(item);
    }
}

