package com.example.planningtravels;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import java.util.ArrayList;
import java.util.List;

public class FragmentCalendar extends Fragment {
    private int width;
    private int height;
    private ArrayList<Fragment> fragments;
    private Integer pastFragnent=null;
    private int idTrip;
    private List<Notes> notes;
    private Button save;
    private ImageView imageBack;
    private AppDatabase DataBase;
    private Button back;
    private ArrayList<CalendarDay> days = new ArrayList<>();
    private ArrayList<CalendarDay> calendarDays = new ArrayList<>();



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View fragment = inflater.inflate(R.layout.calendar, container, false);
        days = new ArrayList<>();
        calendarDays = new ArrayList<>();
        if (container != null) {
            height = container.getHeight()-120;
            width = container.getWidth();
            imageBack= (ImageView) fragment.findViewById(R.id.calendar_background);
            Bitmap imageBackground = BitmapFactory.decodeResource(getResources(), R.drawable.mir);
            double k =Math.min(1.0*(imageBackground.getWidth()-850)/width, 1.0*(imageBackground.getHeight()-200)/height);
            if(k>1.5){
                k=1.5;
            }

            Bitmap croppedBitmap = Bitmap.createBitmap(imageBackground, 850, 200, (int)(width*k),(int)( height*k));
            imageBack.setImageBitmap(croppedBitmap);

        }

        imageBack= (ImageView) fragment.findViewById(R.id.calendar_background);
        Bitmap imageBackground = BitmapFactory.decodeResource(getResources(), R.drawable.mir);
        double k =Math.min(1.0*(imageBackground.getWidth()-850)/width, 1.0*(imageBackground.getHeight()-200)/height);
        if(k>1.5){
            k=1.5;
        }

        Bitmap croppedBitmap = Bitmap.createBitmap(imageBackground, 850, 200, (int)(width*k),(int)( height*k));
        imageBack.setImageBitmap(croppedBitmap);


        com.prolificinteractive.materialcalendarview.MaterialCalendarView calendarView=(com.prolificinteractive.materialcalendarview.MaterialCalendarView)fragment.findViewById(R.id.calendar_calendar);
        calendarView.setOnDateChangedListener(new OnDateSelectedListener() {
            EditText Note = fragment.findViewById(R.id.calendar_note);
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {

                int day=date.getDay();
                int month=date.getMonth()+1;
                int year=date.getYear();
                boolean flag=false;
                System.out.println("CHANGE DATE "+day+"."+month+"."+year);
                for(int i=0;i<notes.size();i++){
                    System.out.println(notes.get(i).day+"."+notes.get(i).month+"."+notes.get(i).year+"________________"+i);
                    if(notes.get(i).day==day && notes.get(i).month==month && notes.get(i).year==year){
                        Note.setText(notes.get(i).Text);
                        flag=true;
                        System.out.println("ADDED DATA");
                        break;

                    }
                }
                if(!flag){
                    Note.setText("");
                    System.out.println("NO DATA");
                }
            }
        });



        back=(Button)fragment.findViewById(R.id.calendar_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fm = getActivity().getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.replace(R.id.place_holder, fragments.get(5));
                ft.commit();
            }
        });
        save=(Button) fragment.findViewById(R.id.calendar_save);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                EditText Text =(EditText)fragment.findViewById(R.id.calendar_note);
                String data=Text.getText().toString();
                com.prolificinteractive.materialcalendarview.MaterialCalendarView calendarView= (com.prolificinteractive.materialcalendarview.MaterialCalendarView)fragment.findViewById(R.id.calendar_calendar);
                CalendarDay calendarDay =calendarView.getSelectedDate();
                int day=calendarDay.getDay();
                int month = calendarDay.getMonth()+1;
                int year = calendarDay.getYear();
                System.out.println("SAVING "+day+"."+month+"."+year);
                Notes note=null;
                for(int i=0;i<notes.size();i++){
                    if(notes.get(i).year==year && notes.get(i).month==month && notes.get(i).day==day){
                        note=notes.get(i);
                    }
                }
                if(note!=null){
                    DeleteNote deleteNote=new DeleteNote(DataBase, note);
                    deleteNote.start();
                    Notes note1 = new Notes();
                    note1.day=day;
                    note1.month=month;
                    note1.year=year;
                    note1.idTrip=idTrip;
                    note1.Text=data;
                    notes.set(notes.indexOf(note),note1);

                }else{
                    Notes note1 = new Notes();
                    note1.day=day;
                    note1.month=month;
                    note1.year=year;
                    note1.idTrip=idTrip;
                    note1.Text=data;
                    notes.add(note1);
                }
                note = new Notes();
                note.day=day;
                note.month=month;
                note.year=year;
                note.idTrip=idTrip;
                note.Text=data;
                SavingNote savingNote=new SavingNote(DataBase, note);
                savingNote.start();

                days.add(new CalendarDay(year, month-1, day));
                calendarView.addDecorator(new BookingDecorator(Color.WHITE, calendarDays));
                calendarView.addDecorator(new BookingDecorator(Color.RED, days));
            }
        });
        for(int i=CalendarDay.today().getYear()-1; i<CalendarDay.today().getYear()+1;i++){
            for(int j=0;j<12;j++){
                for(int f=0;f<32;f++){
                    try{
                        CalendarDay d = new CalendarDay(i, j, f);

                        calendarDays.add(d);

                    }catch (Exception e){
                        continue;
                    }
                }
            }
        }

        for(int i=0;i<notes.size();i++){
            days.add(new CalendarDay(notes.get(i).year, notes.get(i).month-1, notes.get(i).day));
        }

        calendarView.addDecorator(new BookingDecorator(Color.WHITE, calendarDays));
        calendarView.addDecorator(new BookingDecorator(Color.RED, days));

        return fragment;
    }

    public void setFragments(ArrayList<Fragment> fragments){
        this.fragments=fragments;
    }

    public FragmentCalendar(int width, int height){
        this.height=height;
        this.width=width;
    }

    public void setNotes(List<Notes> notes){
        this.notes=notes;
    }
    public void setIdTrip(int idTrip){
        this.idTrip=idTrip;
    }

    public void setPastFragnent(int pastFragnent){
        this.pastFragnent=pastFragnent;
    }

    public void setDataBase(AppDatabase DataBase){
        this.DataBase=DataBase;
    }


    class BookingDecorator implements DayViewDecorator {
        private int mColor;
        private ArrayList<CalendarDay> mCalendarDayCollection;

        public BookingDecorator(int color, ArrayList<CalendarDay> calendarDayCollection) {
            mColor = color;
            mCalendarDayCollection = calendarDayCollection;
        }

        @Override
        public boolean shouldDecorate(CalendarDay day) {
            return mCalendarDayCollection.contains(day);
        }

        @Override
        public void decorate(DayViewFacade view) {
            view.addSpan(new ForegroundColorSpan(mColor));
            //view.setBackgroundDrawable(ContextCompat.getDrawable(getContext(),R.drawable.greenbox));

        }
    }

    public void setHeight(int height){
        this.height=height;
    }

    public void setWidth(int width){
        this.width=width;
    }

}


class CalendarAdapter extends RecyclerView.Adapter<CalendarViewHolder>{
    List<Notes> notes;
    int idTrip;
    EditText Note;
    com.prolificinteractive.materialcalendarview.MaterialCalendarView calendarView;



    CalendarAdapter(int idTrip, List<Notes> notes){
        this.notes=notes;
        this.idTrip=idTrip;
    }

    @Override
    public int getItemCount() {
        return 1;
    }

    @Override
    public CalendarViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_calendar, viewGroup, false);
        CalendarViewHolder pvh = new CalendarViewHolder(v);
        Note= pvh.Note;
        calendarView= pvh.calendarView;
        pvh.calendarView.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                System.out.println("CHANGE DATE");
                int day=date.getDay();
                int month=date.getMonth()+1;
                int year=date.getYear();
                boolean flag=false;

                for(int i=0;i<notes.size();i++){
                    if(notes.get(i).day==day && notes.get(i).month==month && notes.get(i).year==year){
                        Note.setText(notes.get(i).Text);
                        flag=true;
                        System.out.println("ADDED DATA");
                        break;

                    }
                }
                if(!flag){
                    Note.setText("");
                    System.out.println("NO DATA");
                }
            }
        });
        return pvh;
    }


    @Override
    public void onBindViewHolder(CalendarViewHolder ViewHolder, int i) {
        Note = ViewHolder.Note;
        calendarView= ViewHolder.calendarView;
        ViewHolder.calendarView.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                System.out.println("CHANGE DATE");
                int day=date.getDay();
                int month=date.getMonth()+1;
                int year=date.getYear();
                boolean flag=false;
                System.out.println(day+"."+month+"."+year);
                System.out.println("SIZE: "+notes.size());
                for(int i=0;i<notes.size();i++){
                    System.out.println("NOTE "+notes.get(i).day+"."+notes.get(i).month+"."+notes.get(i).year );
                    if(notes.get(i).day==day && notes.get(i).month==month && notes.get(i).year==year){
                        Note.setText(notes.get(i).Text);
                        flag=true;
                        System.out.println("ADDED DATA");
                        break;

                    }
                }
                if(!flag){
                    Note.setHint("На выбранную дату заметок ещё нет");
                    Note.setText("");

                    System.out.println("NO DATA");
                }
            }
        });
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }



}
class CalendarViewHolder extends RecyclerView.ViewHolder {
    com.prolificinteractive.materialcalendarview.MaterialCalendarView calendarView;
    EditText Note;


    CalendarViewHolder(View itemView) {

        super(itemView);
        calendarView = (com.prolificinteractive.materialcalendarview.MaterialCalendarView) itemView.findViewById(R.id.item_calendar_calendar);
        Note=(EditText) itemView.findViewById(R.id.item_calendar_note);
    }

}

class SavingNote extends Thread{
    AppDatabase DataBase;
    Notes note;

    public SavingNote(AppDatabase DataBase, Notes note){
        this.DataBase=DataBase;
        this.note=note;
    }

    public void run(){
        NotesDao notesDao = DataBase.getNotesDao();
        notesDao.insertAll(note);
    }
}


class DeleteNote extends Thread{
    AppDatabase DataBase;
    Notes note;

    public DeleteNote(AppDatabase DataBase, Notes note){
        this.DataBase=DataBase;
        this.note=note;
    }

    public void run(){
        NotesDao notesDao = DataBase.getNotesDao();
        notesDao.delete(note.day, note.month, note.year, note.idTrip);
    }
}
