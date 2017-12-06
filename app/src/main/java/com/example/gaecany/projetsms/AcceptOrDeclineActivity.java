package com.example.gaecany.projetsms;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;

public class AcceptOrDeclineActivity extends AppCompatActivity {
    String numero;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accept_or_decline);
        Bundle extras = getIntent().getExtras();
        numero = extras.getString("numero");
    }

    public void declineInvit(View view) {
        String message = "NOTIFICATION : Invitation refusée !";
        SmsManager.getDefault().sendTextMessage(numero, null, message, null, null);
        goToMainActivity(view);
    }

    public void acceptInvit(View view) {
        String message = "NOTIFICATION : Invitation acceptée !";
        SmsManager.getDefault().sendTextMessage(numero, null, message, null, null);
        goToMainActivity(view);
    }

    public void goToMainActivity(View view) {
        Intent goToMain = new Intent(AcceptOrDeclineActivity.this, MainActivity.class);
        AcceptOrDeclineActivity.this.startActivity(goToMain);

    }

}
