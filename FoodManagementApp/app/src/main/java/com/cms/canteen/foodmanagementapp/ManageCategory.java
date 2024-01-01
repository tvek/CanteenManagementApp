package com.cms.canteen.foodmanagementapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.cms.canteen.foodmanagementapp.Model.Category;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ManageCategory extends AppCompatActivity {

    private FirebaseDatabase database;
    private DatabaseReference category;
    private TextInputLayout categoryNameView, categoryUrlView;
    private Button btnAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_category);

        categoryNameView = findViewById(R.id.category_name_layout);
        categoryUrlView = findViewById(R.id.category_url_layout);
        btnAdd = findViewById(R.id.btn_add);

        database = FirebaseDatabase.getInstance();
        category = database.getReference("Category");

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateCategory();
            }
        });
    }

    private void updateCategory() {
        Category categoryData = new Category(categoryNameView.getEditText().getText().toString(), categoryUrlView.getEditText().getText().toString());
        DatabaseReference newDbRef = category.push();
        newDbRef.setValue(categoryData, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                Toast.makeText(ManageCategory.this, "Category is added successfully.", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }
}