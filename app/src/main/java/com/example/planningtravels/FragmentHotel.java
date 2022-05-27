package com.example.planningtravels;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.util.Linkify;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.util.ArrayList;

public class FragmentHotel extends Fragment {
    private int width;
    private int height;
    private ArrayList<Fragment> fragments;
    private Integer pastFragnent=null;
    private ImageView imageBack;
    private Button back;
    private Button select;
    private String phoneNumber;
    private String adress;
    private String name;
    private String url;
    private TextView nameView;
    private TextView phoneView;
    private TextView adressView;
    private TextView urlView;
    private Trip trip;
    private int idTown;
    private PlaceHotel Hotel;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View fragment = inflater.inflate(R.layout.hotel, container, false);
        imageBack= (ImageView) fragment.findViewById(R.id.hotel_card_background);
        Bitmap imageBackground = BitmapFactory.decodeResource(getResources(), R.drawable.mir);
        double k =Math.min(1.0*(imageBackground.getWidth()-850)/width, 1.0*(imageBackground.getHeight()-200)/height);
        if(k>1.5){
            k=1.5;
        }
        Bitmap croppedBitmap = Bitmap.createBitmap(imageBackground, 850, 200, (int)(width*k),(int)( height*k));
        imageBack.setImageBitmap(croppedBitmap);

        back=(Button)fragment.findViewById(R.id.button_hotel_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (pastFragnent){
                    case(1):
                        FragmentAdding fragmentAdding = (FragmentAdding) fragments.get(1);
                        fragmentAdding.setPastFragnent(3);
                        break;
                }
                FragmentManager fm = getActivity().getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.replace(R.id.place_holder, fragments.get(pastFragnent));
                ft.commit();
            }
        });

        select = (Button) fragment.findViewById(R.id.hotel_select);
        if(((FragmentCard)fragments.get(pastFragnent)).getPastFragment()==0){
            select.setVisibility(View.GONE);
        }else{
            select.setVisibility(View.VISIBLE);
            select.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //saving data
                }
            });

        }
        select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                trip.getTowns().get(idTown).setHotel(Hotel);
                FragmentManager fm = getActivity().getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.replace(R.id.place_holder, fragments.get(pastFragnent));
                ft.commit();
            }
        });

        nameView=(TextView) fragment.findViewById(R.id.hotel_name);
        nameView.setGravity(Gravity.CENTER_VERTICAL);

        nameView.setText(name);
        adressView=(TextView)fragment.findViewById(R.id.hotel_adress);
        adressView.setText("Адрес: "+adress);
        adressView.setGravity(Gravity.CENTER_VERTICAL);
        urlView=(TextView)fragment.findViewById(R.id.hotel_url);
        urlView.setGravity(Gravity.CENTER_VERTICAL);
        urlView.setText(url);

        Linkify.addLinks(urlView, Linkify.WEB_URLS);
        phoneView=(TextView) fragment.findViewById(R.id.hotel_phone);
        phoneView.setGravity(Gravity.CENTER_VERTICAL);
        phoneView.setText("Контактный телефон:\n"+phoneNumber);
        return fragment;
    }

    public void setFragments(ArrayList<Fragment> fragments){
        this.fragments=fragments;
    }

    public FragmentHotel(int width, int height){
        this.height=height;
        this.width=width;
    }

    public void setPastFragnent(int pastFragnent){
        this.pastFragnent=pastFragnent;
    }

    public void setPhoneNumber(String phoneNumber){
        this.phoneNumber=phoneNumber;
    }
    public void setAdress(String adress){
        this.adress=adress;
    }
    public void setName(String name){
        this.name=name;
    }
    public void setUrl(String url){
        this.url=url;
    }

    public void setTrip(Trip trip){
        this.trip=trip;
    }

    public Trip getTrip(){
        return this.trip;
    }

    public void setIdTown(int idTown){
        this.idTown=idTown;
    }

    public void setHotel(PlaceHotel Hotel){
        this.Hotel=Hotel;
    }
    public void setHeight(int height){
        this.height=height;
    }

    public void setWidth(int width){
        this.width=width;
    }

}
