package com.cms.canteen.foodmanagementapp.Model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Request {
    private String phone;
    private String name;
    private String address;
    private String total;
    private List<Order> foods;
    //list


    private String Status;

    public static String STATUS_PREPARING = "1-Preparing";
    public static String STATUS_READY = "2-Ready";
    public static String STATUS_COMPLETED = "3-Completed";
    public static String STATUS_CANCELLED = "4-Cancelled";
    public static List<String> STATUS_LIST = new ArrayList<String>(Arrays.asList(STATUS_PREPARING, STATUS_READY, STATUS_COMPLETED, STATUS_CANCELLED));

    public Request(){

    }

    public Request(String phone, String name, String address, String total, List<Order> foods) {
        this.phone = phone;
        this.name = name;
        this.address = address;
        this.total = total;
        this.foods = foods;
        Status = STATUS_PREPARING;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public List<Order> getFoods() {
        return foods;
    }

    public void setFoods(List<Order> foods) {
        this.foods = foods;
    }


    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }
}
