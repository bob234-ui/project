package com.example.worktracker;

import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Locale;

public class OngoingShiftAdapter extends RecyclerView.Adapter<OngoingShiftAdapter.OngoingShiftViewHolder> {

    private List<OngoingShift> ongoingShifts;

    public OngoingShiftAdapter(List<OngoingShift> ongoingShifts) {
        this.ongoingShifts = ongoingShifts;
    }

    public void updateOngoingShifts(List<OngoingShift> newOngoingShifts) {
        this.ongoingShifts = newOngoingShifts;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public OngoingShiftViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LinearLayout layout = new LinearLayout(parent.getContext());
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(32, 24, 32, 24);

        TextView username = new TextView(parent.getContext());
        TextView start = new TextView(parent.getContext());
        TextView time = new TextView(parent.getContext());
        TextView breaks = new TextView(parent.getContext());
        TextView breakStatus = new TextView(parent.getContext());

        layout.addView(username);
        layout.addView(start);
        layout.addView(time);
        layout.addView(breaks);
        layout.addView(breakStatus);

        return new OngoingShiftViewHolder(layout, username, start, time, breaks, breakStatus);
    }

    @Override
    public void onBindViewHolder(@NonNull OngoingShiftViewHolder holder, int position) {
        OngoingShift shift = ongoingShifts.get(position);

        long elapsed = System.currentTimeMillis() - shift.getShift_start_millis();

        holder.textViewUsername.setText("User: " + shift.getUsername());
        holder.textViewStart.setText("Started: " + shift.getShift_start());
        holder.textViewTime.setText("Time on shift: " + formatTime(elapsed));
        holder.textViewBreaks.setText("Breaks taken: " + shift.getBreak_count());
        holder.textViewBreakStatus.setText("Currently on break: " + (shift.isBreak_active() ? "Yes" : "No"));
    }

    @Override
    public int getItemCount() {
        return ongoingShifts.size();
    }

    private String formatTime(long millis) {
        int seconds = (int) (millis / 1000);
        int hours = seconds / 3600;
        int minutes = (seconds % 3600) / 60;
        int secs = seconds % 60;

        return String.format(Locale.US, "%02d:%02d:%02d", hours, minutes, secs);
    }

    static class OngoingShiftViewHolder extends RecyclerView.ViewHolder {

        TextView textViewUsername;
        TextView textViewStart;
        TextView textViewTime;
        TextView textViewBreaks;
        TextView textViewBreakStatus;

        public OngoingShiftViewHolder(
                @NonNull LinearLayout itemView,
                TextView username,
                TextView start,
                TextView time,
                TextView breaks,
                TextView breakStatus
        ) {
            super(itemView);

            textViewUsername = username;
            textViewStart = start;
            textViewTime = time;
            textViewBreaks = breaks;
            textViewBreakStatus = breakStatus;
        }
    }
}