package com.woyobank.woyobank;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.woyobank.woyobank.adapter.RecyclerViewAdapter;

import java.util.ArrayList;

/*
 * displays all types of transactions
 * passes values and display using recycler view
 * displays both transfer made by the user and transfers received by the user
 */

public class TransactionHistoryActivity extends AppCompatActivity {

    private DatabaseReference mUserHistoryRef;

    //variables
    private ArrayList<String> dateTime = new ArrayList<>();
    private ArrayList<String> title = new ArrayList<>();
    private ArrayList<String> sign = new ArrayList<>();
    private ArrayList<String> amount = new ArrayList<>();
    private ArrayList<String> newBalance = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_history);

        final String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        mUserHistoryRef = FirebaseDatabase.getInstance().getReference().child("transactions").child(userId);
    }

    @Override
    protected void onStart() {
        super.onStart();

        final Query userQuery = mUserHistoryRef.orderByChild("dateTime");
        userQuery.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                String myParentNode = dataSnapshot.getKey();
                System.out.println("My Parent Node: " + myParentNode);

                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    String key = child.getKey().toString();
                    String value = child.getValue().toString();
                    System.out.println(key + " " + value);
                    if (key.equals("dateTime")) {
                        dateTime.add(value);
                        System.out.println(key + " " + value);
                    }
                    else if (key.equals("title")) {
                        title.add(value);
                    }
                    else if (key.equals("sign")) {
                        sign.add(value);
                    }
                    else if (key.equals("amount")) {
                        amount.add(value);
                    }
                    else if (key.equals("newBalance")) {
                        newBalance.add(value);
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

        initRecyclerView();
    }

    private void initRecyclerView (){ // used to call the recycler view from RecyclerViewAdapter
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(this, dateTime, title, sign, amount, newBalance);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
}
