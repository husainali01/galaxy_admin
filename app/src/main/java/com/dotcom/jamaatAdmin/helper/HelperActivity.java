package com.dotcom.jamaatAdmin.helper;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

import java.io.IOException;

public class HelperActivity extends Activity {

    private HelperActivity ctx;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        ctx=this;
        String action= (String)getIntent().getExtras().get("DO");
        if(action.equals("radio")){
            boolean isEnabled = Settings.System.getInt
                    (getContentResolver(),
                            Settings.System.AIRPLANE_MODE_ON, 0) == 1;
            Settings.System.putInt(getContentResolver(),Settings.System.AIRPLANE_MODE_ON, isEnabled ? 0 : 1);
            Intent intent = new Intent(Intent.ACTION_AIRPLANE_MODE_CHANGED);
            intent.putExtra("state", !isEnabled);
            sendBroadcast(intent);
        }
        else if(action.equals("volume")){
            AudioManager mAudioManager = (AudioManager)getSystemService(AUDIO_SERVICE);
            if(mAudioManager.getRingerMode()==AudioManager.RINGER_MODE_NORMAL)
                mAudioManager.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
            else
                mAudioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
        }
        else if(action.equals("reboot")){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Σίγουρα ρε?")
                    .setPositiveButton("Ναι ρε!", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            try {
                                Process proc = Runtime.getRuntime().exec(new String[] { "su", "-c", "reboot" });
                                proc.waitFor();
                            }
                            catch (IOException ioe){
                                Toast.makeText(ctx, "IOException: Είσαι σίγουρα root?", Toast.LENGTH_LONG).show();
                            }
                            catch (InterruptedException ie){
                                Toast.makeText(ctx, "InterruptedException: Είσαι σίγουρα root?", Toast.LENGTH_LONG).show();
                            }
                        }
                    })
                    .setNegativeButton("Μπάα!", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                            ctx.finish();
                        }
                    });
            AlertDialog alert = builder.create();
            alert.setCancelable(true);
            alert.setCanceledOnTouchOutside(false);
            alert.show();
        }
        else if(action.equals("top")){
            String url = "http://www.example.com";
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(url));
            startActivity(i);
            Toast.makeText(ctx, "top", Toast.LENGTH_LONG).show();
           // new MyNotification(this);
        }
        else if(action.equals("app")){
            Toast.makeText(ctx, "app", Toast.LENGTH_LONG).show();
           /* Intent app=new Intent(this, tsapalos11598712.bill3050.shortcuts.CustomShortcutsActivity.class);
            startActivity(app);*/
        }
		/*if(!action.equals("top")){
			try {
			   Object service = getSystemService ("statusbar");
			   Class <?> statusBarManager = Class.forName
				("android.app.StatusBarManager");
			   //Use expand instead of collapse to view the notification area
			   Method collapse = statusBarManager.getMethod ("collapse");
			   collapse.invoke (service);
			} catch (Exception e) {
			   Toast.makeText(getApplicationContext(), e.getMessage(),
				Toast.LENGTH_LONG).show();
			}
		}*/
        if(!action.equals("reboot"))
            finish();
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
    }
}
