package com.example.appointmentmanager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder>{
    private final List<Appointment> appointments;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView timeTextView,titleTextView;
        ImageView beltColorImageView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(v -> Log.d("TAG", "Element " + getAdapterPosition() + " clicked."));
            timeTextView = itemView.findViewById(R.id.timeTextView);
            titleTextView = itemView.findViewById(R.id.titleTextView);
            beltColorImageView = itemView.findViewById(R.id.beltColorImageView);
        }
    }

    public CustomAdapter(List<Appointment> ap) {
        appointments = ap;
    }

    @NonNull
    @Override
    public CustomAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.appointment_layout,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomAdapter.ViewHolder holder, int position) {

        holder.titleTextView.setText(appointments.get(position).title);
        holder.timeTextView.setText(appointments.get(position).date_time);

    }

    @Override
    public int getItemCount() {
        return appointments.size();
    }
}
