package com.example.projectagendak.Calendar;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.projectagendak.R;

import java.util.List;

public class EventAdapter extends ArrayAdapter<Event>
{
    public EventAdapter(@NonNull Context context, List<Event> events)
    {
        super(context, 0, events);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent)
    {
        Event event = getItem(position);
        if (convertView == null)
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.even_cell, parent, false);
        TextView eventCellTV = convertView.findViewById(R.id.eventCellTV);

        if (event != null)
        {
            String eventName = event.getName();

            if (eventName != null)
            {
                eventCellTV.setText(eventName  + " " + CalendarUtils.formattedTime(event.getTime()));
            }
            else
            {
                eventCellTV.setText("Name not available");
            }
        }
        else
        {
            eventCellTV.setText("Null event");
        }

        //String eventTitle = event.getName() + " " + CalendarUtils.formattedTime(event.getTime());
        //eventCellTV.setText(eventTitle);
        return convertView;
    }
}
