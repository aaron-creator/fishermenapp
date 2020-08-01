package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    EditText ed1,ed2;
    Button b1,b2;
    String getUsername,getPassword;
    String apilink=Constants.ApiMainLink+"login_api.php";
    String uid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ed1=(EditText)findViewById(R.id.uname);
        ed2=(EditText)findViewById(R.id.pass);
        b1=(Button)findViewById(R.id.loginbut);
        b2=(Button)findViewById(R.id.regbut);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getUsername=ed1.getText().toString();
                getPassword=ed2.getText().toString();
                callapi();



            }
        });
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),RegisterActivity.class);
                startActivity(intent);
            }
        });
    }

    private void callapi() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, apilink,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

//                        Toast.makeText(getApplicationContext(),"clicked",Toast.LENGTH_LONG).show();
//                        Intent intent=new Intent(getApplicationContext(),AddActivity.class);
//                        startActivity(intent);

                        Toast.makeText(getApplicationContext(), response, Toast.LENGTH_LONG).show();


                        try{
                            JSONObject obj = new JSONObject(response);
                            String rslt = obj.getString("status");
                            uid = obj.getString("id");


                            if (rslt.equals("success")) {

                                // Toast.makeText(getApplicationContext(), "Succesfully Registered", Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(getApplicationContext(), AddActivity.class);
                                SharedPreferences.Editor editor = getSharedPreferences("Login", MODE_PRIVATE).edit();

                                editor.putString("uid", uid);

                                editor.commit();

                                Toast.makeText(getApplicationContext(), uid, Toast.LENGTH_LONG).show();
                                startActivity(intent);

                            }
                            else{
                                Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_LONG).show();

                            }

                        }
                        catch (Exception e){

                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> params = new HashMap<>();
                params.put("email",getUsername);
                params.put("password",getPassword);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }
}
