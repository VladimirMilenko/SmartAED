package com.angelhack.aedlocator;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Camera;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.HashMap;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, LocationListener, GoogleMap.OnMarkerClickListener {
    private HashMap<Marker, Integer> map = new HashMap<>();
    private GoogleMap mMap;
    private LocationManager locationManager;
    private boolean DrawnMarker = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, (LocationListener) this);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mMap = mapFragment.getMap();
        mMap.setMyLocationEnabled(true);
        DrawnMarker = false;
        mMap.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {
            @Override
            public void onMyLocationChange(Location location) {
                if (location != null)
                {
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                            new LatLng(location.getLatitude(), location.getLongitude()), 13));
                    DrawMarker(location);
                    CameraPosition cameraPosition = new CameraPosition.Builder()
                            .target(new LatLng(location.getLatitude(), location.getLongitude()))
                            .zoom(17)
                            .build();
                    new AedFinder().FindNearestAed(mMap, location.getLatitude(), location.getLongitude(), map);
                    // Creates a CameraPosition from the builder
                    mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                }
            }
        });
        //mapFragment.getMapAsync(this);
        mMap.setOnMarkerClickListener(this);

    }
    public void DrawMarker(Location loc){
        for (Marker marker:map.keySet()) {
            marker.remove();
        }
        if(!DrawnMarker){
            DrawnMarker = true;
            new AedFinder().FindNearestAed(mMap, loc.getLatitude(), loc.getLongitude(), map);
        }
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


    }
    @Override
    public void onLocationChanged(Location location) {
        mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(location.getLatitude(), location.getLongitude())));
        //mMap.mark
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        showAlertDialog(marker);
        return false;
    }
    public void showAlertDialog(Marker marker){
        AlertDialog.Builder alBuilder = new AlertDialog.Builder(this);
        int id = map.get(marker);
        alBuilder.setTitle("Enable notitification on AED №" + id + "?");
        alBuilder.setMessage("AED located at " + marker.getTitle());
        alBuilder.setPositiveButton("ENABLE", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                for (Integer val: map.values()
                        ) {
                    new AedFinder().EnableAED(val);
                }
            }
        });
        alBuilder.setNegativeButton("DISMISS", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        alBuilder.create().show();
    }
}
