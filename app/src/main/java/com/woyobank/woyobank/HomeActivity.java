package com.woyobank.woyobank;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;

public class HomeActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

    }

    public void balanceButton(View view) {
        startActivity(new Intent(HomeActivity.this, CheckBalanceActivity.class));
    }

    public void transferButton(View view) {
        startActivity(new Intent(HomeActivity.this, TransferActivity.class));
    }

    public void utilitiesButton(View view) {
        startActivity(new Intent(HomeActivity.this, UtilitiesActivity.class));
    }

    public void transactionHistoryButton(View view) {
        startActivity(new Intent(HomeActivity.this, TransactionHistoryActivity.class));
    }

    public void signOutButton(View view) {
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(HomeActivity.this, LoginActivity.class));
        finish();
    }
}
