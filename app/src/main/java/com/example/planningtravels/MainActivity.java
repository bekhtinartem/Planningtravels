package com.example.planningtravels;

import android.content.Context;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import java.util.ArrayList;


@Database(entities = {TripDataBase.class , Sight.class,Towns.class, Notes.class }, version = 1)
abstract class AppDatabase extends RoomDatabase {
    public abstract TripDataBaseDao getTripDataBaseDao();
    public abstract  TownsDao getTownsDao();
    public abstract SightDao getSightDao();
    public abstract NotesDao getNotesDao();
}

public class MainActivity extends AppCompatActivity {
    private ArrayList<Fragment> fragments=new ArrayList<>();
    private Trip trip= new Trip();
    private AppDatabase db;
    public static Point getAppUsableScreenSize(Context context) {
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        return size;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Display display = getWindowManager().getDefaultDisplay();


        int width =  display.getWidth();
        int height = display.getHeight() - 173;
        System.out.println(width+" "+height);

        db=Room.databaseBuilder(this.getApplicationContext(), AppDatabase.class, "CDataBase").build();
        FragmentRoutes fragmentRoutes = new FragmentRoutes(width, height);
        FragmentAdding fragmentAdding = new FragmentAdding(width, height);
        FragmentWeather fragmentWeather = new FragmentWeather(width, height);
        FragmentHotel fragmentHotel = new FragmentHotel(width, height);
        FragmentSight fragmentSight = new FragmentSight(width, height);
        FragmentAnyroute fragmentAnyroute = new FragmentAnyroute(width, height);
        FragmentCalendar fragmentCalendar = new FragmentCalendar(width, height);
        FragmentCard fragmentCard = new FragmentCard(width, height);


        fragmentRoutes.setFragments(fragments);
        fragmentAdding.setFragments(fragments);
        fragmentWeather.setFragments(fragments);
        fragmentHotel.setFragments(fragments);
        fragmentSight.setFragments(fragments);
        fragmentAnyroute.setFragments(fragments);
        fragmentCalendar.setFragments(fragments);
        fragmentCard.setFragments(fragments);


        fragments.add(fragmentRoutes);
        fragments.add(fragmentAdding);
        fragments.add(fragmentWeather);
        fragments.add(fragmentHotel);
        fragments.add(fragmentSight);
        fragments.add(fragmentAnyroute);
        fragments.add(fragmentCalendar);
        fragments.add(fragmentCard);

        fragmentAdding.setTrip(trip);
        fragmentCard.setTrip(trip);
        fragmentCard.setDataBase(db);
        fragmentRoutes.setDataBase(db);

        setContentView(R.layout.activity);


        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.add(R.id.place_holder, fragmentRoutes);
        ft.commit();
    }

}
