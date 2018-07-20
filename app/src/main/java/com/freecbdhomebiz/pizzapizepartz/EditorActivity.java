/*
 * Copyright (c) 2018  Tina Taylor.
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the
  * GNU General Public License as published by the Free Software Foundation, either version 3 of
  * the License, or any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See
  * the GNU General Public License for more details. https://www.gnu.org/licenses/gpl-3.0.en.html
 */

package com.freecbdhomebiz.pizzapizepartz;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.freecbdhomebiz.pizzapizepartz.data.PizzaContract.PizzaEntry;
import com.freecbdhomebiz.pizzapizepartz.data.PizzaDbHelper;

/**
 * Allows user to create a new ingredient or edit an existing one.
 */
public class EditorActivity extends AppCompatActivity {

    private static final String LOG_TAG = "EditorActivity ";

    /**
     * EditText field to enter the ingredient name
     */
    private EditText mNameEditText;

    /**
     * EditText field to enter the ingredient price
     */
    private EditText mPriceEditText;

    /**
     * EditText field to enter the ingredient quantity
     */
    private EditText mQuantityEditText;

    /**
     * EditText field to enter the supplier's name
     */
    private EditText mSupplierEditText;

    /**
     * EditText field to enter the ingredient quantity
     */
    private EditText mPhoneEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        // Find the views that we will read user input from
        mNameEditText = findViewById(R.id.edit_name);
        mPriceEditText = findViewById(R.id.edit_price);
        mQuantityEditText = findViewById(R.id.edit_quantity);
        mSupplierEditText = findViewById(R.id.edit_supplier);
        mPhoneEditText = findViewById(R.id.edit_phone);
    }

    /**
     * Get user input from editor and save new ingredient into database.
     */
    private void insertIngredient() {
        // Read from user input fields
        String nameString = mNameEditText.getText().toString().trim();
        String priceString = mPriceEditText.getText().toString().trim();
        String quantityString = mQuantityEditText.getText().toString().trim();
        String supplierString = mSupplierEditText.getText().toString().trim();
        String phoneString = mPhoneEditText.getText().toString().trim();

        // Create database helper
        PizzaDbHelper mDbHelper = new PizzaDbHelper(this);

        // Gets the database in write mode
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        // Create a ContentValues object where column names are the keys,
        // and pet attributes from the editor are the values.
        ContentValues values = new ContentValues();
        values.put(PizzaEntry.COLUMN_INGREDIENT_NAME, nameString);
        values.put(PizzaEntry.COLUMN_INGREDIENT_PRICE, priceString);
        values.put(PizzaEntry.COLUMN_INGREDIENT_QUANTITY, quantityString);
        values.put(PizzaEntry.COLUMN_INGREDIENT_SUPPLIER, supplierString);
        values.put(PizzaEntry.COLUMN_SUPPLIER_PHONE, phoneString);

        Log.i(LOG_TAG, "The insertIngredient string= " + values);


        // Insert a new row for pet in the database, returning the ID of that new row.
        long newRowId = db.insert(PizzaEntry.TABLE_NAME, null, values);

        // Show a toast message depending on whether or not the insertion was successful
        if (newRowId == -1) {
            // If the row ID is -1, then there was an error with insertion.
            Toast.makeText(this, "Error with saving ingredient", Toast.LENGTH_SHORT).show();
        } else {
            // Otherwise, the insertion was successful and we can display a toast with the row ID.
            Toast.makeText(this, "Ingredient saved with row id: " + newRowId, Toast.LENGTH_SHORT)
                    .show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_editor.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.menu_editor, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            // Respond to a click on the "Save" menu option
            case R.id.menu_editor_save:
                // Save pet to database
                insertIngredient();
                // Exit activity
                finish();
                return true;
            // More options to come
        }
        return super.onOptionsItemSelected(item);
    }
}