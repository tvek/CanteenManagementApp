package com.cms.canteen.foodmanagementapp.Database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;

import com.cms.canteen.foodmanagementapp.Model.Order;
import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

import java.util.ArrayList;
import java.util.List;

public class Database extends SQLiteAssetHelper {

    private static final String DB_NAME = "Tomato.db";
    private static final int DB_VER=1;

    public Database(Context context) {
        super(context, DB_NAME, null, DB_VER);
    }

    public List<Order> getCarts()
    {
        SQLiteDatabase db = getReadableDatabase();
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

        String[] sqlSelect = {"ProductName","ProductId","Quantity","Price","Discount"};
        String sqlTable = "OrderDetail";

        qb.setTables(sqlTable);
        Cursor c = qb.query(db,sqlSelect,null,null,null,null,null);

        final List<Order> result = new ArrayList<>();
        if(c.moveToFirst())
        {
            do {
                int cursorProductId = c.getColumnIndex("ProductId");
                int cursorProductName = c.getColumnIndex("ProductName");
                int cursorQuantity = c.getColumnIndex("Quantity");
                int cursorPrice = c.getColumnIndex("Price");
                int cursorDiscount = c.getColumnIndex("Discount");
                if ((cursorProductId != -1)) {
                    result.add(new Order(c.getString(cursorProductId),
                            c.getString(cursorProductName),
                            c.getString(cursorQuantity),
                            c.getString(cursorPrice),
                            c.getString(cursorDiscount)
                    ));
                }

            }while (c.moveToNext());
        }
        return result;
    }

    public void addToCart(Order order)
    {
        SQLiteDatabase db = getReadableDatabase();
        String query = String.format("INSERT INTO OrderDetail(ProductId,ProductName,Quantity,Price,Discount) VALUES ('%s','%s','%s','%s','%s');",
        order.getProductId(),
                order.getProductName(),
                order.getQuantity(),
                order.getPrice(),
                order.getDiscount());
        db.execSQL(query);
    }

    public void removeFromCart(Order order){
        SQLiteDatabase db = getReadableDatabase();
        String query = String.format("DELETE FROM OrderDetail WHERE ProductId = \'%s\';",
                order.getProductId());
        db.execSQL(query);
    }

    public void cleanCart()
    {
        SQLiteDatabase db = getReadableDatabase();
        String query = "DELETE FROM OrderDetail";
        db.execSQL(query);
    }
}
