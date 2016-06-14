package com.zj.showgirl;

import android.support.annotation.Nullable;

import com.zj.base.BasePresenter;
import com.zj.base.BaseView;

import java.util.List;

/**
 * Created by CoXier on 2016/5/7.
 */
public interface GirlContract {
    interface View extends BaseView<Presenter> {
        public void showMore(@Nullable List list);

        public void finishRefresh();

        public void showSnackBar();
    }

    interface Presenter extends BasePresenter {

    }
}
