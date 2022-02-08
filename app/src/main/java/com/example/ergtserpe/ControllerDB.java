package com.example.ergtserpe;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;


@Database(entities = {Coordinate.class},version = 1)
public abstract class ControllerDB extends RoomDatabase {

    public abstract CoordinateDAO CoordinateDAO();


}

