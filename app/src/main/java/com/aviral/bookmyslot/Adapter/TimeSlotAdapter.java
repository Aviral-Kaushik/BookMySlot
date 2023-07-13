package com.aviral.bookmyslot.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.aviral.bookmyslot.Models.TimeModel;
import com.aviral.bookmyslot.R;

import java.util.ArrayList;

public class TimeSlotAdapter extends RecyclerView.Adapter<TimeSlotAdapter.ViewHolder>{

    private final ArrayList<TimeModel> timeModels;

    public TimeSlotAdapter(ArrayList<TimeModel> timeModels) {
        this.timeModels = timeModels;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.layout_time_slot,
                parent, false
        );
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.time.setText(timeModels.get(position).getTime());

        if (timeModels.get(position).isBooked()) {
            holder.constraintLayout.setBackground(AppCompatResources.getDrawable(
                    holder.itemView.getContext(),
                    R.drawable.background_booked_slot
            ));
        }

    }

    @Override
    public int getItemCount() {
        return timeModels.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView time;
        private final ConstraintLayout constraintLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            time = itemView.findViewById(R.id.time);
            constraintLayout = itemView.findViewById(R.id.layout_time);
        }
    }

}
