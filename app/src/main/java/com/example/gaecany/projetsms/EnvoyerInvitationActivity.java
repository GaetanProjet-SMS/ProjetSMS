package com.example.gaecany.projetsms;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class EnvoyerInvitationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_envoyer_invitation);
    }

    public void sendMessage(View view) {
        Context context = getApplicationContext();
        EditText editTextNumero = (EditText) findViewById(R.id.editTextNumero);

        String numero = editTextNumero.getText().toString();

        String[] nums = numero.split(";");

        CharSequence erreurNumero = "Le numero doit faire 4 caracteres.";

        int duration = Toast.LENGTH_SHORT;
        Toast toastErreurNum = Toast.makeText(context, erreurNumero, duration);

        if(numero != null && numero.length() < 4){
            toastErreurNum.show();
        }

        if(numero != null && numero.length() >=4){

            for(int i = 0; i < nums.length; i++){
                SmsManager.getDefault().sendTextMessage(nums[i], null, "Invitation", null, null);
            }

        }
    }

    public void goback(View view) {
        Intent goBack = new Intent(EnvoyerInvitationActivity.this, MainActivity.class);
        EnvoyerInvitationActivity.this.startActivity(goBack);
    }
}
