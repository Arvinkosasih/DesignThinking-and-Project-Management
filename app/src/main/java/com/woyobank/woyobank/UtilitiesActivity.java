package com.woyobank.woyobank;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class UtilitiesActivity extends AppCompatActivity {

    final String ELECTRICITY = "ELECTRICITY";
    final String PUB = "PUB";
    final String GAS = "GAS";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_utilities);
    }

    public void electricityButton(View view) {
        Intent electricityIntent = new Intent(UtilitiesActivity.this, UtilitiesActivity2.class);
        electricityIntent.putExtra("util", ELECTRICITY);
        startActivity(electricityIntent);
    }

    public void pubButton(View view) {
        Intent pubIntent = new Intent(UtilitiesActivity.this, UtilitiesActivity2.class);
        pubIntent.putExtra("util", PUB);
        startActivity(pubIntent);
    }

    public void gasButton(View view) {
        Intent gasIntent = new Intent(UtilitiesActivity.this, UtilitiesActivity2.class);
        gasIntent.putExtra("util", GAS);
        startActivity(gasIntent);
    }
}
