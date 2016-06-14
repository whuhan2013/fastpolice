package com.zj.fastpolice;

import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.mikepenz.iconics.IconicsDrawable;
import com.mikepenz.material_design_iconic_typeface_library.MaterialDesignIconic;
import com.zj.bean.CaseData;
import com.zj.fragment.DetailFragment;
import com.zj.utils.NetUtils;
import com.zj.utils.ThemeUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class CaseDetailActivity extends AppCompatActivity {

    ViewPager mViewPager;
    CaseData caseData;
    FloatingActionButton floatingActionButton;
    FloatingActionButton fab_up_state;
    private String[] mNames = {"警察甲", "警察乙", "警察丙", "警察丁", "警察戊", "警察己", "警察庚", "警察辛","警察壬癸","警察癸","青龙","白虎","朱雀","玄武"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_case_detail);
        Intent intent=getIntent();
        Bundle bundle=intent.getBundleExtra("mycase");
        caseData= (CaseData) bundle.getSerializable("casedetial");
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        floatingActionButton= (FloatingActionButton) findViewById(R.id.fab_normal);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    Intent intent=new Intent(CaseDetailActivity.this,RoutePlanningActivity.class);
                    intent.putExtra("destination",caseData.getAddress());
                    startActivity(intent);

                fab_up_state= (FloatingActionButton) findViewById(R.id.fab_up_state);
                fab_up_state.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        new MaterialDialog.Builder(CaseDetailActivity.this)
                                .title("任务状态")
                                .icon(new IconicsDrawable(CaseDetailActivity.this)
                                        .color(ThemeUtils.getThemeColor(CaseDetailActivity.this, R.attr.colorPrimary))
                                        .icon(MaterialDesignIconic.Icon.gmi_account_box)
                                        .sizeDp(20))
                                .content("您要将案件SB250"+caseData.getId()+"状态更新为已处理?")
                                .positiveText("是").onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(MaterialDialog dialog, DialogAction which) {

                                //Toast.makeText(ReportActivity.this, "SUCCESS", Toast.LENGTH_SHORT).show();
                                NetUtils.updateDeadID(CaseDetailActivity.this, caseData.getId());



                            }
                        }).negativeText("否").onNegative(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(MaterialDialog dialog, DialogAction which) {
                                //Toast.makeText(ReportActivity.this, "failed", Toast.LENGTH_SHORT).show();


                            }
                        })
                                .show();
                    }
                });

            }
        });
        //mToolbar.setBackgroundColor(Color.BLUE);

        //使用CollapsingToolbarLayout必须把title设置到CollapsingToolbarLayout上，设置到Toolbar上则不会显示
        CollapsingToolbarLayout mCollapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        mCollapsingToolbarLayout.setTitle("案情");
        //通过CollapsingToolbarLayout修改字体颜色
        mCollapsingToolbarLayout.setExpandedTitleColor(Color.WHITE);//设置还没收缩时状态下字体颜色
        mCollapsingToolbarLayout.setCollapsedTitleTextColor(Color.GREEN);//设置收缩后Toolbar上字体的颜色


        //设置ViewPager
        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(mViewPager);

//给TabLayout增加Tab, 并关联ViewPager
        TabLayout tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
        tabLayout.addTab(tabLayout.newTab().setText("案情介绍"));
        tabLayout.addTab(tabLayout.newTab().setText("承办人"));
        tabLayout.addTab(tabLayout.newTab().setText("处理状态"));
        tabLayout.setupWithViewPager(mViewPager);


    }

    private void setupViewPager(ViewPager mViewPager) {
        MyPagerAdapter adapter = new MyPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(DetailFragment.newInstance(caseData.getDescription()), "案情介绍");
        adapter.addFragment(DetailFragment.newInstance(mNames[caseData.getPoliceid()-1]), "承办人");
        int status=caseData.getDealed();
        String statusText="";
        if (status==0)
        {
            statusText="未处理";
        }else if (status==1)
        {
            statusText="已处理";
        }
        adapter.addFragment(DetailFragment.newInstance(statusText), "处理状态");
        mViewPager.setAdapter(adapter);
    }

    private String getAsset(String fileName) {
        AssetManager am = getResources().getAssets();
        InputStream is = null;
        try {
            is = am.open(fileName, AssetManager.ACCESS_BUFFER);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new Scanner(is).useDelimiter("\\Z").next();
    }


    static class MyPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragments = new ArrayList<>();
        private final List<String> mFragmentTitles = new ArrayList<>();

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        public void addFragment(Fragment fragment, String title) {
            mFragments.add(fragment);
            mFragmentTitles.add(title);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitles.get(position);
        }
    }






}

