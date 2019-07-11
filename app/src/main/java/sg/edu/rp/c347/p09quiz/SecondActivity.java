package sg.edu.rp.c347.p09quiz;

import android.Manifest;
import android.content.Intent;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.PermissionChecker;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class SecondActivity extends AppCompatActivity {

    private GoogleMap map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        Intent i = getIntent();
        final String latS = i.getStringExtra("lat");
        final String lngS = i.getStringExtra("lng");
        final double lat = Double.parseDouble(latS);
        final double lng = Double.parseDouble(lngS);
        Log.d("LAT",String.valueOf(lat));

        FragmentManager fm = getSupportFragmentManager();
        SupportMapFragment mapFragment = (SupportMapFragment)
                fm.findFragmentById(R.id.map);

        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                map = googleMap;

                LatLng pos = new LatLng(lat, lng);
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(pos,
                        14));


                final Marker cp = map.addMarker(new
                        MarkerOptions()
                        .position(pos)
                        .title("Your location is here")
                        .snippet(latS + ", " + lngS)
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));



                UiSettings ui = map.getUiSettings();

                ui.setCompassEnabled(true);
                ui.setZoomControlsEnabled(true);
                ui.setMapToolbarEnabled(true);

                int permissionCheck = ContextCompat.checkSelfPermission(SecondActivity.this,
                        android.Manifest.permission.ACCESS_FINE_LOCATION);

                if (permissionCheck == PermissionChecker.PERMISSION_GRANTED) {
                    map.setMyLocationEnabled(true);
                } else {
                    Log.e("GMap - Permission", "GPS access has not been granted");
                    ActivityCompat.requestPermissions(SecondActivity.this,
                            new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                            0);
                }
                



            }
        });
    }
}
