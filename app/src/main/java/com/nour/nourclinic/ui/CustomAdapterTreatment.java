package com.nour.nourclinic.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.nour.nourclinic.R;
import com.nour.nourclinic.models.Treatment;
import com.nour.nourclinic.utils.DBHelper;

import java.util.ArrayList;

public class CustomAdapterTreatment extends ArrayAdapter {
    private Context context;
    private ArrayList<Treatment> treatments;

    public CustomAdapterTreatment(Context context, int textViewResourceId, ArrayList objects) {
        super(context,textViewResourceId, objects);

        this.context= context;
        treatments=objects;

    }

    private class ViewHolder
    {
        TextView trname;
        TextView trdetails;
        Button btnSave;
        ImageView thumbnail;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        ViewHolder holder=null;
        if (convertView == null)
        {
            LayoutInflater vi = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = vi.inflate(R.layout.treatment_row, null);

            holder = new ViewHolder();
            holder.trname = (TextView) convertView.findViewById(R.id.trnam);
            holder.trdetails = (TextView) convertView.findViewById(R.id.trdetails);
            holder.thumbnail = (ImageView) convertView.findViewById(R.id.icon);
            holder.btnSave = (Button) convertView.findViewById(R.id.btnSave);
            convertView.setTag(holder);

        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }
        final DBHelper dbHelper = new DBHelper(getContext());
        final Treatment t = treatments.get(position);
        holder.trname.setText(t.getName());
        holder.trdetails.setText(t.getDesc());
        if (dbHelper.treatmentExists()){
            holder.btnSave.setText("-");
        }else{
            holder.btnSave.setText("+");
        }

        final ViewHolder finalHolder = holder;
        holder.btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (dbHelper.treatmentExists()){
                    try {
                        dbHelper.deleteTreatment(t.getId());
                        Toast.makeText(context, "Treatment removed from savedlist", Toast.LENGTH_SHORT).show();
                        finalHolder.btnSave.setText("+");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }else{
                    dbHelper.addTreatment(t);
                    Toast.makeText(context, "Treatment added from savedlist", Toast.LENGTH_SHORT).show();
                    finalHolder.btnSave.setText("-");
                }
            }
        });
        return convertView;


    }
}
