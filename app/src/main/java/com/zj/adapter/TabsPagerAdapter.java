package com.zj.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by CoXier on 2016/5/2.
 */

public class TabsPagerAdapter extends FragmentPagerAdapter {
    List<Fragment>  mFragments;
    FragmentManager fm;
    private int curUpdatePager;

    String[] titles = {"警察风采","在逃嫌犯","新闻资讯"};
    boolean[] fragmentsUpdateFlag;


    public void setFragments(List<Fragment> fragments) {
        if(this.mFragments != null){
            FragmentTransaction ft = fm.beginTransaction();
            for(Fragment f:this.mFragments){
                ft.remove(f);
            }
            ft.commit();
            ft=null;
            fm.executePendingTransactions();
        }
        this.mFragments = fragments;
        notifyDataSetChanged();
    }

    public TabsPagerAdapter(FragmentManager fm, List<Fragment> mFragments,boolean[] fragmentsUpdateFlag,String []titles) {
        super(fm);
        this.fm=fm;
        this.mFragments = mFragments;
        this.fragmentsUpdateFlag=fragmentsUpdateFlag;
        this.titles=titles;
        //setFragments(mFragments);
    }

    @Override
    public Fragment getItem(int position) {

        return mFragments.get(position);

    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        //得到缓存的fragment
        Fragment fragment = (Fragment) super.instantiateItem(container,
                position);
        //得到tag，这点很重要
        String fragmentTag = fragment.getTag();
         Log.i("location","===============");
        if (fragmentsUpdateFlag[position % fragmentsUpdateFlag.length]) {
            //如果这个fragment需要更新
            Log.i("location", "fragmentTag==+++++++" + fragmentTag);

                FragmentTransaction ft = fm.beginTransaction();
                //移除旧的fragment
//            if (fragment.isAdded()) {
                ft.remove(fragment);
                //换成新的fragment
                fragment = mFragments.get(position % mFragments.size());
                //添加新fragment时必须用前面获得的tag，这点很重要
                ft.add(container.getId(), fragment, fragmentTag);
//            }else {
//                fragment = mFragments.get(position % mFragments.size());
//                //添加新fragment时必须用前面获得的tag，这点很重要
//                ft.add(container.getId(), fragment);
//            }

                //ft.attach(fragment);
                ft.commit();


            //复位更新标志
            fragmentsUpdateFlag[position % fragmentsUpdateFlag.length] = false;
        }

        return fragment;
    }

    @Override
    public int getCount() {
        return mFragments.size();
    }



    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }



}
