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

public class OrganizationsData extends Thread{
    private String APIKey="7848f7a2-78d6-452f-85e8-9fdc643ed267";

    private String object="Музей";
    private String object1="Достопримечательность";
    private String object2="Парк";

    private String type="biz";
    private String lang="ru_RU";
    private String lat;//широта
    private String lon;//долгота
    private String deltaLat;
    private String deltaLon;
    private String results="40";
    private ArrayList<String> result=new ArrayList<>();
    private FragmentCard fragment;
    private ArrayList<PlaceOrganization> placeOrganizations;

    public OrganizationsData(String lat, String lon, String deltaLat, String deltaLon){
        this.lat=lat;
        this.lon=lon;
        this.deltaLat=deltaLat;
        this.deltaLon=deltaLon;
    }

    public OrganizationsData(String lat, String lon, String deltaLat, String deltaLon, FragmentCard fragment, ArrayList<PlaceOrganization> placeOrganizations){
        this.lat=lat;
        this.lon=lon;
        this.deltaLat=deltaLat;
        this.deltaLon=deltaLon;
        this.fragment=fragment;
        this.placeOrganizations=placeOrganizations;
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
                    System.out.println("ADD 1");
                    result.add(line);
                }
            }

            URL url1 = new URL("https://search-maps.yandex.ru/v1/?text="+object1+"&apikey="+APIKey+"&lang="+lang+"&type="+type+"&ll="+lon+","+lat+"&spn="+deltaLon+","+deltaLat+"&results="+results);
            HttpURLConnection conn1 = (HttpURLConnection) url1.openConnection();
            conn.setRequestMethod("GET");
            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(conn1.getInputStream()))) {
                for (String line; (line = reader.readLine()) != null; ) {
                    System.out.println("ADD 2");
                    result.add(line);
                }
            }


            URL url2 = new URL("https://search-maps.yandex.ru/v1/?text="+object2+"&apikey="+APIKey+"&lang="+lang+"&type="+type+"&ll="+lon+","+lat+"&spn="+deltaLon+","+deltaLat+"&results="+results);
            HttpURLConnection conn2 = (HttpURLConnection) url2.openConnection();
            conn.setRequestMethod("GET");
            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(conn2.getInputStream()))) {
                for (String line; (line = reader.readLine()) != null; ) {
                    System.out.println("ADD 3");
                    result.add(line);
                }
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


        Organization organization1=new Organization();
        Organization organization2=new Organization();
        Organization organization3=new Organization();
        System.out.println(result.size());
        String data1 =result.get(0);
        String data2 =result.get(1);
        String data3 =result.get(2);
        Moshi moshi = new Moshi.Builder().build();
        JsonAdapter<Organization> jsonAdapter = moshi.adapter(Organization.class);
        try {
            organization1 = jsonAdapter.fromJson(data1);
            organization2 = jsonAdapter.fromJson(data2);
            organization3 = jsonAdapter.fromJson(data3);
        } catch (IOException e) {
            e.printStackTrace();
        }

        for(int i=0;i<organization1.features.size();i++){
            placeOrganizations.add(new PlaceOrganization(organization1.features.get(i).properties.CompanyMetaData,(ArrayList<String>) organization1.features.get(i).geometry.coordinates ));
        }
        for(int i=0;i<organization2.features.size();i++){
            placeOrganizations.add(new PlaceOrganization(organization2.features.get(i).properties.CompanyMetaData,(ArrayList<String>) organization2.features.get(i).geometry.coordinates ));
        }
        for(int i=0;i<organization3.features.size();i++){
            placeOrganizations.add(new PlaceOrganization(organization3.features.get(i).properties.CompanyMetaData,(ArrayList<String>) organization3.features.get(i).geometry.coordinates ));
        }

        System.out.println("LOADED ORGANIZATIONS");
    }
    public ArrayList<companyMetaData> getResult(){
        ArrayList<companyMetaData> organization=new ArrayList<>();
        Organization organization1=new Organization();
        Organization organization2=new Organization();
        Organization organization3=new Organization();
        String data1 =result.get(0);
        String data2 =result.get(1);
        String data3 =result.get(2);
        Moshi moshi = new Moshi.Builder().build();
        JsonAdapter<Organization> jsonAdapter = moshi.adapter(Organization.class);
        try {
            organization1 = jsonAdapter.fromJson(data1);
            organization2 = jsonAdapter.fromJson(data2);
            organization3 = jsonAdapter.fromJson(data3);
        } catch (IOException e) {
            e.printStackTrace();
        }
        organization=Organization.toOneArray(organization1.getData(), organization2.getData(), organization3.getData());
        return organization;
    }
}