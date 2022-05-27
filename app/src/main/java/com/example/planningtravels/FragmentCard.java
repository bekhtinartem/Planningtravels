package com.example.planningtravels;


import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.yandex.mapkit.Animation;
import com.yandex.mapkit.MapKitFactory;
import com.yandex.mapkit.geometry.Point;
import com.yandex.mapkit.map.CameraListener;
import com.yandex.mapkit.map.CameraPosition;
import com.yandex.mapkit.map.CameraUpdateSource;
import com.yandex.mapkit.map.Map;
import com.yandex.mapkit.map.MapObject;
import com.yandex.mapkit.map.MapObjectTapListener;
import com.yandex.mapkit.mapview.MapView;
import com.yandex.runtime.image.ImageProvider;

import java.util.ArrayList;

public class FragmentCard extends Fragment {

    private final String MAPKIT_API_KEY = "fdca652f-8967-4fd4-af51-5600090b985b";
    private final Point TARGET_LOCATION = new Point(59.945933, 30.320045);
    private MapView mapView;
    private int width;
    private int height;
    private ArrayList<Fragment> fragments;
    private Integer pastFragnent=0;
    private Button back;
    private boolean created=false;
    private boolean showHotels=false;
    private boolean showLandscapes=false;
    private Button hotels;
    private Button landscapes;
    private double centre_lat=55.751574;
    private double centre_lon=37.573856;
    private ArrayList<PlaceHotel> hotelsData=new ArrayList<>();
    private ArrayList<PlaceOrganization> landscapesData=new ArrayList<>();
    private float zoom=-1;
    private boolean drawedHotels=false;
    private boolean drawedLandscapes=false;
    private HotelsData dataLoader = null;
    private OrganizationsData dataLoaderOrganization=null;
    private int countHotels=0;
    private int countLandscapes=0;
    private Trip trip;
    private Button next;
    private AppDatabase DataBase;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        if(!created) {

            MapKitFactory.setApiKey(MAPKIT_API_KEY);
            MapKitFactory.initialize(getContext());
            MapKitFactory.getInstance().onStart();
            created=true;
        }
        View fragment = inflater.inflate(R.layout.card, container, false);
        mapView = (MapView)fragment.findViewById(R.id.map);

        mapView.getMap().move(
                new CameraPosition(new Point(centre_lat, centre_lon), 11.0f, 0.0f, 0.0f),
                new Animation(Animation.Type.SMOOTH, 0),
                null);

        Button back = (Button)fragment.findViewById(R.id.card_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (pastFragnent){
                    case(0):
                        FragmentRoutes fragmentRoutes = (FragmentRoutes) fragments.get(0);
                        fragmentRoutes.setPastFragnent(7);
                        countHotels=0;
                        break;
                    case(1):
                        FragmentAdding fragmentAdding = (FragmentAdding) fragments.get(1);
                        fragmentAdding.setPastFragnent(7);
                }

                FragmentManager fm = getActivity().getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.replace(R.id.place_holder,fragments.get(pastFragnent));
                ft.commit();
            }
        });

        hotels=(Button)fragment.findViewById(R.id.card_hotel);
        hotels.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showHotels=!showHotels;
                if(!showHotels){

                }else{
                    if(hotelsData.size()==0) {
                        System.out.println("SHOW HOTELS");
                        dataLoader=new HotelsData(Double.toString(centre_lat), Double.toString(centre_lon), "1", "1", (FragmentCard)fragments.get(7) ,hotelsData);
                        dataLoader.start();
                        if(hotelsData.size()!=countHotels){
                            showHotels(11, hotelsData);
                        }
                    }

                }
            }
        });

        landscapes=(Button)fragment.findViewById(R.id.card_landscape);
        landscapes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showLandscapes=!showLandscapes;
                if(!showLandscapes){

                }else{
                    if(landscapesData.size()==0) {
                        System.out.println("SHOW LANDSCAPES");
                        dataLoaderOrganization=new OrganizationsData(Double.toString(centre_lat), Double.toString(centre_lon), "1", "1", (FragmentCard)fragments.get(7) ,landscapesData);
                        dataLoaderOrganization.start();
                        if(landscapesData.size()!=countLandscapes){
                            showLandscapes(11, landscapesData);
                        }
                    }

                }
            }
        });

        mapView.onStart();
        Map map = mapView.getMap();
        map.addCameraListener(new CameraListener() {
            @Override
            public void onCameraPositionChanged(@NonNull Map map, @NonNull CameraPosition cameraPosition, @NonNull CameraUpdateSource cameraUpdateSource, boolean b) {
                if(hotelsData.size()!=countHotels){
                    showHotels(11, hotelsData);
                }
                if(landscapesData.size()!=countLandscapes){
                    showLandscapes(11, landscapesData);
                }
                if(zoom==-1 || (hotelsData==null && showHotels) || (landscapesData==null && showLandscapes)){
                    zoom=cameraPosition.getZoom();
                    centre_lon=cameraPosition.getTarget().getLongitude();
                    centre_lat=cameraPosition.getTarget().getLatitude();
                }
                if(showHotels) {
                    if (Math.abs(centre_lat - cameraPosition.getTarget().getLatitude()) <= 1 && Math.abs(centre_lon - cameraPosition.getTarget().getLongitude()) <= 1) {
                        if(!drawedHotels && dataLoader==null) {
                            HotelsData dataLoader = new HotelsData(Double.toString(centre_lat), Double.toString(centre_lon), "1", "1", (FragmentCard) fragments.get(7), hotelsData);
                            dataLoader.start();
                            drawedHotels = true;
                        }
                    } else {
                        System.out.println("Reload data");
                        zoom = cameraPosition.getZoom();
                        centre_lon = cameraPosition.getTarget().getLongitude();
                        centre_lat = cameraPosition.getTarget().getLatitude();
                        HotelsData dataLoader = new HotelsData(Double.toString(centre_lat), Double.toString(centre_lon), "1", "1", (FragmentCard) fragments.get(7), hotelsData);
                        dataLoader.start();
                    }
                }


                if(showLandscapes) {
                    if (Math.abs(centre_lat - cameraPosition.getTarget().getLatitude()) <= 1 && Math.abs(centre_lon - cameraPosition.getTarget().getLongitude()) <= 1) {
                        if(!drawedLandscapes && dataLoaderOrganization==null) {
                            OrganizationsData dataLoaderOrganization = new OrganizationsData(Double.toString(centre_lat), Double.toString(centre_lon), "1", "1", (FragmentCard) fragments.get(7), landscapesData);
                            dataLoaderOrganization.start();
                            drawedLandscapes = true;
                        }
                    } else {
                        System.out.println("Reload data");
                        zoom = cameraPosition.getZoom();
                        centre_lon = cameraPosition.getTarget().getLongitude();
                        centre_lat = cameraPosition.getTarget().getLatitude();
                        OrganizationsData dataLoaderOrganization = new OrganizationsData(Double.toString(centre_lat), Double.toString(centre_lon), "1", "1", (FragmentCard) fragments.get(7), landscapesData);
                        dataLoaderOrganization.start();
                    }
                }


            }


        });

        next=(Button) fragment.findViewById(R.id.card_next);
        if (pastFragnent==0){
            back.setVisibility(View.VISIBLE);
            next.setVisibility(View.GONE);
        }else {
            back.setVisibility(View.VISIBLE);
            next.setVisibility(View.VISIBLE);
        }
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(pastFragnent!=0) {
                    if (trip.Was.get(trip.Was.size() - 1) == true) {
                        SaveOnDataBase thread = new SaveOnDataBase(trip, DataBase);
                        thread.start();
                        FragmentManager fm = getActivity().getSupportFragmentManager();
                        FragmentTransaction ft = fm.beginTransaction();
                        while (thread.isAlive()){continue;}
                        ft.replace(R.id.place_holder, fragments.get(0));
                        ft.commit();

                    } else {
                        for (int i = 0; i < trip.Was.size(); i++) {
                            if (trip.Was.get(i) == false) {
                                Town towns = trip.getTowns().get(i);
                                mapView.getMap().move(
                                        new CameraPosition(towns.getCoordinates(), 11.0f, 0.0f, 0.0f),
                                        new Animation(Animation.Type.SMOOTH, 0),
                                        null);
                                trip.Was.set(i, true);
                                break;
                            }
                        }

                    }
                }
            }
        });

        return fragment;
    }

    public void setFragments(ArrayList<Fragment> fragments){
        this.fragments=fragments;
    }

    public FragmentCard(int width, int height){
        this.height=height;
        this.width=width;
    }
    @Override
    public void onStop() {
        showHotels=false;
        showLandscapes=false;
        zoom=-1;
        drawedHotels=false;
        drawedLandscapes=false;
        super.onStop();

    }
    public void setPastFragnent(int pastFragnent){
        this.pastFragnent=pastFragnent;
    }
    ArrayList<Button> buttons = new ArrayList<>();
    public void showHotels(float zoom, ArrayList<PlaceHotel> data){
        System.out.println("DRAW HOTELS");
        Map map = mapView.getMap();
        Bitmap bitmap = drawSimpleBitmap("Hotel", (int)(500/zoom), Color.BLUE);
        ImageProvider imageProvider =ImageProvider.fromBitmap(bitmap);
        for(int i=countHotels;i<data.size();i++) {
            countHotels++;
            double lon = data.get(i).coordinates.getLongitude();
            double lat = data.get(i).coordinates.getLatitude();
            map.getMapObjects().addPlacemark(new Point(lat, lon),imageProvider );
        }
        map.getMapObjects().addTapListener(new MapObjectTapListener() {
            @Override
            public boolean onMapObjectTap(@NonNull MapObject mapObject, @NonNull Point point) {
                double lat= point.getLatitude();
                double lon = point.getLongitude();
                for(int i=0;i<data.size();i++){
                    if(Math.abs(data.get(i).coordinates.getLongitude()-lon)<=0.0015 && Math.abs(data.get(i).coordinates.getLatitude()-lat)<=0.0015){
                        FragmentHotel fragmentHotel = (FragmentHotel) fragments.get(3);
                        fragmentHotel.setPastFragnent(7);
                        fragmentHotel.setAdress(data.get(i).data.address);
                        fragmentHotel.setName(data.get(i).data.name);
                        if(data.get(i).data.Phones.get(0).formatted!=null) {
                            fragmentHotel.setPhoneNumber(data.get(i).data.Phones.get(0).formatted);
                        }else{
                            fragmentHotel.setPhoneNumber("Номер телефона отсутствует");
                        }
                        if(data.get(i).data.url!=null) {
                            fragmentHotel.setUrl(data.get(i).data.url);
                        }else{
                            fragmentHotel.setUrl("Сайт отсутствует");
                        }
                        int idTown=0;

                        for(int j=0;j<trip.Was.size();j++){
                            if(trip.Was.get(j)==true){
                                idTown=j;
                            }
                        }
                        fragmentHotel.setTrip(trip);
                        fragmentHotel.setIdTown(idTown);
                        fragmentHotel.setHotel(data.get(i));
                        countLandscapes=0;
                        countHotels=0;
                        FragmentManager fm = getActivity().getSupportFragmentManager();
                        FragmentTransaction ft = fm.beginTransaction();
                        ft.replace(R.id.place_holder, fragments.get(3));
                        ft.commit();
                    }
                }
                return true;
            }
        });
    }

    public void showLandscapes(float zoom, ArrayList<PlaceOrganization> data){
        System.out.println("DRAW LANDSCAPES");
        Map map = mapView.getMap();
        Bitmap bitmap = drawSimpleBitmap("Sight", (int)(500/zoom), Color.GREEN-700);
        ImageProvider imageProvider =ImageProvider.fromBitmap(bitmap);
        for(int i=countLandscapes;i<data.size();i++) {
            countLandscapes++;
            double lon = data.get(i).coordinates.getLongitude();
            double lat = data.get(i).coordinates.getLatitude();
            map.getMapObjects().addPlacemark(new Point(lat, lon),imageProvider );
        }
        map.getMapObjects().addTapListener(new MapObjectTapListener() {
            @Override
            public boolean onMapObjectTap(@NonNull MapObject mapObject, @NonNull Point point) {
                double lat= point.getLatitude();
                double lon = point.getLongitude();



                for(int i=0;i<data.size();i++){
                    if(Math.abs(data.get(i).coordinates.getLongitude()-lon)<=0.0015 && Math.abs(data.get(i).coordinates.getLatitude()-lat)<=0.0015){

                        FragmentSight fragmentSight = (FragmentSight)fragments.get(4);
                        fragmentSight.setPastFragnent(7);
                        fragmentSight.setTrip(trip);
                        fragmentSight.setDataAdress(data.get(i).data.address);
                        int idTown=0;

                        for(int j=0;j<trip.Was.size();j++){
                            if(trip.Was.get(j)==true){
                                idTown=j;
                            }
                        }
                        fragmentSight.setIdTown(idTown);
                        fragmentSight.setSight(data.get(i));
                        fragmentSight.setDataName(data.get(i).data.name);
                        if (data.get(i).data.Phones != null) {
                            if(data.get(i).data.Phones.get(0)!=null) {
                                fragmentSight.setDataPhone(data.get(i).data.Phones.get(0).formatted);
                            }else {
                                fragmentSight.setDataPhone("Номер телефона не найден");
                            }
                            }else{
                                fragmentSight.setDataPhone("Номер телефона не найден");
                        }
                        if(data.get(i).data.Hours!=null) {
                            if (data.get(i).data.Hours.text != null){
                                fragmentSight.setDataTime(data.get(i).data.Hours.text);
                            }else {
                                fragmentSight.setDataTime("Время работы неизвестно");
                            }

                            }else {
                                fragmentSight.setDataTime("Время работы неизвестно");

                            }

                        countLandscapes=0;
                        countHotels=0;
                        FragmentManager fm = getActivity().getSupportFragmentManager();
                        FragmentTransaction ft = fm.beginTransaction();
                        ft.replace(R.id.place_holder, fragments.get(4));
                        ft.commit();
                    }
                }



                return true;
            }
        });
    }

    public Bitmap drawSimpleBitmap(String number, int picSize, int color) {
        Bitmap bitmap = Bitmap.createBitmap(picSize, picSize, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        // отрисовка плейсмарка
        Paint paint = new Paint();
        paint.setColor(color);
        paint.setStyle(Paint.Style.FILL);
        canvas.drawCircle(picSize / 2, picSize / 2, picSize / 2, paint);
        // отрисовка текста
        paint.setColor(Color.WHITE);
        paint.setAntiAlias(true);
        paint.setTextSize(8);
        paint.setTextAlign(Paint.Align.CENTER);
        canvas.drawText(number, picSize / 2, picSize / 2 - ((paint.descent() + paint.ascent()) / 2), paint);
        return bitmap;
    }

    public int getPastFragment(){
        return this.pastFragnent;
    }

    public void setTrip(Trip trip){
        this.trip=trip;
    }

    public Trip getTrip(){
        return this.trip;
    }

    public void setCoordinates(double centre_lat, double centre_lon){
        this.centre_lat=centre_lat;
        this.centre_lon=centre_lon;
    }

    public void setDataBase(AppDatabase dataBase) {
        this.DataBase = dataBase;
    }

    public void setHeight(int height){
        this.height=height;
    }

    public void setWidth(int width){
        this.width=width;
    }

}