package com.zj.base;

import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;

/**
 * Created by jjx on 2016/6/3.
 */
public abstract class GirlBaseFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener{
    public abstract void setRecyclerView();
    public abstract void setSwipeRefreshLayout();
}
