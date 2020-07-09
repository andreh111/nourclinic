package com.nour.nourclinic.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.nour.nourclinic.R;
import com.nour.nourclinic.utils.Configuration;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class AddTreatmentActivity extends AppCompatActivity {
    EditText trname,trprice,trdesc;
    Button addTr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_treatment);

        setTitle("Doctor - Add Treatment");

        trname = (EditText)findViewById(R.id.trname);
        trprice = (EditText)findViewById(R.id.trprice);
        trdesc = (EditText)findViewById(R.id.trdesc);

        addTr = (Button)findViewById(R.id.addtreatment);

        addTr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addTreatment(
                        trname.getText().toString(),
                        trprice.getText().toString(),
                        trdesc.getText().toString(),
                        getIntent().getExtras().getInt("app_id")
                );
            }
        });
    }

    private void addTreatment(final String tname, final String tprice, final String tdesc, final int appid){
        RequestQueue queue = Volley.newRequestQueue(this);

        String url = Configuration.URL + "addTreatment.php";
        StringRequest postRequest = new StringRequest(Request.Method.POST,url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        // response
                        try {
                            JSONObject j = new JSONObject(response);
                            String status = j.getString("status");

                            if (status.equals("SUCCESS")){
                                Toast.makeText(getBaseContext(), ""+j.getString("message"), Toast.LENGTH_LONG).show();
                                trname.setText("");
                                trprice.setText("");
                                trdesc.setText("");

                            }else{
                                Toast.makeText(getBaseContext(), ""+j.getString("message"), Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Log.d("Response", String.valueOf(response));
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        Log.d("Error.Response", error.toString());
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("trname", tname);
                params.put("trprice", tprice);
                params.put("trdesc", tdesc);
                params.put("appid", String.valueOf(appid));

                return params;
            }
        };
        queue.add(postRequest);
    }
}
