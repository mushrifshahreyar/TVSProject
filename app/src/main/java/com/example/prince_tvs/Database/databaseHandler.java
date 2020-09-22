package com.example.prince_tvs.Database;

import android.content.Context;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.prince_tvs.Model.CustomerModel;
import com.example.prince_tvs.Model.blockTime;
import com.google.android.gms.tasks.OnSuccessListener;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class databaseHandler {
    private final String customerDatabase = "customerDetails";
    private final String blockDatabase = "blockServiceAmount";
    private Context context;
    private FirebaseFirestore firestoreDB = FirebaseFirestore.getInstance();

    public databaseHandler(Context context) throws IOException {
        this.context = context;
    }

    public void writeCustomerData(final CustomerModel customerModel) {
        String documentID = customerModel.getName().toLowerCase()+"-"+customerModel.getMobileNumber().toLowerCase();
        firestoreDB.collection(customerDatabase)
                .document(documentID)
                .set(customerModel.toMap()).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Toast.makeText(context,"Successfully Uploaded",Toast.LENGTH_SHORT).show();
        }
        });
    }

    public void writeBlockTime(blockTime blockTime) {
        firestoreDB.collection(blockDatabase)
                .document(blockTime.getDate())
                .set(blockTime.toMap())
                .addOnSuccessListener(new OnSuccessListener<Void>() {

            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(context,"Successfully Uploaded",Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void updateCustomerCount(String date, final TextView freeS, final TextView paidS, final TextView runningS) {

        updateBlockTime(date,freeS,paidS,runningS);
        firestoreDB.collection(customerDatabase).whereEqualTo("date",date).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                int[] count = new int[3];
                List<CustomerModel> customerModels;
                if(!queryDocumentSnapshots.isEmpty()) {
                    customerModels = queryDocumentSnapshots.toObjects(CustomerModel.class);
                    for(CustomerModel customerModel: customerModels) {
                        switch (customerModel.getServiceType()) {
                            case 0: count[0]++;
                                break;
                            case 1: count[1]++;
                                break;
                            case 2: count[2]++;
                                break;
                        }
                    }
                }

                String[] arr_freeS = freeS.getText().toString().split("/");
                String[] arr_paidS = paidS.getText().toString().split("/");
                String[] arr_runningS = runningS.getText().toString().split("/");

                freeS.setText(String.valueOf(count[0]) + " / " + arr_freeS[1]);
                paidS.setText(String.valueOf(count[1]) + " / " + arr_paidS[1]);
                runningS.setText(String.valueOf(count[2]) + " / " + arr_runningS[1]);
            }
        });
    }

    public void updateCustomerCount(String date, final TextView freeS, final TextView paidS, final TextView runningS, final List<DocumentSnapshot> documents) {


        updateBlockTime(date,freeS,paidS,runningS);

        firestoreDB.collection(customerDatabase).whereEqualTo("date",date).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                int[] count = new int[3];
                List<CustomerModel> customerModels;
                if(!queryDocumentSnapshots.isEmpty()) {
                    customerModels = queryDocumentSnapshots.toObjects(CustomerModel.class);
                    for(CustomerModel customerModel: customerModels) {
                        switch (customerModel.getServiceType()) {
                            case 0: count[0]++;
                                break;
                            case 1: count[1]++;
                                break;
                            case 2: count[2]++;
                                break;
                        }
                    }
                }

                String[] arr_freeS = freeS.getText().toString().split("/");
                String[] arr_paidS = paidS.getText().toString().split("/");
                String[] arr_runningS = runningS.getText().toString().split("/");

                freeS.setText(String.valueOf(count[0]) + " / " + arr_freeS[1]);
                paidS.setText(String.valueOf(count[1]) + " / " + arr_paidS[1]);
                runningS.setText(String.valueOf(count[2]) + " / " + arr_runningS[1]);
            }
        });
    }

    private void updateBlockTime(String strDate, final TextView freeS, final TextView paidS, final TextView runningS) {
        SimpleDateFormat formatter = new SimpleDateFormat("EEE, MMM/dd/yyyy");
        final int[] totalServiceAmount = new int[3];
        try {
            Date date = formatter.parse(strDate);
            formatter = new SimpleDateFormat("dd/MM/yyyy");
            String temp = formatter.format(date);

            String[] dates = temp.split("/");

            String docDate = dates[0] + "_" + dates[1] + "_" + dates[2];

            firestoreDB.collection(blockDatabase).document(docDate).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {

                    if(documentSnapshot.exists()) {
                        blockTime blockTime = documentSnapshot.toObject(blockTime.class);
                        totalServiceAmount[0] = blockTime.getFreeService();
                        totalServiceAmount[1] = blockTime.getPaidService();
                        totalServiceAmount[2] = blockTime.getRunningService();

                    }
                    else {
                        totalServiceAmount[0] = 30;
                        totalServiceAmount[1] = 30;
                        totalServiceAmount[2] = 2;
                    }

                    String[] arr_freeS = freeS.getText().toString().split("/");
                    String[] arr_paidS = paidS.getText().toString().split("/");
                    String[] arr_runningS = runningS.getText().toString().split("/");
                    freeS.setText(arr_freeS[0] + " / " + String.valueOf(totalServiceAmount[0]));
                    paidS.setText(arr_paidS[0] + " / " + String.valueOf(totalServiceAmount[1]));
                    runningS.setText(arr_runningS[0] + " / " + String.valueOf(totalServiceAmount[2]));

                }
            });

        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
    public void onUpdateBlockListener(final String date, final TextView freeS, final TextView paidS, final TextView runningS) {
        firestoreDB.collection(blockDatabase).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                List<DocumentSnapshot> documentSnapshots= queryDocumentSnapshots.getDocuments();
                updateBlockTime(date,freeS,paidS,runningS);
            }
        });

    }
    public void onUpdateCustomerListener(final String date, final TextView freeS, final TextView paidS, final TextView runningS) {
        firestoreDB.collection(customerDatabase).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                List<DocumentSnapshot> documentSnapshots= queryDocumentSnapshots.getDocuments();
                updateCustomerCount(date,freeS,paidS,runningS,documentSnapshots);
            }
        });
    }
}
