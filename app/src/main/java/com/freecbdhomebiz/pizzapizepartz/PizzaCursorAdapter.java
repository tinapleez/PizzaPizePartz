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

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.freecbdhomebiz.pizzapizepartz.data.PizzaContract.PizzaEntry;

/**
 * {@link PizzaCursorAdapter} is an adapter for a list or grid view
 * that uses a {@link Cursor} of ingredient data as its data source. This adapter knows
 * how to create list items for each row of ingredient data in the {@link Cursor}.
 */
public class PizzaCursorAdapter extends CursorAdapter {

    /**
     * Constructs a new {@link PizzaCursorAdapter}.
     *
     * @param context The context
     * @param c       The cursor from which to get the data.
     */
    public PizzaCursorAdapter(Context context, Cursor c) {
        super(context, c, 0 /* flags */);
    }

    /**
     * Makes a new blank list item view. No data is set (or bound) to the views yet.
     *
     * @param context app context
     * @param cursor  The cursor from which to get the data. The cursor is already
     *                moved to the correct position.
     * @param parent  The parent to which the new view is attached to
     * @return the newly created list item view.
     */
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        // Return the list item view (instead of null)
        return LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
    }

    /**
     * This method binds the ingredient data (in the current row pointed to by cursor) to the given
     * list item layout. For example, the name for the current ingredient can be set on the name
     * TextView in the list item layout.
     *
     * @param view    Existing view, returned earlier by newView() method
     * @param context app context
     * @param cursor  The cursor from which to get the data. The cursor is already moved to the
     *                correct row.
     */
    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        // Find individual views that we want to modify in the list item layout
        TextView nameTextView = view.findViewById(R.id.the_name);
        TextView priceTextView = view.findViewById(R.id.the_price);
        TextView quantityTextView = view.findViewById(R.id.the_quantity);

        // Find the columns of ingredient attributes that we're interested in
        int nameColumnIndex = cursor.getColumnIndex(PizzaEntry.COLUMN_INGREDIENT_NAME);
        int priceColumnIndex = cursor.getColumnIndex(PizzaEntry.COLUMN_INGREDIENT_PRICE);
        int quantityColumnIndex = cursor.getColumnIndex(PizzaEntry.COLUMN_INGREDIENT_QUANTITY);

        // Read the pet attributes from the Cursor for the current ingredient
        String pName = cursor.getString(nameColumnIndex);
        String pPrice = cursor.getString(priceColumnIndex);
        String pQuantity = cursor.getString(quantityColumnIndex);

        // Create the message to be displayed
        String priceDisplay = ("Price:  $" + pPrice);
        String quantityDisplay = ("Quantity:  " + pQuantity);

        // Update the TextViews with the attributes for the current ingredient
        nameTextView.setText(pName);
        priceTextView.setText(priceDisplay);
        quantityTextView.setText(quantityDisplay);
    }
}

