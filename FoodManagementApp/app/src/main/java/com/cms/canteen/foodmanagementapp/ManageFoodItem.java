package com.cms.canteen.foodmanagementapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.cms.canteen.foodmanagementapp.Model.Category;
import com.cms.canteen.foodmanagementapp.Model.Food;
import com.cms.canteen.foodmanagementapp.Model.Request;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ManageFoodItem extends AppCompatActivity {

    TextInputLayout foodNameLayout, foodDescriptionLayout, foodPriceLayout, foodDiscountLayout, foodImageLayout;
    Button btnAdd;
    Spinner foodCategoryIdSpinner;
    private FirebaseDatabase database;
    private DatabaseReference foodList;
    private DatabaseReference category;
    private final Map<String, String> categoryList = new HashMap<String, String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_food_item);

        foodNameLayout = findViewById(R.id.food_name_layout);
        foodDescriptionLayout = findViewById(R.id.food_description_layout);
        foodPriceLayout = findViewById(R.id.food_price_layout);
        foodDiscountLayout = findViewById(R.id.food_discount_layout);
        foodImageLayout = findViewById(R.id.food_image_layout);
        btnAdd = findViewById(R.id.btn_add_food);
        foodCategoryIdSpinner = findViewById(R.id.food_category_id_spinner);

        // Setup Database
        database = FirebaseDatabase.getInstance();
        foodList = database.getReference("Foods");
        category = database.getReference("Category");

        // Setup Click Listeners
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateFoodItem();
            }
        });

        // Setup Spinner
        category.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Category currentCategory = snapshot.getValue(Category.class);
                    categoryList.put(currentCategory.getName(), snapshot.getKey());
                }

                Set<String> keyList = categoryList.keySet();
                String[] keyArrayList = keyList.toArray(new String[keyList.size()]);
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(ManageFoodItem.this,
                                android.R.layout.simple_spinner_dropdown_item, keyArrayList);
                foodCategoryIdSpinner.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void updateFoodItem() {
        String foodName = foodNameLayout.getEditText().getText().toString();
        String foodDescription = foodDescriptionLayout.getEditText().getText().toString();
        String foodPrice = foodPriceLayout.getEditText().getText().toString();
        String foodDiscount = foodDiscountLayout.getEditText().getText().toString();
        String foodImage = foodImageLayout.getEditText().getText().toString();
        String foodCategoryId = categoryList.get(foodCategoryIdSpinner.getSelectedItem().toString());
        Food foodData = new Food(foodName, foodImage, foodDescription, foodPrice, foodDiscount, foodCategoryId);
        DatabaseReference newDbRef = foodList.push();
        newDbRef.setValue(foodData, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                Toast.makeText(ManageFoodItem.this, "Food Item is added successfully", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }
}