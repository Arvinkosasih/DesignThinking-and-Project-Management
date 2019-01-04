package com.woyobank.woyobank;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class CheckBalance extends AppCompatActivity {

    TextView tvName;
    TextView tvCardNum;
    TextView tvAmount;
    Button historyButton;

    double bal;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_balance);


        tvName = (TextView) findViewById(R.id.tvName);
        tvName.setText("Ilham");

        tvCardNum = (TextView) findViewById(R.id.tvCardNum);
        tvCardNum.setText("12436");

        tvAmount = (TextView) findViewById(R.id.tvAmount);

    }


    @Override
    protected void onStart(){
        super.onStart();

        FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();

        DatabaseReference ref = mDatabase.getReference();

        ref.child("user").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String test = String.valueOf(dataSnapshot.getValue());

                String test2 [] = test.split("=");

                String test3 = test2[1].replace('}',' ').trim();




                bal = Double.parseDouble(test3);


                System.out.println(bal);
                tvAmount.setText(String.valueOf(bal));


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });

    }

}