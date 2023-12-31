package com.cms.canteen.foodmanagementapp.Common;

import com.cms.canteen.foodmanagementapp.Model.User;

import java.text.NumberFormat;
import java.util.Locale;

public class Common {
    public static User currentUser;
    public static String addDefaultCurrency(int number) {
        Locale locale = new Locale("en","IN");
        NumberFormat fmt = NumberFormat.getCurrencyInstance(locale);
        return fmt.format(number);
    }
}
