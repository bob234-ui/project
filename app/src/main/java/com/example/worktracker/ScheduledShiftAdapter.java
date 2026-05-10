package com.example.worktracker;

import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ScheduledShiftAdapter extends RecyclerView.Adapter<ScheduledShiftAdapter.ViewHolder> {

    public interface OnCancelClickListener {
        void onCancelClick(ScheduledShift scheduledShift);
    }

    private List<ScheduledShift> shifts;
    private final OnCancelClickListener listener;

    public ScheduledShiftAdapter(List<ScheduledShift> shifts, OnCancelClickListener listener) {
        this.shifts = shifts;
        this.listener = listener;
    }

    public void updateShifts(List<ScheduledShift> newShifts) {
        this.shifts = newShifts;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ScheduledShiftAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LinearLayout layout = new LinearLayout(parent.getContext());
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(24, 24, 24, 24);

        TextView textViewInfo = new TextView(parent.getContext());
        Button buttonCancel = new Button(parent.getContext());
        buttonCancel.setText("Cancel Shift");

        layout.addView(textViewInfo);
        layout.addView(buttonCancel);

        return new ViewHolder(layout, textViewInfo, buttonCancel);
    }

    @Override
    public void onBindViewHolder(@NonNull ScheduledShiftAdapter.ViewHolder holder, int position) {
        ScheduledShift shift = shifts.get(position);

        holder.textViewInfo.setText(
                "User: " + shift.getUsername() + "\n" +
                        "Date: " + shift.getShift_date() + "\n" +
                        "Time: " + shift.getShift_start() + " - " + shift.getShift_end()
        );

        holder.buttonCancel.setOnClickListener(v -> {
            listener.onCancelClick(shift);
        });
    }

    @Override
    public int getItemCount() {
        return shifts.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        TextView textViewInfo;
        Button buttonCancel;

        public ViewHolder(@NonNull LinearLayout itemView, TextView textViewInfo, Button buttonCancel) {
            super(itemView);
            this.textViewInfo = textViewInfo;
            this.buttonCancel = buttonCancel;
        }
    }
}