package com.example.prince_tvs.Model;

import java.util.HashMap;
import java.util.Map;

public class CustomerModel {
    String name;
    String mobileNumber;
    String vehicleNumber;
    String date;
    int serviceType;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getVehicleNumber() {
        return vehicleNumber;
    }

    public void setVehicleNumber(String vehicleNumber) {
        this.vehicleNumber = vehicleNumber;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getServiceType() {
        return serviceType;
    }

    public void setServiceType(int serviceType) {
        this.serviceType = serviceType;
    }

    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("name", name);
        result.put("mobileNumber", mobileNumber);
        result.put("date", date);
        result.put("serviceType", serviceType);
        result.put("vehicleNumber", vehicleNumber);

        return result;

    }
}
