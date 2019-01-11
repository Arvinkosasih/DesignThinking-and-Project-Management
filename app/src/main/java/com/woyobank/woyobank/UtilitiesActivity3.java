package com.woyobank.woyobank;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.woyobank.woyobank.models.Transaction;
import com.woyobank.woyobank.models.Transfer;
import com.woyobank.woyobank.models.User;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/*
 * confirmation page for paying utilities bills
 */
public class UtilitiesActivity3 extends AppCompatActivity {

    private static final String TAG = "UtilitiesActivity3";

    private DatabaseReference mDatabase;
    private DatabaseReference mUserRef;

    TextView tvTitle, tvBlockNum, tvUnitNum, tvPostalCode, tvAmount;

    String title, blockNum, unit, number, unitNum, postalCode;

    final double AMOUNT = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_utilities3);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        final String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        mUserRef = FirebaseDatabase.getInstance().getReference().child("users").child(userId);

        title = getIntent().getStringExtra("util");
        blockNum = getIntent().getStringExtra("blockNum");
        unit = getIntent().getStringExtra("unit");
        number = getIntent().getStringExtra("number");
        unitNum = "#" + unit + "-" + number;
        postalCode = getIntent().getStringExtra("postalCode");

        tvTitle = findViewById(R.id.tvTitle);
        tvBlockNum = findViewById(R.id.tvBlockNum);
        tvUnitNum = findViewById(R.id.tvUnitNum);
        tvPostalCode = findViewById(R.id.tvPostalCode);
        tvAmount = findViewById(R.id.tvAmount);

        tvTitle.setText(title);
        tvBlockNum.setText(blockNum);
        tvUnitNum.setText(unitNum);
        tvPostalCode.setText(postalCode);
        tvAmount.setText(String.valueOf(AMOUNT));
    }

    public void transferButton(View view) {
        transfer();
        startActivity(new Intent(UtilitiesActivity3.this, HomeActivity.class));
        finish();
    }

    private void transfer() {
        final String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        final String targetId = title;

        mDatabase.addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        User user = dataSnapshot.getValue(User.class);

                        // [START_EXCLUDE]
                        if (user == null) {
                            // User is null, error out
                            Log.e(TAG, "User " + userId + " is unexpectedly null");
                            Toast.makeText(UtilitiesActivity3.this,
                                    "Error: could not fetch user.",
                                    Toast.LENGTH_SHORT).show();
                        }
                        else {
                            String transactionId = mDatabase.child("transactions").push().getKey();
                            addNewTransfer(userId, targetId, transactionId);
                            userTransaction(transactionId);
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
                        String title = tvTitle.getText().toString();

                        if (user == null) {
                            // User is null, error out
                            Log.e(TAG, "User " + userId + " is unexpectedly null");
                            Toast.makeText(UtilitiesActivity3.this,
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

    // Maps are used in the next two methods just to simplify passing the values into the database
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

    /*
     * the format used in the transaction history is a String with "dd-MM-yyyy 00:00"
     * this method converts the time before storing into the database
     * this makes it easier to pull just one String value instead of pulling the timestamp and converting it there
     */
    private String getDateTime(Long timestamp) {
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.getDefault());
        String dateTime = format.format(timestamp);
        return dateTime;
    }
}
