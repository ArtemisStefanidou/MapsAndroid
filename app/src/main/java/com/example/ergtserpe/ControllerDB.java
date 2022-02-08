package com.example.ergtserpe;


import androidx.room.Database;
import androidx.room.RoomDatabase;

/***
 * Του δίνουμε την δομή και την έκδοση της βάσης μας
 * Το χρησιμοποιούμε για να μην κάνουμε απευθείας queries σε sql
 * Ο controller κάνει αυτόματα την μετατροπή από query σε cursor που έχει μία λίστα πχ με αυτά που του
 * έχουμε περάσει ως δομή στη βάση
 * Μας δίνει πρόσβαση στις συνναρτήσεις που έχουμε υλοποιήση στο CoordinateDAO
 */

@Database(entities = {Coordinate.class},version = 1)
public abstract class ControllerDB extends RoomDatabase {

    public abstract CoordinateDAO CoordinateDAO();

}

