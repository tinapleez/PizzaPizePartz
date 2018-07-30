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
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.freecbdhomebiz.pizzapizepartz.data.PizzaContract.PizzaEntry;

/**
 * Allows user to create a new ingredient or edit an existing one.
 */
public class EditorActivity extends AppCompatActivity implements LoaderManager
        .LoaderCallbacks<Cursor> {

    private static final String LOG_TAG = "EditorActivity ";

    /**
     * Identifier for the ingredient data loader
     */
    private static final int EXISTING_PIZZA_LOADER = 0;

    /**
     * Content URI for the existing pet (null if it's a new pet)
     */
    private Uri mCurrentPizzaUri;

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
     * Boolean flag that keeps track of whether the ingredient has been edited (true) or not (false)
     */
    private boolean mPizzaHasChanged = false;

    /**
     * OnTouchListener that listens for any user touches on a View, implying that they are modifying
     * the view, and we change the mPizzaHasChanged boolean to true.
     */
    private View.OnTouchListener mTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            mPizzaHasChanged = true;
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        // Examine the intent that was used to launch this activity,
        // in order to figure out if we're creating a new ingredient or editing an existing one.
        Intent intent = getIntent();
        mCurrentPizzaUri = intent.getData();

        // If the intent DOES NOT contain an ingredient content URI, then we know that we are
        // creating a new ingredient.
        if (mCurrentPizzaUri == null) {
            // This is a new ingredient, so change the app bar to say "Add Ingredient"
            setTitle(getString(R.string.editor_activity_title_new_ingredient));

            // Invalidate the options menu, so the "Delete" menu option can be hidden.
            // (It doesn't make sense to delete an ingredient that hasn't been created yet.)
            invalidateOptionsMenu();
        } else {
            // Otherwise this is an existing ingredient, so change app bar to say "Edit Ingredient"
            setTitle(getString(R.string.editor_activity_title_edit_ingredient));

            TextView entryform = findViewById(R.id.entry_form_label);
            entryform.setText(R.string.editor_activity_update);

            // Initialize a loader to read the ingredient data from the database
            // and display the current values in the editor
            getLoaderManager().initLoader(EXISTING_PIZZA_LOADER, null, this);
        }

        // Find the views that we will read user input from
        mNameEditText = findViewById(R.id.edit_name);
        mPriceEditText = findViewById(R.id.edit_price);
        mQuantityEditText = findViewById(R.id.edit_quantity);
        mQuantityEditText.setText("0");
        mSupplierEditText = findViewById(R.id.edit_supplier);
        mPhoneEditText = findViewById(R.id.edit_phone);
        Button mIncreaseQuantity = findViewById(R.id.button_incr_quantity);
        Button mDecreaseQuantity = findViewById(R.id.button_decr_quantity);
        Button mCallSupplier = findViewById(R.id.button_supplier_phone);
        Button mDeleteRecord = findViewById(R.id.button_delete_record);

        // Setup OnTouchListeners on all the input fields, so we can determine if the user
        // has touched or modified them. This will let us know if there are unsaved changes
        // or not, if the user tries to leave the editor without saving.
        mNameEditText.setOnTouchListener(mTouchListener);
        mPriceEditText.setOnTouchListener(mTouchListener);
        mQuantityEditText.setOnTouchListener(mTouchListener);
        mSupplierEditText.setOnTouchListener(mTouchListener);
        mPhoneEditText.setOnTouchListener(mTouchListener);

        // Plus button click listener to increase quantity by 1
        mIncreaseQuantity.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                String test = mQuantityEditText.getText().toString();
                if (TextUtils.isEmpty(test)) {
                    mQuantityEditText.setText("0");
                }
                int i = Integer.parseInt(mQuantityEditText.getText().toString());
                mQuantityEditText.setText(String.valueOf(i + 1));
                mPizzaHasChanged = true;
            }
        });
        // Minus button click listener to decrease quantity by 1
        mDecreaseQuantity.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                String test = mQuantityEditText.getText().toString();
                if (TextUtils.isEmpty(test)) {
                    mQuantityEditText.setText("0");
                }
                int i = Integer.parseInt(mQuantityEditText.getText().toString());
                if (i > 0) {
                    mQuantityEditText.setText(String.valueOf(i - 1));
                    mPizzaHasChanged = true;
                } else {
                    // Do nothing because we don't want negative quantity
                }
            }
        });
        // Call the Supplier button click listener
        // Intent sends number and opens phone dialer
        mCallSupplier.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                String call = mPhoneEditText.getText().toString();
                if (!TextUtils.isEmpty(call)) {
                    Uri u = Uri.parse("tel:" + call);
                    Intent icall = new Intent(Intent.ACTION_VIEW, u);
                    startActivity(icall);
                } else {
                    // Do nothing because the phone number is null
                }
            }
        });
        // Delete This Record button click listener
        mDeleteRecord.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                showDeleteConfirmationDialog();
            }
        });

    }

    /**
     * Get user input from editor and save new ingredient or update existing into database.
     */
    private void saveIngredient() {
        // Read from user input fields
        String nameString = mNameEditText.getText().toString().trim();
        String priceString = mPriceEditText.getText().toString().trim();
        String quantityString = mQuantityEditText.getText().toString().trim();
        String supplierString = mSupplierEditText.getText().toString().trim();
        String phoneString = mPhoneEditText.getText().toString().trim();

        // Check if any EditText entries are blank. Provide Alert to ask user for correct input.
        if (TextUtils.isEmpty(nameString) || TextUtils.isEmpty
                (priceString) ||
                TextUtils.isEmpty(quantityString) || TextUtils.isEmpty(supplierString) ||
                TextUtils.isEmpty(phoneString)) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(R.string.Alert_EditText_Fields_Cannot_Be_Empty);
            builder.setNegativeButton(R.string.Alert_EditText_OK, new DialogInterface
                    .OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    // User clicked the "OK" button, so dismiss the dialog
                    // and continue editing the ingredient.
                    if (dialog != null) {
                        dialog.dismiss();
                    }
                }
            });
            // Create and show the AlertDialog
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
            return;
        }

        // Check if this is supposed to be a new ingredient
        // and check if all the fields in the editor are blank
        if (mCurrentPizzaUri == null &&
                TextUtils.isEmpty(nameString) && TextUtils.isEmpty(priceString) &&
                TextUtils.isEmpty(quantityString) && TextUtils.isEmpty(supplierString) &&
                TextUtils.isEmpty(phoneString)) {
            // Since no fields were modified, we can return early without creating a new ingredient.
            // No need to create ContentValues and no need to do any ContentProvider operations.
            finish();
        }

        // Create a ContentValues object where column names are the keys,
        // and ingredient attributes from the editor are the values.
        ContentValues values = new ContentValues();
        values.put(PizzaEntry.COLUMN_INGREDIENT_NAME, nameString);

        // If the price is not provided by the user, don't try to parse the string into an
        // integer value. Use 0 by default.
        int price = 0;
        if (!TextUtils.isEmpty(priceString)) {
            price = Integer.parseInt(priceString);
        }
        values.put(PizzaEntry.COLUMN_INGREDIENT_PRICE, price);

        // If the quantity is not provided by the user, don't try to parse the string into an
        // integer value. Use 0 by default.
        int quantity = 0;
        if (!TextUtils.isEmpty(priceString)) {
            quantity = Integer.parseInt(quantityString);
        }
        values.put(PizzaEntry.COLUMN_INGREDIENT_QUANTITY, quantity);

        values.put(PizzaEntry.COLUMN_INGREDIENT_SUPPLIER, supplierString);

        // If the phone is not provided by the user, don't try to parse the string into an
        // integer value. Use 0 by default.
        int phone = 0;
        if (!TextUtils.isEmpty(phoneString)) {
            phone = Integer.parseInt(phoneString);
        }
        values.put(PizzaEntry.COLUMN_SUPPLIER_PHONE, phone);

        Log.i(LOG_TAG, "The insertIngredient string= " + values);

        // Determine if this is a new or existing ingredient by checking if mCurrentPizzaUri is
        // null or not
        if (mCurrentPizzaUri == null) {
            // This is a NEW ingredient, so insert a new ingredient into the provider,
            // returning the content URI for the new ingredient.
            Uri newUri = getContentResolver().insert(PizzaEntry.CONTENT_URI, values);

            // Show a toast message depending on whether or not the insertion was successful.
            if (newUri == null) {
                // If the new content URI is null, then there was an error with insertion.
                Toast.makeText(this, getString(R.string.Toast_Editor_Activity_Error_Saving),
                               Toast.LENGTH_SHORT).show();
            } else {
                // Otherwise, the insertion was successful and we can display a toast.
                Toast.makeText(this, getString(R.string.Toast_EditorActivity_Save_Success),
                               Toast.LENGTH_SHORT).show();
            }
        } else {
            // Otherwise this is an EXISTING ingredient, so update the ingredient with content URI:
            // mCurrentPizzaUri
            // and pass in the new ContentValues. Pass in null for the selection and selection args
            // because mCurrentPizzaUri will already identify the correct row in the database that
            // we want to modify.
            int rowsAffected = getContentResolver().update(mCurrentPizzaUri, values, null, null);

            // Show a toast message depending on whether or not the update was successful.
            if (rowsAffected == 0) {
                // If no rows were affected, then there was an error with the update.
                Toast.makeText(this, getString(R.string.editor_update_ingredient_failed),
                               Toast.LENGTH_SHORT).show();
            } else {
                // Otherwise, the update was successful and we can display a toast.
                Toast.makeText(this, getString(R.string.editor_update_ingredient_successful),
                               Toast.LENGTH_SHORT).show();
            }
        }
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_editor, menu);
        return true;
    }

    /**
     * This method is called after invalidateOptionsMenu(), so that the
     * menu can be updated (some menu items can be hidden or made visible).
     */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        // If this is a new pet, hide the "Delete" menu item.
        if (mCurrentPizzaUri == null) {
            MenuItem menuItem = menu.findItem(R.id.action_delete);
            menuItem.setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // User clicked the "Save" menu option
            case R.id.menu_editor_save:
                // Write ingredient to database
                saveIngredient();

                return true;
            // Respond to a click on the "Delete" menu option
            case R.id.action_delete:
                // Pop up confirmation dialog for deletion
                showDeleteConfirmationDialog();
                return true;
            // Respond to a click on the "Up" arrow button in the app bar
            case android.R.id.home:
                // If the ingredient hasn't changed, continue with navigating up to parent activity
                // which is the {@link MainActivity}.
                if (!mPizzaHasChanged) {
                    NavUtils.navigateUpFromSameTask(EditorActivity.this);
                    return true;
                }
                // Otherwise if there are unsaved changes, setup a dialog to warn the user.
                // Create a click listener to handle the user confirming that
                // changes should be discarded.
                DialogInterface.OnClickListener discardButtonClickListener =
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                // User clicked "Discard" button, navigate to parent activity.
                                NavUtils.navigateUpFromSameTask(EditorActivity.this);
                            }
                        };

                // Show a dialog that notifies the user they have unsaved changes
                showUnsavedChangesDialog(discardButtonClickListener);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * This method is called when the back button is pressed.
     */
    @Override
    public void onBackPressed() {
        // If the ingredient hasn't changed, continue with handling back button press
        if (!mPizzaHasChanged) {
            super.onBackPressed();
            return;
        }

        // Otherwise if there are unsaved changes, setup a dialog to warn the user.
        // Create a click listener to handle the user confirming that changes should be discarded.
        DialogInterface.OnClickListener discardButtonClickListener =
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // User clicked "Discard" button, close the current activity.
                        finish();
                    }
                };

        // Show dialog that there are unsaved changes
        showUnsavedChangesDialog(discardButtonClickListener);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        // Since the editor shows all ingredient attributes, define a projection that contains
        // all columns from the pizzapartz table
        String[] projection = {
                PizzaEntry._ID,
                PizzaEntry.COLUMN_INGREDIENT_NAME,
                PizzaEntry.COLUMN_INGREDIENT_PRICE,
                PizzaEntry.COLUMN_INGREDIENT_QUANTITY,
                PizzaEntry.COLUMN_INGREDIENT_SUPPLIER,
                PizzaEntry.COLUMN_SUPPLIER_PHONE};

        // This loader will execute the ContentProvider's query method on a background thread
        return new CursorLoader(this,   // Parent activity context
                                mCurrentPizzaUri,  // Query the content URI for the current
                                // ingredient
                                projection,        // Columns to include in the resulting Cursor
                                null,      // No selection clause
                                null,     // No selection arguments
                                null);         // Default sort order
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        // Bail early if the cursor is null or there is less than 1 row in the cursor
        if (cursor == null || cursor.getCount() < 1) {
            return;
        }

        // Proceed with moving to the first row of the cursor and reading data from it
        // (This should be the only row in the cursor)
        if (cursor.moveToFirst()) {
            // Find the columns of ingredient attributes that we're interested in
            int nameColumnIndex = cursor.getColumnIndex(PizzaEntry.COLUMN_INGREDIENT_NAME);
            int priceColumnIndex = cursor.getColumnIndex(PizzaEntry.COLUMN_INGREDIENT_PRICE);
            int quantityColumnIndex = cursor.getColumnIndex(PizzaEntry.COLUMN_INGREDIENT_QUANTITY);
            int supplierColumnIndex = cursor.getColumnIndex(PizzaEntry.COLUMN_INGREDIENT_SUPPLIER);
            int phoneColumnIndex = cursor.getColumnIndex(PizzaEntry.COLUMN_SUPPLIER_PHONE);


            // Extract out the value from the Cursor for the given column index
            String name = cursor.getString(nameColumnIndex);
            int price = cursor.getInt(priceColumnIndex);
            int quantity = cursor.getInt(quantityColumnIndex);
            String supplier = cursor.getString(supplierColumnIndex);
            int phone = cursor.getInt(phoneColumnIndex);

            // Update the views on the screen with the values from the database
            mNameEditText.setText(name);
            mPriceEditText.setText(Integer.toString(price));
            mQuantityEditText.setText(Integer.toString(quantity));
            mSupplierEditText.setText(supplier);
            mPhoneEditText.setText(Integer.toString(phone));
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        // If the loader is invalidated, clear out all the data from the input fields.
        mNameEditText.setText("");
        mPriceEditText.setText("");
        mQuantityEditText.setText("");
        mSupplierEditText.setText("");
        mPhoneEditText.setText("");
    }

    /**
     * Show a dialog that warns the user there are unsaved changes that will be lost
     * if they continue leaving the editor.
     *
     * @param discardButtonClickListener is the click listener for what to do when
     *                                   the user confirms they want to discard their changes
     */
    private void showUnsavedChangesDialog(
            DialogInterface.OnClickListener discardButtonClickListener) {
        // Create an AlertDialog.Builder and set the message, and click listeners
        // for the postivie and negative buttons on the dialog.
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.unsaved_changes_dialog_msg);
        builder.setPositiveButton(R.string.discard, discardButtonClickListener);
        builder.setNegativeButton(R.string.keep_editing, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked the "Keep editing" button, so dismiss the dialog
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

    /**
     * Prompt the user to confirm that they want to delete this ingredient.
     */
    private void showDeleteConfirmationDialog() {
        // Create an AlertDialog.Builder and set the message, and click listeners
        // for the positive and negative buttons on the dialog.
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.delete_dialog_msg);
        builder.setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked the "Delete" button, so delete the ingredient.
                deleteIngredient();
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

    /**
     * Perform the deletion of the ingredient in the database.
     */
    private void deleteIngredient() {
        // Only perform the delete if this is an existing ingredient.
        if (mCurrentPizzaUri != null) {
            // Call the ContentResolver to delete the ingredient at the given content URI.
            // Pass in null for the selection and selection args because the mCurrentPizza
            // content URI already identifies the ingredient that we want.
            int rowsDeleted = getContentResolver().delete(mCurrentPizzaUri, null, null);

            // Show a toast message depending on whether or not the delete was successful.
            if (rowsDeleted == 0) {
                // If no rows were deleted, then there was an error with the delete.
                Toast.makeText(this, getString(R.string.editor_delete_ingredient_failed),
                               Toast.LENGTH_SHORT).show();
            } else {
                // Otherwise, the delete was successful and we can display a toast.
                Toast.makeText(this, getString(R.string.editor_delete_ingredient_success),
                               Toast.LENGTH_SHORT).show();
            }
        }
        // Close the activity
        finish();
    }
}