package com.example.worktracker;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Locale;

public class ShiftAdapter extends RecyclerView.Adapter<ShiftAdapter.ShiftViewHolder> {

    private List<Shift> shifts;

    public ShiftAdapter(List<Shift> shifts) {
        this.shifts = shifts;
    }

    public void updateShifts(List<Shift> newShifts) {
        this.shifts = newShifts;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ShiftViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_shift, parent, false);
        return new ShiftViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ShiftViewHolder holder, int position) {
        Shift shift = shifts.get(position);

        holder.textViewDate.setText("Date: " + shift.getShift_date());
        holder.textViewStart.setText("Start: " + shift.getShift_start());
        holder.textViewEnd.setText("End: " + shift.getShift_end());
        holder.textViewBreakCount.setText("Breaks taken: " + shift.getBreak_count());
        holder.textViewBreakTotal.setText("Total break time: " + formatTime(shift.getTotal_break_millis()));
    }

    @Override
    public int getItemCount() {
        return shifts.size();
    }

    private String formatTime(long millis) {
        int seconds = (int) (millis / 1000);
        int hours = seconds / 3600;
        int minutes = (seconds % 3600) / 60;
        int secs = seconds % 60;

        return String.format(Locale.US, "%02d:%02d:%02d", hours, minutes, secs);
    }

    static class ShiftViewHolder extends RecyclerView.ViewHolder {

        TextView textViewDate;
        TextView textViewStart;
        TextView textViewEnd;
        TextView textViewBreakCount;
        TextView textViewBreakTotal;

        public ShiftViewHolder(@NonNull View itemView) {
            super(itemView);

            textViewDate = itemView.findViewById(R.id.textViewShiftDate);
            textViewStart = itemView.findViewById(R.id.textViewShiftStart);
            textViewEnd = itemView.findViewById(R.id.textViewShiftEnd);
            textViewBreakCount = itemView.findViewById(R.id.textViewBreakCount);
            textViewBreakTotal = itemView.findViewById(R.id.textViewBreakTotal);
        }
    }
}