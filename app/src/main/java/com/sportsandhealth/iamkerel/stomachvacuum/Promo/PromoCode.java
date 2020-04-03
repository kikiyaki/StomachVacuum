package com.sportsandhealth.iamkerel.stomachvacuum.Promo;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.sportsandhealth.iamkerel.stomachvacuum.DB;

/**
 * Promo code
 */
public class PromoCode {

    private DB db;

    public PromoCode(Context context) {
        this.db = DB.getInstance(context);
    }

    /**
     * Check if promo code exist
     */
    public boolean isExist() {
        Cursor cursor = this.db
                .getWritableDatabase()
                .rawQuery("SELECT * FROM META_DATA WHERE `key`='promo_code'",
                        null);

        if (cursor.getCount() >= 1) {
            cursor.close();
            return true;
        } else {
            cursor.close();
            return false;
        }
    }

    /**
     * String promo code
     */
    public String code() {
        Cursor cursor = this.db
                .getWritableDatabase()
                .rawQuery("SELECT value FROM META_DATA WHERE `key`=?",
                new String[]{"promo_code"});

        cursor.moveToFirst();
        String code = cursor.getString(0);
        cursor.close();

        return code;
    }
}
