package com.dotcom.jamaatAdmin.activity.Common;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.support.v7.app.NotificationCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.webkit.MimeTypeMap;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.google.android.gms.common.api.GoogleApiClient;
import com.dotcom.jamaatAdmin.R;
import com.dotcom.jamaatAdmin.adapters.AttachmentAdapter;
import com.dotcom.jamaatAdmin.fcm.NLService;
import com.dotcom.jamaatAdmin.interfaces.IAdapterCallBack;
import com.dotcom.jamaatAdmin.model.Attachment;

import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

public class AttachmentDownloadActivity extends BaseActivity implements IAdapterCallBack {
    ListView mDownloadListView;
    AttachmentAdapter attachmentAdapter;
    Context mcContext;
    ArrayList<Attachment> attachments;
    private GoogleApiClient client;
    NotificationCompat.Builder mBuilder;
    NotificationManager mNotifyManager;
    int id = 1;
    ArrayList<AsyncTask<String, String, Void>> arr;
    int counter = 0;
    private NotificationReceiver nReceiver;
    private String fileName, fileType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attachment_download);
        mcContext = this;
        mDownloadListView = (ListView) findViewById(R.id.downloadListView);
        Bundle b = getIntent().getExtras();
        if (b != null) {
            attachments = (ArrayList<Attachment>) b.getSerializable("attachments");
        }
        attachments = (ArrayList<Attachment>) getIntent().getSerializableExtra("attachments");
        attachmentAdapter = new AttachmentAdapter(mcContext, R.layout.attachment_item, attachments,this);
        mDownloadListView.setAdapter(attachmentAdapter);
        Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Download Attachments");


        arr = new ArrayList<AsyncTask<String, String, Void>>();




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

    }

    @Override
    public void onError(VolleyError error, String message, String tag) {

    }

   /* @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);

        MenuItem item = menu.findItem(R.id.search);
        item.setVisible(false);

        return true;
    }*/

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                super.onBackPressed();

                return true;
            case R.id.notification:
                // User chose the "Favorite" action, mark the current item
                // as a favorite...
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onStart() {
        super.onStart();


    }

    @Override
    public void onStop() {
        super.onStop();


    }

    @Override
    public void fragmentCalling(Object calledObject, String position, String value) {
        int incr;
        mNotifyManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        mBuilder = new NotificationCompat.Builder(this);
        mBuilder.setContentTitle("Downloading ...").setContentText("Download in progress").setSmallIcon(R.drawable.ian_small_logo);
        // Start a lengthy operation in a background thread
        mBuilder.setProgress(0, 0, true);
        mNotifyManager.notify(id, mBuilder.build());
        mBuilder.setAutoCancel(true);

        ContentResolver contentResolver = getContentResolver();
        String enabledNotificationListeners = Settings.Secure.getString(contentResolver, "enabled_notification_listeners");
        String packageName = getPackageName();

        // check to see if the enabledNotificationListeners String contains our
        // package name
        if (enabledNotificationListeners == null || !enabledNotificationListeners.contains(packageName)) {
            // in this situation we know that the user has not granted the app
            // the Notification access permission
            // Check if notification is enabled for this application
            Log.i("ACC", "Dont Have Notification access");
            Intent intent = new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS");
            startActivity(intent);
        } else {
            Log.i("ACC", "Have Notification access");
        }

        nReceiver = new NotificationReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(NLService.NOT_TAG);
        registerReceiver(nReceiver, filter);

        Attachment attachment =(Attachment)calledObject;
        fileName = attachment.getAttachmentName();
        String urlsToDownload[] = new String[1];
        urlsToDownload[0] = attachment.getAttachmentUrl();
        fileType = urlsToDownload[0].substring(urlsToDownload[0].lastIndexOf('.')).trim();
        for (incr = 0; incr < urlsToDownload.length; incr++) {
            FileDownloader imageDownloader = new FileDownloader();
            imageDownloader.execute(urlsToDownload[incr],attachment.getAttachmentName());
            arr.add(imageDownloader);
        }
    }

    @Override
    public void fragmentCallingByType(Object calledObject, String position, String value, String type) {

    }

    class NotificationReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String event = intent.getExtras().getString(NLService.NOT_EVENT_KEY);
            Log.i("NotificationReceiver", "NotificationReceiver onReceive : " + event);
            if (event.trim().contentEquals(NLService.NOT_REMOVED)) {
                killTasks();
            }

        }
    }

    private void killTasks() {
        if (null != arr & arr.size() > 0) {
            for (AsyncTask<String, String, Void> a : arr) {
                if (a != null) {
                    Log.i("NotificationReceiver", "Killing download thread");
                    a.cancel(true);
                }
            }
            mNotifyManager.cancelAll();
        }
    }

    private void downloadFileToSdCard(String downloadUrl, String imageName) {
        FileOutputStream fos;
        InputStream inputStream = null;

        try {
            URL url = new URL(downloadUrl);
            /* making a directory in sdcard */
            String sdCard = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString();
            // String sdCard = Environment.getExternalStorageDirectory().toString();
            File myDir = new File(sdCard);

            /* if specified not exist create new */
            if (!myDir.exists()) {
                myDir.mkdir();
                Log.v("", "inside mkdir");
            }

            /* checks the file and if it already exist delete */
            String fname = imageName;
            File file = new File(myDir, fname);
            Log.d("file===========path", "" + file);
            if (file.exists())
                file.delete();

            /* Open a connection */
            URLConnection ucon = url.openConnection();

            HttpURLConnection httpConn = (HttpURLConnection) ucon;
            httpConn.setRequestMethod("GET");
            httpConn.connect();
            inputStream = httpConn.getInputStream();

            /*
             * if (httpConn.getResponseCode() == HttpURLConnection.HTTP_OK) {
             * inputStream = httpConn.getInputStream(); }
             */

            fos = new FileOutputStream(file);
            // int totalSize = httpConn.getContentLength();
            // int downloadedSize = 0;
            byte[] buffer = new byte[1024];
            int bufferLength = 0;
            while ((bufferLength = inputStream.read(buffer)) > 0) {
                fos.write(buffer, 0, bufferLength);
                // downloadedSize += bufferLength;
                // Log.i("Progress:", "downloadedSize:" + downloadedSize +
                // "totalSize:" + totalSize);
            }
            inputStream.close();
            fos.close();
            Log.d("test", "File Saved in sdcard..");
        } catch (IOException io) {
            inputStream = null;
            fos = null;
            io.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }

    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        killTasks();
        if(nReceiver!=null) {
            unregisterReceiver(nReceiver);
        }
    }
    private class FileDownloader extends AsyncTask<String, String, Void> {

        @Override
        protected Void doInBackground(String... param) {
            downloadFileToSdCard(param[0], param[1]);
            return null;
        }

        protected void onProgressUpdate(String... values) {
        }

        @Override
        protected void onPreExecute() {
            Log.i("Async-Example", "onPreExecute Called");
        }

        @Override
        protected void onPostExecute(Void result) {
            Log.i("Async-Example", "onPostExecute Called");

            float len = 1;
            // When the loop is finished, updates the notification
            Intent intent = new Intent();
            intent.setAction(android.content.Intent.ACTION_VIEW);
            String sdCard = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString();
            Toast.makeText(AttachmentDownloadActivity.this,sdCard+ fileName,Toast.LENGTH_LONG).show();
            Log.i("SdCard loaction",sdCard + fileName);
            File file = new File(sdCard +"/"+ fileName); // set your audio path
//            intent.setDataAndType(Uri.fromFile(file), "docx/*");
            intent.setDataAndType(Uri.fromFile(file), MimeTypeMap.getSingleton().getMimeTypeFromExtension(fileType));  // you can also change jpeg to other types

            PendingIntent pIntent = PendingIntent.getActivity(AttachmentDownloadActivity.this, 0, intent, 0);
            if (counter >= len - 1) {

                mBuilder.setContentTitle("Done.");
                mBuilder.setContentText("Download complete")
                        // Removes the progress bar
                        .setProgress(0, 0, false);
                mBuilder.setSmallIcon(R.drawable.ian_small_logo);
                mBuilder.setContentIntent(pIntent);
                mBuilder.setAutoCancel(true);
                mNotifyManager.notify(id, mBuilder.build());


            } else {
                int per = (int) (((counter + 1) / len) * 100f);
                Log.i("Counter", "Counter : " + counter + ", per : " + per);
                mBuilder.setContentText("Downloaded (" + per + "/100");
                mBuilder.setProgress(100, per, false);
                // Displays the progress bar for the first time.
                mNotifyManager.notify(id, mBuilder.build());
            }
            counter++;

        }

    }
}
