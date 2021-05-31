package com.ajou.ourvillage.Apart;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.ajou.ourvillage.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import static com.ajou.ourvillage.Apart.ApartFragment.afterPoint;
import static com.ajou.ourvillage.Apart.ApartFragment.checkNum;
import static com.ajou.ourvillage.Apart.ApartFragment.payPoint;

public class ApartCheckMoneyActivity extends AppCompatActivity {

    private FloatingActionButton mBackBtn;
    private Button btn_pay;
    private TextView tv_will, tv_did;
    public static Activity _ApartCheckMoneyActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apart_check_money);

        mBackBtn = findViewById(R.id.apart_check_money_btn_back);
        mBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        tv_will = findViewById(R.id.apart_money_will);
        tv_did = findViewById(R.id.apart_money_did);


        if (checkNum != 1) {
            tv_will.setText(String.valueOf(payPoint));
            tv_did.setText(String.valueOf(afterPoint));

            System.out.println("여기야");
            System.out.println("paypoint" + payPoint);
            System.out.println("afterpoint" + afterPoint);
        } else {
            payPoint = Integer.parseInt(tv_will.getText().toString());
            afterPoint = Integer.parseInt(tv_did.getText().toString());


            System.out.println("paypoint" + payPoint);
            System.out.println("afterpoint" + afterPoint);
            tv_will.setText(String.valueOf(payPoint));
            tv_did.setText(String.valueOf(afterPoint));
        }

        System.out.println("얼마 " + payPoint + " " + afterPoint);

        btn_pay = findViewById(R.id.apart_btn_pay);
        btn_pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ApartPayActivity.class);
                startActivity(intent);
                checkNum++;
                finish();
            }
        });

    }
}