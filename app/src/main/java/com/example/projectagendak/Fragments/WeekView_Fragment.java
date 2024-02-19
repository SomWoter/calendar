package com.example.projectagendak.Fragments;

import android.os.Bundle;

import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;


import com.example.projectagendak.Calendar.CalendarAdapter;
import com.example.projectagendak.Calendar.CalendarUtils;
import com.example.projectagendak.Calendar.Event;
import com.example.projectagendak.Calendar.EventAdapter;
import com.example.projectagendak.DataBase.EventDao;
import com.example.projectagendak.R;

import java.time.LocalDate;
public class WeekView_Fragment extends Fragment implements CalendarAdapter.OnItemListener, View.OnClickListener {

    private TextView monthYearText;
    private RecyclerView calendarRecyclerView;
    private ListView eventListView;
    private AppCompatButton dailyBtn, previousWeekBtn, nextWeekBtn, newEventBtn;
    EventDao dao;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.fragment_week_view_, container, false);
        initWidgets(rootView);
        dao = new EventDao(getContext());
        setOnClickListener();
        setWeekView();

        // Inflate the layout for this fragment
        return rootView;
    }

    private void initWidgets(View rootView)
    {
        calendarRecyclerView = rootView.findViewById(R.id.calendarRecyclerView);
        monthYearText = rootView.findViewById(R.id.monthYearTv);
        eventListView = rootView.findViewById(R.id.eventListView);
        dailyBtn = rootView.findViewById(R.id.dailyBtn);
        previousWeekBtn = rootView.findViewById(R.id.previousWeekBtn);
        nextWeekBtn = rootView.findViewById(R.id.nextWeekBtn);
        newEventBtn = rootView.findViewById(R.id.newEventBtn);
    }

    private void setOnClickListener()
    {
        dailyBtn.setOnClickListener(this);
        previousWeekBtn.setOnClickListener(this);
        nextWeekBtn.setOnClickListener(this);
        newEventBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v)
    {
        if (v.getId() == R.id.dailyBtn)
            replaceFragment(new DailyCalendar_Fragment(dao));
        else if(v.getId() == R.id.previousWeekBtn)
        {
            CalendarUtils.selectedDate = CalendarUtils.selectedDate.minusWeeks(1);
            setWeekView();
        }
        else if(v.getId() == R.id.nextWeekBtn)
        {
            CalendarUtils.selectedDate = CalendarUtils.selectedDate.plusWeeks(1);
            setWeekView();
        }
        else if(v.getId() == R.id.newEventBtn)
            replaceFragment(new EventEdit_Fragment());
    }

    private void setWeekView()
    {
        monthYearText.setText(CalendarUtils.monthYearFromDate(CalendarUtils.selectedDate));
        ArrayList<LocalDate> days = CalendarUtils.daysInWeekArray(CalendarUtils.selectedDate);

        CalendarAdapter calendarAdapter = new CalendarAdapter(days, this, dao);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getActivity().getApplicationContext(), 7);
        calendarRecyclerView.setLayoutManager(layoutManager);
        calendarRecyclerView.setAdapter(calendarAdapter);
        setEventAdapter();
    }

    private void setEventAdapter() {
        EventDao eventDao = new EventDao(getContext());
        //ArrayList<Event> dailyEvents = Event.eventsForDate(CalendarUtils.selectedDate);
        ArrayList<Event> dailyEvents = eventDao.obtenerEventosPorFecha(CalendarUtils.selectedDate);
        EventAdapter eventAdapter = new EventAdapter(getActivity().getApplicationContext(), dailyEvents);
        eventListView.setAdapter(eventAdapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        setEventAdapter();
    }

    @Override
    public void onCreateView(Bundle savedInstanceState) {
        // NOTHING TO DO
    }

    @Override
    public void onItemClick(int position, LocalDate date)
    {
        CalendarUtils.selectedDate = date;
        setWeekView();
    }

    public void replaceFragment(Fragment fragment)
    {
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }
}