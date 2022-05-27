package com.example.planningtravels;

//Загрузка данных о погоде

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

class WeatherData extends Thread{
    private String APIKey="dd7ec09a-16d6-4e9a-b109-09d4c0479a01";
    private String lang="ru_RU";
    private boolean hours=true;
    private boolean extra=true;
    private String lat;//широта
    private String lon;//долгота
    private String limit;//длительность прогноза (в бесплатной версии не более 7 дней)
    private ArrayList<String> result=new ArrayList<>();
    public WeatherData(String lat, String lon, String limit){
        this.lat=lat;
        this.lon=lon;
        this.limit=limit;
    }
    @Override
    public void run(){
        try {

            URL url = new URL("https://api.weather.yandex.ru/v2/forecast?lat="+lat+"&lon="+lon+ "&lang=" +lang+"&limit="+limit+ "&hours="+hours+"&extra="+extra);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestProperty("X-Yandex-API-Key", APIKey);
            conn.setRequestMethod("GET");
            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()))) {
                for (String line; (line = reader.readLine()) != null; ) {
                    System.out.println("WEATHER   "+line);
                    result.add(line);
                }
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    public Weather getResult(){
        Weather weather=new Weather();
        String data =result.get(0);
        Moshi moshi = new Moshi.Builder().build();
        JsonAdapter<Weather> jsonAdapter = moshi.adapter(Weather.class);
        try {
            weather = jsonAdapter.fromJson(data);
            System.out.println(weather);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return weather;
    }
}