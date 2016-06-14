package com.zj.utils;

import android.content.Context;
import android.view.View;

import com.zj.base.FastPoliceApplication;

/**
 * Created by _SOLID
 * Date:2016/5/20
 * Time:8:42
 */
public class AppUtils {

    public static boolean isFirstRun() {
        return PrefUtils.getBoolean(FastPoliceApplication.getInstance(), "isFirstRun", true);
    }

    public static void setFirstRun(boolean isFirstRun) {
        PrefUtils.putBoolean(FastPoliceApplication.getInstance(), "isFirstRun", isFirstRun);
    }


    public static void feedBack(final Context context, final View view) {
//        new MaterialDialog.Builder(context)
//                .title(R.string.feedback_dialog_title)
//                .inputType(InputType.TYPE_CLASS_TEXT)
//                .input(R.string.feedback_input_hint, R.string.feedback_input_prefill, new MaterialDialog.InputCallback() {
//                    @Override
//                    public void onInput(MaterialDialog dialog, CharSequence input) {
//
//                        if (TextUtils.isEmpty(input)) {
//                            return;
//                        }
//                        FeedBack feedBack = new FeedBack();
//                        feedBack.setContent(input.toString());
//                        feedBack.save(context, new SaveListener() {
//                            @Override
//                            public void onSuccess() {
//                                SnackBarUtils.makeShort(view, context.getResources().getString(R.string.feedback_success)).success();
//                            }
//
//                            @Override
//                            public void onFailure(int i, String s) {
//                                SnackBarUtils.makeShort(view, context.getResources().getString(R.string.feedback_failed)).danger();
//                            }
//                        });
//                    }
//                }).show();
    }
}
