package com.example.planningtravels;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
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
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class FragmentAnyroute extends Fragment {
    private int width;
    private int height;
    private ArrayList<Fragment> fragments;
    private Integer pastFragnent=null;
    private TripDataBase tripDataBase;
    private List<Towns> towns;
    private List<Sight> sights;
    private ImageView imageBack;
    private Button back;
    private List<Notes> notes;
    private Button calendar;
    private AppDatabase DataBase;
    private Button delete;
    int idTrip;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View fragment = inflater.inflate(R.layout.anyroute, container, false);
        imageBack= (ImageView) fragment.findViewById(R.id.anyroutes_background);
        Bitmap imageBackground = BitmapFactory.decodeResource(getResources(), R.drawable.mir);
        double k =Math.min(1.0*(imageBackground.getWidth()-850)/width, 1.0*(imageBackground.getHeight()-200)/height);
        if(k>1.5){
            k=1.5;
        }
        RecyclerView recyclerView =(RecyclerView) fragment.findViewById(R.id.anyroute_data);
        List<TownView> data = new ArrayList<>();

        Bitmap croppedBitmap = Bitmap.createBitmap(imageBackground, 850, 200, (int)(width*k),(int)( height*k));
        System.out.println("INFO "+towns.size()+" "+sights.size());
        for (int i=0;i<towns.size();i++){
            if(towns.get(i).idTrip==tripDataBase.id){
                String Hotel=towns.get(i).nameHotel;
                if(Hotel==null){
                    Hotel = "Отель ещё не выбран";
                }else{
                    Hotel="Выбранный отель: "+Hotel;
                }
                String Sights="";

                System.out.println("SIZE: "+sights.size());
                for (int j=0;j<sights.size();j++){
                    System.out.println(sights.get(j).idTown+ "  "+towns.get(i).idTown);
                    if(sights.get(j).idTown==towns.get(i).idTown){
                        if(Sights.length()==0){
                            Sights+=sights.get(j).nameSight;
                        }else{
                            Sights=Sights+", "+sights.get(j).nameSight;
                        }
                    }
                }
                if(Sights.length()==0){
                    Sights="Вы ещё ничего не запланировали для посещения";
                }else {
                    Sights="Вы планируете посетить "+Sights;
                }
                System.out.println("TOWN "+towns.get(i).nameTown);
                TownView townView  =new TownView(towns.get(i).nameTown,Hotel, Sights);
                data.add(townView);

            }
        }
        AnyrouteAdapter adapter = new AnyrouteAdapter(data);
        recyclerView.setAdapter(adapter);
        System.out.println("INFO "+data.size());
        back = (Button) fragment.findViewById(R.id.anyroute_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fm = getActivity().getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.replace(R.id.place_holder, fragments.get(0));
                ft.commit();
            }
        });

        calendar =(Button) fragment.findViewById(R.id.anyroutes_calendar);
        calendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentCalendar fragmentCalendar = (FragmentCalendar)fragments.get(6);
                fragmentCalendar.setIdTrip(tripDataBase.id);
                fragmentCalendar.setDataBase(DataBase);
                fragmentCalendar.setNotes(notes);
                FragmentManager fm = getActivity().getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.replace(R.id.place_holder, fragmentCalendar);
                ft.commit();
            }
        });
        delete=(Button)fragment.findViewById(R.id.anyroutes_delete);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                сreateDialog(getActivity());
            }
        });

        imageBack.setImageBitmap(croppedBitmap);
        return fragment;
    }
    public void сreateDialog(Activity activity) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle("Удаление")
                .setMessage("Вы уверены, что хотите удалить маршрут?")
                .setPositiveButton("Удалить", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        DeleteTrip deleteTrip = new DeleteTrip(DataBase,idTrip, sights );
                        deleteTrip.start();
                        FragmentManager fm = getActivity().getSupportFragmentManager();
                        FragmentTransaction ft = fm.beginTransaction();
                        ft.replace(R.id.place_holder, fragments.get(0));
                        ft.commit();

                    }
                })
                .setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                });
        builder.create().show();
    }
    public void setFragments(ArrayList<Fragment> fragments){
        this.fragments=fragments;
    }

    public FragmentAnyroute(int width, int height){
        this.height=height;
        this.width=width;
    }

    public void setPastFragnent(int pastFragnent){
        this.pastFragnent=pastFragnent;
    }
    public void setTripDataBase(TripDataBase tripDataBase){
        this.tripDataBase=tripDataBase;
    }
    public void setSights(List<Sight> sights){
        this.sights=sights;
    }
    public void setTowns(List<Towns> towns){
        this.towns=towns;
    }

    public void setNotes(List<Notes> notes){
        this.notes=notes;
    }
    public void setIdTrip(int idTrip){
        this.idTrip = idTrip;
    }
    public void setDataBase(AppDatabase DataBase){
        this.DataBase=DataBase;
    }

    public void setHeight(int height){
        this.height=height;
    }

    public void setWidth(int width){
        this.width=width;
    }

}


class TownView{
    private String Name;
    private String Hotel;
    private String Sights;

    public TownView(String Name, String Hotel, String Sights){
        this.Hotel=Hotel;
        this.Name=Name;
        this.Sights=Sights;
    }
    public String getName(){
        return this.Name;
    }

    public String getHotel(){
        return this.Hotel;
    }


    public String getSights(){
        return this.Sights;
    }
}

class AnyrouteAdapter extends RecyclerView.Adapter<TViewHolder>{
    List<TownView> towns;


    AnyrouteAdapter(List<TownView> towns){
        this.towns = towns;

    }

    @Override
    public int getItemCount() {
        return towns.size();
    }

    @Override
    public TViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_town, viewGroup, false);
        TViewHolder pvh = new TViewHolder(v);
        return pvh;
    }


    @Override
    public void onBindViewHolder(TViewHolder ViewHolder, int i) {
        ViewHolder.Sights.setText(towns.get(i).getSights());
        ViewHolder.Hotel.setText(towns.get(i).getHotel());
        ViewHolder.Town.setText(towns.get(i).getName());
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }



}
class TViewHolder extends RecyclerView.ViewHolder {
    TextView Town;
    TextView Hotel;
    TextView Sights;


    TViewHolder(View itemView) {

        super(itemView);
        Town = (TextView) itemView.findViewById(R.id.item_town_name);
        Hotel=(TextView) itemView.findViewById(R.id.item_town_hotel);
        Sights=(TextView) itemView.findViewById(R.id.item_town_sights);
    }
}


class DeleteTrip extends Thread{
    private AppDatabase DataBase;
    private int idTrip;
    private List<Sight> sights;

    public DeleteTrip(AppDatabase DataBase, int idTrip, List<Sight> sights){
        this.DataBase=DataBase;
        this.idTrip=idTrip;
        this.sights=sights;
    }

    public void run(){
        TripDataBaseDao tripDataBaseDao = DataBase.getTripDataBaseDao();
        SightDao sightDao = DataBase.getSightDao();
        NotesDao notesDao = DataBase.getNotesDao();
        notesDao.deleteTrip(idTrip);
        for(int i=0;i<sights.size();i++) {
            sightDao.delete(sights.get(i).idTown);
        }
        tripDataBaseDao.delete(idTrip);

    }
}