package com.woyobank.woyobank;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

/*
 * initiates the sign up feature by taking input from the user
 * intents the values to SignUpActivity2
 */

public class SignUpActivity extends AppCompatActivity {

    TextInputLayout tiEmail, tiPwd, tiPhone, tiPIN,
            tiName, tiCardNum, tiDueDate, tiCVC;

    String email, password, phone, pin,
            name, cardNum, dueDate, cvc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        tiEmail = findViewById(R.id.tiEmail);
        tiPwd = findViewById(R.id.tiPwd);
        tiPhone = findViewById(R.id.tiPhone);
        tiPIN = findViewById(R.id.tiPIN);
        tiName = findViewById(R.id.tiName);
        tiCardNum = findViewById(R.id.tiCardNum);
        tiDueDate = findViewById(R.id.tiDueDate);
        tiCVC = findViewById(R.id.tiCVC);
    }

    public void nextButton(View view) {
        if (!validateForm())
            return;

        Intent nextIntent = new Intent(SignUpActivity.this, SignUpActivity2.class);

        nextIntent.putExtra("email", email);
        nextIntent.putExtra("password", password);
        nextIntent.putExtra("phone", phone);
        nextIntent.putExtra("pin", pin);

        nextIntent.putExtra("name", name);
        nextIntent.putExtra("cardNum", cardNum);
        nextIntent.putExtra("dueDate", dueDate);
        nextIntent.putExtra("cvc", cvc);

        startActivity(nextIntent);
    }

    /*
     * trim() is not necessary
     * serves as a secondary error checking if someone accidentally types in a whitespace
     */
    private boolean validateForm() {
        boolean result = true;
        email = tiEmail.getEditText().getText().toString().trim();
        password = tiPwd.getEditText().getText().toString().trim();
        phone = tiPhone.getEditText().getText().toString().trim();
        pin = tiPIN.getEditText().getText().toString().trim();

        name = tiName.getEditText().getText().toString().trim();
        cardNum = tiCardNum.getEditText().getText().toString().trim();
        dueDate = tiDueDate.getEditText().getText().toString().trim();
        cvc = tiCVC.getEditText().getText().toString().trim();

        // Handle email format field
        if (!email.contains("@gmail.com")) {
            tiEmail.setError("Email must be in email@gmail.com format");
            result = false;
        } else if (email.length() < 11) {
            tiEmail.setError("Invalid email");
        } else {
            tiEmail.setError(null);
        }

        // Handle password field
        if (password.length() < 6) {
            tiPwd.setError("Password should be at least 6 characters");
            result = false;
        } else {
            tiPwd.setError(null);
        }

        // Handle phone number field
        if (phone.length() != 8) {
            tiPhone.setError("Phone number must be 8 digits");
            result = false;
        } else {
            tiPhone.setError(null);
        }

        // Handle pin field
        if (pin.length() < 6) {
            tiPIN.setError("PIN must be 6 digits");
            result = false;
        } else {
            tiPIN.setError(null);
        }

        // Handle empty name field
        if (name.isEmpty()) {
            tiName.setError("Name cannot be empty");
            result = false;
        } else {
            tiName.setError(null);
        }

        // Handle empty card number field
        if (cardNum.length() != 16) {
            tiCardNum.setError("Card number must be 16 digits");
            result = false;
        } else {
            tiCardNum.setError(null);
        }

        // Handle due date field
        int count = 0;
        if (dueDate.length() != 5) {
            tiDueDate.setError("Due date must be in mm/yy format");
            result = false;
        } else {
            for (int i = 0; i < dueDate.length(); i++) {
                if (dueDate.charAt(i) == '/') {
                    count++;
                }
            }
            if (dueDate.charAt(2) == '/' && count == 1) {
                tiDueDate.setError(null);
            } else {
                tiDueDate.setError("Due date must be in mm/yy format");
                result = false;
            }
        }

        // Handle CVC code field
        if (cvc.length() != 3) {
            tiCVC.setError("CVC code must be 3 digits");
            result = false;
        } else {
            tiCVC.setError(null);
        }

        return result;
    }
}