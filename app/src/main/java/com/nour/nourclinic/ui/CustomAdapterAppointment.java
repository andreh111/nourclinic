package com.nour.nourclinic.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nour.nourclinic.R;
import com.nour.nourclinic.models.Appointment;

import java.util.ArrayList;


public class CustomAdapterAppointment extends ArrayAdapter {
    private Context context;
    private ArrayList<Appointment> appointments;

    public CustomAdapterAppointment(Context context, int textViewResourceId, ArrayList objects) {
        super(context,textViewResourceId, objects);

        this.context= context;
        appointments=objects;

    }

    private class ViewHolder
    {
        TextView appointment;
        TextView appointmentdate;
        ImageView thumbnail;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        ViewHolder holder=null;
        if (convertView == null)
        {
            LayoutInflater vi = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = vi.inflate(R.layout.appointment_row, null);

            holder = new ViewHolder();
            holder.appointment = (TextView) convertView.findViewById(R.id.appointment);
            holder.appointmentdate = (TextView) convertView.findViewById(R.id.address);
            holder.thumbnail = (ImageView) convertView.findViewById(R.id.icon);
            convertView.setTag(holder);

        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }

        Appointment appointment = appointments.get(position);
        holder.appointment.setText("Problem: "+appointment.getProblem());
        holder.appointmentdate.setText("Date: "+appointment.getDate() + " " + appointment.getTime());
        return convertView;


    }
}
