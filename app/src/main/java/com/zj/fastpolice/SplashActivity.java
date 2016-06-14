package com.zj.fastpolice;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.github.paolorotolo.appintro.AppIntro;
import com.github.paolorotolo.appintro.AppIntroFragment;
import com.zj.fragment.StartFragment;
import com.zj.utils.AppUtils;

/**
 * Created by jjx on 2016/6/1.
 */
public class SplashActivity extends AppIntro {
    @Override
    public void init(Bundle savedInstanceState) {
        Handler handler = new Handler();
        if (!AppUtils.isFirstRun()) {
            addSlide(new StartFragment());
            showSkipButton(false);
            setProgressButtonEnabled(false);

            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    startActivity(new Intent(SplashActivity.this, MainActivity.class));
                    finish();
                }
            }, 2000);

        } else {

            int color = getResources().getColor(R.color.colorPrimary);
            //int color=getResources().getColor(R.color.colorPrimary,getTheme());

            addSlide(AppIntroFragment.newInstance("最新", "能及时查看gank.io发布的最新信息", R.drawable.display1, color));
            addSlide(AppIntroFragment.newInstance("分类浏览", "能对信息进行分类浏览", R.drawable.display2, color));
            addSlide(AppIntroFragment.newInstance("排序", "可以对分类进行排序", R.drawable.display3, color));
            addSlide(AppIntroFragment.newInstance("在线收藏", "使用Bomb实现在线收藏", R.drawable.display4, color));
            //addSlide(AppIntroFragment.newInstance("", "干货IO", R.mipmap.logo, R.color.colorPrimary));
            setBarColor(color);
            // Hide Skip/Done button.
            //showSkipButton(false);
            //setProgressButtonEnabled(false);

            // Turn vibration on and set intensity.
            // NOTE: you will probably need to ask VIBRATE permisssion in Manifest.
//            setVibrate(true);
//            setVibrateIntensity(30);

            //setFadeAnimation();
            setZoomAnimation();
            setDoneText("立即开启");
            setSkipText("跳过");
        }
    }

    @Override
    public void onSkipPressed() {

    }

    @Override
    public void onNextPressed() {

    }

    @Override
    public void onDonePressed() {
        AppUtils.setFirstRun(false);
        startActivity(new Intent(SplashActivity.this, MainActivity.class));
        finish();
    }

    @Override
    public void onSlideChanged() {

    }
}
