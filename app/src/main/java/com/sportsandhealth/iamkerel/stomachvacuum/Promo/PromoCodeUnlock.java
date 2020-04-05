package com.sportsandhealth.iamkerel.stomachvacuum.Promo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.sportsandhealth.iamkerel.stomachvacuum.DB;

/**
 * Promo code unlock
 *
 * Using example in activity:
 *
 * PromoCodeUnlock promoCodeUnlock =
 *      new PromoCodeUnlock(this,
 *          new PromoCodeUnlock.OnErrorListener() {
 *              public void onUnlockTrue() {
 *                  ...
 *              }
 *              public void onUnlockFalse() {
 *                  ...
 *              }
 *              public void onError() {
 *               ...
 *              }
 *          });
 * promoCodeUnlock.unlock();
 */
public class PromoCodeUnlock {

    private String baseUrl = "http://34.91.135.208/unlock?api_key=qazwsx123";
    private RequestQueue queue;
    private OnResponseListener onResponseListener;
    private PromoCode promoCode;
    private DB db;

    public PromoCodeUnlock(Context context, OnResponseListener onResponseListener) {
        this.queue = Volley.newRequestQueue(context);
        this.onResponseListener = onResponseListener;
        this.promoCode = new PromoCode(context);
        this.db = DB.getInstance(context);
    }

    /**
     * Create promo code
     */
    public void unlock() {

        String myPromoCode = promoCode.code();
        String url = baseUrl
                + "&user_code=" + myPromoCode;

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JsonElement jsonElement = JsonParser.parseString(response);

                        String status = jsonElement
                                .getAsJsonObject().get("status")
                                .getAsString();

                        if (status.equals("SUCCESS")) {
                            String unlock = jsonElement
                                    .getAsJsonObject().get("data")
                                    .getAsJsonObject().get("unlock")
                                    .getAsString();
                            if (unlock.equals("true")) {
                                writeUnlock();
                                onResponseListener.onUnlockTrue();
                            } else {
                                onResponseListener.onUnlockFalse();
                            }
                        } else {
                            onResponseListener.onError();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                onResponseListener.onError();
            }
        });

        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    /**
     * Check unlock mark in database
     * @return bool
     */
    public boolean isUnlock() {
        Cursor cursor = this.db
                .getWritableDatabase()
                .rawQuery("SELECT value FROM META_DATA WHERE `key`='promo_code_unlock'",
                        null);

        if (cursor.getCount() >= 1) {
            cursor.moveToFirst();
            String promoCodeUnlock = cursor.getString(0);
            cursor.close();
            if (promoCodeUnlock.equals("true")) {
                return true;
            } else {
                return false;
            }
        } else {
            cursor.close();
            return false;
        }
    }

    public interface OnResponseListener {
        void onUnlockTrue();
        void onUnlockFalse();
        void onError();
    }

    /**
     * Write unlock mark into db
     */
    private void writeUnlock() {
        ContentValues values = new ContentValues();
        values.put("key", "promo_code_unlock");
        values.put("value", "true");
        db.getWritableDatabase()
                .insert("META_DATA", null, values);
    }
}
