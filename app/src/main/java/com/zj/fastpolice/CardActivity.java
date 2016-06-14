package com.zj.fastpolice;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.mikepenz.iconics.IconicsDrawable;
import com.mikepenz.material_design_iconic_typeface_library.MaterialDesignIconic;
import com.zj.base.BaseActivity;
import com.zj.custom.RoundImageView;

import java.util.Arrays;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class CardActivity extends BaseActivity {

    @Bind(R.id.card_icon)
    ImageView mCardIcon;
    @Bind(R.id.main_fl_container)
    FrameLayout mFlContainer;
    @Bind(R.id.main_fl_card_back) FrameLayout mFlCardBack;
    @Bind(R.id.main_fl_card_front) FrameLayout mFlCardFront;
    @Bind(R.id.my_image)
    RoundImageView roundImageView;
    @Bind(R.id.card_tv_center)
    TextView cardtvcenter;
    @Bind(R.id.card_back_name)
    TextView cardbackname;

    private AnimatorSet mRightOutSet; // 右出动画
    private AnimatorSet mLeftInSet; // 左入动画

    private boolean mIsShowBack;

    int positon;
    private List<Integer> walls = Arrays.asList(R.mipmap.police1,
            R.mipmap.police2, R.mipmap.police3, R.mipmap.police4, R.mipmap.police5,
            R.mipmap.police6, R.mipmap.police17, R.mipmap.police8, R.mipmap.police9,
            R.mipmap.police10);
    private List<String> names= Arrays.asList("高颖","张婷","朱彦超","陈戴茜","吴蕾蕾",
            "柯占军","黄晶莹","张虹","吴宝山","陶莎莎");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card);

        ButterKnife.bind(this);
        Intent intent=getIntent();
        positon=intent.getIntExtra("positon",0);
        mCardIcon.setImageDrawable(new IconicsDrawable(this).color(Color.WHITE).icon(MaterialDesignIconic.Icon.gmi_arrow_back).sizeDp(20));
        //roundImageView.setBackgroundResource(walls.get(positon));
        roundImageView.setImageResource(walls.get(positon));
        cardtvcenter.setText(names.get(positon));
        cardbackname.setText(names.get(positon));
        setAnimators(); // 设置动画
        setCameraDistance(); // 设置镜头距离





    }


    // 设置动画
    private void setAnimators() {
        mRightOutSet = (AnimatorSet) AnimatorInflater.loadAnimator(this, R.animator.anim_out);
        mLeftInSet = (AnimatorSet) AnimatorInflater.loadAnimator(this, R.animator.anim_in);

        // 设置点击事件
        mRightOutSet.addListener(new AnimatorListenerAdapter() {
            @Override public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                mFlContainer.setClickable(false);
            }
        });
        mLeftInSet.addListener(new AnimatorListenerAdapter() {
            @Override public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                mFlContainer.setClickable(true);
            }
        });
    }

    // 改变视角距离, 贴近屏幕
    private void setCameraDistance() {
        int distance = 16000;
        float scale = getResources().getDisplayMetrics().density * distance;
        mFlCardFront.setCameraDistance(scale);
        mFlCardBack.setCameraDistance(scale);
    }

    // 翻转卡片
    public void flipCard(View view) {
        // 正面朝上
        if (!mIsShowBack) {
            mRightOutSet.setTarget(mFlCardFront);
            mLeftInSet.setTarget(mFlCardBack);
            mRightOutSet.start();
            mLeftInSet.start();
            mIsShowBack = true;
        } else { // 背面朝上
            mRightOutSet.setTarget(mFlCardBack);
            mLeftInSet.setTarget(mFlCardFront);
            mRightOutSet.start();
            mLeftInSet.start();
            mIsShowBack = false;
        }
    }


    @OnClick(R.id.card_icon)
    public void onClick(View view)
    {
        switch (view.getId())
        {
            case R.id.card_icon:
                this.finish();
                break;
        }

    }
}
