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
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.freecbdhomebiz.pizzapizepartz.data.PizzaContract;
import com.freecbdhomebiz.pizzapizepartz.data.PizzaContract.PizzaEntry;
import com.freecbdhomebiz.pizzapizepartz.data.PizzaDbHelper;

/**
 * Displays list of pizza ingredients that were entered and stored in the app database.
 */
public class MainActivity extends AppCompatActivity {

    private static final String LOG_TAG = "MainActivity ";

    private TextView infoView;
    private Button addButton;

    /**
     * Database helper that will provide us access to the database
     */
    private PizzaDbHelper mDbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        infoView = findViewById(R.id.textviewMA);

        addButton = findViewById(R.id.add_button);
        addButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, EditorActivity.class);
                startActivity(intent);
            }
        });

        displayDatabaseInfo();

        // To access the database, create an instance of PizzaDbHelper
        mDbHelper = new PizzaDbHelper(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        displayDatabaseInfo();
    }


    /**
     * Helper method to display info to the main screen
     */
    private void displayDatabaseInfo() {
        // To access our database, we instantiate our subclass of SQLiteOpenHelper
        // and pass the context, which is the current activity.
        PizzaDbHelper mDbHelper = new PizzaDbHelper(MainActivity.this);

        // Create and/or open a database to read from it
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        //Define projection of columns to use in query
        String[] projection = {
                PizzaEntry._ID,
                PizzaEntry.COLUMN_INGREDIENT_NAME,
                PizzaEntry.COLUMN_INGREDIENT_PRICE,
                PizzaEntry.COLUMN_INGREDIENT_QUANTITY,
                PizzaEntry.COLUMN_INGREDIENT_SUPPLIER,
                PizzaEntry.COLUMN_SUPPLIER_PHONE
        };

        // Perform SQL query to get a Cursor that contains specified rows from the pets
        // table.
        Cursor cursor = db.query(PizzaContract.PizzaEntry.TABLE_NAME,
                                 projection,
                                 null,
                                 null,
                                 null,
                                 null,
                                 null);


        try {
            // Create a table header in the textView:
            // _id - Name - Price - Quantity - Supplier - Phone Number
            // The pizzapartz table contains cursor.getCount (number of rows) of ingredients.

            //Build a message to show the number of ingredients (rows) in the table
            String info = getString(R.string.mainactivity_info_1) + cursor.getCount() + getString
                    (R.string.mainactivity_info2);
            infoView.setText(info);

            infoView.append(PizzaEntry._ID + " - " +
                                    PizzaEntry.COLUMN_INGREDIENT_NAME + " - " +
                                    PizzaEntry.COLUMN_INGREDIENT_PRICE + " - " +
                                    PizzaEntry.COLUMN_INGREDIENT_QUANTITY + " - " +
                                    PizzaEntry.COLUMN_INGREDIENT_SUPPLIER + " - " +
                                    PizzaEntry.COLUMN_SUPPLIER_PHONE + "\n");

            // Get the index of each column in the table
            int idColumnIndex = cursor.getColumnIndex(PizzaEntry._ID);
            int nameColumnIndex = cursor.getColumnIndex(PizzaEntry.COLUMN_INGREDIENT_NAME);
            int priceColumnIndex = cursor.getColumnIndex(PizzaEntry.COLUMN_INGREDIENT_PRICE);
            int quantityColumnIndex = cursor.getColumnIndex(PizzaEntry.COLUMN_INGREDIENT_QUANTITY);
            int supplierColumnIndex = cursor.getColumnIndex(PizzaEntry.COLUMN_INGREDIENT_SUPPLIER);
            int phoneColumnIndex = cursor.getColumnIndex(PizzaEntry.COLUMN_SUPPLIER_PHONE);

            // Display the data in the same order as the header
            // Iterate through all the returned rows in the Cursor
            while (cursor.moveToNext()) {
                int currentID = cursor.getInt(idColumnIndex);
                String currentName = cursor.getString(nameColumnIndex);
                int currentPrice = cursor.getInt(priceColumnIndex);
                int currentQuantity = cursor.getInt(quantityColumnIndex);
                String currentSupplier = cursor.getString(supplierColumnIndex);
                int currentPhone = cursor.getInt(phoneColumnIndex);

                // Create the display of the values from each column of the current row of the
                // cursor in the TextView
                infoView.append(("\n" + currentID + " - " +
                        currentName + " - " +
                        currentPrice + " - " +
                        currentQuantity + " - " +
                        currentSupplier + " - " +
                        currentPhone));
            }
        } finally {
            cursor.close();
        }
    }

    /**
     * Helper method to insert dummy ingredient data into the database for testing/debugging.
     */
    private void insertDummy() {
        // Gets the database in write mode
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        // Declare dummy data
        String dummyName = getString(R.string.Anchovies);
        int dummyPrice = 6;
        int dummyQuantity = 13;
        String dummySupplier = getString(R.string.YummyGoodStuff);
        int dummyPhone = 9876543;

        // Create a ContentValues object where column names are the keys,
        // and dummy data are the values.
        ContentValues values = new ContentValues();
        values.put(PizzaEntry.COLUMN_INGREDIENT_NAME, dummyName);
        values.put(PizzaEntry.COLUMN_INGREDIENT_PRICE, dummyPrice);
        values.put(PizzaEntry.COLUMN_INGREDIENT_QUANTITY, dummyQuantity);
        values.put(PizzaEntry.COLUMN_INGREDIENT_SUPPLIER, dummySupplier);
        values.put(PizzaEntry.COLUMN_SUPPLIER_PHONE, dummyPhone);


        // Insert a new row for Anchovies in the database, returning the ID of that new row.
        // The second argument is null so that the framework will not insert a row when there are
        // no values.
        // The third argument is the ContentValues object containing the all info data for
        // Anchovies.
        Log.i(LOG_TAG, "The insertdummy string= " + values);

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
        // Inflate the menu options from the res/menu/menu_main.xml file.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option
        switch (item.getItemId()) {
            // Respond to a click on the "Insert dummy data" menu option
            case R.id.menu_insert_dummy_data:
                insertDummy();
                displayDatabaseInfo();
                return true;
            // Other options/cases can be added to the menu_main here
        }
        return super.onOptionsItemSelected(item);
    }
}
