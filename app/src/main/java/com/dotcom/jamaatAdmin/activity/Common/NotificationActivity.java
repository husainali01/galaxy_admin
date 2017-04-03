package com.dotcom.jamaatAdmin.activity.Common;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.dotcom.jamaatAdmin.adapters.NotificationAdapter;
import com.dotcom.jamaatAdmin.util.Constants;
import com.dotcom.jamaatAdmin.util.SharedPreferencesManager;
import com.dotcom.jamaatAdmin.util.network.NetworkUtil;
import com.dotcom.jamaatAdmin.R;
import com.dotcom.jamaatAdmin.model.Notification;
import com.dotcom.jamaatAdmin.util.AppUtility;
import com.dotcom.jamaatAdmin.view.ShadowVerticalSpaceItemDecorator;
import com.dotcom.jamaatAdmin.view.VerticalSpaceItemDecorator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class NotificationActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener {
    private SwipeRefreshLayout swipeRefreshLayout;
    android.support.v7.widget.RecyclerView mListView;
    NotificationAdapter mAdapter;
    ArrayList<Notification> notificationArrayList;
    private ProgressDialog dialog;
    private NetworkUtil networkUtil;
    Context mContext;
    TextView empty,dayTV,monthTV,miqaatTv,englishDayTv,englishMothTv;
    boolean doubleBackToExitPressedOnce = false;
    private Toast toast;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        notificationArrayList = new ArrayList<>();
        mContext = this;
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        mListView = (android.support.v7.widget.RecyclerView) findViewById(R.id.notification_listView);
        empty = (TextView) findViewById(R.id.empty);
//        mListView.setEmptyView(empty);
        dayTV = (TextView) findViewById(R.id.day);
        englishDayTv = (TextView) findViewById(R.id.english_day);
        englishMothTv = (TextView) findViewById(R.id.english_month_year);
        miqaatTv = (TextView) findViewById(R.id.miqaatTv);
        monthTV = (TextView) findViewById(R.id.month_year);
        Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setTitle("Notifications");
        Calendar calendar = Calendar.getInstance();
        int today = calendar.get(Calendar.DAY_OF_MONTH);
        int eYear = calendar.get(Calendar.YEAR);
        String eMonth = calendar.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault());
        englishDayTv.setText(""+today);
        englishMothTv.setText(eMonth+","+" "+ eYear);
        // 4. Initialize ItemAnimator, LayoutManager and ItemDecorators
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        mListView.setHasFixedSize(true);
        int verticalSpacing = 20;
        VerticalSpaceItemDecorator itemDecorator =
                new VerticalSpaceItemDecorator(verticalSpacing);
        ShadowVerticalSpaceItemDecorator shadowItemDecorator =
                new ShadowVerticalSpaceItemDecorator(this, R.drawable.drop_shadow);

    // 7. Set the LayoutManager
        mListView.setLayoutManager(layoutManager);

    // 8. Set the ItemDecorators
        mListView.addItemDecoration(shadowItemDecorator);
        mListView.addItemDecoration(itemDecorator);
        mAdapter = new NotificationAdapter(NotificationActivity.this, R.layout.notification_list_item,
                notificationArrayList);
        mListView.setAdapter(mAdapter);
        swipeRefreshLayout.setOnRefreshListener(this);

        /**
         * Showing Swipe Refresh animation on activity create
         * As animation won't start on onCreate, post runnable is used
         */
        swipeRefreshLayout.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        //isSwipeRefresh = true;
                                        swipeRefreshLayout.setRefreshing(true);
                                        showDialog();
                                        if (NetworkUtil.isConnected()) {
                                            loadJsonData(Request.Method.GET, Constants.NOTIFICATIONS_LIST, null, Constants.NOTIFICATIONS_LIST, false);
                                        } else {
                                            swipeRefreshLayout.setRefreshing(false);
                                            empty.setVisibility(View.VISIBLE);
                                            empty.setText(R.string.no_internet_connection);
                                            Toast.makeText(NotificationActivity.this, R.string.network_not_connected_error_msg, Toast.LENGTH_LONG).show();
                                        }
                                    }
                                }
        );
    }

    @Override
    protected String getTagName() {
        return null;
    }

    @Override
    public void showProgress(boolean show, String tag) {
//        if (show) {
//            empty.setVisibility(View.VISIBLE);
//            empty.setText("");
//            showDialog();
//        } else{
//            empty.setVisibility(View.GONE);
//            hideDialog();
//        }
    }
    public void showDialog() {
        dialog = new ProgressDialog(this);
        dialog.setMessage("Please wait...");
        dialog.setCancelable(true);
        dialog.show();
    }

    public void hideDialog() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }

    @Override
    public void onSuccess(JSONObject response, String tag) {
        Log.v("Notification result::",response.toString());
        if(tag.equalsIgnoreCase(Constants.NOTIFICATIONS_LIST)) {
            try {
                int status = response.getInt("status");
                if (status == 200) {
                    JSONObject dataObj = response.optJSONObject("misri");
                    String day = dataObj.optString("d");
                    String arrabicDay = AppUtility.getArabicNumbers(day);
                    String month = dataObj.optString("m");
                    String year = dataObj.optString("y");
                    String arabicYear = AppUtility.getArabicNumbers(year);
                    String miqaat = dataObj.optString("miqaat");
                    JSONArray dataArray = response.optJSONArray("data");
                    for (int i = 0; i < dataArray.length(); i++) {
                        Notification notification = new Notification();
                        JSONObject notificationObj = dataArray.getJSONObject(i);
                        String message = notificationObj.optString("message");
                        long DateTimeStamp = notificationObj.optInt("timestamp");
                        String date = AppUtility.getDateTime(DateTimeStamp);
                        notification.setTitle(message);
                        notification.setDate(date);

                        if (notification != null) {
                            notificationArrayList.add(notification);
                        }
                    }
                    dayTV.setText(arrabicDay);
                    monthTV.setText(month + " "+ arabicYear);
                    miqaatTv.setText(miqaat);
//                        swipeRefreshLayout.setRefreshing(false);
//                    mAdapter = new NotificationAdapter(NotificationActivity.this, R.layout.notification_list_item,
//                            notificationArrayList);
//                    mListView.setAdapter(mAdapter);
//                    mAdapter.setItems(notificationArrayList);
                    mAdapter.notifyDataSetChanged();
                    // Call onLoadMoreComplete when the LoadMore task, has finished
                } else {
                    String message = response.optString("message");
                    Toast.makeText(NotificationActivity.this, message, Toast.LENGTH_LONG).show();
                }
                swipeRefreshLayout.setRefreshing(false);
                hideDialog();
            } catch (JSONException je) {
                je.printStackTrace();
            }
        }
    }

    @Override
    public void onError(VolleyError error, String message, String tag) {
        // This is wrong implementation we implemented this to handle a error with deleted meeting it will be corrected at backend
        // In this we first hit meeting api with given meeting id and when it throw error we show a toast and when it give success we take it to meeting detail page
        if (tag.equalsIgnoreCase(Constants.MEETING_DETAILS)) {
            String string = NotificationActivity.this.getString(R.string.record_deleted);
            showAToast(string);
        }
    }

    @Override
    protected void onStart() {
        SharedPreferencesManager.removePreference("notificationMessage");
        super.onStart();
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

//        this.doubleBackToExitPressedOnce = true;
//        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

            this.doubleBackToExitPressedOnce = true;
            Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
//             Respond to the action bar's Up/Home button
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    public boolean isNBadgeVisible(){
        return false;
    }

    @Override
    public void onRefresh() {
        showDialog();
        notificationArrayList = new ArrayList<>();
        mAdapter = new NotificationAdapter(NotificationActivity.this, R.layout.notification_list_item,
                notificationArrayList);
        mListView.setAdapter(mAdapter);
        if (NetworkUtil.isConnected()) {
            loadJsonData(Request.Method.GET, Constants.NOTIFICATIONS_LIST, null, Constants.NOTIFICATIONS_LIST, false);
        } else {
            swipeRefreshLayout.setRefreshing(false);
            empty.setVisibility(View.VISIBLE);
            empty.setText(R.string.no_internet_connection);
            Toast.makeText(NotificationActivity.this, R.string.network_not_connected_error_msg, Toast.LENGTH_LONG).show();
        }
    }
    public void showAToast (String st){ //"Toast toast" is declared in the class
        try{ toast.getView().isShown();     // true if visible
            toast.setText(st);
        } catch (Exception e) {         // invisible if exception
            toast = Toast.makeText(NotificationActivity.this, st, Toast.LENGTH_SHORT);
        }
        toast.show();  //finally display it
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                toast.cancel();
            }
        }, 500);
    }

}
