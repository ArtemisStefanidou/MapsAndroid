package com.example.ergtserpe;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface CoordinateDAO {
    @Query("SELECT * FROM COORDINATE")
    public List<Coordinate> getAllCoordinates();

    @Insert
    public void insertCoordinate(Coordinate... coordinates);
}
