package com.woyobank.woyobank;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.woyobank.woyobank.models.User;

/*
 * show the name, card number, and the user's balance
 */
public class CheckBalanceActivity extends AppCompatActivity {

    private DatabaseReference mUserRef;
    TextView tvName, tvCardNum, tvBalance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_balance);

        //these variables allows us to get the data based on the uniquely generated userId
        final String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        mUserRef = FirebaseDatabase.getInstance().getReference().child("users").child(userId);

        tvName = findViewById(R.id.tvName);
        tvCardNum = findViewById(R.id.tvCardNum);
        tvBalance = findViewById(R.id.tvBalance);
    }

    @Override
    protected void onStart() {
        super.onStart();

        //onStart is only used to update the balance of the user when an update is applied on the database
        mUserRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                tvName.setText(user.name);
                tvCardNum.setText(user.cardNum);
                tvBalance.setText(String.valueOf(user.balance));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
