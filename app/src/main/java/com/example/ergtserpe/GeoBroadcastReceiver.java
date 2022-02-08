package com.example.ergtserpe;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingEvent;

import java.util.List;



public class GeoBroadcastReceiver extends BroadcastReceiver {

    private static final String TAG = "GeoBroadcastReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        Toast.makeText(context, "Geofence triggered...", Toast.LENGTH_SHORT).show();

        /**----- Enter or Exit and the Coordinates-----*/
        GeofencingEvent geofencingEvent = GeofencingEvent.fromIntent(intent);

        if(geofencingEvent.hasError()){
            Log.d(TAG,"onReceive: Error receiving geo event");
            return;
        }

        /**-----LIST OF GEOFENCE-----*/
        List<Geofence> geofenceList = geofencingEvent.getTriggeringGeofences();
        //Log.d(TAG, String.valueOf(geofenceList));


        /**-----FOR SELF DEBUG-----*/
        for(Geofence geofence:geofenceList){
            Log.d(TAG,"On Receive"+geofence.toString()+geofence.getRequestId());
        }

        /**----- Enter or Exit-----*/
        int transitionType = geofencingEvent.getGeofenceTransition();

        /**-----Coordinates-----*/
        Location userLocation = geofencingEvent.getTriggeringLocation();

        /**----- Create Coordinate Object for Insert in DB-----*/
        ControllerDB controller = Room.databaseBuilder(context,ControllerDB.class,"COORDINATE").build();
        CoordinateDAO coordinateDAO= controller.CoordinateDAO();
        Coordinate coordinate = new Coordinate();

        coordinate.setLatitude(userLocation.getLatitude());
        coordinate.setLongitude(userLocation.getLongitude());
        coordinate.setTransition(transitionType);
        coordinate.setTimestamp(userLocation.getTime());

        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {

                coordinateDAO.insertCoordinate(coordinate);
                List<Coordinate> coordinateList = coordinateDAO.getAllCoordinates();
                for(Coordinate coordinates:coordinateList){
                    Log.d(TAG,"lat"+coordinates.getLatitude());
                    Log.d(TAG,"lon"+coordinates.getLongitude());
                    Log.d(TAG,"transitionType"+coordinates.getTransition());
                    Log.d(TAG,"timestamp"+coordinates.getTimestamp());
                }

            }
        });

        t.start();


        switch (transitionType){
            case Geofence.GEOFENCE_TRANSITION_ENTER:
                Toast.makeText(context, "Geofence transition enter", Toast.LENGTH_SHORT).show();
                break;
            case Geofence.GEOFENCE_TRANSITION_EXIT:
                Toast.makeText(context, "Geofence transition exit", Toast.LENGTH_SHORT).show();
                break;
        }

        /**-----Close DataBase-----*/
        controller.close();
    }



}