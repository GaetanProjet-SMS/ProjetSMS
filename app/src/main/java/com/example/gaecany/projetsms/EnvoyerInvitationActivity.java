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
import android.support.v4.app.ActivityCompat;
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
    private LocationManager locationManager;
    String lattitude, longitude;
    EditText position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_envoyer_invitation);

        Bundle extras = getIntent().getExtras();
        if(extras != null && extras.size() != 0){
            double latitude = 0;
            latitude = extras.getDouble("latitude");
            double longitude = 0;
            longitude = extras.getDouble("longitude");
            if(latitude != 0 && longitude != 0){
                position = (EditText) findViewById(R.id.locationEditView);
                position.setText(Double.toString(latitude) + Double.toString(longitude));
            }
        }

        ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps();

        } else if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {

            position = (EditText) findViewById(R.id.locationEditView);
            position.setText(getLocation());
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
                Toast.makeText(this, "Unable to Trace your location", Toast.LENGTH_SHORT).show();
            }

            if (locationAUtiliser != null) {
                double latti = locationAUtiliser.getLatitude();
                double longi = locationAUtiliser.getLongitude();
                lattitude = String.valueOf(latti);
                longitude = String.valueOf(longi);

                pos = lattitude + " " + longitude;
            }
        }

        return pos;
    }

    protected void buildAlertMessageNoGps() {

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Please Turn ON your GPS Connection")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
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
        EditText editTextNumero = (EditText) findViewById(R.id.editTextNumero);

        String numero = editTextNumero.getText().toString();

        String[] nums = numero.split(";");

        CharSequence erreurNumero = "Le numero doit faire 4 caracteres.";

        int duration = Toast.LENGTH_SHORT;
        Toast toastErreurNum = Toast.makeText(context, erreurNumero, duration);


            if(numero != null && numero.length() >=4){

                for(int i = 0; i < nums.length; i++){
                    SmsManager.getDefault().sendTextMessage(nums[i], null, "Invitation", null, null);
                }

            }

        if(numero != null && numero.length() < 4){
            toastErreurNum.show();
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
