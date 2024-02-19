package com.example.projectagendak.Fragments;

import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;

import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.projectagendak.Calendar.CalendarUtils;
import com.example.projectagendak.Calendar.Event;
import com.example.projectagendak.DataBase.EventDao;
import com.example.projectagendak.R;

import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.Locale;


public class EventEdit_Fragment extends Fragment implements View.OnClickListener {

    private AppCompatButton timeButton, saveEventBtn;
    private EditText eventNameET;
    private TextView eventDateTV, eventTimeTV;
    private LocalTime time;
    private int hour, minute;
    private EventDao dao;


    public EventEdit_Fragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.fragment_event_edit_, container, false);
        initWidgets(rootView);
        setOnClickListener();

        // Inicializar hora y minuto con la hora actual
        LocalTime currentTime = LocalTime.now();
        hour = currentTime.getHour();
        minute = currentTime.getMinute();

        time = LocalTime.of(hour, minute);
        eventDateTV.setText("Date: " + CalendarUtils.formattedDate(CalendarUtils.selectedDate));
        eventTimeTV.setText("Time: " + CalendarUtils.formattedTime(time));

        dao = new EventDao(getContext());

        return rootView;
    }

    private void setOnClickListener()
    {
        timeButton.setOnClickListener(this);
        saveEventBtn.setOnClickListener(this);
    }

    private void initWidgets(View rootView)
    {
        eventNameET = rootView.findViewById(R.id.eventNameTV);
        eventDateTV = rootView.findViewById(R.id.eventDateTV);
        eventTimeTV = rootView.findViewById(R.id.eventTimeTV);
        timeButton = rootView.findViewById(R.id.timeButton);
        saveEventBtn = rootView.findViewById(R.id.saveEventBtn);
    }

    @Override
    public void onClick(View v)
    {
        if (v.getId() == R.id.timeButton)
        {
            pickTimeAction(v);
        }
        else if (v.getId() == R.id.saveEventBtn)
        {
            String eventName = eventNameET.getText().toString();
            Event newEvent = new Event(eventName, CalendarUtils.selectedDate, time);

            Event.eventsList.add(newEvent);
            dao.insertarEvento(newEvent);
            //finish();
            replaceFragment(new DailyCalendar_Fragment(dao));
        }
    }

    public void pickTimeAction(View v)
    {
        TimePickerDialog.OnTimeSetListener onTimeSetListener = (timePicker, selectedHour, selectedMinute) ->
        {
            hour = selectedHour;
            minute = selectedMinute;
            timeButton.setText(String.format(Locale.getDefault(), "%02d:%02d", hour, minute));
            time = LocalTime.of(hour, minute);
        };

        int style = AlertDialog.THEME_HOLO_DARK;
        TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(), style, onTimeSetListener, hour, minute, true);
        timePickerDialog.setTitle("Select time");
        timePickerDialog.show();
    }

    private LocalTime setTimeNow(){
        LocalTime currentTime = LocalTime.now();
        // Redondeamos la hora actual a minutos
        return currentTime.truncatedTo(ChronoUnit.MINUTES);
    }


    public void replaceFragment(Fragment fragment)
    {
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }
}