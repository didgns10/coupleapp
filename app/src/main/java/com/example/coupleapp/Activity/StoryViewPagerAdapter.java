package com.example.coupleapp.Activity;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class StoryViewPagerAdapter  extends FragmentPagerAdapter {

    private final List<Fragment> fragmentList = new ArrayList<>();
    private final List<String> titleList = new ArrayList<>();

    // 생성자
    public StoryViewPagerAdapter(FragmentManager fm)
    {
        super(fm);
    }

    @Override
    public Fragment getItem(int position)
    {
        return fragmentList.get(position);
    }

    @Override
    public int getCount()
    {
        return titleList.size();
    }

    // 함수 재정의
    @Override
    public CharSequence getPageTitle(int position)
    {
        return super.getPageTitle(position);
    }

    // 뷰페이저에 프래그먼트 추가
    public void addFragment(Fragment fragment, String title) {
        fragmentList.add(fragment);
        titleList.add(title);
    }

}
