package com.inceptionlabs.restaraunt.Activities;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.inceptionlabs.restaraunt.R;

public class ApplyCoupon extends Activity {

    private EditText coupon;
    private Button cancel , apply;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apply_coupon);

        coupon = (EditText) findViewById(R.id.coupon);

        apply = (Button) findViewById(R.id.apply);
        apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(coupon.getText().toString().equals(""))
                {
                    Toast.makeText(ApplyCoupon.this,"Please enter coupon code",Toast.LENGTH_SHORT).show();
                }
                else if(coupon.getText().toString().equals("SUPER20") || coupon.getText().toString().equals("Super20") ||coupon.getText().toString().equals("super20"))
                {
                    if(getIntent().getDoubleExtra("bill",0) > 10) {
                        double dis = (double) (getIntent().getDoubleExtra("bill",0) * 0.2);
                        setResult(5, new Intent().putExtra("discount", dis));
                        finish();
                    } else {
                        Toast.makeText(ApplyCoupon.this,"Total bill must be $ 10 or more",Toast.LENGTH_SHORT).show();
                    }
                }
                else if(coupon.getText().toString().equals("SAVE30") || coupon.getText().toString().equals("Save30") ||coupon.getText().toString().equals("save30"))
                {
                    if(getIntent().getDoubleExtra("bill",0) > 15) {
                        double dis = (double) (getIntent().getDoubleExtra("bill",0) * 0.3);
                        setResult(5, new Intent().putExtra("discount", dis));
                        finish();
                    } else {
                        Toast.makeText(ApplyCoupon.this,"Total bill must be $ 15 or more",Toast.LENGTH_SHORT).show();
                    }
                }

                else {
                    Toast.makeText(ApplyCoupon.this,"Invalid coupon",Toast.LENGTH_SHORT).show();
                }
            }
        });

        cancel = (Button) findViewById(R.id.cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(5,new Intent().putExtra("discount", 0));
                finish();
            }
        });


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        setResult(5 , new Intent().putExtra("discount", 0));
        finish();
    }
}
