package com.example.planningtravels;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class HotelsData extends Thread{
    private String APIKey="7848f7a2-78d6-452f-85e8-9fdc643ed267";
    private String object="Отель";
    private String type="biz";
    private String lang="ru_RU";
    private String lat;//широта
    private String lon;//долгота
    private String deltaLat;
    private String deltaLon;
    private String results="100";
    FragmentCard fragment = null;
    ArrayList<PlaceHotel> hotels=null;
    private ArrayList<String> result=new ArrayList<>();
    public HotelsData(String lat, String lon, String deltaLat, String deltaLon){
        this.lat=lat;
        this.lon=lon;
        this.deltaLat=deltaLat;
        this.deltaLon=deltaLon;
    }

    public HotelsData(String lat, String lon, String deltaLat, String deltaLon, FragmentCard fragment){
        this.lat=lat;
        this.lon=lon;
        this.deltaLat=deltaLat;
        this.deltaLon=deltaLon;
        this.fragment=fragment;
    }

    public HotelsData(String lat, String lon, String deltaLat, String deltaLon, FragmentCard fragment, ArrayList<PlaceHotel> hotels){
        this.lat=lat;
        this.lon=lon;
        this.deltaLat=deltaLat;
        this.deltaLon=deltaLon;
        this.fragment=fragment;
        this.hotels=hotels;
    }

    @Override
    public void run(){
        try {

            URL url = new URL("https://search-maps.yandex.ru/v1/?text="+object+"&apikey="+APIKey+"&lang="+lang+"&type="+type+"&ll="+lon+","+lat+"&spn="+deltaLon+","+deltaLat+"&results="+results);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()))) {
                for (String line; (line = reader.readLine()) != null; ) {
                    result.add(line);
                }
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Hotels hotels=new Hotels();
        String data =result.get(0);
        Moshi moshi = new Moshi.Builder().build();
        JsonAdapter<Hotels> jsonAdapter = moshi.adapter(Hotels.class);
        try {
            hotels = jsonAdapter.fromJson(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
        ArrayList<PlaceHotel> placeHotel = PlaceHotel.getPlases(hotels);
        for(int i=0;i<placeHotel.size();i++){
            this.hotels.add(placeHotel.get(i));
        }

    }
    public ArrayList<PlaceHotel> getResult(){
        Hotels hotels=new Hotels();
        String data =result.get(0);
        Moshi moshi = new Moshi.Builder().build();
        JsonAdapter<Hotels> jsonAdapter = moshi.adapter(Hotels.class);
        try {
            hotels = jsonAdapter.fromJson(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
        ArrayList<PlaceHotel> placeHotel = PlaceHotel.getPlases(hotels);


        return placeHotel;
    }
    public void draw(){
        if(fragment!=null){
            Hotels hotels=new Hotels();
            String data =result.get(0);
//        Gson g = new Gson();
//        Hotels hotels1 = g.fromJson(data, Hotels.class);
//        System.out.println(hotels1);
            Moshi moshi = new Moshi.Builder().build();
            JsonAdapter<Hotels> jsonAdapter = moshi.adapter(Hotels.class);
            try {
                hotels = jsonAdapter.fromJson(data);
            } catch (IOException e) {
                e.printStackTrace();
            }
            ArrayList<PlaceHotel> placeHotel = PlaceHotel.getPlases(hotels);
            fragment.showHotels(11, placeHotel);
            System.out.println("DRAW FROM THREAD");
        }
    }

}
