package com.example.gaecany.projetsms;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.widget.Toast;

public class InvitReceiver extends BroadcastReceiver {

    private final String ACTION_RECEIVE_SMS  = "android.provider.Telephony.SMS_RECEIVED";

    @Override
    public void onReceive(Context context, Intent intent)
    {

        if (intent.getAction().equals(ACTION_RECEIVE_SMS))
        {
            Bundle bundle = intent.getExtras();
            if (bundle != null)
            {
                Object[] pdus = (Object[]) bundle.get("pdus");

                final SmsMessage[] messages = new SmsMessage[pdus.length];
                for (int i = 0; i < pdus.length; i++)  {  messages[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);  }  if (messages.length > -1)
                    {
                        final String message = messages[0].getMessageBody();
                        final String numero = messages[0].getDisplayOriginatingAddress();

                        if(!message.startsWith("NOTIFICATION")){
                            Intent acceptOrDeclineIntent = new Intent(context, AcceptOrDeclineActivity.class);
                            acceptOrDeclineIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            acceptOrDeclineIntent.putExtra("numero", numero); //Optional parameters
                            acceptOrDeclineIntent.putExtra("position", message); //Optional parameters
                            context.startActivity(acceptOrDeclineIntent);
                        }
                        else{
                            Toast toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
                            toast.show();
                        }
                    }
            }
        }

    }
}
