package com.dotcom.jamaatAdmin.view;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;



/**
 * Created by anjanik on 1/8/16.
 */

public class CustomSwipeToRefresh extends SwipeRefreshLayout {
    private LoadMoreListView mListView;

    public CustomSwipeToRefresh(Context context) {
        super(context);
    }

    public void setListView(LoadMoreListView listView) {
        mListView = listView;
    }

    @Override
    public boolean canChildScrollUp() {
        if (mListView == null) {
            return true;
        }

        return mListView.canScrollVertically(-1);
    }
}