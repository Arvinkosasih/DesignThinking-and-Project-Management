package com.woyobank.woyobank;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.woyobank.woyobank.models.User;

public class SignUpActivity2 extends AppCompatActivity {

    private static final String TAG = "SignUpActivity2";

    private FirebaseAuth mAuth;

    private DatabaseReference mDatabase;

    TextView tvEmail, tvName, tvPhone, tvPIN, tvCardNum;

    String email, password, phone, pin,
            name, cardNum, dueDate, cvc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up2);

        mAuth = FirebaseAuth.getInstance();

        mDatabase = FirebaseDatabase.getInstance().getReference();

        email = getIntent().getStringExtra("email");
        password = getIntent().getStringExtra("password");
        phone = getIntent().getStringExtra("phone");
        pin = getIntent().getStringExtra("pin");
        name = getIntent().getStringExtra("name");
        cardNum = getIntent().getStringExtra("cardNum");
        dueDate = getIntent().getStringExtra("dueDate");
        cvc = getIntent().getStringExtra("cvc");

        tvEmail = findViewById(R.id.tvEmail);
        tvName = findViewById(R.id.tvName);
        tvPhone = findViewById(R.id.tvPhone);
        tvPIN = findViewById(R.id.tvPIN);
        tvCardNum = findViewById(R.id.tvCardNum);

        tvEmail.setText(email);
        tvName.setText(name);
        tvPhone.setText(phone);
        tvPIN.setText(pin);
        tvCardNum.setText(cardNum);
    }

    private void signUp() {
        Log.d(TAG, "signUp");

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "SignUp:onComplete:" + task.isSuccessful());

                        if (task.isSuccessful()) {
                            onAuthSuccess(task.getResult().getUser());
                            Toast.makeText(SignUpActivity2.this, "Sign Up Success",
                                    Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Toast.makeText(SignUpActivity2.this, "Sign Up Failed",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void onAuthSuccess(FirebaseUser user) {
        addNewUser(user.getUid(), user.getEmail(),
                phone, pin, name, cardNum, dueDate, cvc);

        startActivity(new Intent(SignUpActivity2.this, HomeActivity.class));
        finish();
    }

    public void yesButton(View view) {
        signUp();
    }

    private void addNewUser(String id, String email, String phone, String pin,
                            String name, String cardNum, String dueDate, String cvc) {
        final double BALANCE = 100;

        User user = new User(email, phone, pin, name, cardNum, dueDate, cvc, BALANCE);
        mDatabase.child("users").child(id).setValue(user);
    }
}
