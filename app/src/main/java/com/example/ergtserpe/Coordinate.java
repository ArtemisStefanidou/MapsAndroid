package com.example.ergtserpe;


import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * Δημιουργία του αντίστοιχου πίνακα στη βάση μου (σε ένα αντίστοιχο αρχείο)
 * Το αρχείο δημιουργείται όταν ο χρήστης χρησιμοποιήσει για πρώτη φορά την εφαρμογή
 * Κάθε φορά που ο χρήστης θα τρέχει την δεύτερη εφαρμογή η βάση θα του δείχνει όλα τα geofence του μέχρι στιγμής
 * Το αρχείο της βάσης θα διαγραφτεί όταν ο χρήστης αποφασίσει να απεγκαταστήσει την εφαρμογή από το κινητό του
 * Για να αναγνωρίσει το πρόγραμμα μας τα annotations και να μπορέσουμε να φτιάξουμε τη βάση μας
 * πρέπει να κάνουμε implement στο gradle το room
 */
@Entity(tableName = "COORDINATE")
public class Coordinate {

    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "LAT")
    public Double latitude;

    @ColumnInfo(name = "LONG")
    public Double longitude;

    @ColumnInfo(name = "TRANSITION")
    public int transition;

    @ColumnInfo(name = "TIMESTAMP")
    public Long timestamp;


    public int getId() {
        return id;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public int getTransition() {
        return transition;
    }

    public void setTransition(int transition) {
        this.transition = transition;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }
}
