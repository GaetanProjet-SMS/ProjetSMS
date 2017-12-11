package com.example.gaecany.projetsms;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.provider.ContactsContract;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

public class EnvoyerInvitationActivity extends AppCompatActivity {
    private final int PICK_CONTACT = 2015;
    private static final int REQUEST_LOCATION = 1;
    private static final int MY_PERMISSIONS_REQUEST_SENS_SMS=1;
    private LocationManager locationManager;
    String latitude, longitude;
    EditText position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_envoyer_invitation);
        position = (EditText) findViewById(R.id.locationEditView);
        Bundle extras = getIntent().getExtras();
        if(extras != null && extras.size() != 0){

            double lati = 0;
            lati = extras.getDouble("latitude");
            double longi = 0;
            longi = extras.getDouble("longitude");
            position.setText(Double.toString(lati) + " ; " + Double.toString(longi));

        }

        ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps();

        } else if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {

            if(position.getText().length() == 0) {
                position.setText(getLocation());
            }
        }
        (findViewById(R.id.pickContact)).setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_PICK, ContactsContract.CommonDataKinds.Phone.CONTENT_URI);
                startActivityForResult(i, PICK_CONTACT);
            }
        });
    }
    private String getLocation() {
        String pos = "";
        if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission
                (getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);

        } else {

            Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

            Location location1 = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

            Location location2 = locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);

            Location locationAUtiliser = null;


            if (location != null) {
                locationAUtiliser = location;

            } else if (location1 != null) {
                locationAUtiliser = location1;

            } else if (location2 != null) {
                locationAUtiliser = location2;

            } else {
                Toast.makeText(this, "Impossible de vous localiser", Toast.LENGTH_SHORT).show();
            }

            if (locationAUtiliser != null) {
                double latti = locationAUtiliser.getLatitude();
                double longi = locationAUtiliser.getLongitude();
                latitude = String.valueOf(latti);
                longitude = String.valueOf(longi);

                pos = latitude + " ; " + longitude;
            }
        }

        return pos;
    }

    protected void buildAlertMessageNoGps() {

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Veuillez activer votre connexion GPS")
                .setCancelable(false)
                .setPositiveButton("Oui", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("Non", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PICK_CONTACT && resultCode == RESULT_OK) {
            Uri contactUri = data.getData();
            Cursor cursor = getContentResolver().query(contactUri, null, null, null, null);
            cursor.moveToFirst();
            int column = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
            EditText numerosSaisis = (EditText)findViewById(R.id.editTextNumero);
            if(!numerosSaisis.getText().toString().contains(cursor.getString(column))){
                if(numerosSaisis.getText().length() == 0){
                    numerosSaisis.setText(cursor.getString(column));
                }
                else{
                    numerosSaisis.setText(numerosSaisis.getText() + ";" + cursor.getString(column));
                }
            }

        }
    }

    public void sendMessage(View view) {


        Context context = getApplicationContext();

        if (ContextCompat.checkSelfPermission(context, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS}, MY_PERMISSIONS_REQUEST_SENS_SMS);
        }
        else{
            formatEtEnvoyerSMS(context);
        }
    }

    private void formatEtEnvoyerSMS(Context context) {
        EditText editTextNumero = (EditText) findViewById(R.id.editTextNumero);
        String numero = editTextNumero.getText().toString();
        String[] nums = numero.split(";");

        CharSequence erreurNumero = "Veuillez saisir le bon format de numéro";
        CharSequence erreurPosition = "Veuillez saisir un lieu de rendez-vous";

        int duration = Toast.LENGTH_SHORT;
        Toast toastErreurNum = Toast.makeText(context, erreurNumero, duration);
        Toast toastErreurPos = Toast.makeText(context, erreurPosition, duration);

        if (numero != null && numero.length() >= 4) {

            for (int i = 0; i < nums.length; i++) {
                SmsManager.getDefault().sendTextMessage(nums[i], null, String.valueOf(position.getText()), null, null);
            }

        }

        if(position == null || this.position.getText().toString().length() == 0){
            toastErreurPos.show();
        }

        if (numero != null && numero.length() < 4) {
            toastErreurNum.show();
        }
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        Context context = getApplicationContext();
        if (requestCode == MY_PERMISSIONS_REQUEST_SENS_SMS) {

            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                formatEtEnvoyerSMS(context);
            }
            else{
                Toast.makeText(context, "Refus autorisation d'envoie des sms", Toast.LENGTH_SHORT).show();
            }
        }
    }


    public void goback(View view) {
        Intent goBack = new Intent(EnvoyerInvitationActivity.this, MainActivity.class);
        EnvoyerInvitationActivity.this.startActivity(goBack);
    }

    public void changerPosition(View view) {
        Intent changerPosition = new Intent(EnvoyerInvitationActivity.this, ChangerPositionActivity.class);
        EnvoyerInvitationActivity.this.startActivity(changerPosition);
    }
}
