package com.example.prince_tvs;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.prince_tvs.Database.databaseHandler;
import com.example.prince_tvs.Model.CustomerModel;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.EventListener;
import java.util.List;

public class CustomerRegisterActivity extends AppCompatActivity {

    private TextInputLayout customerNameLayout, customerMobNoLayout, customerVehNoLayout;
    private EditText customerName, customerMobNo, customerVehNo;
    private TextView dateNo,dateMonthYear, dateDay;
    public static TextView paidS, freeS, runningS;
    private int PaidService = 30;
    private int FreeService = 30;
    private int RunningService = 2;
    private Spinner customerServiceType;
    private ArrayAdapter<CharSequence> customerServiceTypeAdapter;
    private LinearLayout customerDateLayout;
    private int serviceType = 0;
    private Button registerCustomerButton;
    private String getDate;
    private databaseHandler databaseHandler;
    private Toolbar toolbar;
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.d("Debugging","menu created");
        getMenuInflater().inflate(R.menu.mymenu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_blockTime:
                Intent intent = new Intent(CustomerRegisterActivity.this, BlockTimeActivity.class);
                startActivity(intent);

                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_page);

        //For initialization of layout items
        initialization();


        setSupportActionBar(toolbar);


        if(databaseHandler != null) {
            databaseHandler.onUpdateCustomerListener(getDate, freeS, paidS, runningS);
            databaseHandler.onUpdateBlockListener(getDate, freeS, paidS, runningS);
        }
        customerServiceType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                serviceType = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //### Function for updating service value and block value
        //updateServiceValues(getApplicationContext(),getDate);
        if(databaseHandler != null) {
            databaseHandler.updateCustomerCount(getDate, freeS, paidS, runningS);
        }

        //Service value update
        paidS.setText("0" + " / " + String.valueOf(PaidService));
        freeS.setText("0" + " / " + String.valueOf(FreeService));
        runningS.setText("0" + " / " + String.valueOf(RunningService));

        customerDateLayout.setOnClickListener(onClickCalendarActivity());

        //to be called on pressing done button;

        registerCustomerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CustomerModel customerModel = new CustomerModel();
                if(validateDetails()) {
                    customerModel.setName(customerName.getText().toString());
                    customerModel.setMobileNumber(customerMobNo.getText().toString());
                    customerModel.setDate(getDate);
                    customerModel.setVehicleNumber(customerVehNo.getText().toString());
                    customerModel.setServiceType(serviceType);

                    databaseHandler database = null;
                    try {
                        database = new databaseHandler(getApplicationContext());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    database.writeCustomerData(customerModel);

                }
            }
        });
    }

    private void updateServiceValues(Context context, String getDate) {
        if(databaseHandler != null) {
            databaseHandler.updateCustomerCount(getDate, freeS, paidS, runningS);
        }
    }

    private void dateTextSetter(String date) {
        String[] parse = date.split(",");
        dateDay.setText(parse[0]);
        String[] strDate = parse[1].trim().split("/");
        dateMonthYear.setText(strDate[0] + " " + strDate[2]);
        dateNo.setText(strDate[1]);
    }

    //Function for validating Items
    private boolean validateDetails() {

        return true;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if(resultCode == RESULT_OK) {
                getDate = data.getStringExtra("DateValue");
                dateTextSetter(getDate);

                //Calling function for updating servicing values

                if(databaseHandler != null) {
                    databaseHandler.updateCustomerCount(getDate, freeS, paidS, runningS);
                }
            }
        }
    }

    private View.OnClickListener onClickCalendarActivity() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CustomerRegisterActivity.this, calendarActivity.class);
                startActivityForResult(intent,1);
            }
        };
    }

    private void initialization() {
        toolbar = findViewById(R.id.Toolbar);

        customerNameLayout = findViewById(R.id.customerNameLayout);
        customerName = findViewById(R.id.customerName);

        customerMobNoLayout = findViewById(R.id.customerMobileNumberLayout);
        customerMobNo = findViewById(R.id.customerMobileNumber);

        customerVehNoLayout = findViewById(R.id.customerVehicleNumberLayout);
        customerVehNo = findViewById(R.id.customerVehicleNumber);

        dateNo = findViewById(R.id.customerDate);
        dateMonthYear = findViewById(R.id.customerDateMonthYear);
        dateDay = findViewById(R.id.customerDateDay);
        customerDateLayout = findViewById(R.id.customerDateLayout);

        paidS = findViewById(R.id.paidService);
        freeS = findViewById(R.id.freeService);
        runningS = findViewById(R.id.runningService);

        registerCustomerButton = findViewById(R.id.customerDoneButton);
        customerServiceType = findViewById(R.id.customerServiceType);

        databaseHandler = null;
        try {
            databaseHandler = new databaseHandler(getApplicationContext());
        } catch (IOException e) {
            e.printStackTrace();
        }

        //### Initializing and setting adapter for dropdown for service type
        customerServiceTypeAdapter = ArrayAdapter.createFromResource(
                this,
                R.array.service_type_drop_down,
                android.R.layout.simple_spinner_dropdown_item);
        customerServiceTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        customerServiceType.setAdapter(customerServiceTypeAdapter);

        //### Initialize Date
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEE, MMM/dd/yyyy");
        Date dateObject = Calendar.getInstance().getTime();
        getDate = simpleDateFormat.format(dateObject);
        dateTextSetter(getDate);

    }
}