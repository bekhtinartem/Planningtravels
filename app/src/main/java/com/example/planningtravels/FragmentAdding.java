package com.example.planningtravels;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.yandex.mapkit.geometry.Point;

import java.util.ArrayList;

public class FragmentAdding extends Fragment {
    private int width;
    private int height;
    private int idPast=6;
    private ImageView imageBack;
    private Button back;
    private Button weather;
    private EditText NameTrip;
    private EditText town;
    private Button next;
    private Button add;
    private com.prolificinteractive.materialcalendarview.MaterialCalendarView calendar;
    CalendarDay startDay=null;
    CalendarDay endDay=null;
    private Integer pastFragnent=null;
    private ArrayList<Fragment> fragments;
    private ArrayList<CalendarDay> days =new ArrayList<>();
    private Trip trip;
    private String checkName="";
    private String checkTown="";
    ArrayList<CalendarDay> calendarDays = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View fragment = inflater.inflate(R.layout.adding, container, false);

        if(idPast==-1){
            trip.getTowns().clear();
            idPast=6;
        }
        imageBack= (ImageView) fragment.findViewById(R.id.adding_background);
        Bitmap imageBackground = BitmapFactory.decodeResource(getResources(), R.drawable.mir);
        double k =Math.min(1.0*(imageBackground.getWidth()-850)/width, 1.0*(imageBackground.getHeight()-200)/height);
        if(k>1.5){
            k=1.5;
        }

        town = (EditText) fragment.findViewById(R.id.adding_town);
        NameTrip = (EditText) fragment.findViewById(R.id.adding_name) ;
        back=(Button)fragment.findViewById(R.id.adding_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FragmentRoutes fragmentRoutes=(FragmentRoutes)fragments.get(0);
                fragmentRoutes.setPastFragnent(1);
                FragmentManager fm = getActivity().getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.replace(R.id.place_holder,fragments.get(0));
                ft.commit();
            }
        });
        next=(Button)fragment.findViewById(R.id.adding_next);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkName="";
                checkTown="";
                TownData townData = new TownData(town.getText().toString());
                townData.start();
                if(saveData(false)) {

                    FragmentCard fragmentCard = (FragmentCard) fragments.get(7);
                    trip.Was.set(0, true);
                    while (townData.isAlive()) {
                        continue;
                    }

                    fragmentCard.setCoordinates(trip.getTowns().get(0).getCoordinates().getLatitude(), trip.getTowns().get(0).getCoordinates().getLongitude());
                    fragmentCard.setPastFragnent(1);
                    FragmentManager fm = getActivity().getSupportFragmentManager();
                    FragmentTransaction ft = fm.beginTransaction();

                    ft.replace(R.id.place_holder, fragments.get(7));
                    ft.commit();
                }
            }
        });
        add=(Button)fragment.findViewById(R.id.adding_add);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveData(true);

            }
        });
        weather = (Button)fragment.findViewById(R.id.adding_weather);
        weather.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nameTown=town.getText().toString();
                TownData townData = new TownData(nameTown);
                townData.start();
                if(startDay!=null && endDay!=null){
                    int day=startDay.getDay();
                    int month = startDay.getMonth()+1;
                    int year = startDay.getYear();
                    while(townData.isAlive()){continue;}
                    double lon=0;
                    double lat=0;
                    if(townData.getTownJSON().features.size()>0){
                        lat = Float.parseFloat(townData.getTownJSON().features.get(0).geometry.coordinates.get(1));
                        lon =Float.parseFloat(townData.getTownJSON().features.get(0).geometry.coordinates.get(0));
                        WeatherData weatherData = new WeatherData(Double.toString(lat), Double.toString(lon), "7");
                        weatherData.start();
                        FragmentWeather fragmentWeather = (FragmentWeather)fragments.get(2);

                        fragmentWeather.setPastFragnent(1);
                        fragmentWeather.setEndDay(endDay);
                        fragmentWeather.setStartDay(startDay);
                        fragmentWeather.setCountDays(days.size());
                        fragmentWeather.setNameTown(nameTown);
                        FragmentManager fm = getActivity().getSupportFragmentManager();
                        FragmentTransaction ft = fm.beginTransaction();
                        while (weatherData.isAlive()){continue;}

                        Weather weather = weatherData.getResult();
                        fragmentWeather.setWeather(weather);
                        ft.replace(R.id.place_holder,fragments.get(2));
                        ft.commit();
                    }else{
                        Toast.makeText(getContext(), "Данные введены некорректно",  Toast.LENGTH_SHORT).show();

                    }
                }else{
                    Toast.makeText(getContext(), "Данные введены некорректно",  Toast.LENGTH_SHORT).show();
                }

            }
        });


        Bitmap croppedBitmap = Bitmap.createBitmap(imageBackground, 850, 200, (int)(width*k),(int)( height*k));
        imageBack.setImageBitmap(croppedBitmap);

        for(int i=CalendarDay.today().getYear()-1; i<CalendarDay.today().getYear()+1;i++){
            for(int j=0;j<12;j++){
                for(int f=0;f<32;f++){
                    try{
                        CalendarDay d = new CalendarDay(i, j, f);

                        calendarDays.add(d);

                    }catch (Exception e){
                        continue;
                    }
                }
            }
        }


        calendar = (com.prolificinteractive.materialcalendarview.MaterialCalendarView) fragment.findViewById(R.id.adding_calendar);

        calendar.addDecorator(new BookingDecorator(Color.WHITE, calendarDays));


        calendar.setOnDateChangedListener(new OnDateSelectedListener() {

            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                System.out.println(days.size());
                if(startDay==null){
                    startDay=date;
                }else{
                    if(endDay==null){
                        endDay=date;
                        if((endDay.getYear()>startDay.getYear() )|| (endDay.getYear()==startDay.getYear() && endDay.getMonth()>startDay.getMonth()) || (endDay.getYear()==startDay.getYear() && endDay.getMonth()==startDay.getMonth() && endDay.getDay()>=startDay.getDay())){
                            boolean flag=true;
                            for(int i=startDay.getYear();i<=endDay.getYear();i++){
                                System.out.println("YEAR "+i);
                                if(!flag) {
                                    break;
                                }
                                for(int j=0;j<12;j++){
                                    if(!flag){
                                        break;
                                    }
                                    for(int k=1;k<32;k++){
                                        if(((endDay.getYear()>=i )|| (endDay.getYear()==i && endDay.getMonth()>j) || (endDay.getYear()==i && endDay.getMonth()==j && endDay.getDay()>=k)) && ((i>startDay.getYear() ) || (i==startDay.getYear() && j>startDay.getMonth()) || (i==startDay.getYear() && j==startDay.getMonth() && k>=startDay.getDay()))) {
                                            CalendarDay calendarDay = new CalendarDay(i, j, k);
                                            days.add(calendarDay);
                                            if (i == endDay.getYear() && j == endDay.getMonth() && k == endDay.getDay()) {
                                                flag = false;
                                                break;
                                            }
                                        }

                                    }
                                }
                            }
                        }
                        calendar.addDecorator(new BookingDecorator(Color.WHITE, calendarDays));
                        calendar.addDecorator(new BookingDecorator(Color.RED, days));



                    }else{
                        days.clear();
                        calendar.addDecorator(new BookingDecorator(Color.WHITE, calendarDays));

                        calendar.addDecorator(new BookingDecorator(Color.RED, days));
                        endDay=null;
                        startDay=date;
                    }
                }
            }
        });

    calendar.setArrowColor(Color.GREEN);

    NameTrip.setText(checkName);
    town.setText(checkTown);
    return fragment;
    }
    private boolean saveData(boolean clear){// for saving data about town and clearing edittext
        if(days.size()!=0){
            checkName=NameTrip.getText().toString();
            checkTown=String.copyValueOf(town.getText().toString().toCharArray());
            String townName = String.copyValueOf(town.getText().toString().toCharArray());
            TownData townData = new TownData(townName);
            townData.start();
            String name = NameTrip.getText().toString();
            if(name==""){
                Toast.makeText(getContext(), "Данные введены некорректно",  Toast.LENGTH_SHORT).show();
                return false;
            }
            while (townData.isAlive()){continue;}
            if(townData.getResult()){

                Town town=new Town();
                town.setEndDay(days.get(days.size()-1));
                town.setStartDay(days.get(0));
                town.setName(townName);
                town.setCoordinates(new Point(Double.parseDouble(townData.getTownJSON().features.get(0).geometry.coordinates.get(1)), Double.parseDouble(townData.getTownJSON().features.get(0).geometry.coordinates.get(0))));
                trip.setName(name);
                trip.addTown(town);

            }else{
                Toast.makeText(getContext(), "Город не найден",  Toast.LENGTH_SHORT).show();
                if(clear) {
                    town.setText("");
                }
                return false;

            }
        }else{
            Toast.makeText(getContext(), "Данные введены некорректно", Toast.LENGTH_SHORT).show();
            if(clear) {
                town.setText("");
            }

            return false;

        }
        days.clear();
        calendar.addDecorator(new BookingDecorator(Color.WHITE, calendarDays));

        calendar.addDecorator(new BookingDecorator(Color.RED, days));
        if(clear) {
            town.setText("");
        }

        return true;

    }

    public FragmentAdding(int width, int height){
        this.height=height;
        this.width=width;
    }

    public void setFragments(ArrayList<Fragment> fragments){
        this.fragments=fragments;
    }

    public void setPastFragnent(int pastFragnent){
        this.pastFragnent=pastFragnent;
    }
    class BookingDecorator implements DayViewDecorator {
        private int mColor;
        private ArrayList<CalendarDay> mCalendarDayCollection;

        public BookingDecorator(int color, ArrayList<CalendarDay> calendarDayCollection) {
            mColor = color;
            mCalendarDayCollection = calendarDayCollection;
        }

        @Override
        public boolean shouldDecorate(CalendarDay day) {
            return mCalendarDayCollection.contains(day);
        }

        @Override
        public void decorate(DayViewFacade view) {
            view.addSpan(new ForegroundColorSpan(mColor));
            //view.setBackgroundDrawable(ContextCompat.getDrawable(getContext(),R.drawable.greenbox));

        }
    }

    public void setHeight(int height){
        this.height=height;
    }

    public void setWidth(int width){
        this.width=width;
    }

    public void setTrip(Trip trip){
    this.trip=trip;
    }

    public Trip getTrip(){
        return this.trip;
    }

    public void setIdPast(int idPast){
        this.idPast=idPast;
    }

    public int getIdPast() {
        return idPast;
    }
}




