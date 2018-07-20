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

package com.freecbdhomebiz.pizzapizepartz.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.freecbdhomebiz.pizzapizepartz.data.PizzaContract.PizzaEntry;


/**
 * Database helper class that manages database creation and versioning.
 */
public class PizzaDbHelper extends SQLiteOpenHelper {

    private static final String LOG_TAG = "PizzaDbHelper ";

    /**
     * Name of the database file
     */
    private static final String DATABASE_NAME = "pizzapartz.db";

    /**
     * Database version. If you change the database schema (add columns,etc.), you must
     * increment the database version.
     */
    private static final int DATABASE_VERSION = 1;

    /**
     * Constructor method of {@link PizzaDbHelper}.
     *
     * @param context of the app
     */
    public PizzaDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * This method is called when the database is created for the first time (ex. when the app is
     * installed.)
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        // Build a String that makes the SQL command to create the ingredient table
        String SQL_CREATE_PIZZAPARTZ_TABLE = "CREATE TABLE " + PizzaEntry
                .TABLE_NAME + " ("
                + PizzaEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + PizzaEntry.COLUMN_INGREDIENT_NAME + " TEXT NOT NULL, "
                + PizzaEntry.COLUMN_INGREDIENT_PRICE + " INTEGER NOT NULL, "
                + PizzaEntry.COLUMN_INGREDIENT_QUANTITY + " INTEGER NOT NULL, "
                + PizzaEntry.COLUMN_INGREDIENT_SUPPLIER + " TEXT NOT NULL, "
                + PizzaEntry.COLUMN_SUPPLIER_PHONE + " INTEGER NOT NULL)";

        // Execute the built SQL command
        Log.i(LOG_TAG, SQL_CREATE_PIZZAPARTZ_TABLE);
        db.execSQL(SQL_CREATE_PIZZAPARTZ_TABLE);
    }

    /**
     * This method is called when the database is changed and re-versioned.
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Use this method to update the db files based on changes to the schema.
        // Current version is 1.
    }
}

