package com.example.hp.park;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

public class ThreeFragment  extends Fragment  {

    private EditText etInput;
    private Button btnCreateQr;
    private ImageView imageView;
    private Button ScanButton;
    private Button mLogoutBtn;
    private Button payment;



    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    public ThreeFragment(){
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.activity_three_fragment, container, false);
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        super.onCreate(savedInstanceState);
     //   setContentView(R.layout.activity_three_fragment);

        etInput=  (EditText) getView().findViewById(R.id.etInput);
        btnCreateQr = (Button) getView().findViewById(R.id.btnCreate);
        imageView = (ImageView)getView().findViewById(R.id.imageView);
        ScanButton = (Button) getView().findViewById(R.id.ScanButton);
        mLogoutBtn = (Button) getView().findViewById(R.id.logout);
        payment = (Button) getView().findViewById(R.id.payment);


        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser()==null)
                {
                    startActivity(new Intent(getActivity(),UserLoginActivity.class));
                }
            }
        };



        ScanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scan();
            }
        });
        mLogoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth.signOut();
            }
        });




        btnCreateQr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String text = etInput.getText().toString().trim();

                if (text!=null)
                {
                    MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
                    try {


                        BitMatrix bitMatrix = multiFormatWriter.encode(text, BarcodeFormat.QR_CODE, 500, 500);
                        BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
                        Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
                        imageView.setImageBitmap(bitmap);
                    }
                    catch (WriterException e)
                    {
                        e.printStackTrace();
                    }
                }
            }
        });

        payment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pay();
            }
        });

    }


    public void scan()
    {
        Intent intent = new Intent(getActivity(), ScannerActivity.class);
        startActivity(intent);
    }

    public void pay()
    {
        Intent intent = new Intent(getActivity(), Payment.class);
        startActivity(intent);
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

}
