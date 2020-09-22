package com.example.prince_tvs;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private Button loginButton;
    private EditText loginName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
        loginButton = findViewById(R.id.login_button);
        loginName = findViewById(R.id.login_name);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = loginName.getText().toString();
                if(true) {
                    Intent intent = new Intent(MainActivity.this, CustomerRegisterActivity.class);
                    startActivity(intent);
                    finish();
                }


            }
        });
    }
}