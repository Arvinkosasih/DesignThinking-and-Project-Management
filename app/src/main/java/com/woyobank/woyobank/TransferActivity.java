package com.woyobank.woyobank;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

/*
 * initiates the transfer feature by taking in the target card number and amount
 */
public class TransferActivity extends AppCompatActivity {

    TextInputLayout tiTargetCardNum, tiAmount;

    String targetCardNum;
    String amount;// "amount" is used throughout the project to indicate the cost or a general change in balance

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transfer);

        tiTargetCardNum = findViewById(R.id.tiTargetCardNum);
        tiAmount = findViewById(R.id.tiAmount);
    }

    public void nextButton(View view) {
        if (!validateForm()) {
            return;
        }

        Intent nextIntent = new Intent(TransferActivity.this, TransferActivity2.class);

        nextIntent.putExtra("targetCardNum", targetCardNum);
        nextIntent.putExtra("amount", amount);

        startActivity(nextIntent);
    }

    private boolean validateForm() {
        boolean result = true;
        targetCardNum = tiTargetCardNum.getEditText().getText().toString().trim();
        amount = tiAmount.getEditText().getText().toString().trim();

        // Handle target card number field
        if (targetCardNum.length() != 16) {
            tiTargetCardNum.setError("Target card number must be 16 digits");
            result = false;
        }
        else {
            tiTargetCardNum.setError(null);
        }

        // Handle empty amount field
        if (amount.isEmpty()) {
            tiAmount.setError("Amount cannot be empty");
            result = false;
        }
        else {
            tiAmount.setError(null);
        }

        return result;
    }
}
