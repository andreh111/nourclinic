package com.nour.nourclinic.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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
import com.nour.nourclinic.models.Patient;
import com.nour.nourclinic.utils.Configuration;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class DoctorActivity extends AppCompatActivity {
    ArrayList<Patient> patientArrayList;
    CustomAdapterPatient patientAdapter;
    ListView listPats;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor);

        setTitle("Doctor - Patients List");

        listPats = (ListView)findViewById(R.id.listpat);

        getPatients();


        listPats.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Patient p = patientArrayList.get(i);

                Intent intent = new Intent(DoctorActivity.this,PatientAppointmentsActivity.class);
                intent.putExtra("userid",p.getId());
                startActivity(intent);
            }
        });

    }

    private void getPatients() {
        patientArrayList = new ArrayList<Patient>();

        RequestQueue queue = Volley.newRequestQueue(this);
        final String url = Configuration.URL + "getPatients.php";

        // prepare the Request
        JsonArrayRequest getRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d("pa",response.toString());
                        try {
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject j = response.getJSONObject(i);
                                patientArrayList.add(new Patient(
                                        j.getInt("userid") ,
                                        j.getString("firstname")+" "+j.getString("lastname"),
                                        j.getString("address")
                                ));
                            }
                            Log.d("pa",patientArrayList.toString());
                            patientAdapter = new CustomAdapterPatient(getBaseContext(),R.layout.patient_row,patientArrayList);
                            listPats.setAdapter(patientAdapter);

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.doctor_menu, menu);
        return true;
    }

    public void logout(){
        SharedPreferences prefs = getSharedPreferences(Configuration.SHRD_PREF, MODE_PRIVATE);
        prefs.edit().putBoolean("loggedin", false).apply();
        Intent i = new Intent(this,LoginActivity.class);
        startActivity(i);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_logout:
                logout();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        Intent a = new Intent(Intent.ACTION_MAIN);
        a.addCategory(Intent.CATEGORY_HOME);
        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(a);
    }
}
