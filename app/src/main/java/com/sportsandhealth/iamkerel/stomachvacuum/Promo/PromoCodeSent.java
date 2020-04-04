package com.sportsandhealth.iamkerel.stomachvacuum.Promo;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

/**
 * Promo code sending
 *
 * Using example in activity:
 *
 * PromoCodeSent promoCodeSent =
 *      new PromoCodeSent(this,
 *          new PromoCodeSent.OnErrorListener() {
 *              public void onError() {
 *                  ...
 *              };
 *              public void onSuccess() {
 *                  ...
 *              }
 *          });
 * promoCodeSent.send("REF123");
 */
public class PromoCodeSent {

    private String baseUrl = "http://34.91.135.208/append?api_key=qazwsx123";
    private RequestQueue queue;
    private OnResponseListener onResponseListener;
    private PromoCode promoCode;

    public PromoCodeSent(Context context, OnResponseListener onResponseListener) {
        this.queue = Volley.newRequestQueue(context);
        this.onResponseListener = onResponseListener;
        this.promoCode = new PromoCode(context);
    }

    /**
     * Send promo code
     */
    public void send(String forPromoCode) {

        String myPromoCode = promoCode.code();
        String url = baseUrl
                + "&user_code=" + myPromoCode
                + "&for_user_code=" + forPromoCode;

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        onResponseListener.onSuccess();
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

    public interface OnResponseListener {
        void onError();
        void onSuccess();
    }
}
