package com.nour.nourclinic.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
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

public class LoginActivity extends AppCompatActivity {
    EditText emailTxt,passTxt;
    Button registerBtn,loginBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        setTitle("Login");

        registerBtn = (Button)findViewById(R.id.registerbtn);
        loginBtn = (Button)findViewById(R.id.loginbtn);

        emailTxt = (EditText)findViewById(R.id.email);
        passTxt = (EditText)findViewById(R.id.pass);


        //open register activity
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login(emailTxt.getText().toString(),passTxt.getText().toString());
            }
        });


        //open register activity
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),RegisterActivity.class);
                startActivity(i);
            }
        });
    }

    private void login(final String email, final String password){
        RequestQueue queue = Volley.newRequestQueue(this);

        String url = Configuration.URL + "login.php";
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
                                Toast.makeText(LoginActivity.this, ""+user.getString("status"), Toast.LENGTH_SHORT).show();
                                JSONObject userdata = user.getJSONObject("user");
                                SharedPreferences.Editor editor = getSharedPreferences(Configuration.SHRD_PREF, MODE_PRIVATE).edit();
                                editor.putBoolean("loggedin",true);
                                editor.putString("firstname", userdata.getString("firstname"));
                                editor.putString("lastname", userdata.getString("lastname"));
                                editor.putString("loginemail", userdata.getString("loginemail"));
                                editor.putString("roleid", userdata.getString("roleid"));
                                editor.putString("userid", userdata.getString("userid"));
                                editor.apply();

                                if (userdata.getString("roleid").equals("3")){//patient
                                    Intent i = new Intent(getApplicationContext(),MainActivity.class);
                                    startActivity(i);
                                }else if(userdata.getString("roleid").equals("2")){//doctor
                                    Intent i = new Intent(getApplicationContext(),DoctorActivity.class);
                                    startActivity(i);
                                }


                            }else{
                                Toast.makeText(LoginActivity.this, ""+user.getString("status"), Toast.LENGTH_SHORT).show();
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
                params.put("email", email);
                params.put("password", password);

                return params;
            }
        };
        queue.add(postRequest);
    }

    @Override
    protected void onStart() {
        super.onStart();

        SharedPreferences prefs = getSharedPreferences(Configuration.SHRD_PREF, MODE_PRIVATE);
        boolean loggedIn = prefs.getBoolean("loggedin", false);
        String role = prefs.getString("roleid", "");
        if (loggedIn & role.equals("3")){
            Intent i = new Intent(getApplicationContext(),MainActivity.class);
            startActivity(i);
        }else if(loggedIn & role.equals("2")){
            Intent i = new Intent(getApplicationContext(),DoctorActivity.class);
            startActivity(i);
        }
    }
}
