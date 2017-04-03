package com.dotcom.jamaatAdmin.fcm;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.android.volley.VolleyError;
import com.dotcom.jamaatAdmin.activity.Common.BaseActivity;
import com.dotcom.jamaatAdmin.activity.Common.NotificationActivity;
import com.dotcom.jamaatAdmin.util.Constants;
import com.dotcom.jamaatAdmin.util.SharedPreferencesManager;

import org.json.JSONException;
import org.json.JSONObject;

public class NotificationHelperActivity extends BaseActivity {
    String userType;
    boolean isNativeNotification;
//    String id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferencesManager.removePreference("notificationMessage");
        userType = SharedPreferencesManager.getStringPreference(Constants.USERTYPE,null);
        int unseen = SharedPreferencesManager.getIntPreference(Constants.UNSEEN_NOTIFICATION_COUNT, 0);
        if(unseen>0){
            unseen--;
        }
        SharedPreferencesManager.setPreference(Constants.UNSEEN_NOTIFICATION_COUNT,unseen);
        String notificationType = getIntent().getStringExtra("notificationType");
        String id = getIntent().getStringExtra("id");
        if(id.equalsIgnoreCase("NULL")){
            id = null;
        }
        isNativeNotification = getIntent().getBooleanExtra("isNativeNotification",true);
        boolean isSubscribed = getIntent().getBooleanExtra("isSubscribed",false);
//        boolean isSubscription = getIntent().getBooleanExtra("isSubscription",false);
        int notificationId = getIntent().getIntExtra("notificationId",0);
        if(isNativeNotification){
            try {
                JSONObject notificationJsonObj = null;
                notificationJsonObj = new JSONObject();
                notificationJsonObj.put("notificationId", notificationId);
                loadJsonData(Constants.NOTIFICATION_READ, notificationJsonObj.toString(), Constants.NOTIFICATION_READ);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        navigateToPage(notificationType,id,isNativeNotification,isSubscribed);
    }
    public void  navigateToPage(String notificationType,String id,boolean isNativeNotification,boolean isSubscribed)
    {
        if(id != null && id.length()>0) {
//            switch (notificationType) {
//
//                case "dealDetail":
//                    Intent dealDetailIntent = new Intent(NotificationHelperActivity.this, DealDetailTab.class);
//                    dealDetailIntent.putExtra("dealId", id);
//                    dealDetailIntent.putExtra("pageId", 0);
//                    dealDetailIntent.putExtra("isSubscribed", isSubscribed);
//                    dealDetailIntent.putExtra("isNativeNotification", isNativeNotification);
//                    startActivity(dealDetailIntent);
//                    finish();
//                    break;
//                case "dealComment":
//
//                    Intent dealCommentIntent = new Intent(NotificationHelperActivity.this, DiscussionActivity.class);
//                    dealCommentIntent.putExtra("dealId", id);
//                    dealCommentIntent.putExtra("isNativeNotification", isNativeNotification);
//                    startActivity(dealCommentIntent);
//                    finish();
//                    break;
//                case "pitchDetail":
//                    Intent pitchDetailIntent = new Intent(NotificationHelperActivity.this, NotificationActivity.class);
////                    pitchDetailIntent.putExtra("dealId", id);
//                    pitchDetailIntent.putExtra("isNativeNotification", isNativeNotification);
//                    startActivity(pitchDetailIntent);
//                    finish();
//                    break;
//                case "dealCommitment":
//
////                    dealCommentIntent.putExtra("dealId", id);
//                        Intent dealCommitmentIntent = new Intent(NotificationHelperActivity.this, CommitmentDetailActivity.class);
//                        dealCommitmentIntent.putExtra("dealId", id);
//                        dealCommitmentIntent.putExtra("isNativeNotification", isNativeNotification);
//                        startActivity(dealCommitmentIntent);
//                    finish();
//                    break;
//                case "meetingDetail":
//                    // This is wrong implementation we implemented this to handle a error with deleted meeting it will be corrected at backend
//                    // In this we first hit meeting api with given meeting id and when it throw error we show a toast and when it give success we take it to meeting detail page
//                    String replacedURl = Constants.MEETING_DETAILS.replace(Constants.MEETINGID,""+id);
//                    loadJsonData(Request.Method.GET, replacedURl, null, Constants.MEETING_DETAILS,false);
//                    break;
//                case "membershipDetail":
//                    if(userType.equalsIgnoreCase("IAN Team")){
//                        Intent meetingCommentIntent = new Intent(NotificationHelperActivity.this, NotificationActivity.class);
//                        meetingCommentIntent.putExtra("isNativeNotification", isNativeNotification);
//                        startActivity(meetingCommentIntent);
//                        finish();
//                    }else {
//                        Intent membershipIntent = new Intent(NotificationHelperActivity.this, MemberDetailActivity.class);
//                        membershipIntent.putExtra("memberId", id);
//                        membershipIntent.putExtra("isNativeNotification", isNativeNotification);
//                        startActivity(membershipIntent);
//                        finish();
//                    }
//                    break;
//                case "meetingComment":
//                    Intent meetingCommentIntent = new Intent(NotificationHelperActivity.this, NotificationActivity.class);
//                    meetingCommentIntent.putExtra("isNativeNotification", isNativeNotification);
////                meetingCommentIntent.putExtra("memberId",id);
////                meetingIntent.putExtra(Constants.FRAGMENTNAME,"meetingcomment");
//                    startActivity(meetingCommentIntent);
//                    finish();
//                    break;
//                case "meetingCancelled":
//                    Intent meetingCancelledIntent = new Intent(NotificationHelperActivity.this, NotificationActivity.class);
//                    meetingCancelledIntent.putExtra("isNativeNotification", isNativeNotification);
////                meetingCancelledIntent.putExtra("memberId",id);
////                meetingIntent.putExtra(Constants.FRAGMENTNAME,"meetingcomment");
//                    startActivity(meetingCancelledIntent);
//                    finish();
//                    break;
//
//                default:
//                    Intent intent = new Intent(NotificationHelperActivity.this, NotificationActivity.class);
//                    intent.putExtra("isNativeNotification", isNativeNotification);
//                    startActivity(intent);
//                    finish();
//            }
        }else{
            Intent intent = new Intent(NotificationHelperActivity.this, NotificationActivity.class);
            intent.putExtra("isNativeNotification", isNativeNotification);
            startActivity(intent);
            finish();
        }
    }

    @Override
    protected String getTagName() {
        return null;
    }

    @Override
    public void showProgress(boolean show, String tag) {

    }

    @Override
    public void onSuccess(JSONObject response, String tag) {
        if(tag.equalsIgnoreCase(Constants.NOTIFICATION_READ)){
            // This is wrong implementation we implemented this to handle a error with deleted meeting it will be corrected at backend
            // In this we first hit meeting api with given meeting id and when it throw error we show a toast and when it give success we take it to meeting detail page
            try {
                int status = response.getInt("status");
                if (status == 200) {
                    Log.i("Response Notification::",response.toString());
//                    "data": {
//                        "unreadCount": 263
//                    }
                    JSONObject jsonObject = response.optJSONObject("data");
                    int unreadCount = jsonObject.optInt("unreadCount");
                    SharedPreferencesManager.setPreference(Constants.UNSEEN_NOTIFICATION_COUNT, unreadCount);
                }
            }
            catch (JSONException je)
            {
                je.printStackTrace();
            }
        }else if (tag.equalsIgnoreCase(Constants.MEETING_DETAILS)) {
            try{
                int status = response.getInt("status");
                if (status == 200) {
//                    Intent meetingIntent = new Intent(NotificationHelperActivity.this, MeetingDetailActivity.class);
//                    String meetingId = getIntent().getStringExtra("id");
//                    meetingIntent.putExtra("meetingId", meetingId);
//                    meetingIntent.putExtra("isNativeNotification", isNativeNotification);
////                   meetingIntent.putExtra(Constants.FRAGMENTNAME,"meetingcomment");
//                    startActivity(meetingIntent);
//                    finish();

                }
            }catch (Exception je){
                je.printStackTrace();
            }
        }
    }

    @Override
    public void onError(VolleyError error, String message, String tag) {
        // This is wrong implementation we implemented this to handle a error with deleted meeting it will be corrected at backend
        // In this we first hit meeting api with given meeting id and when it throw error we take it to Notification list page
        if (tag.equalsIgnoreCase(Constants.MEETING_DETAILS)) {
            Intent meetingCommentIntent = new Intent(NotificationHelperActivity.this, NotificationActivity.class);
            meetingCommentIntent.putExtra("isNativeNotification", isNativeNotification);
            startActivity(meetingCommentIntent);
            finish();
        }
    }
}
