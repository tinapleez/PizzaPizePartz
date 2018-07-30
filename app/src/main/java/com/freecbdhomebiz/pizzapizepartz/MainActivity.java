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

import android.app.AlertDialog;
import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.freecbdhomebiz.pizzapizepartz.data.PizzaContract.PizzaEntry;

/**
 * Displays list of pizza ingredients that were entered and stored in the app database.
 */
public class MainActivity extends AppCompatActivity implements LoaderManager
        .LoaderCallbacks<Cursor> {

    /**
     * Identifier for the pizza data loader
     */
    private static final int PIZZA_LOADER = 0;

    /**
     * Adapter for the ListView
     */
    PizzaCursorAdapter mCursorAdapter;

    TextView quantityTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button addButton = findViewById(R.id.add_button);
        addButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, EditorActivity.class);
                startActivity(intent);
            }
        });

        quantityTextView = findViewById(R.id.the_quantity);

        // Find the ListView which will be populated with the pizza ingredient data
        ListView pizzaListView = findViewById(R.id.list);

        // Find and set empty view on the ListView, so that it only shows when the list has 0 items.
        View emptyView = findViewById(R.id.empty_view);
        pizzaListView.setEmptyView(emptyView);

        // Setup an Adapter to create a list item for each row of ingredient data in the Cursor.
        // There is no ingredient data yet (until the loader finishes) so pass in null for the
        // Cursor.
        mCursorAdapter = new PizzaCursorAdapter(this, null);
        pizzaListView.setAdapter(mCursorAdapter);

        // Setup the item click listener
        pizzaListView.setOnItemClickListener(new AdapterView.OnItemClickListener()

        {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                // Create new intent to go to {@link EditorActivity}
                Intent intent = new Intent(MainActivity.this, EditorActivity.class);

                // Form the content URI that represents the specific ingredient that was clicked on,
                // by appending the "id" (passed as input to this method) onto the
                // {@link PizzaEntry#CONTENT_URI}. For example, the URI would be
                // "content://com.freecbdhomebiz.pizzapizepartz/pizzas/2"
                // if the ingredient with ID 2 was clicked on.
                Uri currentPizzaUri = ContentUris.withAppendedId(PizzaEntry.CONTENT_URI, id);

                // Set the URI on the data field of the intent
                intent.setData(currentPizzaUri);

                // Launch the {@link EditorActivity} to display the data for the current ingredient.
                startActivity(intent);
            }
        });

        // Kick off the loader
        getLoaderManager().

                initLoader(PIZZA_LOADER, null, this);
    }

    /**
     * Sale button decreases the quantity by 1
     * Called from PizzaCursorAdapter
     *
     * @param id       is the current ingredient id
     * @param quantity is the current quantity
     */
    public void onClickSale(long id, int quantity) {
        Uri currentPizzaUri = ContentUris.withAppendedId(PizzaEntry.CONTENT_URI, id);
        Log.v("MainActivity", "Uri: " + currentPizzaUri);
        if (quantity > 0) {
            quantity--;
            ContentValues values = new ContentValues();
            values.put(PizzaEntry.COLUMN_INGREDIENT_QUANTITY, quantity);
            int rowsAffected = getContentResolver().update(currentPizzaUri, values, null, null);
        } else {
            Toast toast = Toast.makeText(this, R.string.mainactivity_toast_quantity, Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }
    }

    /**
     * Helper method to insert hardcoded ingredient data into the database. For debugging purposes
     * and for fun.
     */
    private void insertPizza() {
        // Create a ContentValues object where column names are the keys,
        // and Anchovies attributes are the values.
        ContentValues values = new ContentValues();
        values.put(PizzaEntry.COLUMN_INGREDIENT_NAME, "Anchovies");
        values.put(PizzaEntry.COLUMN_INGREDIENT_PRICE, 5);
        values.put(PizzaEntry.COLUMN_INGREDIENT_QUANTITY, 7);
        values.put(PizzaEntry.COLUMN_INGREDIENT_SUPPLIER, "Supplier");
        values.put(PizzaEntry.COLUMN_SUPPLIER_PHONE, 5555555);

        // Insert a new row for Anchovies into the provider using the ContentResolver.
        // Use the {@link PizzaEntry#CONTENT_URI} to indicate that we want to insert
        // into the pizzapartz database table.
        // Receive the new content URI that will allow us to access Anchovies data in the future.
        Uri newUri = getContentResolver().insert(PizzaEntry.CONTENT_URI, values);
    }

    /**
     * Helper method to delete all ingredients in the database.
     */
    private void deleteAllPizza() {
        int rowsDeleted = getContentResolver().delete(PizzaEntry.CONTENT_URI, null, null);
        Log.v("MainActivity", rowsDeleted + " rows deleted from pizzapartz database");
    }

    private void showDeleteConfirmationDialog() {
        // Create an AlertDialog.Builder and set the message, and click listeners
        // for the positive and negative buttons on the dialog.
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.delete_dialog_msg);
        builder.setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked the "Delete" button, so delete the ingredient.
                deleteAllPizza();
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked the "Cancel" button, so dismiss the dialog
                // and continue editing the ingredient.
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });

        // Create and show the AlertDialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
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
                insertPizza();
                return true;
            // Respond to a click on the "Delete all entries" menu option
            case R.id.action_delete_all_entries:
                showDeleteConfirmationDialog();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        // Define a projection that specifies the columns from the table we care about.
        String[] projection = {
                PizzaEntry._ID,
                PizzaEntry.COLUMN_INGREDIENT_NAME,
                PizzaEntry.COLUMN_INGREDIENT_PRICE,
                PizzaEntry.COLUMN_INGREDIENT_QUANTITY,
                PizzaEntry.COLUMN_INGREDIENT_SUPPLIER,
                PizzaEntry.COLUMN_SUPPLIER_PHONE};

        // This loader will execute the ContentProvider's query method on a background thread
        return new CursorLoader(this,   // Parent activity context
                                PizzaEntry.CONTENT_URI,   // Provider content URI to query
                                projection,             // Columns to include in the resulting
                                // Cursor
                                null,                   // No selection clause
                                null,                   // No selection arguments
                                null);                  // Default sort order
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        // Update {@link PizzaCursorAdapter} with this new cursor containing updated pet data
        mCursorAdapter.swapCursor(data);

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        // Callback called when the data needs to be deleted
        mCursorAdapter.swapCursor(null);
    }
}