package com.example.planningtravels;

import com.yandex.mapkit.geometry.Point;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

class Hotels{
    public String type;
    public props properties;
    public List<Features> features;

    public ArrayList<companyMetaData> getData(){
        ArrayList<companyMetaData> data = new ArrayList<>();
        for(int i=0;i<features.size();i++){
            data.add(features.get(i).properties.CompanyMetaData);
        }
        return data;
    }
}

class props{
    public responseMetaData ResponseMetaData;
}

class responseMetaData{
    public searchRequest SearchRequest;
    public searchResponse SearchResponse;
}

class searchRequest{
    public String request;
    public int results;
    public int skip;
    public List<List<String> > boundedBy;
}

class searchResponse{
    public int found;
    public List<List<String> > boundedBy;
    public String display;
}

class Features{
    public String type;
    public PropertiesFeauteres properties;
    public Geometry geometry;
}

class PropertiesFeauteres{
    public companyMetaData CompanyMetaData;
    public String description;
    public String name;
}

class companyMetaData{
    public String id;
    public String name;
    public String address;
    public String url;
    public List<categories> Categories;
    public List<phones> Phones;
    public hours Hours;
}

class categories{
    public String Class;// In json class
    public String name;

}

class phones{
    public String type;
    public String formatted;
}

class availabilities{
    public boolean Everyday;
    public  boolean TwentyFourHours;
}

class Geometry{
    public String type;
    public List<String> coordinates;
}

class PlaceHotel{
    public companyMetaData data;
    public Point coordinates;

    public PlaceHotel() {}

    public static ArrayList<PlaceHotel> getPlases(Hotels hotels){
        ArrayList<PlaceHotel> out = new ArrayList<>();
        for(int i=0;i<hotels.features.size();i++){
            out.add(new PlaceHotel(hotels.features.get(i).properties.CompanyMetaData, (ArrayList<String>) hotels.features.get(i).geometry.coordinates));
        }
        return out;
    }
    public PlaceHotel(companyMetaData data, ArrayList<String> coordinates){
        this.coordinates = new Point(Double.parseDouble(coordinates.get(1)), Double.parseDouble(coordinates.get(0)));
        this.data=data;
    }


    public static ArrayList<PlaceHotel> toOneArray(ArrayList<PlaceHotel> a, ArrayList<PlaceHotel> b, ArrayList<PlaceHotel> c){
        ArrayList<PlaceHotel> arr = new ArrayList<>();
        for(int i=0;i<a.size();i++){
            arr.add(a.get(i));
        }

        for(int i=0;i<b.size();i++){
            arr.add(b.get(i));
        }

        for(int i=0;i<c.size();i++){
            arr.add(c.get(i));
        }

        TreeSet<PlaceHotel> set = new TreeSet<>(arr);
        ArrayList<PlaceHotel> out= new ArrayList<>(set);
        return out;
    }

    public PlaceHotel copy(){
        PlaceHotel hotel = new PlaceHotel();
        hotel.data=new companyMetaData();
        hotel.coordinates = new Point(this.coordinates.getLatitude(), this.coordinates.getLongitude());
        hotel.data.url=this.data.url;
        hotel.data.address = this.data.address;
        hotel.data.id = this.data.id;
        hotel.data.name =this.data.name;
        ArrayList<phones> Phones = new ArrayList<phones>();
        if(this.data.Phones!=null) {
            for (int i = 0; i < this.data.Phones.size(); i++) {
                phones Phone = new phones();
                Phone.type = String.copyValueOf(this.data.Phones.get(i).type.toCharArray());
                Phone.formatted = String.copyValueOf(this.data.Phones.get(i).formatted.toCharArray());
                Phones.add(Phone);
            }
        }
        hotel.data.Phones = Phones;
        ArrayList<categories> Categories = new ArrayList<>();
        for(int i=0;i<this.data.Categories.size();i++){
            categories Category = new categories();
            if(this.data.Categories.get(i).Class!=null) {
                Category.Class = String.copyValueOf(this.data.Categories.get(i).Class.toCharArray());
            }else{
                Category.Class="";
            }
            if(this.data.Categories.get(i).name!=null) {
                Category.name = String.copyValueOf(this.data.Categories.get(i).name.toCharArray());
            }else{
                Category.name="";
            }
            Categories.add(Category);
        }
        hotel.data.Categories= Categories;
        hours Hours = new hours();
        if(this.data.Hours!=null) {
            Hours.text = this.data.Hours.text;
        }else{
            Hours.text="";
        }
        ArrayList<availabilities> Availabilities = new ArrayList<>();
        for(int i=0;i<this.data.Hours.Availabilities.size();i++){
            availabilities Availabilitie = new availabilities();
            Availabilitie.Everyday=this.data.Hours.Availabilities.get(i).Everyday;
            Availabilitie.TwentyFourHours=this.data.Hours.Availabilities.get(i).TwentyFourHours;
            Availabilities.add(Availabilitie);
        }
        Hours.Availabilities=Availabilities;
        hotel.data.Hours=Hours;


        return hotel;
    }
}

class PlaceLandscapes{
    public companyMetaData data;
    public Point coordinates;

    public static ArrayList<PlaceLandscapes> getPlases(Organization organization){
        ArrayList<PlaceLandscapes> out = new ArrayList<>();
        for(int i=0;i<organization.features.size();i++){
            out.add(new PlaceLandscapes(organization.features.get(i).properties.CompanyMetaData, (ArrayList<String>) organization.features.get(i).geometry.coordinates));
        }
        return out;
    }
    public PlaceLandscapes(companyMetaData data, ArrayList<String> coordinates){
        this.coordinates = new Point(Double.parseDouble(coordinates.get(1)), Double.parseDouble(coordinates.get(0)));
        this.data=data;
    }
}