package com.example.prince_tvs;

import android.app.DatePickerDialog;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.example.prince_tvs.Database.databaseHandler;
import com.example.prince_tvs.Model.blockTime;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class BlockTimeActivity extends AppCompatActivity {
    private EditText blockPaidService;
    private EditText blockFreeService;
    private EditText blockRunningService;
    private EditText blockDateSelection;
    private Toolbar toolbar;

    private String strDate;
    private Button blockDone;

    private databaseHandler databaseHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_block_time);

        initialization();
        setSupportActionBar(toolbar);

        blockDateSelection.setOnClickListener(datePicker());

        blockDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(verify_blockValues()) {
                    blockTime blockTime = new blockTime();
                    blockTime.setFreeService(Integer.valueOf(blockFreeService.getText().toString()));
                    blockTime.setPaidService(Integer.valueOf(blockPaidService.getText().toString()));
                    blockTime.setRunningService(Integer.valueOf(blockRunningService.getText().toString()));
                    blockTime.setDate(strDate);

                    if(databaseHandler != null) {
                        databaseHandler.writeBlockTime(blockTime);
                    }
                }
                else {
                    Toast.makeText(getApplicationContext(),"Invalid Value",Toast.LENGTH_SHORT).show();
                }
            }
        });

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private View.OnClickListener datePicker() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    final Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
                    int mYear = calendar.get(Calendar.YEAR);
                    int mMonth = calendar.get(Calendar.MONTH);
                    int mDay = calendar.get(Calendar.DAY_OF_MONTH);
                    DatePickerDialog datePickerDialog = new DatePickerDialog(
                            BlockTimeActivity.this, new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                            strDate = String.valueOf(dayOfMonth) + "/" + String.valueOf((month+1) % 12) + "/" + String.valueOf(year);


                            try {
                                Date date = formatter.parse(strDate);
                                strDate = formatter.format(date);
                                blockDateSelection.setText(strDate);

                                String[] dates = strDate.split("/");
                                strDate = dates[0] + "_" + dates[1] + "_" + dates[2];


                            } catch (ParseException e) {
                                e.printStackTrace();
                            }

                        }
                    }, mYear, mMonth, mDay);

                    datePickerDialog.show();
                }

            }
        };
    }

    private boolean validateInteger(int value) {
        if(value <= 0 || value >= 60) {
            return false;
        }
        return true;
    }

    private boolean verify_blockValues() {
        String paidText = blockPaidService.getText().toString();
        String freeText = blockFreeService.getText().toString();
        String runningText = blockRunningService.getText().toString();
        String dateText = blockDateSelection.getText().toString();
        if(paidText.isEmpty() || freeText.isEmpty() || runningText.isEmpty() || dateText.isEmpty()) {
            return false;
        }

        int paidValue = Integer.valueOf(paidText);
        int freeValue = Integer.valueOf(freeText);
        int runningValue = Integer.valueOf(runningText);

        if(validateInteger(paidValue) && validateInteger(freeValue) && validateInteger(runningValue)) {
            return true;
        }
        return false;
    }

    private void initialization() {
        toolbar = findViewById(R.id.Toolbar);
        toolbar.setTitle("Add Block Amount");
        toolbar.setNavigationIcon(R.drawable.ic_back_button);

        blockPaidService = findViewById(R.id.blockPaidService);
        blockFreeService = findViewById(R.id.blockFreeService);
        blockRunningService = findViewById(R.id.blockRunningService);
        blockDateSelection = findViewById(R.id.blockDateSelection);
        blockDone = findViewById(R.id.blockDone);

        databaseHandler = null;
        try {
            databaseHandler = new databaseHandler(getApplicationContext());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}