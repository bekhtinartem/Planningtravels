package com.example.planningtravels;

import androidx.room.Dao;
import androidx.room.Entity;
import androidx.room.Insert;
import androidx.room.PrimaryKey;
import androidx.room.Query;

import java.util.List;

@Entity
class TripDataBase {
    @PrimaryKey(autoGenerate = true) int id;
    String name;
    String startDate;
    String endDate;
}

@Dao
interface TripDataBaseDao{
    @Insert
    void insertAll(TripDataBase... tripDataBase);

    @Query("DELETE FROM TripDataBase WHERE (id=:id)" )
    void delete(int id);

    @Query("SELECT * FROM tripDataBase")
    List<TripDataBase> getAllTripDataBase();

}



@Entity
class Towns{
    @PrimaryKey(autoGenerate = true) int idTown;
    int idTrip;
    double lonHotel;
    double latHotel;
    String nameTown;
    String idHotel;
    String nameHotel;
    String addressHotell;
    String urlHotel;
}

@Dao
interface TownsDao{

    @Insert
    void insertAll(Towns... towns);

    @Query("DELETE FROM Towns WHERE (idTown=:id)" )
    void delete(int id);

    @Query("SELECT * FROM towns")
    List<Towns> getAllTowns();
}



@Entity
class Sight{
    @PrimaryKey(autoGenerate = true) int id;
    double lonSight;
    double latSight;
    int idTown;
    String idSight;
    String nameSight;
    String adressSight;
    String urlSight;
}

@Dao
interface SightDao{
    @Insert
    void insertAll(Sight... sights);

    @Query("DELETE FROM Sight WHERE (idTown=:idTown)" )
    void delete(int idTown);

    @Query("SELECT * FROM sight")
    List<Sight> getAllSight();

}

@Entity
class Notes{
    @PrimaryKey(autoGenerate = true) int idNote;
    int idTrip;
    String Text;
    int day;
    int month;
    int year;
}

@Dao
interface NotesDao{
    @Insert
    void insertAll(Notes... notes);

    @Query("DELETE FROM Notes WHERE (day=:day and month=:month and year=:year and idTrip=:idTrip)" )
    void delete(int day, int month, int year, int idTrip);

    @Query("DELETE FROM Notes WHERE (idTrip=:idTrip)" )
    void deleteTrip(int idTrip);

    @Query("SELECT * FROM notes")
    List<Notes> getAllNotes();

}