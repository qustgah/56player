package com.ijiaban.actionbar.pulltorefreshlib;


/*
 * Copyright 2013 Chris Banes
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


import android.annotation.TargetApi;
import android.os.Build;
import android.view.View;
import android.widget.AbsListView;

/**
 * FIXME
 */
public class AbsListViewDelegate implements ViewDelegate {

    public static final Class[] SUPPORTED_VIEW_CLASSES =  { AbsListView.class };

    @Override
    public boolean isReadyForPull(View view, final float x, final float y) {
        boolean ready = false;

        // First we check whether we're scrolled to the top
        //检查是否滑动到顶部
        AbsListView absListView = (AbsListView) view;
        if (absListView.getCount() == 0) {
            ready = true;
        } else if (absListView.getFirstVisiblePosition() == 0) {
            final View firstVisibleChild = absListView.getChildAt(0);
            ready = firstVisibleChild != null && firstVisibleChild.getTop() >= absListView.getPaddingTop();
        }

        // Then we have to check whether the fast scroller is enabled, and check we're not starting
        // the gesture from the scroller
        //判断快速滚动条是否可用
        //判断是否通过滚动手势
        if (ready && absListView.isFastScrollEnabled() && isFastScrollAlwaysVisible(absListView)) {
            switch (getVerticalScrollbarPosition(absListView)) {
                case View.SCROLLBAR_POSITION_RIGHT:
                    ready = x < absListView.getRight() - absListView.getVerticalScrollbarWidth();
                    break;
                case View.SCROLLBAR_POSITION_LEFT:
                    ready = x > absListView.getVerticalScrollbarWidth();
                    break;
            }
        }

        return ready;
    }

    int getVerticalScrollbarPosition(AbsListView absListView) {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB ?
                CompatV11.getVerticalScrollbarPosition(absListView) :
                Compat.getVerticalScrollbarPosition(absListView);
    }

    boolean isFastScrollAlwaysVisible(AbsListView absListView) {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB ?
                CompatV11.isFastScrollAlwaysVisible(absListView) :
                Compat.isFastScrollAlwaysVisible(absListView);
    }

    static class Compat {
        static int getVerticalScrollbarPosition(AbsListView absListView) {
            return View.SCROLLBAR_POSITION_RIGHT;
        }
        static boolean isFastScrollAlwaysVisible(AbsListView absListView) {
            return false;
        }
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    static class CompatV11 {
        static int getVerticalScrollbarPosition(AbsListView absListView) {
            return absListView.getVerticalScrollbarPosition();
        }
        static boolean isFastScrollAlwaysVisible(AbsListView absListView) {
            return absListView.isFastScrollAlwaysVisible();
        }
    }
}
