package com.woyobank.woyobank;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

/*
 * uses the data stored in the database to verify user
 */

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";

    TextInputLayout tiEmail, tiPwd;
    String email, password;
    TextView tvForgotPwd;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

        tiEmail = findViewById(R.id.tiEmail);
        tiPwd = findViewById(R.id.tiPwd);

        tvForgotPwd = findViewById(R.id.tvForgotPwd);
    }

    private boolean validateForm() {
        boolean result = true;
        email = tiEmail.getEditText().getText().toString().trim();
        password = tiPwd.getEditText().getText().toString().trim();

        // Handle empty email field
        if (email.isEmpty()) {
            tiEmail.setError("Email cannot be empty");
            result = false;
        }

        // Handle empty password field
        if (password.isEmpty()) {
            tiPwd.setError("Password cannot be empty");
            result = false;
        }

        return result;
    }

    public void loginButton(View view) {
        Log.d(TAG, "login");
        if (!validateForm()) {
            return;
        }

        mAuth.signInWithEmailAndPassword(email, password) //sign in using the built-in firebase authenticator
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "Login:onComplete:" + task.isSuccessful());

                        // logs in the user when email and password matches that of saved in the database
                        if (task.isSuccessful()) {
                            startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                            Toast.makeText(LoginActivity.this, "Login Success",
                                    Toast.LENGTH_SHORT).show();
                            finish();
                        }
                        // no further error checking required other than the built-in authenticator
                        else {
                            Toast.makeText(LoginActivity.this, "Login Failed",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public void signUpButton(View view) {
        startActivity(new Intent(LoginActivity.this, SignUpActivity.class));
        finish();
    }

    public void tvForgotPwd(View view){
        startActivity(new Intent(LoginActivity.this, AccountRecoveryActivity.class));
        finish();
    }
}
