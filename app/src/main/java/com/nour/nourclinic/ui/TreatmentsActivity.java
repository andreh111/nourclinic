package com.nour.nourclinic.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.nour.nourclinic.R;
import com.nour.nourclinic.models.Treatment;
import com.nour.nourclinic.utils.Configuration;
import com.nour.nourclinic.utils.DBHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class TreatmentsActivity extends AppCompatActivity {

    ListView lstTr;
    ArrayList<Treatment> treatmentArrayList;
    CustomAdapterTreatment customAdapter;
    DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_treatments);

        dbHelper = new DBHelper(getApplicationContext());


        lstTr = (ListView)findViewById(R.id.lstTreatments);

        getTreatments(getIntent().getExtras().getInt("appid"));


        lstTr.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Treatment t = treatmentArrayList.get(i);

                dbHelper.addTreatment(t);

            }
        });
        ArrayList<Treatment> arr = dbHelper.getTreatments();
        Toast.makeText(this, ""+arr.toString(), Toast.LENGTH_SHORT).show();

    }
    private void getTreatments(int appid) {
        treatmentArrayList = new ArrayList<>();

        RequestQueue queue = Volley.newRequestQueue(this);
        final String url = Configuration.URL + "getTreatments.php?appid=" + appid;

        // prepare the Request
        JsonArrayRequest getRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject j = response.getJSONObject(i);
                                treatmentArrayList.add(new Treatment(
                                        j.getInt("trid") ,
                                        j.getString("trname") + "",
                                        j.getInt("trprice"),
                                        j.getString("trdesc"),
                                        j.getInt("appid")
                                ));
                            }
                            customAdapter = new CustomAdapterTreatment(getApplicationContext(), R.layout.treatment_row, treatmentArrayList);
                            lstTr.setAdapter(customAdapter);

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
