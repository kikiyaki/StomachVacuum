package com.sportsandhealth.iamkerel.stomachvacuum.Promo;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;

import com.sportsandhealth.iamkerel.stomachvacuum.DB;
import com.sportsandhealth.iamkerel.stomachvacuum.R;
import com.sportsandhealth.iamkerel.stomachvacuum.lib.CurrentContext;

/**
 * Modal window for ad disable
 */
public class PromoModal {
    private DB db;
    private int thresholdCount = 2;

    public PromoModal() {
        this.db = DB.getInstance(CurrentContext.get());
    }

    public boolean isShown() {
        Cursor cursor = this.db
                .getWritableDatabase()
                .rawQuery("SELECT value FROM META_DATA WHERE `key`='promo_window_impressions'",
                        null);
        if (cursor.getCount() >= 1) {
            cursor.moveToFirst();
            String value = cursor.getString(0);
            int impressionsCount = Integer.getInteger(value);
            cursor.close();
            return impressionsCount >= thresholdCount;
        } else {
            cursor.close();
            return false;
        }
    }

    /**
     * Show modal window
     */
    public void show() {
        final Context currentContext = CurrentContext.get();
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(currentContext);
        alertDialogBuilder.setTitle(currentContext.getResources().getString(R.string.end_day_activity__promo_title));
        alertDialogBuilder
                .setMessage(R.string.end_day_activity__promo_description)
                .setCancelable(false)
                .setPositiveButton(currentContext.getResources().getString(R.string.end_day_activity__yes),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Intent intent = new Intent(currentContext, MyPromoCodeActivity.class);
                                currentContext.startActivity(intent);
                            }
                        })
                .setNegativeButton(currentContext.getResources().getString(R.string.end_day_activity__no),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
        increase();
    }

    /**
     * Increase impressions counter
     */
    private void increase() {
        Cursor cursor = this.db
                .getWritableDatabase()
                .rawQuery("SELECT value FROM META_DATA WHERE `key`='promo_window_impressions'",
                        null);
        if (cursor.getCount() >= 1) {
            cursor.moveToFirst();
            String value = cursor.getString(0);
            int impressionsCount = Integer.getInteger(value);
            impressionsCount++;

            ContentValues values = new ContentValues();
            values.put("key", "promo_window_impressions");
            values.put("value", String.valueOf(impressionsCount));
            db.getWritableDatabase()
                    .update("META_DATA", values, "key=?", new String[]{"promo_window_impressions"});
            cursor.close();
        } else {
            ContentValues values = new ContentValues();
            values.put("key", "promo_window_impressions");
            values.put("value", "1");
            db.getWritableDatabase()
                    .insert("META_DATA", null, values);
            cursor.close();
        }
    }
}
