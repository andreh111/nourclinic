package com.nour.nourclinic.ui.appointments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.nour.nourclinic.models.Appointment;
import com.nour.nourclinic.utils.Configuration;
import com.nour.nourclinic.ui.CustomAdapterAppointment;
import com.nour.nourclinic.R;
import com.nour.nourclinic.ui.TreatmentsActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

public class AppointmentsFragment extends Fragment {

    ArrayList<Appointment> appointmentsList;
    CustomAdapterAppointment customAdapterAppointment;
    ListView listApps;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_appointments, container, false);

        SharedPreferences prefs = getContext().getSharedPreferences(Configuration.SHRD_PREF, MODE_PRIVATE);
        String user = prefs.getString("userid", "");
        int userid = Integer.parseInt(user);

        listApps = (ListView) root.findViewById(R.id.listapp);

        getAppointments(userid);

        listApps.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Appointment p = appointmentsList.get(i);
                Intent intent = new Intent(getContext(), TreatmentsActivity.class);
                intent.putExtra("appid",p.getId());
                startActivity(intent);
            }
        });


        return root;
    }

    private void getAppointments(int user) {
        appointmentsList = new ArrayList<>();

        RequestQueue queue = Volley.newRequestQueue(getContext());
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
                            customAdapterAppointment = new CustomAdapterAppointment(getContext(), R.layout.appointment_row, appointmentsList);
                            listApps.setAdapter(customAdapterAppointment);

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