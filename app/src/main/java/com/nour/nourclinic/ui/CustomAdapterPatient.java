package com.nour.nourclinic.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nour.nourclinic.R;
import com.nour.nourclinic.models.Patient;

import java.util.ArrayList;


public class CustomAdapterPatient extends ArrayAdapter {
    private Context context;
    private ArrayList<Patient> patients;

    public CustomAdapterPatient(Context context, int textViewResourceId, ArrayList objects) {
        super(context,textViewResourceId, objects);

        this.context= context;
        patients=objects;

    }

    private class ViewHolder
    {
        TextView t1;
        TextView t2;
        ImageView thumbnail;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        ViewHolder holder=null;
        if (convertView == null)
        {
            LayoutInflater vi = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = vi.inflate(R.layout.patient_row, null);

            holder = new ViewHolder();
            holder.t1 = (TextView) convertView.findViewById(R.id.txt1);
            holder.t2 = (TextView) convertView.findViewById(R.id.txt2);
            holder.thumbnail = (ImageView) convertView.findViewById(R.id.icon);
            convertView.setTag(holder);

        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }

        Patient patient = patients.get(position);
        holder.t1.setText(patient.getPatientName()+"");
        holder.t2.setText("Address: "+patient.getPatientAddress()+"");
        return convertView;


    }
}
