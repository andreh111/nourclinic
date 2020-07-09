package com.nour.nourclinic.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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

public class RegisterActivity extends AppCompatActivity {

    Button register;
    EditText fullname,address,email,pass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        setTitle("Register");

        fullname = (EditText)findViewById(R.id.fullname);
        address = (EditText)findViewById(R.id.address);
        email = (EditText)findViewById(R.id.email);
        pass = (EditText)findViewById(R.id.pass);

        register = (Button)findViewById(R.id.registerbtn);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                register(
                        fullname.getText().toString(),
                        address.getText().toString(),
                        email.getText().toString(),
                        pass.getText().toString()
                );
            }
        });
    }

    private void register(final String fullname,final String address,final String email, final String password){
        RequestQueue queue = Volley.newRequestQueue(this);

        String url = Configuration.URL + "register.php";
        StringRequest postRequest = new StringRequest(Request.Method.POST,url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        // response
                        try {
                            JSONObject user = new JSONObject(response);
                            String status = user.getString("status");

                            if (status.equals("SUCCESS")){
                                Toast.makeText(RegisterActivity.this, ""+user.getString("message"), Toast.LENGTH_SHORT).show();
                                Intent i = new Intent(getApplicationContext(),LoginActivity.class);
                                startActivity(i);
                            }else{
                                Toast.makeText(RegisterActivity.this, ""+user.getString("message"), Toast.LENGTH_SHORT).show();
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
                params.put("name", fullname);
                params.put("address", address);
                params.put("email", email);
                params.put("pass", password);

                return params;
            }
        };
        queue.add(postRequest);
    }
}
