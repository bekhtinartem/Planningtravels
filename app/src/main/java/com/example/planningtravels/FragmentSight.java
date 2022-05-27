package com.example.planningtravels;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
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

public class FragmentSight extends Fragment {
    private int width;
    private int height;
    private ArrayList<Fragment> fragments;
    private Integer pastFragnent=null;
    private ImageView imageBack;
    private Button back;
    private Button select;
    private TextView name;
    private TextView time;
    private TextView adress;
    private TextView phoneNumber;
    private String dataName;
    private String dataAdress;
    private String dataPhone;
    private String dataTime;
    private Trip trip;
    private PlaceOrganization sight;
    private int idTown;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View fragment = inflater.inflate(R.layout.sight, container, false);
        imageBack= (ImageView) fragment.findViewById(R.id.sight_background);
        Bitmap imageBackground = BitmapFactory.decodeResource(getResources(), R.drawable.mir);
        double k =Math.min(1.0*(imageBackground.getWidth()-850)/width, 1.0*(imageBackground.getHeight()-200)/height);
        if(k>1.5){
            k=1.5;
        }
        Bitmap croppedBitmap = Bitmap.createBitmap(imageBackground, 850, 200, (int)(width*k),(int)( height*k));
        imageBack.setImageBitmap(croppedBitmap);
        back = (Button)fragment.findViewById(R.id.sight_back);
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

        select = (Button)fragment.findViewById(R.id.sight_select);
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
                trip.getTowns().get(idTown).addSight(sight);
                FragmentManager fm = getActivity().getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.replace(R.id.place_holder, fragments.get(pastFragnent));
                ft.commit();
            }
        });

        name=(TextView) fragment.findViewById(R.id.sight_name);
        name.setText(dataName);

        time=(TextView)fragment.findViewById(R.id.sight_time);
        time.setText("Работает: "+dataTime);

        adress=(TextView) fragment.findViewById(R.id.sight_adress);
        adress.setText(dataAdress);

        phoneNumber = (TextView) fragment.findViewById(R.id.sight_phone);
        phoneNumber.setText(dataPhone);

        return fragment;
    }

    public void setFragments(ArrayList<Fragment> fragments){
        this.fragments=fragments;
    }

    public FragmentSight(int width, int height){
        this.height=height;
        this.width=width;
    }

    public void setPastFragnent(int pastFragnent){
        this.pastFragnent=pastFragnent;
    }

    public void setTrip(Trip trip){
        this.trip=trip;
    }

    public Trip getTrip(){
        return this.trip;
    }

    public void setDataName(String dataName){
        this.dataName=dataName;
    }

    public void setDataAdress(String dataAdress){
        this.dataAdress=dataAdress;
    }

    public void setDataPhone(String dataPhone){
        this.dataPhone=dataPhone;
    }

    public void setDataTime(String dataTime){
        this.dataTime=dataTime;
    }

    public void setSight(PlaceOrganization sight){
        this.sight=sight;
    }

    public void setIdTown(int idTown){
        this.idTown=idTown;
    }

    public void setHeight(int height){
        this.height=height;
    }

    public void setWidth(int width){
        this.width=width;
    }

}
