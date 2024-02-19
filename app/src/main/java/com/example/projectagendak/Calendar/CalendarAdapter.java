package com.example.projectagendak.Calendar;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projectagendak.DataBase.EventDao;
import com.example.projectagendak.R;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class CalendarAdapter extends RecyclerView.Adapter<CalendarViewHolder> {
    private final ArrayList<LocalDate> days;
    private final OnItemListener onItemListener;
    private final EventDao eventDao;


    public CalendarAdapter(ArrayList<LocalDate> days, OnItemListener onItemListener, EventDao eventDao) {
        this.days = days;
        this.onItemListener = onItemListener;
        this.eventDao = eventDao;

    }



    @NonNull
    @Override
    public CalendarViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.calendar_cell, parent, false);
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        if (days.size() > 15) //month view
            layoutParams.height = (int) (parent.getHeight() * 0.1666666666);
        else
            layoutParams.height = (int) (parent.getHeight());

        return new CalendarViewHolder(view, onItemListener, days);
    }

    @Override
    public void onBindViewHolder(@NonNull CalendarViewHolder holder, int position) {
        final LocalDate date = days.get(position);

        holder.dayOfMonth.setText(String.valueOf(date.getDayOfMonth()));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int adapterPosition = holder.getAdapterPosition();
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    LocalDate clickedDate = days.get(adapterPosition);
                    onItemListener.onItemClick(adapterPosition, clickedDate);
                }
            }
        });

        if (hasEvent(date))
        {
            int color = ContextCompat.getColor(holder.itemView.getContext(), R.color.green);
            holder.parentView.setBackgroundColor(color);
        }

        if (date.equals(CalendarUtils.selectedDate))
            holder.parentView.setBackgroundColor(Color.DKGRAY);

        if (date.getMonth().equals(CalendarUtils.selectedDate.getMonth())) {
            holder.dayOfMonth.setTextColor(Color.WHITE);
        } else
            holder.dayOfMonth.setTextColor(Color.DKGRAY);

        // Set margins for the item view
        ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) holder.itemView.getLayoutParams();
        int margin = holder.itemView.getContext().getResources().getDimensionPixelSize(R.dimen.cell_margin); // Define the margin dimension in resources
        layoutParams.setMargins(margin, margin, margin, margin); // Set margins (left, top, right, bottom)
        holder.itemView.setLayoutParams(layoutParams);
    }


    private boolean hasEvent(LocalDate date){
         //  eventDao.eliminarTodosLosEventos(); // to remove

        // Obtener la lista de eventos para la fecha seleccionada
        List<Event> eventsForDate = eventDao.obtenerEventosPorFecha(date);

        // Verificar si la lista de eventos no está vacía
        return !eventsForDate.isEmpty();
    }


    @Override
    public int getItemCount() {
        return days.size();
    }

    public interface OnItemListener {
        void onCreateView(Bundle savedInstanceState);

        void onItemClick(int position, LocalDate date);
    }


}
