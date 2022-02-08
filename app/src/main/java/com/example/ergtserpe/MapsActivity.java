package com.example.ergtserpe;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.app.PendingIntent;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingClient;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMapLongClickListener {

    private static final String TAG = "MapsActivity";
    private final int FINE_LOCATION_ACCESS_REQUEST_CODE = 10001;//SEVERAL 'LOCATION_ACCESS' IN A SINGLE ACTIVITY
    private final int BACKGROUND_LOCATION_ACCESS_REQUEST_CODE = 10002;//SEVERAL 'LOCATION_ACCESS' IN A SINGLE ACTIVITY
    private GoogleMap mMap;
    private GeofencingClient geofencingClient;

    private GeoHelper geoHelper;
    private final float GEOFENCE_RADIUS = 200; //egv tha thn pairnv apo ton user
    private final String GEOFENCE_ID = "SOME_GEO_ID";

    /**
     * Υλοποιώ μία μέθοδο για να μου επιστρέφει το content έτσι ώστε να μπορέσω να το
     * χρησιμοποίησω στον Broadcast και να καλέσω σωστά τη βάση μου
     */

    private static Context context;

    public static Context getContext() {
        return context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        context = this;

        setContentView(R.layout.activity_maps);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

        mapFragment.getMapAsync(this);

        geofencingClient = LocationServices.getGeofencingClient(this);
        geoHelper = new GeoHelper(this);
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

        enableUserLocation();

        mMap.setOnMapLongClickListener(this);
    }

    /**-----Permissions-----*/
    private void enableUserLocation() {
        //BAGINEI ENA POP UPP STON XRHSTH KAI TON RVTAEI AN THELEI NA ENERGOPOIHSH THN TOPOUESIA TOY AN PEI NAI GINETAI TO PARAKATO
        if (ContextCompat.checkSelfPermission(MapsActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
//            Toast.makeText(MapsActivity.this, "enableUserLocation", Toast.LENGTH_SHORT).show();
            mMap.setMyLocationEnabled(true);
        } else {
            //Γνωρίζει εάν πρέπει να δείξετε τη διεπαφή χρήστη με αιτιολογία πριν ζητήσετε άδεια.
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                //we need to show user a dialog for displaying why the permission is needed and then ask for the permission...
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, FINE_LOCATION_ACCESS_REQUEST_CODE);
                Toast.makeText(MapsActivity.this, "ELSEIF", Toast.LENGTH_SHORT).show();
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, FINE_LOCATION_ACCESS_REQUEST_CODE);
                Toast.makeText(MapsActivity.this, "elseEnableUserLocation", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == FINE_LOCATION_ACCESS_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {//an exei xorhghthei h adeia
                ///WE have the permission
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.

                }
                mMap.setMyLocationEnabled(true);
            } else {

                //we do not have the permissions...
                MapsActivity.this.finish();
                System.exit(0);

            }
        }
    }


    //long --> paratetamenh diarkeia to click pou ua kanei o xrhsths
    @Override
    public void onMapLongClick(LatLng latLng) {
        /**
         * Στην αρχή δεν μου δούλευαν ορισμένα πράγματα που αφορούσαν τους χάρτες έτσι θυμήθηκα αυτό
         * που είχαμε αναφέρει στο μάθημα
         * ότι τα διαφορετικά API έχουν διαφορετικά πράγματα έτσι για να υποστηρίζει η εφαρμογή μου
         * παλιότερες εκδόσηξς από το API 31 που έχω εγώ χρησιμοποίησα αυτό το if
         */
        if(Build.VERSION.SDK_INT>=29){
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_BACKGROUND_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                mMap.clear();
                //gia na mporei o xrhsths na bazei mono ena marker me circle kathe fora
                addMarker(latLng);
                addCircle(latLng, GEOFENCE_RADIUS);
                addGeofence(latLng, GEOFENCE_RADIUS); //input from a user
            }else{
                if(ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.ACCESS_BACKGROUND_LOCATION)){
                    ActivityCompat.requestPermissions(this, new String[] { Manifest.permission.ACCESS_BACKGROUND_LOCATION},BACKGROUND_LOCATION_ACCESS_REQUEST_CODE);
                }{
                    ActivityCompat.requestPermissions(this, new String[] { Manifest.permission.ACCESS_BACKGROUND_LOCATION},BACKGROUND_LOCATION_ACCESS_REQUEST_CODE);
                }
            }
        }else {
            mMap.clear();
            //gia na mporei o xrhsths na bazei mono ena marker me circle kathe fora
            addMarker(latLng);
            addCircle(latLng, GEOFENCE_RADIUS);
            addGeofence(latLng, GEOFENCE_RADIUS); //input from a user
        }
    }


    private void addGeofence(LatLng latLng, float geofence_radius) {


        Geofence geofence = geoHelper.getGeofence(GEOFENCE_ID, latLng, geofence_radius, Geofence.GEOFENCE_TRANSITION_ENTER | Geofence.GEOFENCE_TRANSITION_EXIT);

        GeofencingRequest geofencingRequest = geoHelper.getGeofencingRequest(geofence);
        PendingIntent pendingIntent = geoHelper.getPendingIntent();

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.

            return;
        }
        geofencingClient.addGeofences(geofencingRequest, pendingIntent)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d(TAG, "onSuccess: Geofence Added...");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        String errorMessage = geoHelper.getErrorString(e);
                        Log.d(TAG, "onFailure" + errorMessage);
                    }
                });




    }

    private void addMarker(LatLng latLng) {
        MarkerOptions markerOptions = new MarkerOptions().position(latLng);
        mMap.addMarker(markerOptions);
    }


    private void addCircle(LatLng latLng,float radius) {
        CircleOptions circleOptions = new CircleOptions();
        circleOptions.center(latLng);
        circleOptions.radius(radius);
        circleOptions.strokeColor(Color.argb(255,255,0,0));
        circleOptions.fillColor(Color.argb(64,255,0,0));
        circleOptions.strokeWidth(4);
        mMap.addCircle(circleOptions);

    }


}