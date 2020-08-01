package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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

public class RegisterActivity extends AppCompatActivity {
    EditText ed1,ed2,ed3,ed4,ed5,ed6,ed7,ed8;
    Button b1,b2;
    String getName,getMob,getAddr,getEmail,getplace,getUname,getPass,getcpass;
    String apilink=Constants.ApiMainLink+"addfisherman_insert_api.php";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ed1=(EditText)findViewById(R.id.name);
        ed2=(EditText)findViewById(R.id.addr);
        ed3=(EditText)findViewById(R.id.mob);
        ed4=(EditText)findViewById(R.id.email);
        ed5=(EditText)findViewById(R.id.place);
        ed6=(EditText)findViewById(R.id.uname);
        ed7=(EditText)findViewById(R.id.pass);
        ed8=(EditText)findViewById(R.id.cpass);
        b1=(Button)findViewById(R.id.reg);
        b2=(Button)findViewById(R.id.log);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getName = ed1.getText().toString();
                getAddr = ed2.getText().toString();
                getMob = ed3.getText().toString();
                getEmail = ed4.getText().toString();
                getplace = ed5.getText().toString();
                getUname = ed6.getText().toString();
                getPass = ed7.getText().toString();
                getcpass = ed8.getText().toString();
                if (getPass.equals(getcpass)) {
                    Log.d("value", getName);
                    Log.d("value", getAddr);
                    Log.d("value", getMob);
                    Log.d("value", getEmail);
                    Log.d("value", getplace);
                    Log.d("value", getUname);
                    Log.d("value", getPass);
                    Log.d("value", getcpass);
                    callapi();
                }
                else {
                    Toast.makeText(getApplicationContext(), "Password doesn't match", Toast.LENGTH_LONG).show();
                }
            }
        });
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent);
            }
        });
    }

    private void callapi() {
        StringRequest stringRequest=new StringRequest(Request.Method.POST, apilink,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                      Toast.makeText(getApplicationContext(),response,Toast.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(),String.valueOf(error),Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> params=new HashMap<>();
                params.put("name",getName);
                params.put("address",getAddr);
                params.put("mobile",getMob);
                params.put("e-mail",getEmail);
                params.put("place",getplace);
                params.put("username",getUname);
                params.put("password",getPass);
                return params;
            }
        };
        RequestQueue requestQueue= Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }
}
