package com.nour.nourclinic.ui.schedule;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.levitnudi.legacytableview.LegacyTableView;
import com.nour.nourclinic.utils.Configuration;
import com.nour.nourclinic.R;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class ScheduleFragment extends Fragment {

    LegacyTableView legacyTableView;


    public ScheduleFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_schedule, container, false);
        //set table title labels
        LegacyTableView.insertLegacyTitle("Day", "From", "To");

        legacyTableView = (LegacyTableView)root.findViewById(R.id.legacy_table_view);

        getSchedule();

        return root;
    }

    public void getSchedule(){
        RequestQueue queue = Volley.newRequestQueue(getContext());
        final String url = Configuration.URL + "getDoctorSchedule.php";

        // prepare the Request
        JsonArrayRequest getRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject j = response.getJSONObject(i);
                                //set table contents as string arrays
                                legacyTableView.insertLegacyContent(
                                        j.getString("dayname")+"",
                                        j.getString("av_from")+"",
                                        j.getString("av_to")+""
                                );


                            }
                            legacyTableView.setContent(LegacyTableView.readLegacyContent());
                            legacyTableView.setTitle(LegacyTableView.readLegacyTitle());

                            legacyTableView.setInitialScale(100);//default initialScale is zero (0)

                            //if you want a smaller table, change the padding setting
                            legacyTableView.setTablePadding(20);

                            //to enable users to zoom in and out:
                            legacyTableView.setTitleTextSize(40);
                            legacyTableView.setContentTextSize(40);

                            //remember to build your table as the last step
                            legacyTableView.build();



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
