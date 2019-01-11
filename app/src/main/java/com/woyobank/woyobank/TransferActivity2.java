package com.woyobank.woyobank;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.woyobank.woyobank.models.Transaction;
import com.woyobank.woyobank.models.Transfer;
import com.woyobank.woyobank.models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.sql.Date;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class TransferActivity2 extends AppCompatActivity {

    private static final String TAG = "TransferActivity2";

    private DatabaseReference mDatabase;
    private DatabaseReference mUserRef;
    private DatabaseReference mTargetRef;

    TextView tvUserName, tvUserCardNum,
            tvTargetName, tvTargetCardNum,
            tvAmount;

    private String targetCardNum;
    double amount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transfer2);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        final String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        mUserRef = FirebaseDatabase.getInstance().getReference().child("users").child(userId);

        targetCardNum = getIntent().getStringExtra("targetCardNum");
        amount = Double.parseDouble(getIntent().getStringExtra("amount"));

        tvUserName = findViewById(R.id.tvUserName);
        tvUserCardNum = findViewById(R.id.tvUserCardNum);
        tvTargetName = findViewById(R.id.tvTargetName);
        tvTargetCardNum = findViewById(R.id.tvTargetCardNum);
        tvAmount = findViewById(R.id.tvAmount);
        tvAmount.setText(String.valueOf(amount));
    }

    @Override
    protected void onStart() {
        super.onStart();

        /*
         * the system prints present in the query below is not part of the display
         * this was used to monitor the variables that were passed through the logcat
         */
        final Query userQuery = mDatabase.child("users").orderByChild("cardNum");
        userQuery.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                String myParentNode = dataSnapshot.getKey();
                System.out.println("My Parent Node: " + myParentNode);
                for (DataSnapshot child: dataSnapshot.getChildren()) {
                    String key = child.getKey().toString();
                    String value = child.getValue().toString();
                    System.out.println(key + " " + value);
                    if (key.equals("cardNum")) {
                        if (value.equals(targetCardNum)) {
                            mTargetRef = FirebaseDatabase.getInstance().getReference().child("users").child(myParentNode);
                            targetRefListener();
                        }
                        else {
                            System.out.println("No record");
                        }
                    }
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        userRefListener();
    }

    public void transferButton(View view) {
        transfer();
        startActivity(new Intent(TransferActivity2.this, HomeActivity.class));
    }

    public void userRefListener() {
        mUserRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                tvUserName.setText(user.name);
                tvUserCardNum.setText(user.cardNum);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void targetRefListener() {
        mTargetRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                tvTargetName.setText(user.name);
                tvTargetCardNum.setText(user.cardNum);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void transfer() {
        final String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        final String targetId = mTargetRef.getKey();

        mDatabase.addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        User user = dataSnapshot.getValue(User.class);

                        // [START_EXCLUDE]
                        if (user == null) {
                            // User is null, error out
                            Log.e(TAG, "User " + userId + " is unexpectedly null");
                            Toast.makeText(TransferActivity2.this,
                                    "Error: could not fetch user.",
                                    Toast.LENGTH_SHORT).show();
                        }
                        else {
                            String transactionId = mDatabase.child("transactions").push().getKey();
                            addNewTransfer(userId, targetId, transactionId);
                            userTransaction(transactionId);
                            targetTransaction(transactionId);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Log.w(TAG, "getUser:onCancelled", databaseError.toException());
                    }
                });
    }

    private void userTransaction(final String transactionId) {
        final String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        final double amount = Double.parseDouble(tvAmount.getText().toString());
        final String sign = "-";//this is a variable that will be passed on and used in the transfer history

        mUserRef.addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        User user = dataSnapshot.getValue(User.class);
                        double oldBalance = user.balance;
                        double newBalance = oldBalance - amount;
                        String title = tvTargetCardNum.getText().toString();

                        if (user == null) {
                            // User is null, error out
                            Log.e(TAG, "User " + userId + " is unexpectedly null");
                            Toast.makeText(TransferActivity2.this,
                                    "Error: could not fetch user.",
                                    Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Long timestamp = getTimestamp();
                            String dateTime = getDateTime(timestamp);
                            addNewTransaction(userId, amount, newBalance, timestamp, dateTime, sign, title, transactionId);
                            mUserRef.child("balance").setValue(newBalance);
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    private void targetTransaction(final String transactionId) {
        final String targetId = mTargetRef.getKey();
        final double amount = Double.parseDouble(tvAmount.getText().toString());
        final String sign = "+";//this is a variable that will be passed on and used in the transfer history

        mTargetRef.addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        User user = dataSnapshot.getValue(User.class);
                        double oldBalance = user.balance;
                        double newBalance = oldBalance + amount;
                        String title = tvUserCardNum.getText().toString();

                        if (user == null) {
                            // User is null, error out
                            Log.e(TAG, "User " + targetId + " is unexpectedly null");
                            Toast.makeText(TransferActivity2.this,
                                    "Error: could not fetch user.",
                                    Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Long timestamp = getTimestamp();
                            String dateTime = getDateTime(timestamp);
                            addNewTransaction(targetId, amount, newBalance, timestamp, dateTime, sign, title, transactionId);
                            mTargetRef.child("balance").setValue(newBalance);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    private void addNewTransfer(String userId, String targetId, String transactionId) {
        String key = mDatabase.child("transfers").push().getKey();
        Transfer transfer = new Transfer(userId, targetId, transactionId);
        Map<String, Object> transferValues = transfer.toMap();

        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/transfers/" + key, transferValues);

        mDatabase.updateChildren(childUpdates);
    }

    private void addNewTransaction(String id, double amount, double newBalance, Long timestamp, String dateTime, String sign, String title, String transactionId) {

        Transaction transaction = new Transaction(amount, newBalance, timestamp, dateTime, sign, title);
        Map<String, Object> transactionValues = transaction.toMap();

        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/transactions/" + id + "/" + transactionId, transactionValues);

        mDatabase.updateChildren(childUpdates);
    }

    private Long getTimestamp() {
        Long timestamp = System.currentTimeMillis();
        return timestamp;
    }

    private String getDateTime(Long timestamp) {
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.getDefault());
        String dateTime = format.format(timestamp);
        return dateTime;
    }
}
