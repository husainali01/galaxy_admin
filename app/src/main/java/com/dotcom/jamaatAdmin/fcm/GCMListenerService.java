/**
 * Copyright 2015 Google Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.dotcom.jamaatAdmin.fcm;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.text.TextUtils;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.dotcom.jamaatAdmin.util.Constants;
import com.dotcom.jamaatAdmin.util.SharedPreferencesManager;
import com.dotcom.jamaatAdmin.util.app.IANappApplication;
import com.dotcom.jamaatAdmin.R;
import com.dotcom.jamaatAdmin.activity.Common.NotificationActivity;

import org.json.JSONObject;

import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;


public class GCMListenerService extends FirebaseMessagingService {

	private static final String TAG = "GCMListenerService";

	/**
	 * Called when message is received.
	 *
	 * @param from SenderID of the sender.
	 * @param data Data bundle containing message data as key/value pairs. For
	 *             Set of keys use data.keySet().
	 */
	AtomicInteger notificationCounter = new AtomicInteger();
	@Override
	public void onMessageReceived(RemoteMessage message){
		String from = message.getFrom();
		Map data = message.getData();
		Log.d(TAG, "GCM Message From: " + from);
		Log.d(TAG, "GCM Message: " + message.getData());
		//Verify If user is loggedIn and has not loggedout intentionally
		//displayCustomNotification(0, data);
		//displayNotificationInNotificationBar(0, data);
		try {
			if (IANappApplication.getInstance().isBackGround()) {
				//Display Notification in Notification bar
				displayNotificationInNotificationBar(data);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	/**
	 * Create and show a simple notification containing the received GCM
	 * message.
	 * <p/>
	 * <p/>
	 * GCM message received.
	 */
	public void displayNotificationInNotificationBar(Map data) {
		try {
			String messageData = data.get("message").toString();
			int notifyID = Constants.NOTIFICATION_ID;
			//String eData = data.getString("extra_data");
			String message="";
			if (!TextUtils.isEmpty(messageData)) {
				JSONObject extra_data = new JSONObject(messageData);
				message = extra_data.optString("message");
			}

			// This intent is fired when notification is clicked

			NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

			Uri defaultSoundUri = RingtoneManager
					.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
			Intent intent;
			String messages = SharedPreferencesManager.getStringPreference("notificationMessage",null);
			if(messages != null && !messages.isEmpty()){
				messages = message + "," + messages;
				SharedPreferencesManager.setPreference("notificationMessage",messages);

				intent = new Intent(this, NotificationActivity.class);
			}else{
				SharedPreferencesManager.setPreference("notificationMessage",message);

				intent = new Intent(this, NotificationActivity.class);
			}
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
					intent, PendingIntent.FLAG_UPDATE_CURRENT);


			String currentMessages = SharedPreferencesManager.getStringPreference("notificationMessage",null);
			String[] messagesArray = currentMessages.split(",");

			int count = messagesArray.length;
			if (messagesArray.length == 0) {

				notificationManager.cancel(notifyID);
				return;
			}

			final String GROUP_KEY_MESSAGES = "group_key_messages";

			// Group notification that will be visible on the phone
			NotificationCompat.Builder mBuilder =
					new NotificationCompat.Builder(this);
			mBuilder.setTicker(getString(R.string.app_name))
					.setDefaults(Notification.DEFAULT_ALL)
					.setContentTitle(getString(R.string.app_name))
					.setSound(defaultSoundUri)
					.setAutoCancel(true)
					.setOnlyAlertOnce(false)
//					.setPriority(Notification.PRIORITY_MAX)
//					.setOngoing(true)
//					.setWhen( System.currentTimeMillis() )
					.setContentIntent(pendingIntent)
					.setSmallIcon(R.mipmap.ic_launcher)
					.setGroup(GROUP_KEY_MESSAGES)
					.setGroupSummary(true)
					.build();

			NotificationCompat.Style style;
			if (count > 1) {
				NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();
				style = inboxStyle;

				mBuilder.setContentTitle(getString(R.string.app_name));

				for (String r : messagesArray) {
					inboxStyle.addLine(r);
				}
				mBuilder.setContentText(count + " new messages");
				inboxStyle.setBigContentTitle(getString(R.string.app_name));
				inboxStyle.setSummaryText(count + " new messages");
			} else {
				NotificationCompat.BigTextStyle bigTextStyle = new NotificationCompat.BigTextStyle();
				style = bigTextStyle;

//				bigTextStyle.setBigContentTitle(messagesArray[0].substring(0, 10).concat(" ..."));
				bigTextStyle.setBigContentTitle(getString(R.string.app_name));
				mBuilder.setContentText(message);
				bigTextStyle.bigText(message);
//				bigTextStyle.setSummaryText(count + " new event");
			}
			mBuilder.setStyle(style);

			NotificationManagerCompat notificationManager1 = NotificationManagerCompat.from(this);
			notificationManager1.notify(notifyID, mBuilder.build());

		} catch (Exception ex) {

		}
	}
public void setNotificationSound(){
	Uri defaultSoundUri = RingtoneManager
			.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
	NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		NotificationCompat.Builder mBuilder =
				new NotificationCompat.Builder(this);
	mBuilder.setSound(defaultSoundUri);
//    mBuilder.setVibrate(new long[] { 1000, 1000, 1000, 1000, 1000 });
	NotificationManagerCompat notificationManager1 = NotificationManagerCompat.from(this);
	notificationManager1.notify(1, mBuilder.build());
	}

}
