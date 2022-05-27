package com.example.planningtravels;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class FragmentRoutes extends Fragment {
    private int width;
    private int height;
    private Button addRoute;
    private ImageView imageBack;
    private Button card;
    private ArrayList<Fragment> fragments;
    private Integer pastFragnent=null;
    private AppDatabase DataBase;
    private List<TripDataBase> tripDataBases;
    private List<Towns> townsData;
    private List<Sight> sightsData;
    List<Notes> notes;

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        DataLoader dataLoader = new DataLoader(DataBase);
        dataLoader.start();
        View fragment =   inflater.inflate(R.layout.routes, container, false);


        addRoute=(Button)fragment.findViewById(R.id.routes_add);
        addRoute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentAdding fragmentAdding = (FragmentAdding)fragments.get(1);
                fragmentAdding.setPastFragnent(0);
                fragmentAdding.setIdPast(-1);
                FragmentManager fm = getActivity().getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.replace(R.id.place_holder,fragments.get(1));
                ft.commit();
            }
        });
        card=(Button)fragment.findViewById(R.id.routes_card);
        card.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                FragmentCard fragmentCard = (FragmentCard) fragments.get(7);
                fragmentCard.setPastFragnent(0);
                FragmentManager fm = getActivity().getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.replace(R.id.place_holder,fragments.get(7));
                ft.commit();
            }
        });
        imageBack= (ImageView) fragment.findViewById(R.id.card_background);
        Bitmap imageBackground = BitmapFactory.decodeResource(getResources(), R.drawable.mir);
        double k =Math.min(1.0*(imageBackground.getWidth()-850)/width, 1.0*(imageBackground.getHeight()-200)/height);
        if(k>1.5){
            k=1.5;
        }

        Bitmap croppedBitmap = Bitmap.createBitmap(imageBackground, 850, 200, (int)(width*k),(int)( height*k));

        RecyclerView recyclerView = (RecyclerView) fragment.findViewById(R.id.routes_data);
        while (dataLoader.isAlive()){
            continue;
        }


        tripDataBases = dataLoader.getTripDataBase();
        townsData= dataLoader.getTowns();
        sightsData = dataLoader.getSights();
        notes = dataLoader.getNotes();
        List<Route> routeList;
        routeList = new ArrayList<>();
        RVAdapter adapter = new RVAdapter(routeList);
        recyclerView.setAdapter(adapter);
        for(int i=0;i<tripDataBases.size();i++){
            int t=i;
            routeList.add(new Route("   "+tripDataBases.get(i).name, "                             "+tripDataBases.get(i).startDate+" - "+tripDataBases.get(i).endDate, new View.OnClickListener(){

                @Override
                public void onClick(View view) {
                    System.out.println("CLICKED");
                    FragmentManager fm = getActivity().getSupportFragmentManager();
                    FragmentAnyroute fragmentAnyroute=(FragmentAnyroute)fragments.get(5);
                    fragmentAnyroute.setDataBase(DataBase);
                    fragmentAnyroute.setIdTrip(tripDataBases.get(t).id);
                    fragmentAnyroute.setTripDataBase(tripDataBases.get(t));
                    fragmentAnyroute.setSights(sightsData);
                    fragmentAnyroute.setNotes(notes);
                    fragmentAnyroute.setTowns(townsData);
                    FragmentTransaction ft = fm.beginTransaction();
                    ft.replace(R.id.place_holder, fragments.get(5));
                    ft.commit();
                }
            }));
        }
        imageBack.setImageBitmap(croppedBitmap);



        return fragment;
    }

    public FragmentRoutes(int width, int height){
        this.height=height;
        this.width=width;
    }

    public void setFragments(ArrayList<Fragment> fragments){
        this.fragments=fragments;
    }

    public void setPastFragnent(int pastFragnent){
        this.pastFragnent=pastFragnent;
    }

    public void setDataBase(AppDatabase DataBase){
        this.DataBase=DataBase;
    }
}

class DataLoader extends Thread{
    AppDatabase DataBase;
    List<TripDataBase> data;
    List<Towns> TownsData;
    List<Sight> SightData;
    List<Notes> notes;
    public DataLoader(AppDatabase DataBase){
        this.DataBase=DataBase;
    }

    public void run(){
        TripDataBaseDao TripsData = DataBase.getTripDataBaseDao();
        TownsDao townsDao = DataBase.getTownsDao();
        data=TripsData.getAllTripDataBase();
        TownsData=townsDao.getAllTowns();
        SightDao sightDao = DataBase.getSightDao();
        SightData = sightDao.getAllSight();
        NotesDao notesDao = DataBase.getNotesDao();
        notes=notesDao.getAllNotes();
    }
    public List<TripDataBase> getTripDataBase(){
        return this.data;
    }
    public List<Towns> getTowns(){
        return this.TownsData;
    }
    public List<Sight> getSights(){
        return this.SightData;
    }
    public List<Notes> getNotes(){
        return this.notes;
    }
}
class Route {
    String name;
    String Dates;
    View.OnClickListener listener;

    Route(String name, String Dates, View.OnClickListener listener) {
        this.name = name;
        this.Dates = Dates;
        this.listener=listener;
    }

}



class RVAdapter extends RecyclerView.Adapter<RVAdapter.PersonViewHolder>{
    List<Route> routes;


    RVAdapter(List<Route> persons){
        this.routes = persons;

    }

    @Override
    public int getItemCount() {
        return routes.size();
    }

    @Override
    public PersonViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item, viewGroup, false);
        PersonViewHolder pvh = new PersonViewHolder(v);
        return pvh;
    }


    @Override
    public void onBindViewHolder(PersonViewHolder personViewHolder, int i) {
        personViewHolder.button.setOnClickListener(routes.get(i).listener);
        personViewHolder.button.setText(routes.get(i).name+"                                  \n");
        personViewHolder.button.setTextSize(20);
        personViewHolder.Dates.setText("\n\n\n                   "+routes.get(i).Dates);


    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public static class PersonViewHolder extends RecyclerView.ViewHolder {
        TextView Dates;
        Button button;
        FrameLayout frameLayout;

        View.OnClickListener listener;

        PersonViewHolder(View itemView) {

            super(itemView);
            frameLayout=(FrameLayout)itemView.findViewById(R.id.item_layout);
            Dates = (TextView)itemView.findViewById(R.id.item_dates);
            button = (Button) itemView.findViewById(R.id.item_button);
            frameLayout.bringChildToFront(Dates);
            button.setOnClickListener(listener);
        }
    }

}