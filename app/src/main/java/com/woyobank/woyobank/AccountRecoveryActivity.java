package com.woyobank.woyobank;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
/*
 * sends an email to the entered address
 * a link will be given to reset the password
 */

public class AccountRecoveryActivity extends AppCompatActivity {

    private static final String TAG = "ForgotPasswordActivity";

    private FirebaseAuth mAuth;

    TextInputLayout tiEmail;
    String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_recovery);

        tiEmail = findViewById(R.id.tiEmail);

        mAuth = FirebaseAuth.getInstance();
    }

    public void sendButton(View view) {
        Log.d(TAG, "send to email");
        if (!validateForm()) {
            return;
        }

        mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(AccountRecoveryActivity.this, "We have sent you instructions to reset your password.", Toast.LENGTH_LONG).show();
                    startActivity(new Intent(AccountRecoveryActivity.this, LoginActivity.class));
                } else {
                    Toast.makeText(AccountRecoveryActivity.this, "Failed to send reset email.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private boolean validateForm() {
        boolean result = true;
        email = tiEmail.getEditText().getText().toString().trim();

        if (!email.contains("@gmail.com")){
            tiEmail.setError("Email must be in @gmail.com format");
            result = false;
        }

        return result;
    }
}
