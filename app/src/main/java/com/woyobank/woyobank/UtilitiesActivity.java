package com.woyobank.woyobank;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

/*
 * receives input by user to for paying utilities bills
 */
public class UtilitiesActivity extends AppCompatActivity {

    // this string variable replaces the target user card number in the transaction history
    final String ELECTRICITY = "ELECTRICITY";
    final String PUB = "PUB";
    final String GAS = "GAS";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_utilities);
    }

    //the same activity is used for 3 following buttons
    //only the passed string value differs
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
