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
import android.widget.Button;
import android.widget.TextView;

import com.example.projectagendak.Calendar.CalendarAdapter;
import com.example.projectagendak.Calendar.CalendarUtils;
import com.example.projectagendak.DataBase.EventDao;
import com.example.projectagendak.R;

import java.time.LocalDate;
import java.util.ArrayList;

public class MonthlyView_Fragment extends Fragment implements CalendarAdapter.OnItemListener, View.OnClickListener {


    private TextView monthYearText;
    private RecyclerView calendarRecyclerView;
    private AppCompatButton weeklyBtn, nextMonthBtn, previousMonthBtn;
    View rootView;
    EventDao dao;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_monthly_view_, container, false);
        initWidgets(rootView);
        setOnClickListener();
        dao = new EventDao(getContext());

        CalendarUtils.selectedDate = LocalDate.now();
        setMonthView();

        return rootView;
    }

    @Override
    public void onClick(View v)
    {
        if (v.getId() == R.id.weeklyBtn)
        {
            replaceFragment(new WeekView_Fragment());
        }
        else if(v.getId() == R.id.previousMonthBtn)
        {
            CalendarUtils.selectedDate = CalendarUtils.selectedDate.minusMonths(1);
            setMonthView();
        }
        else if(v.getId() == R.id.nextMonthBtn)
        {
            CalendarUtils.selectedDate = CalendarUtils.selectedDate.plusMonths(1);
            setMonthView();
        }
    }

    private void setOnClickListener()
    {
        weeklyBtn.setOnClickListener(this);
        nextMonthBtn.setOnClickListener(this);
        previousMonthBtn.setOnClickListener(this);
    }

    private void initWidgets(View rootView)
    {
        weeklyBtn = rootView.findViewById(R.id.weeklyBtn);
        nextMonthBtn = rootView.findViewById(R.id.nextMonthBtn);
        previousMonthBtn = rootView.findViewById(R.id.previousMonthBtn);
        calendarRecyclerView = rootView.findViewById(R.id.calendarRecyclerView);
        monthYearText = rootView.findViewById(R.id.monthYearTv);
    }

    @Override
    public void onCreateView(Bundle savedInstanceState) {

    }

    @Override
    public void onItemClick(int position, LocalDate date)
    {
        if (date != null)
        {
            CalendarUtils.selectedDate = date;
//            setMonthView();
            // Inicia el fragmento deseado pasando la fecha seleccionada como argumento
            WeekView_Fragment newFragment = new WeekView_Fragment();
            Bundle args = new Bundle();
            args.putSerializable("selectedDate", date);
            newFragment.setArguments(args);

            // Reemplaza el fragmento actual con el nuevo fragmento
            FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.frame_layout, newFragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        }
    }

    private void setMonthView()
    {
        monthYearText.setText(CalendarUtils.monthYearFromDate(CalendarUtils.selectedDate));
        ArrayList<LocalDate> daysInMoth = CalendarUtils.daysInMonthArray();

        CalendarAdapter calendarAdapter = new CalendarAdapter(daysInMoth, this, dao);

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getActivity().getApplicationContext(), 7);
        calendarRecyclerView.setLayoutManager(layoutManager);
        calendarRecyclerView.setAdapter(calendarAdapter);
    }

    public void replaceFragment(Fragment fragment)
    {
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }
}