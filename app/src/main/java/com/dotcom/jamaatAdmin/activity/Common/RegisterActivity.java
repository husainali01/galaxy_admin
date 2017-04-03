package com.dotcom.jamaatAdmin.activity.Common;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.dotcom.jamaatAdmin.R;
import com.dotcom.jamaatAdmin.util.Constants;

import org.json.JSONException;
import org.json.JSONObject;

public class RegisterActivity extends BaseActivity {
    private EditText nameET,numberET,pinET,categoryET;
    private ProgressDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resister);
        nameET = (EditText) findViewById(R.id.nameET);
        numberET = (EditText) findViewById(R.id.numberET);
        pinET = (EditText) findViewById(R.id.passwordET);
        categoryET = (EditText) findViewById(R.id.associatedET);

        Button submitBtn = (Button) findViewById(R.id.submit_button);
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = nameET.getText().toString();
                String number = numberET.getText().toString();
                String pin = pinET.getText().toString();
                String org = categoryET.getText().toString();
                JSONObject data = null;
                try {
                    data =  new JSONObject();
                    if(name.equalsIgnoreCase("")||name == null){
                        nameET.setError("please enter name");
                    }else if (number.equalsIgnoreCase("")||number == null){
                        numberET.setError("please enter number");
                    }else if(pin.equalsIgnoreCase("")||pin == null){
                        pinET.setError("please enter pin");
                    }else{
                        data.put("org",org);
                        data.put("name",name);
                        data.put("username",number);
                        data.put("pin",pin);
                        loadJsonData(Constants.REGISTER, data.toString(), Constants.REGISTER);
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
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

    @Override
    public void onSuccess(JSONObject response, String tag) {
        if (tag.equals(Constants.REGISTER)) {
            try {
                Log.v("Login result::", response.toString());
                int status = response.getInt("status");
                if (status == 200) {
                    String message = response.optString("message");
                    Toast.makeText(this, message, Toast.LENGTH_LONG).show();
                    nameET.setText("");
                    numberET.setText("");
                    pinET.setText("");
                } else {
                    String message = response.optString("message");
                    Toast.makeText(this, message, Toast.LENGTH_LONG).show();
                }
            } catch (JSONException je) {
                je.printStackTrace();
            }
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
    public void onError(VolleyError error, String message, String tag) {

    }
}
