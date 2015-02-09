package com.ijiaban.actionbar.pulltorefreshlib;


import android.annotation.SuppressLint;
import android.view.View;

@SuppressLint("NewApi") class CompatV16 {

    static void postOnAnimation(View view, Runnable runnable) {
        view.postOnAnimation(runnable);
    }

}
