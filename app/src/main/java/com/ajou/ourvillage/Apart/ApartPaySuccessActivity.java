package com.ajou.ourvillage.Apart;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.ajou.ourvillage.R;


public class ApartPaySuccessActivity extends AppCompatActivity {

    private Button btn_complete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apart_pay_success);

        btn_complete = findViewById(R.id.apart_btn_complete_pay);
        btn_complete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ApartCheckMoneyActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}