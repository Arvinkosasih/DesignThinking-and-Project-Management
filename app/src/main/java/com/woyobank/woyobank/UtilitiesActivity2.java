package com.woyobank.woyobank;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.woyobank.woyobank.models.User;

public class UtilitiesActivity2 extends AppCompatActivity {

    private DatabaseReference mUserRef;
    TextView tvBalance, tvUtil;
    TextInputLayout tiBlockNum, tiUnit, tiNumber, tiPostalCode;
    String util, blockNum, unit, number, postalCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_utilities2);

        final String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        mUserRef = FirebaseDatabase.getInstance().getReference().child("users").child(userId);

        util = getIntent().getStringExtra("util");

        tvBalance = findViewById(R.id.tvBalance);
        tvUtil = findViewById(R.id.tvUtil);
        tiBlockNum = findViewById(R.id.tiBlockNum);
        tiUnit = findViewById(R.id.tiUnit);
        tiNumber = findViewById(R.id.tiNumber);
        tiPostalCode = findViewById(R.id.tiPostalCode);

        tvUtil.setText(util);
    }

    @Override
    protected void onStart() {
        super.onStart();

        mUserRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                tvBalance.setText(String.valueOf(user.balance));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void nextButton(View view) {
        if (!validateForm()) {
            return;
        }

        Intent nextIntent = new Intent(UtilitiesActivity2.this, UtilitiesActivity3.class);

        nextIntent.putExtra("util", util);
        nextIntent.putExtra("blockNum", blockNum);
        nextIntent.putExtra("unit", unit);
        nextIntent.putExtra("number", number);
        nextIntent.putExtra("postalCode", postalCode);

        startActivity(nextIntent);

    }

    private boolean validateForm() {
        boolean result = true;
        blockNum = tiBlockNum.getEditText().getText().toString().trim();
        unit = tiUnit.getEditText().getText().toString().trim();
        number = tiNumber.getEditText().getText().toString().trim();
        postalCode = tiPostalCode.getEditText().getText().toString().trim();

        // Handle block number field
        if (blockNum.length() != 3) {
            tiBlockNum.setError("Block Number must be 3 digits");
            result = false;
        }
        else {
            tiBlockNum.setError(null);
        }

        // Handle unit number field
        if (unit.length() != 2) {
            tiUnit.setError("Unit must be 2 digits");
            result = false;
        }
        else {
            tiUnit.setError(null);
        }

        if (number.length() != 3) {
            tiNumber.setError("Number must be 3 digits");
            result = false;
        }
        else {
            tiNumber.setError(null);
        }

        // Handle postal code field
        if (postalCode.length() != 6) {
            tiPostalCode.setError("Postal code must be 6 digits");
            result = false;
        }
        else {
            tiPostalCode.setError(null);
        }

        return result;
    }
}
