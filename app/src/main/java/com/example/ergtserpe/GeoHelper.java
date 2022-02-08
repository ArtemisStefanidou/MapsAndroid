package com.example.ergtserpe;

import android.app.PendingIntent;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;

import java.lang.String;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofenceStatusCodes;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.maps.model.LatLng;

public class GeoHelper extends ContextWrapper {

    private static final String TAG = "GeoHelper";
    PendingIntent pendingIntent;

    public GeoHelper(Context base) {

        super(base);
    }

    /**
     * Όταν μπει ο χρήστης στο geofence τότε θα ενεργοποιηθεί και θα στείλει αν έκανε enter or exit
     * @param geofence
     * @return
     */
    public GeofencingRequest getGeofencingRequest(Geofence geofence){

        return new GeofencingRequest.Builder()
                .addGeofence(geofence)
                .setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER | GeofencingRequest.INITIAL_TRIGGER_EXIT)
                .build();
    }

    /**
     * Για δημιουργία κύκλου
     */
    public Geofence getGeofence(String ID, LatLng latLng, float radius,int transitionTypes){

        return new Geofence.Builder()
                .setCircularRegion(latLng.latitude,latLng.longitude,radius)
                .setRequestId(ID)
                .setTransitionTypes(transitionTypes)//σε ποια transitions να ακούει
                .setLoiteringDelay(5000)//meta apo posa seconds uew na deijei oti px eisai mesa sthn perioxh
                .setExpirationDuration(Geofence.NEVER_EXPIRE) //για να υπάρχει ακόμα ο κύκλος και μετα το delay
                .build();
    }



    public PendingIntent getPendingIntent(){

        if (pendingIntent != null){

            return pendingIntent;

        }
        Intent intent = new Intent(this,GeoBroadcastReceiver.class);//mporv n abalv to this gt kanei extend ContextWrapper
        pendingIntent = PendingIntent.getBroadcast(this,2607,intent,PendingIntent.FLAG_UPDATE_CURRENT);

        return pendingIntent;
    }

    public String getErrorString(Exception e){
        if(e instanceof ApiException){
            ApiException apiException = (ApiException) e;
            switch (apiException.getStatusCode()){
                case GeofenceStatusCodes
                        .GEOFENCE_NOT_AVAILABLE:
                    return "GEOFENCE_IS_NOT_AVAILABLE";
                case GeofenceStatusCodes
                        .GEOFENCE_TOO_MANY_GEOFENCES:
                    return "GEOFENCE_IS_MORE_THAN_100_GEOFENCIES";
                case GeofenceStatusCodes
                        .GEOFENCE_TOO_MANY_PENDING_INTENTS:
                    return "GEOFENCE_TOO_MANY_PENDING_INTENTS";

            }
        }
        return e.getLocalizedMessage();
    }

}
