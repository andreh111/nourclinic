package com.nour.nourclinic.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.nour.nourclinic.R;
import com.nour.nourclinic.models.Appointment;
import com.nour.nourclinic.utils.Configuration;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class PatientAppointmentsActivity extends AppCompatActivity {
    ArrayList<Appointment> appointmentsList;
    CustomAdapterAppointment customAdapterAppointment;
    ListView listApps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_appointments);

        setTitle("Doctor - Patient Appointments");


        listApps = (ListView) findViewById(R.id.listpatapp);



        int userid= getIntent().getExtras().getInt("userid");

        getAppointments(userid);

        listApps.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Appointment app = appointmentsList.get(i);
                Intent intent = new Intent(getApplicationContext(),AddTreatmentActivity.class);
                intent.putExtra("app_id",app.getId());
                startActivity(intent);
            }
        });



    }

    private void getAppointments(int user) {
        appointmentsList = new ArrayList<>();

        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        final String url = Configuration.URL + "getAppointments.php?user=" + user;

        // prepare the Request
        JsonArrayRequest getRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject j = response.getJSONObject(i);
                                appointmentsList.add(new Appointment(
                                        j.getInt("app_id") ,
                                        j.getString("app_date") + "",
                                        j.getString("app_time") + "",
                                        j.getString("firstname") + " " + j.getString("lastname"),
                                        j.getString("problem") + ""
                                ));
                            }
                            customAdapterAppointment = new CustomAdapterAppointment(getApplicationContext(), R.layout.appointment_row, appointmentsList);
                            listApps.setAdapter(customAdapterAppointment);

                            customAdapterAppointment.notifyDataSetChanged();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Log.d("Response", response.toString());
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Error.Response", error.toString());
                    }
                }
        );

        // add it to the RequestQueue
        queue.add(getRequest);
    }
}
