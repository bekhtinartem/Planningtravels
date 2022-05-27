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

import com.prolificinteractive.materialcalendarview.CalendarDay;

import java.util.ArrayList;

public class FragmentWeather extends Fragment {
    private int width;
    private int height;
    private ArrayList<Fragment> fragments;
    private Integer pastFragnent=null;
    private String town;
    private ImageView imageBack;
    private Button back;
    private CalendarDay startDay;
    private CalendarDay endDay;
    private TextView Data;
    private int countDays;
    private String nameTown;
    private TextView Town;
    private Weather weather;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View fragment = inflater.inflate(R.layout.weather, container, false);
        imageBack= (ImageView) fragment.findViewById(R.id.weather_background);
        Bitmap imageBackground = BitmapFactory.decodeResource(getResources(), R.drawable.mir);
        double k =Math.min(1.0*(imageBackground.getWidth()-850)/width, 1.0*(imageBackground.getHeight()-200)/height);
        if(k>1.5){
            k=1.5;
        }
        Bitmap croppedBitmap = Bitmap.createBitmap(imageBackground, 850, 200, (int)(width*k),(int)( height*k));
        imageBack.setImageBitmap(croppedBitmap);

        back = (Button)fragment.findViewById(R.id.weather_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentAdding fragmentAdding = (FragmentAdding)fragments.get(1);
                fragmentAdding.setPastFragnent(2);
                FragmentManager fm = getActivity().getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.replace(R.id.place_holder,fragments.get(1));
                ft.commit();
            }
        });
        System.out.println("START DAY: "+startDay.getDay()+"."+startDay.getMonth()+"."+startDay.getYear());
        System.out.println("END DAY: "+endDay.getDay()+"."+endDay.getMonth()+"."+endDay.getYear());

        ArrayList<Forecasts> data= new ArrayList<>();
        for(int i=0;i<weather.forecasts.size();i++){
            String date = weather.forecasts.get(i).date;
            String d="";
            String m="";
            String y="";
            char arr[]= date.toCharArray();
            for(int j=0;j<arr.length;j++){
                if(j<=3){
                    y+=arr[j];
                }
                if(j>4 && j<=6){
                    m+=arr[j];
                }
                if(j>7){
                    d+=arr[j];
                }
            }
            int day=Integer.parseInt(d);
            int month = Integer.parseInt(m);
            int year = Integer.parseInt(y);
            if(year>startDay.getYear() || (year==startDay.getYear() && month>startDay.getMonth()+1) ||(year==startDay.getYear() && month==startDay.getMonth()+1 && day>=startDay.getDay())){
                if(year<endDay.getYear() || (year==endDay.getYear() && month<endDay.getMonth()+1) ||(year==startDay.getYear() && month==endDay.getMonth()+1 && day<=endDay.getDay()))
                    data.add(weather.forecasts.get(i));

            }

            System.out.println(day+"."+month+"."+year);
        }

        Data=(TextView) fragment.findViewById(R.id.weahter_data);
        String info="";
        for(int i=0;i<data.size();i++){
            info=info+data.get(i).date+"         "+(int)data.get(i).parts.day.temp_max+"/"+(int)data.get(i).parts.day.temp_min+"        "+data.get(i).parts.day.condition+"\n";
        }

        if (info.length()==0){
            info="К сожалению, информация о погоде на выбранные даты недоступна";
        }else{
            if(data.size()!=countDays){
                info+="\nК сожалению, иформация о погоде доступна только о части выбранного периода";
            }
        }
        Data.setText(info);

        Town=(TextView)fragment.findViewById(R.id.weather_info);
        Town.setText("г. "+nameTown);
        return fragment;
    }

    public void setFragments(ArrayList<Fragment> fragments){
        this.fragments=fragments;
    }

    public FragmentWeather(int width, int height){
        this.height=height;
        this.width=width;
    }

    public FragmentWeather(int width, int height, String town){
        this.height=height;
        this.width=width;
        this.town=String.copyValueOf(town.toCharArray());
    }

    public void setEndDay(CalendarDay endDay) {
        this.endDay = endDay;
    }

    public void setWeather(Weather weather){
        this.weather=weather;
    }

    public void setStartDay(CalendarDay startDay){
        this.startDay=startDay;
    }

    public void setPastFragnent(int pastFragnent){
        this.pastFragnent=pastFragnent;
    }

    public void setCountDays(int countDays){
        this.countDays=countDays;
    }

    public void setNameTown(String nameTown){
        this.nameTown=nameTown;
    }

    public void setHeight(int height){
        this.height=height;
    }

    public void setWidth(int width){
        this.width=width;
    }

}
