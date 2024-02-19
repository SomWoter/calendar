package com.example.projectagendak.Fragments;

import static com.example.projectagendak.Calendar.CalendarUtils.selectedDate;

import android.os.Bundle;

import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.projectagendak.Calendar.CalendarUtils;
import com.example.projectagendak.Calendar.Event;
import com.example.projectagendak.Calendar.HourAdapter;
import com.example.projectagendak.Calendar.HourEvent;
import com.example.projectagendak.DataBase.EventDao;
import com.example.projectagendak.R;

import java.time.LocalTime;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class DailyCalendar_Fragment extends Fragment implements View.OnClickListener {

    private AppCompatButton previousDayBtn, nextDayBtn, newEventBtn;
    private TextView monthDayText, dayOfWeekTV;
    private ListView hourListView;
    private EventDao dao;

    public DailyCalendar_Fragment(EventDao dao){
        this.dao = dao;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.fragment_daily_calendar_, container, false);
        initWidgets(rootView);
        setOnClickListener(); // Move this line here
        return rootView;
    }

    @Override
    public void onResume()
    {
        super.onResume();
        setDayView();
    }

    private void setDayView()
    {
        monthDayText.setText(CalendarUtils.monthDayFromDate(selectedDate));
        String dayOfWeek = selectedDate.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.getDefault());
        dayOfWeekTV.setText(dayOfWeek);
        setHourAdapter();
    }

    private void setHourAdapter()
    {
        List<HourEvent> hourEvents = createHourEventsFromDatabase();
        HourAdapter hourAdapter = new HourAdapter(getActivity().getApplicationContext(), hourEventList());
        hourListView.setAdapter(hourAdapter);
    }

    private List<HourEvent> createHourEventsFromDatabase() {
        List<HourEvent> hourEvents = new ArrayList<>();
        Map<LocalTime, List<Event>> eventsByHour = groupEventsByHour(getEventsFromDatabase());

        for (Map.Entry<LocalTime, List<Event>> entry : eventsByHour.entrySet()){
            HourEvent hourEvent = new HourEvent(entry.getKey(), new ArrayList<>(entry.getValue()));
            hourEvents.add(hourEvent);
        }

        return hourEvents;
    }

    private Map<LocalTime, List<Event>> groupEventsByHour(List<Event> events){
        Map<LocalTime, List<Event>> eventsByHour = new HashMap<>();

        for (Event event : events){
            LocalTime eventTime = event.getTime();
            List<Event> eventsAtThisHour = eventsByHour.getOrDefault(eventTime, new ArrayList<>());
            eventsAtThisHour.add(event);
            eventsByHour.put(eventTime, eventsAtThisHour);
        }

        return eventsByHour;
    }

    private List<Event> getEventsFromDatabase(){
        return dao.obtenerTodosLosEventos();
    }


    private List<HourEvent> hourEventList()
    {
        ArrayList<HourEvent> list = new ArrayList<>();
        for (int hour = 0; hour < 24; hour++)
        {
            LocalTime time = LocalTime.of(hour, 0);
            //ArrayList<Event> events = Event.eventsForDateAndTime(selectedDate, time);
            ArrayList<Event> events = dao.getEventsForDateAndTime(selectedDate, time);

            HourEvent hourEvent = new HourEvent(time, events);
            list.add(hourEvent);
        }
        int a = 0;
        if (list.size() > 0){
            for (HourEvent e: list) {
                System.out.println(e.getEvents().get(a).getTime());
                a++;
            }
        }else{
            System.out.println("List vacía");
        }
        return list;
    }

    private void setOnClickListener()
    {
        previousDayBtn.setOnClickListener(this);
        nextDayBtn.setOnClickListener(this);
        newEventBtn.setOnClickListener(this);
    }

    private void initWidgets(View rootView)
    {
        monthDayText = rootView.findViewById(R.id.monthDayText);
        dayOfWeekTV = rootView.findViewById(R.id.dayOfWeekTV);
        hourListView = rootView.findViewById(R.id.hourListView);
        previousDayBtn = rootView.findViewById(R.id.previousDayBtn);
        nextDayBtn = rootView.findViewById(R.id.nextDayBtn);
        newEventBtn = rootView.findViewById(R.id.newEventBtn);
    }

    @Override
    public void onClick(View v)
    {
        if (v.getId() == R.id.previousDayBtn)
        {
            CalendarUtils.selectedDate = CalendarUtils.selectedDate.minusDays(1);
            setDayView();
        }
        else if (v.getId() == R.id.nextDayBtn)
        {
            CalendarUtils.selectedDate = CalendarUtils.selectedDate.plusDays(1);
            setDayView();
        }
        else if (v.getId() == R.id.newEventBtn)
        {
            replaceFragment(new EventEdit_Fragment());
        }
    }

    public void replaceFragment(Fragment fragment)
    {
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }
}