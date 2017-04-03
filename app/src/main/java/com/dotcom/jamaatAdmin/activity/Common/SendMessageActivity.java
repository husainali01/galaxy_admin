package com.dotcom.jamaatAdmin.activity.Common;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.dotcom.jamaatAdmin.R;
import com.dotcom.jamaatAdmin.util.Constants;
import com.dotcom.jamaatAdmin.util.SharedPreferencesManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;

public class SendMessageActivity extends BaseActivity {
    private EditText messageET,timeET,associationET;
    private Button sendButton;
    static EditText dateET;
    private ProgressDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_message);
        messageET = (EditText) findViewById(R.id.message_editText);
        associationET = (EditText) findViewById(R.id.association_ET);
        timeET = (EditText) findViewById(R.id.timeET);
        dateET = (EditText) findViewById(R.id.dateET);
        sendButton = (Button) findViewById(R.id.message_send_button);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String message = messageET.getText().toString();
                String date = dateET.getText().toString();
                String time = timeET.getText().toString();
                String association = associationET.getText().toString();
                JSONObject data = null;
                try {
                    data =  new JSONObject();
                    if(message.equalsIgnoreCase("")||message == null){
                        messageET.setError("please enter messaage");
                    }else if(association.equalsIgnoreCase("")||association == null){

                    }
                    else{
                        data.put("val",message);
                        data.put("timestamp",date+" "+time);
                        data.put("org",association);
                        loadJsonData(Constants.SEND_MESSAGE, data.toString(), Constants.SEND_MESSAGE);
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
        dateET.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTruitonDatePickerDialog(v);
            }
        });

        timeET.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO Auto-generated method stub
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                int seconds = mcurrentTime.get(Calendar.SECOND);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(SendMessageActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        String SelectedHour,SelectedMinute;
                        if((selectedHour) < 10){

                            SelectedHour = "0" + selectedHour;
                        }else{
                            SelectedHour = ""+selectedHour;
                        }
                        if(selectedMinute < 10){

                            SelectedMinute  = "0" + selectedMinute ;
                        }else{
                            SelectedMinute = ""+selectedMinute;
                        }
                        timeET.setText( SelectedHour + ":" + SelectedMinute+ ":00" );
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();

            }
        });
    }

    @Override
    protected String getTagName() {
        return null;
    }

    @Override
    public void showProgress(boolean show, String tag) {
        if (show) {
            showDialog();
        } else{
            hideDialog();
        }
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
        if (tag.equals(Constants.SEND_MESSAGE)) {
            try {
                Log.v("Login result::", response.toString());
                int status = response.getInt("status");
                if (status == 200) {
                    String message = response.optString("message");
                    Toast.makeText(this, message, Toast.LENGTH_LONG).show();
                } else {
                    String message = response.optString("message");
                    Toast.makeText(this, message, Toast.LENGTH_LONG).show();
                }
            } catch (JSONException je) {
                je.printStackTrace();
            }
        }
    }

    @Override
    public void onError(VolleyError error, String message, String tag) {

    }
    public void showTruitonDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(SendMessageActivity.this.getSupportFragmentManager(), "datePicker");
    }
    public static class DatePickerFragment extends DialogFragment implements
            DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);
            // Create a new instance of DatePickerDialog and return it
            DatePickerDialog datePickerDialog ;
            datePickerDialog = new DatePickerDialog(getActivity(), this, year, month, day);
            datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
            return datePickerDialog;
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            // Do something with the date chosen by the user
            String Month, Day;
            if((month+1) < 10){

                Month = "0" + (month + 1);
            }else{
                Month = ""+(month + 1);
            }
            if(day < 10){

                Day  = "0" + day ;
            }else{
                Day = ""+day;
            }
                dateET.setText(year + "-" + (Month ) + "-" + Day);

        }
    }
}
