package com.sportsandhealth.iamkerel.stomachvacuum.lib;

import android.content.ContentValues;
import android.content.Context;

import com.sportsandhealth.iamkerel.stomachvacuum.DB;

/**
 * Removal all training progress
 */
public class ProgressRemoval {
    private DB db;

    public ProgressRemoval(Context context) {
        this.db = DB.getInstance(context);
    }

    /**
     * Remove all training progress
     */
    public void remove() {
        ContentValues values = new ContentValues();
        values.put("done", 0);
        db.getWritableDatabase()
                .update("DATA", values, null, null);
    }
}
