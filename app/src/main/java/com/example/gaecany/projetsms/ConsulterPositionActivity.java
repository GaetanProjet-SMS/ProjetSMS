package com.example.gaecany.projetsms;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class ConsulterPositionActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private String latitude, longitude;
    private String numero;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consulter_position);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        Bundle extras = getIntent().getExtras();
        latitude = extras.getString("latitude");
        longitude = extras.getString("longitude");
        numero = extras.getString("numero");
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng lieu = new LatLng(Double.valueOf(this.latitude), Double.valueOf(this.longitude));
        mMap.addMarker(new MarkerOptions().position(lieu).title("Lieu du rendez-vous"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(lieu));
    }

    public void retourInvit(View view) {
        Intent retourInvit = new Intent(ConsulterPositionActivity.this, AcceptOrDeclineActivity.class);
        retourInvit.putExtra("position", this.latitude + " ; " + this.longitude);
        retourInvit.putExtra("numero", this.numero);
        ConsulterPositionActivity.this.startActivity(retourInvit);
    }
}
