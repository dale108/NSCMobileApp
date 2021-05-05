package com.nscmobileapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;


public class TrafficActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_traffic);

        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);
    }
}