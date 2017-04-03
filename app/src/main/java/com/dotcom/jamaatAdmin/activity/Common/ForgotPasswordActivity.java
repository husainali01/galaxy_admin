package com.dotcom.jamaatAdmin.activity.Common;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.VolleyError;
import com.dotcom.jamaatAdmin.R;
import com.dotcom.jamaatAdmin.util.network.NetworkUtil;

import org.json.JSONObject;

public class ForgotPasswordActivity extends BaseActivity {

    private NetworkUtil networkUtil;
    private Context mContext;
    private EditText mEmailAddress;
    private Button proceedButton;
    private ProgressDialog mDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.forgot_password_dialog);
        mEmailAddress = (EditText) findViewById(R.id.email_address_textview);
        proceedButton = (Button) findViewById(R.id.sendBtn);
        super.onCreate(savedInstanceState);
        proceedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + "+918602297118"));
                startActivity(intent);

            }
        });
        Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Forgot Password");
    }

    @Override
    protected String getTagName() {
        return null;
    }

    @Override
    public void showProgress(boolean show, String tag) {
        if (show) {
            showDialog();
        } else
            hideDialog();
    }

    @Override
    public void onSuccess(JSONObject response, String tag) {

    }

    @Override
    public void onError(VolleyError error, String message, String tag) {
    }
    public void showDialog() {
        mDialog = new ProgressDialog(this);
        mDialog.setMessage("Please wait...");
        mDialog.setCancelable(true);
        mDialog.show();
    }

    public void hideDialog() {
        if (mDialog != null && mDialog.isShowing()) {
            mDialog.dismiss();
        }
    }
    public boolean isNBadgeVisible(){
        return false;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                super.onBackPressed();

                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
