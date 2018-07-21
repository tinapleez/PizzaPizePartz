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
//Parts of this app are taken from my Udacity Android Basics Nanodegree Pets classsroom practice
// app which is copyrighted to Udacity.

package com.freecbdhomebiz.pizzapizepartz;

import android.content.ContentValues;
import android.content.Intent;
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

    /**
     * Becomes true when MainActivity menu option is "Insert dummy data" is clicked
     */
    private boolean insertTheDummy = false;

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

        // Passes boolean (true) from MainActivity menu option clicked, and starts the write data
        // to database process
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            insertTheDummy = extras.getBoolean("insertDummyData");
            insertIngredient();
        }
    }

    /**
     * Get user input from editor, OR, get dummy data, and save new ingredient into database.
     */
    private void insertIngredient() {
        String nameString;
        String priceString;
        String quantityString;
        String supplierString;
        String phoneString;

        // If the Intent from the MainActivity to insert dummy data was not called then get
        // the data from the EdtText fields
        if (!insertTheDummy) {
            // Read from user input fields
            nameString = mNameEditText.getText().toString().trim();
            priceString = mPriceEditText.getText().toString().trim();
            quantityString = mQuantityEditText.getText().toString().trim();
            supplierString = mSupplierEditText.getText().toString().trim();
            phoneString = mPhoneEditText.getText().toString().trim();
        } else {
            //Use dummy data from receiving Intent with Extras from the menu
            // switch in MainActivity
            nameString = getString(R.string.Anchovies);
            priceString = getString(R.string.dummyPrice);
            quantityString = getString(R.string.dummyQuantity);
            supplierString = getString(R.string.dummySupplier);
            phoneString = getString(R.string.dummyPhone);
            // Reset the insert dummy data boolean
            insertTheDummy = false;
        }
        // Create database helper
        PizzaDbHelper mDbHelper = new PizzaDbHelper(this);

        // Gets the database in write mode
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        // Create a ContentValues object where column names are the keys,
        // and ingredient attributes from the editor are the values.
        ContentValues values = new ContentValues();
        values.put(PizzaEntry.COLUMN_INGREDIENT_NAME, nameString);
        values.put(PizzaEntry.COLUMN_INGREDIENT_PRICE, priceString);
        values.put(PizzaEntry.COLUMN_INGREDIENT_QUANTITY, quantityString);
        values.put(PizzaEntry.COLUMN_INGREDIENT_SUPPLIER, supplierString);
        values.put(PizzaEntry.COLUMN_SUPPLIER_PHONE, phoneString);

        Log.i(LOG_TAG, "The insertIngredient string= " + values);

        // Insert a new row for an ingredient in the database, returning the ID of that new row.
        long newRowId = db.insert(PizzaEntry.TABLE_NAME, null, values);

        // Show a toast message depending on whether or not the insertion was successful
        if (newRowId == -1) {
            // If the row ID is -1, then there was an error with insertion.
            Toast.makeText(this, R.string.Toast_Editor_Activity_Error_Saving, Toast
                    .LENGTH_SHORT).show();
        } else {
            // Otherwise, the insertion was successful and we can display a toast with the row ID.
            Toast.makeText(getApplicationContext(), getString(R.string.Toast_EditorActivity_newRowId) +
                                   newRowId,
                           Toast.LENGTH_SHORT)
                    .show();
        }
        Intent i = new Intent(EditorActivity.this, MainActivity.class);
        startActivity(i);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_editor, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // User clicked the "Save" menu option
            case R.id.menu_editor_save:
                // Write new ingredient to database
                insertIngredient();
                return true;
            // More menu options to come
        }
        return super.onOptionsItemSelected(item);
    }
}