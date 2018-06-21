package com.example.hp.park;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONException;
import org.json.JSONObject;

public class Payment extends AppCompatActivity  implements PaymentResultListener{

    EditText value;
    Button pay;
    int payamount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        value = (EditText)findViewById(R.id.input);
        pay= (Button) findViewById(R.id.razorpay);

        pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startPayment();
            }
        });

    }

    private void startPayment()
    {
        payamount=Integer.parseInt(value.getText().toString());


        Checkout checkout = new Checkout();

        checkout.setImage(R.mipmap.ic_razorpay);//logo
        final Activity activity= this;

        try{

            JSONObject options = new JSONObject();
            options.put("description", "Order #007");

            options.put("currency","INR");

            options.put("amount",payamount*100);

            checkout.open(activity,options);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onPaymentSuccess(String s) {

        Toast.makeText(Payment.this, "PAYMENT SUCCESSFUL",Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onPaymentError(int i, String s) {

        Toast.makeText(Payment.this, "PAYMENT FAILED",Toast.LENGTH_SHORT).show();
    }
}
