package com.example.planningtravels;

public class SaveOnDataBase extends Thread{
    Trip trip;
    AppDatabase DataBase;
    public SaveOnDataBase(Trip trip, AppDatabase DataBase){
        this.trip=trip;
        this.DataBase=DataBase;
    }
    public void run(){
        trip.setStartDate(trip.getTowns().get(0).getStartDay());
        trip.setEndDate(trip.getTowns().get(trip.getTowns().size() - 1).getEndDay());
        TripDataBase tripDataBase = new TripDataBase();
        tripDataBase.name=trip.getName();

        tripDataBase.endDate = new String(Integer.toString(trip.getEndDate().getDay()) + "." + Integer.toString(trip.getEndDate().getMonth() + 1) + "." + Integer.toString(trip.getEndDate().getYear()));
        tripDataBase.startDate = new String(Integer.toString(trip.getStartDate().getDay()) + "." + Integer.toString(trip.getStartDate().getMonth() + 1) + "." + Integer.toString(trip.getStartDate().getYear()));
        DataBase.getTripDataBaseDao().insertAll(tripDataBase);
        for (int i = 0; i < trip.getTowns().size(); i++) {
            Towns townsDataBase = new Towns();
            townsDataBase.idTrip = DataBase.getTripDataBaseDao().getAllTripDataBase().get(DataBase.getTripDataBaseDao().getAllTripDataBase().size() - 1).id;
            townsDataBase.nameTown=trip.getTowns().get(i).getName();

            if(trip.getTowns().get(i).getHotel()!=null) {
                townsDataBase.latHotel = trip.getTowns().get(i).getHotel().coordinates.getLatitude();
                townsDataBase.lonHotel = trip.getTowns().get(i).getHotel().coordinates.getLongitude();
                townsDataBase.addressHotell = trip.getTowns().get(i).getHotel().data.address;
                townsDataBase.nameHotel = trip.getTowns().get(i).getHotel().data.name;
                townsDataBase.urlHotel = trip.getTowns().get(i).getHotel().data.address;
                townsDataBase.idHotel = trip.getTowns().get(i).getHotel().data.id;
            }
            DataBase.getTownsDao().insertAll(townsDataBase);

            for (int j = 0; j < trip.getTowns().get(i).getSights().size(); j++) {
                int TownId;
                Sight sightDataBase = new Sight();
                sightDataBase.idTown=DataBase.getTownsDao().getAllTowns().get(DataBase.getTownsDao().getAllTowns().size()-1).idTown;
                sightDataBase.adressSight = trip.getTowns().get(i).getSights().get(j).data.address;
                sightDataBase.nameSight = trip.getTowns().get(i).getSights().get(j).data.name;
                sightDataBase.urlSight = trip.getTowns().get(i).getSights().get(j).data.url;
                sightDataBase.idSight = trip.getTowns().get(i).getSights().get(j).data.id;
                sightDataBase.latSight = trip.getTowns().get(i).getSights().get(j).coordinates.getLatitude();
                sightDataBase.lonSight = trip.getTowns().get(i).getSights().get(j).coordinates.getLongitude();
                DataBase.getSightDao().insertAll(sightDataBase);
                System.out.println("ALL SAVED");

            }
        }
    }
}
