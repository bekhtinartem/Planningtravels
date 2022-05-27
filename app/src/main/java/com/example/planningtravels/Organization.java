package com.example.planningtravels;


import com.yandex.mapkit.geometry.Point;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

class Organization{
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

    public static ArrayList<companyMetaData> toOneArray(ArrayList<companyMetaData> a, ArrayList<companyMetaData> b, ArrayList<companyMetaData> c){
        ArrayList<companyMetaData> arr = new ArrayList<>();
        for(int i=0;i<a.size();i++){
            arr.add(a.get(i));
        }

        for(int i=0;i<b.size();i++){
            arr.add(b.get(i));
        }

        for(int i=0;i<c.size();i++){
            arr.add(c.get(i));
        }

        TreeSet<companyMetaData> set = new TreeSet<>(arr);
        ArrayList<companyMetaData> out= new ArrayList<>(set);
        return out;
    }

}
class PlaceOrganization{
    public companyMetaData data;
    public Point coordinates;

    public PlaceOrganization() {

    }

    public static ArrayList<PlaceOrganization> getPlases(Organization organization){
        ArrayList<PlaceOrganization> out = new ArrayList<>();
        for(int i=0;i<organization.features.size();i++){
            out.add(new PlaceOrganization(organization.features.get(i).properties.CompanyMetaData,(ArrayList<String>) organization.features.get(i).geometry.coordinates));
        }
        return out;
    }
    public PlaceOrganization(companyMetaData data, ArrayList<String> coordinates){
        this.coordinates = new Point(Double.parseDouble(coordinates.get(1)), Double.parseDouble(coordinates.get(0)));

        this.data=data;
    }


    public static ArrayList<PlaceOrganization> toOneArray(ArrayList<PlaceOrganization> a, ArrayList<PlaceOrganization> b, ArrayList<PlaceOrganization> c){
        ArrayList<PlaceOrganization> arr = new ArrayList<>();
        for(int i=0;i<a.size();i++){
            arr.add(a.get(i));
        }

        for(int i=0;i<b.size();i++){
            arr.add(b.get(i));
        }

        for(int i=0;i<c.size();i++){
            arr.add(c.get(i));
        }

        TreeSet<PlaceOrganization> set = new TreeSet<>(arr);
        ArrayList<PlaceOrganization> out= new ArrayList<>(set);
        return out;
    }
    public PlaceOrganization copy(){
        PlaceOrganization organization = new PlaceOrganization();
        organization.data=new companyMetaData();
        organization.coordinates = new Point(this.coordinates.getLatitude(), this.coordinates.getLongitude());
        organization.data.url=this.data.url;
        organization.data.address = this.data.address;
        organization.data.id = this.data.id;
        organization.data.name = this.data.name;
        ArrayList<phones> Phones = new ArrayList<phones>();
        if(this.data.Phones!=null){
            for (int i = 0; i < this.data.Phones.size(); i++) {
                phones Phone = new phones();
                Phone.type = String.copyValueOf(this.data.Phones.get(i).type.toCharArray());
                Phone.formatted = String.copyValueOf(this.data.Phones.get(i).formatted.toCharArray());
                Phones.add(Phone);
            }
        }
        organization.data.Phones = Phones;
        ArrayList<categories> Categories = new ArrayList<>();
        if(this.data.Categories!=null) {
            for (int i = 0; i < this.data.Categories.size(); i++) {
                categories Category = new categories();
                if (this.data.Categories.get(i).Class != null) {
                    Category.Class = String.copyValueOf(this.data.Categories.get(i).Class.toCharArray());
                } else {
                    Category.Class = "";
                }
                if (this.data.Categories.get(i).name != null) {
                    Category.name = String.copyValueOf(this.data.Categories.get(i).name.toCharArray());
                } else {
                    Category.name = "";
                }
                Categories.add(Category);
            }
        }
        organization.data.Categories= Categories;
        hours Hours = new hours();
        if(this.data.Hours!=null) {
            Hours.text = this.data.Hours.text;
        }else{
            Hours.text="";
        }
        ArrayList<availabilities> Availabilities = new ArrayList<>();
        if(data.Hours!=null){
            if(data.Hours.Availabilities!=null){


                for(int i=0;i<this.data.Hours.Availabilities.size();i++){
                    availabilities Availabilitie = new availabilities();
                    Availabilitie.Everyday=this.data.Hours.Availabilities.get(i).Everyday;
                    Availabilitie.TwentyFourHours=this.data.Hours.Availabilities.get(i).TwentyFourHours;
                    Availabilities.add(Availabilitie);
                }
            }
        }
        Hours.Availabilities=Availabilities;
        organization.data.Hours=Hours;


        return organization;
    }

}