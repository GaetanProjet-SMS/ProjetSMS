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
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class AcceptOrDeclineActivity extends AppCompatActivity {
    private String numero;
    private String latitude, longitude;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_accept_or_decline);
        Bundle extras = getIntent().getExtras();
        numero = extras.getString("numero");
        String position = extras.getString("position");

        String[] positionFormat = position.split(";");
        this.latitude =  positionFormat[0];
        this.longitude = positionFormat[1];

        TextView lat = (TextView) findViewById(R.id.latitudeTV);
        TextView longi = (TextView) findViewById(R.id.longitudeTV);
        TextView num = (TextView) findViewById(R.id.numTV);

        lat.setText(lat.getText() + " " + latitude);
        longi.setText(longi.getText() + " " + longitude);
        num.setText(num.getText() + " " + numero);
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

    public void consulterLieu(View view) {
        Intent goToConsulterLieu = new Intent(AcceptOrDeclineActivity.this, ConsulterPositionActivity.class);
        goToConsulterLieu.putExtra("latitude", this.latitude);
        goToConsulterLieu.putExtra("longitude", this.longitude);
        goToConsulterLieu.putExtra("numero", this.numero);
        AcceptOrDeclineActivity.this.startActivity(goToConsulterLieu);
    }
}
