package com.cms.canteen.foodmanagementapp.Model;

public class User {
    private String Name;
    private String Password;
    private String Phone;
    private String UserType;

    public static String DEFAULT_USER_TYPE = "general_user";


    public User(){

    }

    public User(String name, String password) {
        Name = name;
        Password = password;
        UserType = DEFAULT_USER_TYPE;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public String getName() {
        return Name;
    }
    public void setName(String name)
    {
        Name = name;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password){
        Password = password;
    }

    public String getUsertype() {
        return UserType;
    }

    public void setUsertype(String userType) {
        UserType = userType;
    }
}
