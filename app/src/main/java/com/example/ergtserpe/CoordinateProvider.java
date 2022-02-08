package com.example.ergtserpe;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.Room;
import androidx.sqlite.db.SimpleSQLiteQuery;

/**
 * Provider --> τον χρειαζόμαστε για να μπορέσουμε να επικοινωνούμε με άλλες εφαρμοφές,στην ουσία
 * να μπορέσει αυτή η εφαρμογή να διαθέση τις πληροφορίες της σε άλλες εφαρμογές
 */

public class CoordinateProvider extends ContentProvider {
    private UriMatcher uriMatcher;
    Context context;

    //καλείται με το που θα τρέξει το πρόγραμμα
    @Override
    public boolean onCreate() {

        //αρχικοποίηση uri
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        //δημιουργία του uri που θα "χτυπήσει" η δεύτερη εφαρμογή
        uriMatcher.addURI(dbDetails.AUTHORITY,dbDetails.TABLE_NAME,dbDetails.DB_VERSION);
        return false;

    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {

        context = MapsActivity.getContext();
        ControllerDB controller = Room.databaseBuilder(context,ControllerDB.class,"COORDINATE").build();
        Cursor cursor = null;

        switch(uriMatcher.match(uri)){

            case 1:
                //select * from COORDINATE
                SimpleSQLiteQuery query = new SimpleSQLiteQuery("SELECT * FROM "+dbDetails.TABLE_NAME);

                cursor = controller.query(query);
                break;
            default:
                //this query does not exist
                throw new IllegalArgumentException("This URI does not exist: " + uri);

        }
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }
}
