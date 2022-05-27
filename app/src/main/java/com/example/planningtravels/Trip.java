package com.example.planningtravels;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.yandex.mapkit.geometry.Point;

import java.util.ArrayList;

public class Trip {
    private CalendarDay startDate;
    private CalendarDay endDate;
    private String name;
    private ArrayList<Town> towns= new ArrayList<>();
    ArrayList<Boolean> Was = new ArrayList<>();

    public void setStartDate(CalendarDay startDate){
        this.startDate=startDate;
    }

    public void setEndDate(CalendarDay endDate){
        this.endDate=endDate;
    }

    public void addTown(Town town){
        Was.add(false);
        this.towns.add(town);
    }

    public void setName(String name){
        this.name =String.copyValueOf(name.toCharArray());
    }

    public CalendarDay getStartDate(){
        return this.startDate;
    }

    public CalendarDay getEndDate(){
        return this.endDate;
    }

    public String getName(){
        return this.name;
    }
    public ArrayList<Town> getTowns(){
        return this.towns;
    }
}

class Town{
    private CalendarDay startDay;
    private CalendarDay endDay;
    private String name;
    private PlaceHotel hotel ;
    private ArrayList<PlaceOrganization> sights= new ArrayList<>();
    private Point Coordinates;

    public Point getCoordinates(){
        return this.Coordinates;
    }

    public void setCoordinates(Point Coordinates){
        this.Coordinates=Coordinates;
    }

    public CalendarDay getStartDay(){
        return this.startDay;
    }

    public CalendarDay getEndDay(){
        return this.endDay;
    }

    public String getName(){
        return this.name;
    }

    public PlaceHotel getHotel(){
        return this.hotel;
    }

    public ArrayList<PlaceOrganization> getSights() {
        return this.sights;
    }

    public void setStartDay(CalendarDay startDay){
        this.startDay = startDay;
    }

    public void setEndDay(CalendarDay endDay){
        this.endDay=endDay;
    }

    public void setName(String name){
        this.name=String.copyValueOf(name.toCharArray());
    }
    public void setHotel(PlaceHotel hotel){
        this.hotel = hotel.copy();
    }
    public void addSight(PlaceOrganization sight){
        sights.add(sight.copy());
    }

}
