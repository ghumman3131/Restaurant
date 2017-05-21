package com.inceptionlabs.restaraunt.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.inceptionlabs.restaraunt.R;

public class Payment_options extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_options);
    }

    public void paytm_pay(View view) {

        Intent i = new Intent(Payment_options.this , Paytm_pay.class);

        i.putExtra("pay", getIntent().getDoubleExtra("pay", 0));
        i.putExtra("bill", getIntent().getDoubleExtra("bill", 0));
        i.putExtra("discount", getIntent().getDoubleExtra("discount", 0));

        startActivity(i);
        finish();
    }

    public void debit_card_pay(View view) {

        Intent i = new Intent(Payment_options.this , Payment.class);

        i.putExtra("pay", getIntent().getDoubleExtra("pay", 0));
        i.putExtra("bill", getIntent().getDoubleExtra("bill", 0));
        i.putExtra("discount", getIntent().getDoubleExtra("discount", 0));


        startActivity(i);
        finish();
    }
}
