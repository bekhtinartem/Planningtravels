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

public class TownData extends Thread{
    private String APIKey="7848f7a2-78d6-452f-85e8-9fdc643ed267";
    private String town;
    private String type="geo";
    private String lang="ru_RU";
    private ArrayList<String> result=new ArrayList<>();
    private TownJSON townJSON = new TownJSON();
    public TownData(String town){
        this.town = town;
    }


    @Override
    public void run(){
        try {

            URL url = new URL("https://search-maps.yandex.ru/v1/?text="+"город "+town+"&apikey="+APIKey+"&lang="+lang+"&type="+type);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
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
        String data =result.get(0);
        Moshi moshi = new Moshi.Builder().build();
        JsonAdapter<TownJSON> jsonAdapter = moshi.adapter(TownJSON.class);
        try {
            townJSON = jsonAdapter.fromJson(data);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    public boolean getResult(){



        return townJSON!=null;
    }

    public TownJSON getTownJSON(){
        return this.townJSON;
    }
}