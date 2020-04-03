package com.sportsandhealth.iamkerel.stomachvacuum.Promo;

import android.content.ContentValues;
import android.content.Context;

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
 * Promo code creating
 */
public class PromoCodeCreated {

    private DB db;
    private String url = "http://34.91.135.208/create?api_key=qazwsx123";
    private RequestQueue queue;

    public PromoCodeCreated(Context context) {
        this.db = DB.getInstance(context);
        this.queue = Volley.newRequestQueue(context);
    }

    /**
     * Create promo code
     */
    public void create() {

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JsonElement jsonElement = JsonParser.parseString(response);
                        String newPromoCode = jsonElement
                                .getAsJsonObject().get("data")
                                .getAsJsonObject().get("NEW_USER_CODE")
                                .getAsString();

                        write(newPromoCode);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });

        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    /**
     * Write in database
     */
    private void write(String code) {
        ContentValues values = new ContentValues();
        values.put("key", "promo_code");
        values.put("value", code);
        db.getWritableDatabase()
                .insert("META_DATA", null, values);
    }
}
