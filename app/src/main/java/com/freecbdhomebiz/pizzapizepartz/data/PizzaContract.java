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

import android.provider.BaseColumns;


public final class PizzaContract {

    private PizzaContract() {
    }

    /**
     * Constant variables for the database entries
     */
    public static final class PizzaEntry implements BaseColumns {

        /**
         * Name of database table for the pizza ingredients
         */
        public final static String TABLE_NAME = "pizzapartz";

        /**
         * _ID
         * Type: INTEGER
         */
        public final static String _ID = BaseColumns._ID;

        /**
         * Name of the ingredient
         * Type: TEXT
         */
        public final static String COLUMN_INGREDIENT_NAME = "Ingredient";

        /**
         * Price of the ingredient in whole dollars
         * Type: INTEGER
         */
        public final static String COLUMN_INGREDIENT_PRICE = "Price";

        /**
         * Quantity of ingredient in stock
         * Type: INTEGER
         */
        public final static String COLUMN_INGREDIENT_QUANTITY = "Quantity";

        /**
         * Supplier of the ingredient
         * Type: TEXT
         */
        public final static String COLUMN_INGREDIENT_SUPPLIER = "Supplier";

        /**
         * Supplier's phone number without dashes
         * Type: INTEGER
         */
        public final static String COLUMN_SUPPLIER_PHONE = "Phone_Number";
    }
}

