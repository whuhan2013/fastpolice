package com.zj.fastpolice;

import android.animation.Animator;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.color.ColorChooserDialog;
import com.bumptech.glide.Glide;
import com.github.fabtransitionactivity.SheetLayout;
import com.mikepenz.foundation_icons_typeface_library.FoundationIcons;
import com.mikepenz.iconics.IconicsDrawable;
import com.mikepenz.iconics.typeface.IIcon;
import com.mikepenz.material_design_iconic_typeface_library.MaterialDesignIconic;
import com.yyydjk.gank.theme.ColorUiUtil;
import com.yyydjk.gank.theme.Theme;
import com.yyydjk.gank.widget.ResideLayout;
import com.zj.adapter.TabsPagerAdapter;
import com.zj.base.BaseActivity;
import com.zj.base.FastPoliceApplication;
import com.zj.fragment.CaseListFragment;
import com.zj.fragment.PoliceCaseFragment;
import com.zj.fragment.PoliceFragment;
import com.zj.showgirl.GirlFragment;
import com.zj.utils.PreUtils;
import com.zj.utils.SystemUtils;
import com.zj.utils.ThemeUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import jp.wasabeef.glide.transformations.CropCircleTransformation;
import me.xiaopan.android.content.res.DimenUtils;
import me.xiaopan.android.preference.PreferencesUtils;


public class MainActivity extends BaseActivity implements ColorChooserDialog.ColorCallback,SheetLayout.OnFabAnimationEndListener{

    @Bind(R.id.menu)
    RelativeLayout mMenu;
    @Bind(R.id.resideLayout)
    ResideLayout mResideLayout;
    @Bind(R.id.status_bar)
    View mStatusBar;
    @Bind(R.id.all)
    TextView mAll;
//    @Bind(R.id.fuli)
//    TextView mFuli;
    @Bind(R.id.android)
    TextView mAndroid;
    @Bind(R.id.ios)
    TextView mIos;
//    @Bind(R.id.video)
//    TextView mVideo;
//    @Bind(R.id.front)
//    TextView mFront;
//    @Bind(R.id.resource)
//    TextView mResource;
//    @Bind(R.id.about)
//    TextView mAbout;

//    @Bind(R.id.app)
//    TextView mApp;
    @Bind(R.id.scrollView)
    ScrollView mScrollView;
    @Bind(R.id.theme)
    TextView mTheme;
    @Bind(R.id.avatar)
    ImageView mAvatar;
    @Bind(R.id.desc)
    TextView mDesc;
    @Bind(R.id.icon)
    ImageView mIcon;
    @Bind(R.id.title)
    TextView mTitle;
//    @Bind(R.id.more)
//    TextView mMore;
    @Bind(R.id.tab)
    TabLayout mTab;
    @Bind(R.id.vp)
    ViewPager mVP;
    @Bind(R.id.fla_upload)
    FloatingActionButton mFab;
    @Bind(R.id.bottom_sheet)
    SheetLayout mSheetLayout;

    private static final int REQUEST_CODE = 1;

    private Fragment currentFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        ButterKnife.bind(this);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            mStatusBar.setVisibility(View.VISIBLE);
            mStatusBar.getLayoutParams().height = SystemUtils.getStatusHeight(this);
            mStatusBar.setLayoutParams(mStatusBar.getLayoutParams());
        } else {
            mStatusBar.setVisibility(View.GONE);
        }

        setIconDrawable(mAll, MaterialDesignIconic.Icon.gmi_view_comfy);
        //setIconDrawable(mFuli, MaterialDesignIconic.Icon.gmi_mood);
        setIconDrawable(mAndroid, MaterialDesignIconic.Icon.gmi_android);
        setIconDrawable(mIos, MaterialDesignIconic.Icon.gmi_apple);
//        setIconDrawable(mVideo, MaterialDesignIconic.Icon.gmi_collection_video);
//        setIconDrawable(mFront, MaterialDesignIconic.Icon.gmi_language_javascript);
//        setIconDrawable(mResource, FontAwesome.Icon.faw_location_arrow);
//        setIconDrawable(mApp, MaterialDesignIconic.Icon.gmi_apps);
//        setIconDrawable(mAbout, MaterialDesignIconic.Icon.gmi_account);
//        setIconDrawable(mSetting, MaterialDesignIconic.Icon.gmi_settings);
        setIconDrawable(mTheme, MaterialDesignIconic.Icon.gmi_palette);
        //setIconDrawable(mMore, MaterialDesignIconic.Icon.gmi_more);


        Glide.with(MainActivity.this)
                .load(R.drawable.avastar)
                .placeholder(new IconicsDrawable(this)
                        .icon(FoundationIcons.Icon.fou_photo)
                        .color(Color.GRAY)
                        .backgroundColor(Color.WHITE)
                        .roundedCornersDp(40)
                        .paddingDp(15)
                        .sizeDp(75))
                .bitmapTransform(new CropCircleTransformation(this))
                .dontAnimate()
                .into(mAvatar);


        if (PreferencesUtils.getBoolean(this, "isFirst", true)) {
            mResideLayout.openPane();
            PreferencesUtils.putBoolean(this, "isFirst", false);
        }
        mIcon.setImageDrawable(new IconicsDrawable(this).color(Color.WHITE).icon(MaterialDesignIconic.Icon.gmi_view_comfy).sizeDp(20));
        mTitle.setText("menu");

        setTabs();
        initFabEvent();


    }


    private void initFabEvent() {
        mSheetLayout.setFab(mFab);
        mSheetLayout.setFabAnimationEndListener(this);
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSheetLayout.expandFab();
            }
        });

    }

    @Override
    public void onFabAnimationEnd() {
        Intent intent = new Intent(MainActivity.this, ReportActivity.class);
        startActivityForResult(intent,REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==REQUEST_CODE){
            mSheetLayout.contractFab();
        }
    }


    private void setTabs() {
        List<Fragment> fragments = new ArrayList<>();
        fragments.add(new PoliceFragment());
        fragments.add(new GirlFragment());
        fragments.add(new CaseListFragment());
        boolean[] fragmentsUpdateFlag = { false, false, false, false };
        String[] titles = {"警察风采","在逃嫌犯","案件中心"};

        TabsPagerAdapter adapter = new TabsPagerAdapter(getSupportFragmentManager(), fragments,fragmentsUpdateFlag,titles);
        mVP.setAdapter(adapter);
        mVP.setOffscreenPageLimit(2);
        mTab.setupWithViewPager(mVP);
    }



    private void setIconDrawable(TextView view, IIcon icon) {
        view.setCompoundDrawablesWithIntrinsicBounds(new IconicsDrawable(this)
                        .icon(icon)
                        .color(Color.WHITE)
                        .sizeDp(16),
                null, null, null);
        view.setCompoundDrawablePadding(DimenUtils.dp2px(this, 10));
    }

    private void switchFragment(Fragment fragment) {
//        if (currentFragment == null || !fragment.getClass().getName().equals(currentFragment.getClass().getName())) {
//            getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment).commit();
//            currentFragment = fragment;
//        }
    }



    @OnClick({R.id.avatar, R.id.all,  R.id.android,
            R.id.about,R.id.ios,
             R.id.theme, R.id.icon})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.avatar:
                break;
            case R.id.all:
                mResideLayout.closePane();
                mIcon.setImageDrawable(new IconicsDrawable(this).color(Color.WHITE).icon(MaterialDesignIconic.Icon.gmi_view_comfy).sizeDp(20));
                mTitle.setText(R.string.app_name);
                //switchFragment(new AllFragment());
                break;
//            case R.id.fuli:
//                mResideLayout.closePane();
//                mIcon.setImageDrawable(new IconicsDrawable(this).color(Color.WHITE).icon(MaterialDesignIconic.Icon.gmi_mood).sizeDp(20));
//                mTitle.setText(R.string.fuli);
//                //switchFragment(new FuLiFragment());
//                Intent intent=new Intent(MainActivity.this,RadarActivity.class);
//                startActivity(intent);
//                //this.finish();
//                break;
            case R.id.android:
                List<Fragment> fragments2 = new ArrayList<>();
                fragments2.add(new PoliceFragment());
                fragments2.add(new GirlFragment());
                fragments2.add(new CaseListFragment());
                boolean[] fragmentsUpdateFlag2 = { false, false, true};
                String[] titles2 = {"警察风采","在逃嫌犯","案件中心"};
                FastPoliceApplication fastPoliceApplication=new FastPoliceApplication();
                fastPoliceApplication.setRole(1);
                TabsPagerAdapter adapter2 = new TabsPagerAdapter(getSupportFragmentManager(), fragments2,fragmentsUpdateFlag2,titles2);
                //adapter.setFragments((fragments));
                adapter2.notifyDataSetChanged();
                mVP.setAdapter(adapter2);
                //mVP.getAdapter().notifyDataSetChanged();
                mTab.setupWithViewPager(mVP);
                mResideLayout.closePane();
                mIcon.setImageDrawable(new IconicsDrawable(this).color(Color.WHITE).icon(MaterialDesignIconic.Icon.gmi_android).sizeDp(20));
                mTitle.setText("警局版");
                //switchFragment(new AndroidFragment());

//                new MaterialDialog.Builder(this)
//                        .title("123")
//                        .content("456")
//                        .positiveText("Y")
//                        .negativeText("N").onPositive(new MaterialDialog.SingleButtonCallback() {
//                    @Override
//                    public void onClick(MaterialDialog dialog, DialogAction which) {
//                        Toast.makeText(getApplication(),"SUCCESS",Toast.LENGTH_SHORT).show();
//                    }
//                }).onNegative(new MaterialDialog.SingleButtonCallback() {
//                    @Override
//                    public void onClick(MaterialDialog dialog, DialogAction which) {
//                        Toast.makeText(getApplication(),"failed",Toast.LENGTH_SHORT).show();
//                    }
//                })
//                        .show();
                break;
            case R.id.ios:
                List<Fragment> fragments = new ArrayList<>();
                fragments.add(new PoliceFragment());
                fragments.add(new GirlFragment());
                fragments.add(new PoliceCaseFragment());
                boolean[] fragmentsUpdateFlag = { false, false, true};
                String[] titles = {"警察风采","在逃嫌犯","我的任务"};
//                FastPoliceApplication fastPoliceApplication=new FastPoliceApplication();
//                fastPoliceApplication.setRole(1);
                TabsPagerAdapter adapter = new TabsPagerAdapter(getSupportFragmentManager(), fragments,fragmentsUpdateFlag,titles);
                //adapter.setFragments((fragments));
                adapter.notifyDataSetChanged();
                mVP.setAdapter(adapter);
                //mVP.getAdapter().notifyDataSetChanged();
                mTab.setupWithViewPager(mVP);
                mResideLayout.closePane();
                mIcon.setImageDrawable(new IconicsDrawable(this).color(Color.WHITE).icon(MaterialDesignIconic.Icon.gmi_apple).sizeDp(20));
                mTitle.setText("警察版");
                //switchFragment(new IOSFragment());
                break;

//            case R.id.video:
//                mResideLayout.closePane();
//                mIcon.setImageDrawable(new IconicsDrawable(this).color(Color.WHITE).icon(MaterialDesignIconic.Icon.gmi_collection_video).sizeDp(20));
//                mTitle.setText(R.string.video);
//                //switchFragment(new VideoFragment());
//                break;
//            case R.id.front:
//                mResideLayout.closePane();
//                mIcon.setImageDrawable(new IconicsDrawable(this).color(Color.WHITE).icon(MaterialDesignIconic.Icon.gmi_language_javascript).sizeDp(20));
//                mTitle.setText(R.string.front);
//                //switchFragment(new FrontFragment());
//                break;
//            case R.id.resource:
//                mResideLayout.closePane();
//                mIcon.setImageDrawable(new IconicsDrawable(this).color(Color.WHITE).icon(FontAwesome.Icon.faw_location_arrow).sizeDp(20));
//                mTitle.setText(R.string.resource);
//                //switchFragment(new ResourceFragment());
//                break;
//            case R.id.app:
//                mResideLayout.closePane();
//                mIcon.setImageDrawable(new IconicsDrawable(this).color(Color.WHITE).icon(MaterialDesignIconic.Icon.gmi_apps).sizeDp(20));
//                mTitle.setText(R.string.app);
//                //switchFragment(new AppFragment());
//                break;
//            case R.id.more:
//                mResideLayout.closePane();
//                mIcon.setImageDrawable(new IconicsDrawable(this).color(Color.WHITE).icon(MaterialDesignIconic.Icon.gmi_more).sizeDp(20));
//                mTitle.setText(R.string.more);
//                //switchFragment(new MoreFragment());
//                break;


                        //            case R.id.setting:
//                mResideLayout.closePane();
//                mIcon.setImageDrawable(new IconicsDrawable(this).color(Color.WHITE).icon(MaterialDesignIconic.Icon.gmi_settings).sizeDp(20));
//                mTitle.setText(R.string.setting);
//                switchFragment(new SettingFragment());
//                break;
            case R.id.about:
                new MaterialDialog.Builder(this)
                        .title(R.string.about)
                        .icon(new IconicsDrawable(this)
                                .color(ThemeUtils.getThemeColor(this, R.attr.colorPrimary))
                                .icon(MaterialDesignIconic.Icon.gmi_account)
                                .sizeDp(20))
                        .content(R.string.about_me)
                        .positiveText(R.string.close)
                        .show();
                break;
            case R.id.theme:
                new ColorChooserDialog.Builder(this, R.string.theme)
                        .customColors(R.array.colors, null)
                        .doneButton(R.string.done)
                        .cancelButton(R.string.cancel)
                        .allowUserColorInput(false)
                        .allowUserColorInputAlpha(false)
                        .show();

                break;
            case R.id.icon:
                mResideLayout.openPane();
                break;
        }
    }


    @Override
    public void onColorSelection(ColorChooserDialog dialog, int selectedColor) {

        if (selectedColor == ThemeUtils.getThemeColor(this, R.attr.colorPrimary))
            return;
        //EventBus.getDefault().post(new SkinChangeEvent());

        if (selectedColor == getResources().getColor(R.color.colorBluePrimary)) {
            setTheme(R.style.BlueTheme);
            PreUtils.setCurrentTheme(this, Theme.Blue);

        } else if (selectedColor == getResources().getColor(R.color.colorRedPrimary)) {
            setTheme(R.style.RedTheme);
            PreUtils.setCurrentTheme(this, Theme.Red);

        } else if (selectedColor == getResources().getColor(R.color.colorBrownPrimary)) {
            setTheme(R.style.BrownTheme);
            PreUtils.setCurrentTheme(this, Theme.Brown);

        } else if (selectedColor == getResources().getColor(R.color.colorGreenPrimary)) {
            setTheme(R.style.GreenTheme);
            PreUtils.setCurrentTheme(this, Theme.Green);

        } else if (selectedColor == getResources().getColor(R.color.colorPurplePrimary)) {
            setTheme(R.style.PurpleTheme);
            PreUtils.setCurrentTheme(this, Theme.Purple);

        } else if (selectedColor == getResources().getColor(R.color.colorTealPrimary)) {
            setTheme(R.style.TealTheme);
            PreUtils.setCurrentTheme(this, Theme.Teal);

        } else if (selectedColor == getResources().getColor(R.color.colorPinkPrimary)) {
            setTheme(R.style.PinkTheme);
            PreUtils.setCurrentTheme(this, Theme.Pink);

        } else if (selectedColor == getResources().getColor(R.color.colorDeepPurplePrimary)) {
            setTheme(R.style.DeepPurpleTheme);
            PreUtils.setCurrentTheme(this, Theme.DeepPurple);

        } else if (selectedColor == getResources().getColor(R.color.colorOrangePrimary)) {
            setTheme(R.style.OrangeTheme);
            PreUtils.setCurrentTheme(this, Theme.Orange);

        } else if (selectedColor == getResources().getColor(R.color.colorIndigoPrimary)) {
            setTheme(R.style.IndigoTheme);
            PreUtils.setCurrentTheme(this, Theme.Indigo);

        } else if (selectedColor == getResources().getColor(R.color.colorLightGreenPrimary)) {
            setTheme(R.style.LightGreenTheme);
            PreUtils.setCurrentTheme(this, Theme.LightGreen);

        } else if (selectedColor == getResources().getColor(R.color.colorDeepOrangePrimary)) {
            setTheme(R.style.DeepOrangeTheme);
            PreUtils.setCurrentTheme(this, Theme.DeepOrange);

        } else if (selectedColor == getResources().getColor(R.color.colorLimePrimary)) {
            setTheme(R.style.LimeTheme);
            PreUtils.setCurrentTheme(this, Theme.Lime);

        } else if (selectedColor == getResources().getColor(R.color.colorBlueGreyPrimary)) {
            setTheme(R.style.BlueGreyTheme);
            PreUtils.setCurrentTheme(this, Theme.BlueGrey);

        } else if (selectedColor == getResources().getColor(R.color.colorCyanPrimary)) {
            setTheme(R.style.CyanTheme);
            PreUtils.setCurrentTheme(this, Theme.Cyan);

        }
        final View rootView = getWindow().getDecorView();
        rootView.setDrawingCacheEnabled(true);
        rootView.buildDrawingCache(true);

        final Bitmap localBitmap = Bitmap.createBitmap(rootView.getDrawingCache());
        rootView.setDrawingCacheEnabled(false);
        if (null != localBitmap && rootView instanceof ViewGroup) {
            final View tmpView = new View(getApplicationContext());
            tmpView.setBackgroundDrawable(new BitmapDrawable(getResources(), localBitmap));
            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            ((ViewGroup) rootView).addView(tmpView, params);
            tmpView.animate().alpha(0).setDuration(400).setListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {
                    ColorUiUtil.changeTheme(rootView, getTheme());
                    System.gc();
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    ((ViewGroup) rootView).removeView(tmpView);
                    localBitmap.recycle();
                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            }).start();
        }

    }



    @Override
    protected void onDestroy() {
        ButterKnife.unbind(this);
        super.onDestroy();
    }
}
