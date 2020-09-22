package com.example.prince_tvs;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.example.prince_tvs.Database.databaseHandler;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class calendarActivity extends AppCompatActivity {
    private CalendarView calendarView;
    private TextView paidService;
    private TextView freeService;
    private TextView runningService;
    private Date date;
    private String strDate;
    private Button button;
    private Intent intent = new Intent();
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
        initialization();

        setSupportActionBar(toolbar);

        date = Calendar.getInstance().getTime();
        final SimpleDateFormat formatter = new SimpleDateFormat("EEE, MMM/dd/yyyy");
        strDate = formatter.format(date);

        updateCustomerCount(getApplicationContext());
        intent.putExtra("DateValue",strDate);
        calendarView.setMinDate(Calendar.getInstance().getTimeInMillis());

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_CANCELED,intent);
                finish();
            }
        });

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                String temp = String.valueOf(month + 1) + '/' + String.valueOf(dayOfMonth) + '/' + String.valueOf(year);

                try {
                    date = new SimpleDateFormat("MM/dd/yyyy").parse(temp);
                    strDate = formatter.format(date);
                    intent.putExtra("DateValue",strDate);

                    updateCustomerCount(getApplicationContext());

                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_OK,intent);
                finish();
            }
        });

    }

    public void updateCustomerCount(Context context) {
        databaseHandler databaseHandler = null;
        try {

            databaseHandler = new databaseHandler(context);
            databaseHandler.updateCustomerCount(strDate,freeService,paidService,runningService);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        intent.putExtra("DateValue", strDate);
        setResult(RESULT_OK, intent);
        finish();
    }

    private void initialization() {
        toolbar = findViewById(R.id.Toolbar);
        toolbar.setTitle("Calendar");
        toolbar.setNavigationIcon(R.drawable.ic_back_button);
        setSupportActionBar(toolbar);

        calendarView = findViewById(R.id.calendarView);
        paidService = findViewById(R.id.calendarPaidService);
        freeService = findViewById(R.id.calendarFreeService);
        runningService = findViewById(R.id.calendarRunningService);
        button = findViewById(R.id.calendarDoneButton);
    }
}