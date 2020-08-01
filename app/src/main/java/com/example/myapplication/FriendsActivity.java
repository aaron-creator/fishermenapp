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

import java.util.HashMap;
import java.util.Map;

public class FriendsActivity extends AppCompatActivity {
    EditText ed1,ed2;
    Button b1;
    String getName,getMob,Sharedval;
    String apilink=Constants.ApiMainLink+"friends_insert_api.php";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);


        SharedPreferences editor = getSharedPreferences("Login", MODE_PRIVATE);
        Sharedval = editor.getString("uid", null);
        ed1=(EditText)findViewById(R.id.name);
        ed2=(EditText)findViewById(R.id.mob);
        b1=(Button)findViewById(R.id.savebut);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getName=ed1.getText().toString();
                getMob=ed2.getText().toString();
                Log.d("value",getName);
                Log.d("value",getMob);
                callapi();

            }
        });
    }

    private void callapi() {
        StringRequest stringRequest=new StringRequest(Request.Method.POST, apilink,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(getApplicationContext(),response,Toast.LENGTH_LONG).show();

                        Intent intent=new Intent(getApplicationContext(),AddActivity.class);
                    startActivity(intent);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                         Toast.makeText(getApplicationContext(),String.valueOf(error),Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String,String> getParams() throws AuthFailureError {
                HashMap<String,String> params=new HashMap<>();
                params.put("name",getName);
                params.put("mobile",getMob);
                params.put("userId",Sharedval);
                return params;
            }
        };
        RequestQueue requestQueue=Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }
}
