package com.example.gaecany.projetsms;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void inviteContact(View view) {
        Intent inviteContactIntent = new Intent(MainActivity.this, EnvoyerInvitationActivity.class);
        MainActivity.this.startActivity(inviteContactIntent);
    }
}
